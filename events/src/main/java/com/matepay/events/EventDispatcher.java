package com.matepay.events;

public interface EventDispatcher {
    void register(String eventName, EventHandler handler) throws Exceptions;
    void dispatch(Event event);
    boolean has(String eventName, EventHandler handler);
    void remove(String eventName, EventHandler handler) throws Exceptions;
    void clear();
}
