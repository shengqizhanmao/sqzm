package com.lin.sqzmYxlt.controller;


import com.lin.common.service.FriendsService;
import com.lin.common.service.UserService;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArraySet;

@ServerEndpoint(value = "/websocket/{formUserId}")
@Component
public class WebSocketController {
    public static UserService userService;
    public static FriendsService friendsService;
    private static Map<String,Session> map = new HashMap<>();
    //用来存放每个客户端对应的MyWebSocket对象。
    private static CopyOnWriteArraySet<WebSocketController> webSocketSet = new CopyOnWriteArraySet<>();
    //与某个客户端的连接会话，需要通过它来给客户端发送数据
    private Session session;
    private String formUsername;
    @OnOpen
    public void onOpen(Session session, @PathParam("formUsername") String formUsername) throws IOException {
        this.session = session;
        this.formUsername =formUsername;
        map.put(formUsername, session);
        webSocketSet.add(this);     //加入set中
        System.out.println("session:"+session);
        System.out.println("map:"+map);
        System.out.println("有新连接加入！"+formUsername+"加入聊天室,当前在线人数为" + webSocketSet.size());
        this.session.getAsyncRemote().sendText("当前在线人数为："+webSocketSet.size());
    }
    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose() {
        webSocketSet.remove(this);  //从set中删除
        System.out.println("有一连接关闭！当前在线人数为" + webSocketSet.size());
    }
    /**
     * 收到客户端消息后调用的方法
     *
     * @param message 客户端发送过来的消息*/
    @OnMessage
    public void onMessage(String message, Session session,@PathParam("formUsername") String formUsername) {
        System.out.println("来自客户端的消息-->"+formUsername+": " + message);
        Session formSession = map.get(formUsername);
        formSession.getAsyncRemote().sendText("123");
//         Session toSession = map.get(formUsername);
//        toSession.getAsyncRemote().sendText("1234");
    }
    /**
     * 发生错误时调用
     *
     */
    @OnError
    public void onError(Session session, Throwable error) {
        System.out.println("发生错误");
        error.printStackTrace();
    }
    /**
     * 群发自定义消息
     * */
    public  void broadcast(String message){
        for (WebSocketController item : webSocketSet) {
            item.session.getAsyncRemote().sendText(message);//异步发送消息.
        }
    }
}
