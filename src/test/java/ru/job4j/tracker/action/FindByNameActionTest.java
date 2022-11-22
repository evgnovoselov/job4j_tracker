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

public class FindByNameActionTest {
    @Test
    public void whenFindByNameOneItemThenGetOneItem() {
        Output output = new StubOutput();
        Store tracker = new MemTracker();
        String findName = "Item for find by name!";
        Item item = tracker.add(new Item(findName));
        FindByNameAction action = new FindByNameAction(output);
        Input input = mock(Input.class);
        when(input.askStr(any(String.class))).thenReturn(findName);
        action.execute(input, tracker);
        String ln = System.lineSeparator();
        assertEquals("=== Find items by name ===" + ln + item + ln, output.toString());
    }

    @Test
    public void whenFindByNameTwoItemsThenGetTwoItems() {
        Output output = new StubOutput();
        Store tracker = new MemTracker();
        String findName = "Item for find by name!";
        Item itemFirst = tracker.add(new Item(findName));
        Item itemSecond = tracker.add(new Item(findName));
        FindByNameAction action = new FindByNameAction(output);
        Input input = mock(Input.class);
        when(input.askStr(any(String.class))).thenReturn(findName);
        action.execute(input, tracker);
        String ln = System.lineSeparator();
        assertEquals("=== Find items by name ===" + ln + itemFirst + ln + itemSecond + ln, output.toString());
    }

    @Test
    public void whenNotFindByNameThenEmptyAnswer() {
        Output output = new StubOutput();
        Store tracker = new MemTracker();
        String findName = "Item for find by name!";
        Item itemFirst = tracker.add(new Item("Not find this item"));
        FindByNameAction action = new FindByNameAction(output);
        Input input = mock(Input.class);
        when(input.askStr(any(String.class))).thenReturn(findName);
        action.execute(input, tracker);
        String ln = System.lineSeparator();
        assertEquals("=== Find items by name ===" + ln, output.toString());
    }
}