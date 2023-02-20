package com.lin.sqzmYxlt.config;


import com.lin.common.service.FriendsService;
import com.lin.common.service.UserService;
import com.lin.sqzmYxlt.controller.WebSocketController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.web.socket.server.standard.ServerEndpointExporter;

/**
 * @author lin
 */

@Configuration
public class WebSocketConfig  {
    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        return new ServerEndpointExporter();
    }

    @Autowired
    public void setUserService(UserService userService){
        WebSocketController.userService=userService;
    }
    @Autowired
    public void setFriendsService(FriendsService friendsService){
        WebSocketController.friendsService=friendsService;
    }


}