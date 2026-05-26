package com.tommy.bookstore.entity;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.util.List;

public class BookstoreService {
    private SessionFactory factory =
            HibernateUtil.getFactory();

    public void createPublisher (Publisher publisher) {
        Session session = factory.getCurrentSession();
        session.beginTransaction();
        session.save(publisher);
        session.getTransaction().commit();
    }

    public void createMagazine (Magazine magazine) {
        Session session = factory.getCurrentSession();
        session.beginTransaction();
        session.save(magazine);
        session.getTransaction().commit();
    }

    public List<Magazine> getAllMagazines (long ID) {
        Session session = factory.getCurrentSession();
        session.beginTransaction();
        List <Magazine> list = session.createQuery("from Magazine m where m.publisher.id = :id", Magazine.class).setParameter("id", ID).getResultList();
        session.getTransaction().commit();
        return list;
    }

    public Publisher findPublisherById (Long id) {
        Session session = factory.getCurrentSession();

        session.beginTransaction();
        Publisher publisher = session.get(Publisher.class, id);
        session.getTransaction().commit();
        return publisher;
    }



}