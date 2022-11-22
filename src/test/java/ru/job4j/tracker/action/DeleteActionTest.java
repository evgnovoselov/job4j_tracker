package ru.job4j.tracker.action;

import org.junit.Test;
import org.mockito.Mockito;
import ru.job4j.tracker.input.Input;
import ru.job4j.tracker.model.Item;
import ru.job4j.tracker.output.Output;
import ru.job4j.tracker.output.StubOutput;
import ru.job4j.tracker.store.MemTracker;
import ru.job4j.tracker.store.Store;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class DeleteActionTest {

    @Test
    public void whenDeleteItemThenDeleted() {
        Output output = new StubOutput();
        Store tracker = new MemTracker();
        tracker.add(new Item("Item for delete"));
        DeleteAction deleteAction = new DeleteAction(output);
        Input input = mock(Input.class);
        when(input.askInt(any(String.class))).thenReturn(1);
        deleteAction.execute(input, tracker);
        String ln = System.lineSeparator();
        assertEquals("=== Delete item ===" + ln + "Item is successfully deleted!" + ln, output.toString());
    }

    @Test
    public void whenDeleteNotHaveItemThenWrong() {
        Output output = new StubOutput();
        Store tracker = new MemTracker();
        DeleteAction deleteAction = new DeleteAction(output);
        Input input = mock(Input.class);
        deleteAction.execute(input, tracker);
        String ln = System.lineSeparator();
        assertEquals("=== Delete item ===" + ln + "Wrong id!" + ln, output.toString());
    }
}