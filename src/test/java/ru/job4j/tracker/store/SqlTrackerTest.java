package ru.job4j.tracker.store;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.job4j.tracker.model.Item;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;

import static org.assertj.core.api.Assertions.assertThat;

public class SqlTrackerTest {
    private static Connection connection;

    @BeforeAll
    public static void initConnection() {
        try (InputStream in = SqlTrackerTest.class.getClassLoader().getResourceAsStream("connection.properties")) {
            Properties config = new Properties();
            config.load(in);
            Class.forName(config.getProperty("driver-class-name"));
            connection = DriverManager.getConnection(
                    config.getProperty("url"),
                    config.getProperty("username"),
                    config.getProperty("password")
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @AfterAll
    public static void closeConnection() throws SQLException {
        if (connection != null) {
            connection.close();
        }
    }

    @AfterEach
    public void wipeTable() throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement("delete from items")) {
            statement.execute();
        }
    }

    @Test
    public void whenSaveItemAndFindByGeneratedIdThenMustBeTheSame() {
        SqlTracker tracker = new SqlTracker(connection);
        Item item = new Item("item");
        tracker.add(item);
        assertThat(tracker.findById(item.getId())).isEqualTo(item);
    }

    @Test
    public void whenFindAllThenGetAll() {
        SqlTracker tracker = new SqlTracker(connection);
        List<Item> items = List.of(
                new Item("First"),
                new Item("Second")
        );
        items.forEach(tracker::add);
        assertThat(tracker.findAll()).isEqualTo(items);
    }

    @Test
    public void whenFindByNameThenGetItemsTheSameName() {
        SqlTracker tracker = new SqlTracker(connection);
        List<Item> items = List.of(
                new Item("First"),
                new Item("Second"),
                new Item("First")
        );
        items.forEach(tracker::add);
        List<Item> expected = List.of(items.get(0), items.get(2));
        assertThat(tracker.findByName("First")).isEqualTo(expected);
    }

    @Test
    public void whenFindByIdThenGetItem() {
        SqlTracker tracker = new SqlTracker(connection);
        List<Item> items = List.of(
                new Item("First"),
                new Item("Second"),
                new Item("First")
        );
        items.forEach(tracker::add);
        assertThat(tracker.findById(items.get(1).getId())).isEqualTo(items.get(1));
    }

    @Test
    public void whenFindByIdNotFoundThenGetNull() {
        SqlTracker tracker = new SqlTracker(connection);
        List<Item> items = List.of(
                new Item("First"),
                new Item("Second"),
                new Item("First")
        );
        items.forEach(tracker::add);
        assertThat(tracker.findById(0)).isNull();
    }

    @Test
    public void whenReplaceItemThenItemHaveNewName() {
        SqlTracker tracker = new SqlTracker(connection);
        Item item = new Item("First");
        tracker.add(item);
        tracker.replace(item.getId(), new Item("Second"));
        assertThat(tracker.findById(item.getId()).getName()).isEqualTo("Second");
    }

    @Test
    public void whenDeleteItemThenGetNullFromId() {
        SqlTracker tracker = new SqlTracker(connection);
        Item item = new Item("First");
        tracker.add(item);
        tracker.delete(item.getId());
        assertThat(tracker.findById(item.getId())).isNull();
    }
}