package com.matepay.events;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class EventDispatcherImpl implements EventDispatcher {
    private HashMap<String, Set<EventHandler>> handlers = new HashMap<>();

    @Override
    public void register(String eventName, EventHandler handler) throws Exceptions {
        if (handlers.containsKey(eventName)) {
            final var isRegistered = handlers.get(eventName).add(handler);
            if (!isRegistered) throw new Exceptions.EventHandlerAlreadyRegistered();
            return;
        }

        handlers.put(eventName, new HashSet<>() {{ add(handler); }});

    }

    @Override
    public void dispatch(Event event) {
        if (!handlers.containsKey(event.getName())) return;

        //This implementation is likely to lead to concurrency problems
        //Improve later.
        handlers.get(event.getName()).forEach(h -> h.handle(event));
    }

    @Override
    public boolean has(String eventName, EventHandler handler) {
        if (!handlers.containsKey(eventName)) return false;

        return handlers.get(eventName).contains(handler);
    }

    @Override
    public void remove(String eventName, EventHandler handler) throws Exceptions {
        if (!handlers.containsKey(eventName)) throw new Exceptions.EventNameNotRegistered();

        final var registeredHandlers = handlers.get(eventName);
        if (!registeredHandlers.contains(handler)) throw new Exceptions.EventHandlerNotRegistered();

        registeredHandlers.remove(handler);

        if (handlers.get(eventName).isEmpty()) handlers.remove(eventName);
    }

    @Override
    public void clear() {
        handlers.clear();
    }
}
