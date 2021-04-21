package com.chatservice.controller;

import com.chatservice.model.ChatMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping
public class MessageController {

    private List<String> currentOnline = new ArrayList<>();
    private List<ChatMessage> currentMessages = new ArrayList<>();




    //    final static Logger loggy = Logger.getLogger(UserController.class);
//    static {
////        loggy.setLevel(Level.ALL);
//        //loggy.setLevel(Level.ERROR);
//    }

    /**
     * Chat message websocket endpoint for passing messages to and from the client.
     * @param msg Messsage object from the client HTTP request body.
     * @return
     * @throws Exception
     */
    @MessageMapping("/chat")
    @SendTo("/topic/messages")
    public ChatMessage send(ChatMessage msg) throws Exception {
        String time = new SimpleDateFormat("HH:mm").format(new Date());
        System.out.println(msg);
        ChatMessage newMsg = new ChatMessage(msg.getSender(), msg.getText(), time, msg.getImgUrl());
        addToCurrentMessages(newMsg);
//        loggy.info("Sending the message to the chat room");
        return newMsg;
    }

    @MessageMapping("/onlineUsers")
    @SendTo("/topic/status")
    public String sendOnlineUsers(String incomingUser) throws Exception {
        for(int i = 0; i < currentOnline.size(); i++) {
            if(incomingUser.equals(currentOnline.get(i))){
//                loggy.info("Sent back information on currently online Users");
                return currentOnline.toString();
            }
        }
        currentOnline.add(incomingUser);
//        loggy.info("Sent back information on currently online Users and added new User:"+incomingUser);
        return currentOnline.toString();
    }

    @MessageMapping("/disconnect")
    @SendTo("/topic/status")
    public String disconnectUser(String leavingUser) throws Exception {
        System.out.println("in disconnect");
        for(int i = 0; i < currentOnline.size(); i++) {
            if(leavingUser.equals(currentOnline.get(i))){
                currentOnline.remove(i);
                System.out.println("user disconnected");
            }
        }
//        loggy.info("disconnected user:"+leavingUser+" from chatroom");
        return currentOnline.toString();
    }

    @MessageMapping("/loadMessages")
    @SendTo("/topic/loadMessages")
    public String sendOldMessages(String str) throws Exception {
        System.out.println("Sending older messages");
        return arrayListToJSON(currentMessages);
    }

    public void addToCurrentMessages(ChatMessage msg) {
        if(currentMessages.size() > 24) {
            currentMessages.remove(0);
            currentMessages.add(msg);
        }
        else {
            currentMessages.add(msg);
        }
    }

    public String arrayListToJSON(List<ChatMessage> currentMessages) {
        String newList = "[";
        for(int i=0; i< currentMessages.size(); i++) {
            newList += ("{\"sender\":\"" + currentMessages.get(i).getSender() + "\",\"text\":\"" + currentMessages.get(i).getText() + "\",\"time\":\"" + currentMessages.get(i).getTime() +"\"}");
            if(!(i == currentMessages.size()-1)){
                newList += ",";
            }
        }
        newList += "]";
        System.out.println(newList);
        return newList;
    }
}
