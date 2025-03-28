package com.matepay.events;

public class Exceptions extends Exception {
    public static class EventHandlerAlreadyRegistered extends Exceptions{}
    public static class EventHandlerNotRegistered extends Exceptions{}
    public static class EventNameNotRegistered extends Exceptions{}
}
