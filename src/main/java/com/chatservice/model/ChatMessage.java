package com.chatservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class ChatMessage {

    private String sender;
    private String text;
    private String time;
    private String imgUrl;

    public ChatMessage(String sender, String text, String imgUrl) {
        this.sender = sender;
        this.text = text;
        this.imgUrl = imgUrl;
    }
}
