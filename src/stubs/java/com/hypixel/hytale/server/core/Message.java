package com.hypixel.hytale.server.core;

public class Message {
    private final String text;

    private Message(String text) {
        this.text = text;
    }

    public static Message raw(String text) {
        return new Message(text);
    }

    public String getText() {
        return text;
    }
}
