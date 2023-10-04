package ru.job4j.tracker.demo;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import ru.job4j.tracker.model.Item;
import ru.job4j.tracker.model.Role;
import ru.job4j.tracker.model.User;
import ru.job4j.tracker.model.UserMessenger;

import java.util.List;

public class ParticipatesDemo {
    public static void main(String[] args) {
        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder().configure().build();
        try {
            SessionFactory sessionFactory = new MetadataSources(registry).buildMetadata().buildSessionFactory();
            Role role = new Role();
            role.setName("ADMIN");
            create(role, sessionFactory);
            User user = new User();
            user.setName("Admin Admin");
            user.setMessengers(List.of(
                    new UserMessenger(0, "tg", "@tg"),
                    new UserMessenger(0, "wa", "@wa")
            ));
            user.setRole(role);
            create(user, sessionFactory);
            Item item = new Item();
            item.setName("Learn Hibernate");
            item.setParticipates(List.of(user));
            create(item, sessionFactory);
            sessionFactory.openSession()
                    .createQuery("from Item where id = :id", Item.class)
                    .setParameter("id", item.getId())
                    .getSingleResult()
                    .getParticipates()
                    .forEach(System.out::println);
            delete(item, sessionFactory);
            delete(user, sessionFactory);
            delete(role, sessionFactory);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            StandardServiceRegistryBuilder.destroy(registry);
        }
    }

    private static <T> void create(T model, SessionFactory sessionFactory) {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        session.persist(model);
        session.getTransaction().commit();
        session.close();
    }

    private static <T> void delete(T model, SessionFactory sessionFactory) {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        session.remove(model);
        session.getTransaction().commit();
        session.close();
    }
}
