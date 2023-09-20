package ru.job4j.tracker.demo;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import ru.job4j.tracker.model.Role;
import ru.job4j.tracker.model.User;

import java.util.List;

public class HbmToOneDemo {
    public static void main(String[] args) {
        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder().configure().build();
        try {
            SessionFactory sf = new MetadataSources(registry).buildMetadata().buildSessionFactory();
            Role role = new Role();
            role.setName("ADMIN");
            create(role, sf);
            User user = new User();
            user.setName("Admin Admin");
            user.setRole(role);
            create(user, sf);
            findAll(User.class, sf).forEach(System.out::println);
            delete(user, sf);
            delete(role, sf);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            StandardServiceRegistryBuilder.destroy(registry);
        }
    }

    private static <T> void create(T model, SessionFactory sf) {
        Session session = sf.openSession();
        session.beginTransaction();
        session.persist(model);
        session.getTransaction().commit();
        session.close();
    }

    private static <T> List<T> findAll(Class<T> cl, SessionFactory sf) {
        Session session = sf.openSession();
        session.beginTransaction();
        List<T> list = session.createQuery("from " + cl.getName(), cl).list();
        session.getTransaction().commit();
        session.close();
        return list;
    }

    private static void delete(Object obj, SessionFactory sf) {
        Session session = sf.openSession();
        session.beginTransaction();
        session.remove(obj);
        session.getTransaction().commit();
        session.close();
    }
}
