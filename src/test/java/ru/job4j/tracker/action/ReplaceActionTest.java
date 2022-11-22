package ru.job4j.tracker.action;

import org.junit.Test;
import ru.job4j.tracker.input.Input;
import ru.job4j.tracker.model.Item;
import ru.job4j.tracker.output.Output;
import ru.job4j.tracker.output.StubOutput;
import ru.job4j.tracker.store.MemTracker;
import ru.job4j.tracker.store.Store;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class ReplaceActionTest {
    @Test
    public void whenReplaceItemThenReplaced() {
        Output output = new StubOutput();
        Store tracker = new MemTracker();
        tracker.add(new Item("Replaced item"));
        String replacedName = "New item name";
        ReplaceAction rep = new ReplaceAction(output);
        Input input = mock(Input.class);
        when(input.askInt(any(String.class))).thenReturn(1);
        when(input.askStr(any(String.class))).thenReturn(replacedName);
        rep.execute(input, tracker);
        String ln = System.lineSeparator();
        assertEquals("=== Edit item ===" + ln + "Item is successfully replaced!" + ln, output.toString());
        assertEquals(replacedName, tracker.findAll().get(0).getName());
    }

    @Test
    public void whenNotHaveItemReplaceThenTextWrong() {
        Output output = new StubOutput();
        Store tracker = new MemTracker();
        ReplaceAction rep = new ReplaceAction(output);
        Input input = mock(Input.class);
        rep.execute(input, tracker);
        String ln = System.lineSeparator();
        assertEquals("=== Edit item ===" + ln + "Item with id=0 not found." + ln, output.toString());
    }
}