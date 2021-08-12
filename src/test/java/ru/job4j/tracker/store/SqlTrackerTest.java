package ru.job4j.tracker.store;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import ru.job4j.tracker.model.Item;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.*;

public class SqlTrackerTest {
    private static Connection connection;

    @BeforeClass
    public static void initConnection() {
        try (InputStream in = SqlTrackerTest.class.getClassLoader().getResourceAsStream("test.properties")) {
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

    @AfterClass
    public static void closeConnection() throws SQLException {
        if (connection != null) {
            connection.close();
        }
    }

    @After
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
        assertThat(tracker.findById(item.getId()), is(item));
    }

    @Test
    public void whenFindAllThenGetAll() {
        SqlTracker tracker = new SqlTracker(connection);
        List<Item> items = List.of(
                new Item("First"),
                new Item("Second")
        );
        items.forEach(tracker::add);
        assertThat(tracker.findAll(), is(items));
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
        assertThat(tracker.findByName("First"), is(expected));
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
        assertThat(tracker.findById(items.get(1).getId()), is(items.get(1)));
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
        assertThat(tracker.findById(0), is(nullValue()));
    }

    @Test
    public void whenReplaceItemThenItemHaveNewName() {
        SqlTracker tracker = new SqlTracker(connection);
        Item item = new Item("First");
        tracker.add(item);
        tracker.replace(item.getId(), new Item("Second"));
        assertThat(tracker.findById(item.getId()).getName(), is("Second"));
    }
}