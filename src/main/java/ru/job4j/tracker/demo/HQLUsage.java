package ru.job4j.tracker.demo;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.query.Query;
import ru.job4j.tracker.model.Item;

public class HQLUsage {
    public static void main(String[] args) {
        StandardServiceRegistry registry = new StandardServiceRegistryBuilder().configure().build();
        try (SessionFactory sessionFactory = new MetadataSources(registry).buildMetadata().buildSessionFactory()) {
            Session session = sessionFactory.openSession();
            Item item = new Item("New Item 111122223333");
            create(session, item);
            Query<Item> query = session.createQuery("from Item", Item.class);
            for (Item value : query.list()) {
                System.out.println(value);
            }
            unique(session, item.getId());
            update(session, item.getId());
            unique(session, item.getId());
            delete(session, item.getId());
            all(session);
            item = new Item("New Item 111122223333");
            insert(session, item);
            all(session);
            unique(session, item.getId());
            delete(session, item.getId());
            session.close();
        } finally {
            StandardServiceRegistryBuilder.destroy(registry);
        }
    }

    public static Item create(Session session, Item item) {
        try {
            session.beginTransaction();
            session.save(item);
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
        }
        return item;
    }

    public static void all(Session session) {
        session.beginTransaction();
        Query<Item> query = session.createQuery("from Item", Item.class);
        for (Item item : query.list()) {
            System.out.println(item);
        }
        session.getTransaction().commit();
    }

    public static void unique(Session session, int id) {
        session.beginTransaction();
        Query<Item> query = session.createQuery("from Item as i where i.id = :id", Item.class);
        query.setParameter("id", id);
        System.out.println(query.uniqueResult());
        session.getTransaction().commit();
    }

    public static void update(Session session, int id) {
        try {
            session.beginTransaction();
            session.createQuery("update Item set name = :name where id = :id")
                    .setParameter("name", "new name Item")
                    .setParameter("id", id)
                    .executeUpdate();
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
        }
    }

    public static void delete(Session session, int id) {
        try {
            session.beginTransaction();
            session.createQuery("delete Item where id = :id")
                    .setParameter("id", id)
                    .executeUpdate();
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
        }
    }

    public static void insert(Session session, Item item) {
        try {
            session.beginTransaction();
            session.save(item);
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
        }
    }
}

