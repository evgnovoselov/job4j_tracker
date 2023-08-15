package ru.job4j.tracker.store;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import ru.job4j.tracker.model.Item;

import java.util.List;
import java.util.function.Function;

public class HbmTracker implements Store, AutoCloseable {
    private final StandardServiceRegistry registry = new StandardServiceRegistryBuilder().configure().build();
    private final SessionFactory sf = new MetadataSources(registry).buildMetadata().buildSessionFactory();

    @Override
    public void init() {

    }

    @Override
    public Item add(Item item) {
        return fromTransaction(session -> {
            session.save(item);
            return item;
        });
    }

    @Override
    public boolean replace(int id, Item item) {
        item.setId(id);
        return fromTransaction(session -> {
            session.update(item);
            return true;
        }) != null;
    }

    @Override
    public boolean delete(int id) {
        return fromTransaction(session -> {
            Item item = new Item();
            item.setId(id);
            session.delete(item);
            return true;
        }) != null;
    }

    @Override
    public List<Item> findAll() {
        return fromTransaction(session -> session.createQuery("from Item ", Item.class).list());
    }

    @Override
    public List<Item> findByName(String key) {
        return fromTransaction(session -> session.createQuery("from Item where name like :key", Item.class)
                .setParameter("key", "%" + key + "%")
                .list());
    }

    @Override
    public Item findById(int id) {
        return fromTransaction(session -> session.get(Item.class, id));
    }

    @Override
    public void close() {
        StandardServiceRegistryBuilder.destroy(registry);
    }

    private <R> R fromTransaction(Function<Session, R> action) {
        R result = null;
        Session session = sf.openSession();
        try (session) {
            session.beginTransaction();
            R actual = action.apply(session);
            session.getTransaction().commit();
            result = actual;
        } catch (Exception e) {
            session.getTransaction().rollback();
        }
        return result;
    }
}
