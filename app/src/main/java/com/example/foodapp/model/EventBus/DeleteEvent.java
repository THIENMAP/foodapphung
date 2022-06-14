package com.example.foodapp.model.EventBus;

public class DeleteEvent {
    public int pos;

    public DeleteEvent(int pos) {
        this.pos = pos;
    }

    public int getPos() {
        return pos;
    }
}
