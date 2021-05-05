package com.chatservice.controller;

import com.chatservice.model.ChatMessage;
import com.chatservice.model.UserWithImg;
import lombok.Data;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@Data
public class MessageController {

    /**
     * Fields for a list of users that are currently online, the most recent 25 chat messages, and
     * a list of users who are currently typing.
     */
    private List<UserWithImg> currentOnline;
    private List<ChatMessage> currentMessages;
    private List<String> currentTyping;

    public MessageController() {
        this.currentOnline = new ArrayList<>();
        this.currentMessages = new ArrayList<>();
        this.currentTyping = new ArrayList<>();
    }

    /**
     * Chat message websocket endpoint for passing messages to and from the client.
     * @param msg Messsage object from the client HTTP request body.
     * @return The message object with date added.
     * @throws Exception Websocket Exception
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

    /**
     * Adds the newly online user to the list of all online users and returns it to the frontend.
     * @param incomingUser newly online user
     * @return A stringified list of all online users
     * @throws Exception Websocket Exception
     */
    @MessageMapping("/onlineUsers")
    @SendTo("/topic/status")
    public String sendOnlineUsers(UserWithImg incomingUser) throws Exception {
        for(int i = 0; i < currentOnline.size(); i++) {
            if(incomingUser.getUserName().equals(currentOnline.get(i).getUserName())){
//                loggy.info("Sent back information on currently online Users");
                return userWithImgToJSON(currentOnline);
            }
        }
        currentOnline.add(incomingUser);
//        loggy.info("Sent back information on currently online Users and added new User:"+incomingUser);
        return userWithImgToJSON(currentOnline);
    }

    /**
     * Removes the user leaving the chat room from the list of online users and returns the updated
     * list to the front end.
     * @param leavingUser User that is exiting the chat room
     * @return A stringified list of users still in the chat room.
     * @throws Exception Websocket exception
     */
    @MessageMapping("/disconnect")
    @SendTo("/topic/status")
    public String disconnectUser(String leavingUser) throws Exception {
        System.out.println("in disconnect");
        String user = leavingUser.substring(1, leavingUser.length()-1);
        System.out.println(user);
        for(int i = 0; i < currentOnline.size(); i++) {
            System.out.println(currentOnline.get(i).getUserName());
            if(user.equals(currentOnline.get(i).getUserName())){
                currentOnline.remove(i);
                System.out.println("user disconnected");
            }
        }
//        loggy.info("disconnected user:"+leavingUser+" from chatroom");
        return userWithImgToJSON(currentOnline);
    }

    /**
     * Returns a list of the most recent 25 chat messages to users that enter the chat room after
     * these messages were sent.
     * @param str Can be anything; it is never used in the method body.
     * @return A stringified list of the most recent 25 chat messages.
     * @throws Exception Websocket exception
     */
    @MessageMapping("/loadMessages")
    @SendTo("/topic/loadMessages")
    public String sendOldMessages(String str) throws Exception {
        System.out.println("Sending older messages");
        return arrayListToJSON(currentMessages);
    }

    /**
     * Takes in a username and adds it to the list of currently typing users and then returns the list
     * to the front end.
     * @param username the username of the user that is currently typing
     * @return A stringified list of all users that are currently typing.
     * @throws Exception Websocket excpetion
     */
    @MessageMapping("/typing")
    @SendTo("/topic/typing")
    public String newTyping(String username) throws Exception {
        System.out.println("new user typing");
        for(int i = 0; i<currentTyping.size(); i++){
            if(currentTyping.get(i).equals(username)) {
                return currentTyping.toString();
            }
        }
        currentTyping.add(username);
        return currentTyping.toString();
    }

    /**
     * Removes the user that has stopped typing from the currently typing list and returns the updated
     * list to the front end.
     * @param username The username of the user that is no longer typing
     * @return A stringified list of all users that are still typing
     * @throws Exception Websocket exception
     */
    @MessageMapping("/notTyping")
    @SendTo("/topic/typing")
    public String removeTyping(String username) throws Exception {
        System.out.println("User no longer typing");
        for(int i = 0; i<currentTyping.size(); i++){
            if(currentTyping.get(i).equals(username)) {
                currentTyping.remove(i);
            }
        }
        return currentTyping.toString();
    }

    /**
     * Adds a new message to the list of chat messages. If the list is of length 25, the method will
     * remove the oldest chat message and add the new message at the end.
     * @param msg The message to be added to the list of recent chat messages
     */
    public void addToCurrentMessages(ChatMessage msg) {
        if(currentMessages.size() > 24) {
            currentMessages.remove(0);
            currentMessages.add(msg);
        }
        else {
            currentMessages.add(msg);
        }
    }

    /**
     * Takes a given list and puts it in JSON format to be sent to the front end.
     * @param currentMessages A list of chat messages to be stringified.
     * @return A stringified version of the list of chat messages.
     */
    public String arrayListToJSON(List<ChatMessage> currentMessages) {
        String newList = "[";
        for(int i=0; i< currentMessages.size(); i++) {
            newList += ("{\"sender\":\"" + currentMessages.get(i).getSender() + "\",\"text\":\"" + currentMessages.get(i).getText() + "\",\"time\":\"" + currentMessages.get(i).getTime() + "\",\"imgUrl\":\"" + currentMessages.get(i).getImgUrl() +"\"}");
            if(!(i == currentMessages.size()-1)){
                newList += ",";
            }
        }
        newList += "]";
        System.out.println(newList);
        return newList;
    }

    /**
     * Takes a given list and puts it in JSON format to be sent to the front end.
     * @param currentOnline A list of UserWithImg objects to be stringified
     * @return Stringified version of the list of users.
     */
    public String userWithImgToJSON(List<UserWithImg> currentOnline) {
        String newList = "[";
        for(int i=0; i<currentOnline.size(); i++) {
            newList += ("{\"userName\":\"" + currentOnline.get(i).getUserName() + "\",\"imgUrl\":\"" + currentOnline.get(i).getImgUrl() +"\"}");
            if(!(i == currentOnline.size()-1)){
                newList += ",";
            }
        }
        newList += "]";
        return newList;
    }
}
