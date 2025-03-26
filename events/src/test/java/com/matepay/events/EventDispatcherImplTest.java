package com.matepay.events;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class EventDispatcherImplTest {
    EventDispatcherImpl sut;
    EventHandlerImpl handler1;
    EventHandlerImpl handler2;
    EventImpl event1;
    EventImpl event2;

    @BeforeEach
    void setup() {
        sut = new EventDispatcherImpl();
        handler1 = new EventHandlerImpl();
        handler2 = new EventHandlerImpl();
        event1 = new EventImpl();
        event2 = new EventImpl();
    }

    @Nested
    @DisplayName("Register")
    class RegisterTests {
        @Test
        void shouldThrowWhenEventHandlerIsAlreadyRegistered() throws Exceptions {
            String eventName = "USER_REGISTERED";
            sut.register(eventName, handler1);

            assertThrows(
                    Exceptions.EventHandlerAlreadyRegistered.class,
                    () -> sut.register(eventName, handler1)
            );
        }

        @Test
        void shouldRegisterHandlerCorrectly() throws Exceptions {
            String eventName = "USER_REGISTERED";
            sut.register(eventName, handler1);

            assertTrue(sut.has(eventName, handler1));
        }
    }

    @Nested
    @DisplayName("Has")
    class HasTests {
        @Test
        void shouldReturnTrueWhenEventAndHandlerAreRegistered() throws Exceptions {
            String eventName = "USER_REGISTERED";
            sut.register(eventName, handler1);

            assertTrue(sut.has(eventName, handler1));
        }

        @Test
        void shouldReturnFalseWhenEventIsNotRegistered() {
            String eventName = "USER_REGISTERED";

            assertFalse(sut.has(eventName, handler1));
        }

        @Test
        void shouldReturnFalseWhenEventExistsButHandlerIsNotRegistered() throws Exceptions {
            String eventName = "USER_REGISTERED";
            sut.register(eventName, handler1);

            assertFalse(sut.has(eventName, handler2));
        }
    }

    @Nested
    @DisplayName("Clear")
    class ClearTests {
        @Test
        void shouldClearAllRegisteredHandlers() throws Exceptions, NoSuchFieldException, IllegalAccessException {
            Field handlersField = EventDispatcherImpl.class.getDeclaredField("handlers");
            handlersField.setAccessible(true);
            sut.register("USER_REGISTERED", handler1);
            sut.register("ACCOUNT_REGISTERED", handler1);

            sut.clear();

            final var handlers = (HashMap<String, Set<EventHandlerImpl>>) handlersField.get(sut);
            assertEquals(0, handlers.size());
        }
    }

}