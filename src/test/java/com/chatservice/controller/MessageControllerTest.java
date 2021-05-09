package com.chatservice.controller;

import com.chatservice.model.ChatMessage;
import com.chatservice.model.UserWithImg;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class MessageControllerTest {

    public MessageController controller;

    @Spy
    public MessageController spyController;

    @BeforeEach
    void setUp() {
        controller = new MessageController();
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void send() throws Exception{
        //arrange
        ChatMessage input = new ChatMessage("user", "hello", "google.png");
        ChatMessage expected = new ChatMessage("user", "hello", new SimpleDateFormat("HH:mm").format(new Date()), "google.png");

        List<ChatMessage> expectedList = new ArrayList<>();
        expectedList.add(expected);

        //act
        ChatMessage actualReturn = controller.send(input);

        //assert
        assertEquals(expected, actualReturn);
        assertEquals(expectedList, controller.getCurrentMessages());
    }

    @Test
    void sendOnlineUsers() throws Exception{
        //arrange
        UserWithImg input = new UserWithImg("user", "profile.png",1);
        List<UserWithImg> list = new ArrayList<>();
        list.add(input);
        String expected = controller.userWithImgToJSON(list);

        //act
        String actualReturn = spyController.sendOnlineUsers(input);

        //assert
        Mockito.verify(spyController).userWithImgToJSON(list);
        assertEquals(expected, actualReturn);
    }

    @Test
    void disconnectUser() throws Exception{
        //arrange
        String input = "user";
        List<String> list = new ArrayList<>();
        list.add(input);

        List<UserWithImg> expectedList = new ArrayList<>();
        String expected = controller.userWithImgToJSON(expectedList);

        //act
        String actualReturn = spyController.disconnectUser(input);

        //assert
        Mockito.verify(spyController).userWithImgToJSON(expectedList);
        assertEquals(expected, actualReturn);
    }

    @Test
    void sendOldMessages() throws Exception{
        //arrange
        List<ChatMessage> expected = new ArrayList<>();
        expected.add(new ChatMessage("user", "hello", "14:26", "profile.png"));
        controller.setCurrentMessages(expected);
        String expectedReturn = controller.arrayListToJSON(expected);

        //act
        String actual = controller.sendOldMessages("");

        //assert
        assertEquals(expectedReturn, actual);
    }

    @Test
    void newTyping() throws Exception{
        //arrange
        String typer = "user";
        List<String> typers = new ArrayList<>();
        typers.add(typer);
        controller.setCurrentTyping(typers);
        String expected = typers.toString();

        //act
        String actual = controller.newTyping("user");

        //assert
        assertEquals(typers, controller.getCurrentTyping());
        assertEquals(expected, actual);
    }

    @Test
    void removeTyping() throws Exception{
        //arrange
        String typer = "user";
        List<String> typers = new ArrayList<>();
        typers.add(typer);
        controller.setCurrentTyping(typers);
        List<String> emptyTypers = new ArrayList<>();
        String expected = emptyTypers.toString();

        //act
        String actual = controller.removeTyping("user");

        //assert
        assertEquals(emptyTypers, controller.getCurrentTyping());
        assertEquals(expected, actual);
    }

    @Test
    void addToCurrentMessages() {
        //arrange
        ChatMessage msgToAdd = new ChatMessage("user", "hello", "14:26", "profile.png");
        List<ChatMessage> expected = new ArrayList<>();
        expected.add(msgToAdd);

        //act
        controller.addToCurrentMessages(msgToAdd);

        //assert
        assertEquals(expected, controller.getCurrentMessages());
    }

    @Test
    void arrayListToJSON() {
        //arrange
        List<ChatMessage> input = new ArrayList<>();
        input.add(new ChatMessage("user", "hello", "14:26", "profile.png"));
        String expected = "[{\"sender\":\"user\",\"text\":\"hello\",\"time\":\"14:26\",\"imgUrl\":\"profile.png\"}]";

        //act
        String actual = controller.arrayListToJSON(input);

        //assert
        assertEquals(expected, actual);
    }

    @Test
    void userWithImgToJSON() {
        //arrange
        List<UserWithImg> input = new ArrayList<>();
        input.add(new UserWithImg("user", "profile.png",1));
        String expected = "[{\"userName\":\"user\",\"userID\":\"1\",\"imgUrl\":\"profile.png\"}]";

        //act
        String actual = controller.userWithImgToJSON(input);

        //assert
        assertEquals(expected, actual);
    }
}