package ru.job4j.tracker.action;

import org.junit.jupiter.api.Test;
import ru.job4j.tracker.input.Input;
import ru.job4j.tracker.model.Item;
import ru.job4j.tracker.output.Output;
import ru.job4j.tracker.output.StubOutput;
import ru.job4j.tracker.store.MemTracker;
import ru.job4j.tracker.store.Store;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class FindByIdActionTest {

    @Test
    public void whenFindByIdItemThenGetItem() {
        Output output = new StubOutput();
        Store tracker = new MemTracker();
        Item item = tracker.add(new Item("Item for find!"));
        FindByIdAction findByIdAction = new FindByIdAction(output);
        Input input = mock(Input.class);
        when(input.askInt(any(String.class))).thenReturn(item.getId());
        findByIdAction.execute(input, tracker);
        String ln = System.lineSeparator();
        assertThat(output.toString()).isEqualTo("=== Find item by Id ===" + ln + item + ln);
    }

    @Test
    public void whenFindByIdItemNotFoundThenWrong() {
        Output output = new StubOutput();
        Store tracker = new MemTracker();
        tracker.add(new Item("Item not found!"));
        FindByIdAction findByIdAction = new FindByIdAction(output);
        Input input = mock(Input.class);
        findByIdAction.execute(input, tracker);
        String ln = System.lineSeparator();
        assertThat(output.toString()).isEqualTo("=== Find item by Id ===" + ln + "Wrong id! Not found" + ln);
    }
}