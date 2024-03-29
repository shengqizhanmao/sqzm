package com.lin.sqzmYxlt.controller;


import com.alibaba.fastjson.JSON;
import com.lin.common.WebSocketGetMeg;
import com.lin.common.WebSocketPushMeg;
import com.lin.common.pojo.Friends;
import com.lin.common.service.FriendsService;
import com.lin.common.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArraySet;

@ServerEndpoint(value = "/websocket/{formUserId}")
@Component
@Slf4j
public class WebSocketController {
    public static UserService userService;
    public static FriendsService friendsService;
    private static Map<String, Session> map = new HashMap<>();
    //用来存放每个客户端对应的MyWebSocket对象。
    private static CopyOnWriteArraySet<WebSocketController> webSocketSet = new CopyOnWriteArraySet<>();
    //与某个客户端的连接会话，需要通过它来给客户端发送数据
    private Session session;
    private String formUserId;

    /*
    * 连接websocket初始化
    * */
    @OnOpen
    public void onOpen(@PathParam("formUserId") String formUserId,
                       Session session) {
        this.session = session;
        this.formUserId = formUserId;
        map.put(formUserId, session);
        webSocketSet.add(this);     //加入set中
        System.out.println("有新连接加入！id为:" + formUserId +
                "连接webSocket,当前在线人数为" + webSocketSet.size());
    }
    /**
     * 收到客户端消息后调用的方法
     *
     * @param message 客户端发送过来的消息
     */
    @OnMessage
    public void onMessage(String message, Session session,
                          @PathParam("formUserId") String formUserId) {
        WebSocketGetMeg webSocketGetMeg = JSON.parseObject(message,
                WebSocketGetMeg.class);
        Session formSession = map.get(formUserId);
        //心跳测试
        if (webSocketGetMeg.getType().equals("heartbeat")) {
            WebSocketPushMeg webSocketPushMeg = copy(webSocketGetMeg,
                    "心跳测试成功");
            String s = JSON.toJSONString(webSocketPushMeg);
            formSession.getAsyncRemote().sendText(s);
            return;
        }
        //保存消息到数据库
        WebSocketPushMeg webSocketPushMeg = copy(webSocketGetMeg);
        String pushMeg = JSON.toJSONString(webSocketPushMeg);
        Friends friends = copyFriends(webSocketPushMeg);
        try {
            friendsService.save(friends);
        } catch (Exception e) {
            log.error(e.toString());
            webSocketPushMeg.setMsg("发送失败,请反馈给问题" + e);
            pushMeg = JSON.toJSONString(webSocketPushMeg);
            formSession.getAsyncRemote().sendText(pushMeg);
            return;
        }
        //formUserId-->toUserId发送消息
        String toUserId = webSocketGetMeg.getToUserId();
        //toUserId未连接到websocket
        try {
            if (!map.get(toUserId).isOpen()) {
                formSession.getAsyncRemote().sendText(pushMeg);
                return;
            }
        } catch (NullPointerException e) {
            formSession.getAsyncRemote().sendText(pushMeg);
            return;
        }
        Session toSession = map.get(toUserId);
        formSession.getAsyncRemote().sendText(pushMeg);
        toSession.getAsyncRemote().sendText(pushMeg);
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
     * 发生错误时调用
     */
    @OnError
    public void onError(Session session, Throwable error) {
        System.out.println("发生错误");
        error.printStackTrace();
    }

    /**
     * 群发自定义消息
     */
//    public  void broadcast(String message){
//        for (WebSocketController item : webSocketSet) {
//            item.session.getAsyncRemote().sendText(message);//异步发送消息.
//        }
//    }
    private WebSocketPushMeg copy(WebSocketGetMeg webSocketGetMeg) {
        WebSocketPushMeg webSocketPushMeg = new WebSocketPushMeg();
        webSocketPushMeg.setMsg(webSocketGetMeg.getMsg());
        webSocketPushMeg.setType(webSocketGetMeg.getType());
        webSocketPushMeg.setFormUserId(webSocketGetMeg.getFormUserId());
        webSocketPushMeg.setToUserId(webSocketGetMeg.getToUserId());
        webSocketPushMeg.setCreatedDate(new Date());
        return webSocketPushMeg;
    }

    private Friends copyFriends(WebSocketPushMeg webSocketPushMeg) {
        Friends friends = new Friends();
        friends.setFormUserId(webSocketPushMeg.getFormUserId());
        friends.setToUserId(webSocketPushMeg.getToUserId());
        friends.setMsg(webSocketPushMeg.getMsg());
        friends.setCreatedDate(webSocketPushMeg.getCreatedDate());
        return friends;
    }

    private WebSocketPushMeg copy(WebSocketGetMeg webSocketGetMeg, String Meg) {
        WebSocketPushMeg webSocketPushMeg = new WebSocketPushMeg();
        webSocketPushMeg.setMsg(Meg);
        webSocketPushMeg.setType(webSocketGetMeg.getType());
        webSocketPushMeg.setFormUserId(webSocketGetMeg.getFormUserId());
        webSocketPushMeg.setToUserId(webSocketGetMeg.getToUserId());
        webSocketPushMeg.setCreatedDate(new Date());
        return webSocketPushMeg;
    }
}
