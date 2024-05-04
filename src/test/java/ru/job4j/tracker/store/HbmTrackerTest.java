package ru.job4j.tracker.store;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.job4j.tracker.model.Item;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class HbmTrackerTest {
    private static HbmTracker tracker;

    @BeforeAll
    static void beforeAll() {
        tracker = new HbmTracker();
        clearData();
    }

    private static void clearData() {
        tracker.findAll().forEach(item -> tracker.delete(item.getId()));
    }

    @AfterAll
    static void afterAll() {
        tracker.close();
    }

    @BeforeEach
    void setUp() {
        clearData();
    }

    @Test
    void whenAddNewItemThenTrackerHasSameItem() {
        Item item = new Item("test 1");
        tracker.add(item);
        Item actualItem = tracker.findById(item.getId());
        assertThat(actualItem.getName()).isEqualTo(item.getName());
    }

    @Test
    void whenFindByIdThenReturn() {
        Item item = new Item("Name Item");
        tracker.add(item);

        Item actualItem = tracker.findById(item.getId());

        assertThat(actualItem.getName()).isEqualTo("Name Item");
    }

    @Test
    void whenFindByIdNotHaveThenReturnNull() {
        Item actualItem = tracker.findById(1);

        assertThat(actualItem).isNull();
    }

    @Test
    void whenFindAllThenReturnAll() {
        List<Item> items = List.of(
                new Item("Item 1"),
                new Item("Item 2"),
                new Item("Item 3")
        );
        items.forEach(tracker::add);

        List<Item> actualItems = tracker.findAll();

        List<Item> expectedItems = List.of(
                new Item("Item 1"),
                new Item("Item 2"),
                new Item("Item 3")
        );
        assertThat(
                actualItems.stream().map(Item::getName).toList()
        ).isEqualTo(
                expectedItems.stream().map(Item::getName).toList()
        );
    }

    @Test
    void whenFindAllEmpty() {

        List<Item> actualItems = tracker.findAll();

        assertThat(actualItems).isEqualTo(List.of());
    }

    @Test
    void whenDeleteThenReturnTrueAndNotHaveElement() {
        List<Item> items = List.of(
                new Item("Item 1"),
                new Item("Item 2"),
                new Item("Item 3")
        );
        items.forEach(tracker::add);

        boolean isDeleted = tracker.delete(items.get(1).getId());
        List<Item> actualItems = tracker.findAll();

        List<Item> expectedItems = List.of(
                new Item("Item 1"),
                new Item("Item 3")
        );
        assertThat(isDeleted).isTrue();
        assertThat(
                actualItems.stream().map(Item::getName).toList()
        ).isEqualTo(
                expectedItems.stream().map(Item::getName).toList()
        );
    }

    @Test
    void whenDeleteNotElementIdThenReturnFalseAndNotDeletedElement() {
        Item item = new Item("Item 1");
        tracker.add(item);

        boolean isDeleted = tracker.delete(item.getId() + 1);
        List<Item> actualItems = tracker.findAll();

        List<Item> expectedItems = List.of(
                new Item("Item 1")
        );
        assertThat(isDeleted).isFalse();
        assertThat(
                actualItems.stream().map(Item::getName).toList()
        ).isEqualTo(
                expectedItems.stream().map(Item::getName).toList()
        );
    }

    @Test
    void whenReplaceItemThenItemHaveNewName() {
        Item item = new Item("Item 1");
        tracker.add(item);
        Item replaceItem = new Item("New Item Name");

        boolean isReplaced = tracker.replace(item.getId(), replaceItem);
        Item actualItem = tracker.findById(replaceItem.getId());

        assertThat(isReplaced).isTrue();
        assertThat(actualItem.getName()).isEqualTo("New Item Name");
    }

    @Test
    void whenNotHaveReplaceItemThenReplacedFalse() {
        Item replaceItem = new Item("New Item Name");

        boolean isReplaced = tracker.replace(1, replaceItem);

        assertThat(isReplaced).isFalse();
    }

    @Test
    void whenFindByNameThenReturnElementsLikeName() {
        List<Item> items = List.of(
                new Item("Item 1"),
                new Item("Second"),
                new Item("Item 3")
        );
        items.forEach(tracker::add);

        List<Item> actualItems = tracker.findByName("tem");

        List<Item> expectedItems = List.of(
                new Item("Item 1"),
                new Item("Item 3")
        );
        assertThat(
                actualItems.stream().map(Item::getName).toList()
        ).isEqualTo(
                expectedItems.stream().map(Item::getName).toList()
        );
    }
}