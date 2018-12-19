package com.example.mhmd.motepad;

public class MessageEvent {

    public final String message;
    public  int itemIndex ;
    public String itemName;

    public MessageEvent(String message) {
        this.message = message;
    }

    public MessageEvent(String message, int itemIndex) {
        this.message = message;
        this.itemIndex = itemIndex;
    }
}


