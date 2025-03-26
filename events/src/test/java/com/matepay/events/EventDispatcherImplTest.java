package com.matepay.events;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EventDispatcherImplTest {
    EventDispatcherImpl sut;
    EventHandler handler1;
    EventHandler handler2;
    Event event1;
    Event event2;

    @BeforeEach
    void setup() {
        sut = new EventDispatcherImpl();
        handler1 = new EventHandlerImpl();
        handler2 = new EventHandlerImpl();
        event1 = new EventImpl("USER_REGISTERED");
        event2 = new EventImpl("ACCOUNT_REGISTERED");
    }

    @Nested
    @DisplayName("Register")
    class RegisterTests {
        @Test
        void shouldThrowWhenEventHandlerIsAlreadyRegistered() throws Exceptions {
            sut.register(event1.getName(), handler1);

            assertThrows(
                    Exceptions.EventHandlerAlreadyRegistered.class,
                    () -> sut.register(event1.getName(), handler1)
            );
        }

        @Test
        void shouldRegisterHandlerCorrectly() throws Exceptions {
            sut.register(event1.getName(), handler1);

            assertTrue(sut.has(event1.getName(), handler1));
        }
    }

    @Nested
    @DisplayName("Has")
    class HasTests {
        @Test
        void shouldReturnTrueWhenEventAndHandlerAreRegistered() throws Exceptions {
            sut.register(event1.getName(), handler1);

            assertTrue(sut.has(event1.getName(), handler1));
        }

        @Test
        void shouldReturnFalseWhenEventIsNotRegistered() {
            assertFalse(sut.has(event1.getName(), handler1));
        }

        @Test
        void shouldReturnFalseWhenEventExistsButHandlerIsNotRegistered() throws Exceptions {
            sut.register(event1.getName(), handler1);

            assertFalse(sut.has(event1.getName(), handler2));
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

    @Nested
    @DisplayName("Dispatch")
    class DispatchTests {
        @Test
        void shouldCallHandlersToHandle() throws Exceptions {
            handler1 = mock(EventHandler.class);
            sut.register(event1.getName(), handler1);

            sut.dispatch(event1);

            verify(handler1, times(1)).handle(event1);
        }
    }
}