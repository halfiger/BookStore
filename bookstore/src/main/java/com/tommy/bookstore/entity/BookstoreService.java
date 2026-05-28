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
        List <Magazine> list = session
                .createQuery("from Magazine m where m.publisher.id = :id",
                        Magazine.class)
                .setParameter("id", ID)
                .getResultList();
        session.getTransaction().commit();
        return list;
    }
    //task4
    public Publisher findPublisherById (Long id) {
        Session session = factory.getCurrentSession();
        session.beginTransaction();
        Publisher publisher = session.get(Publisher.class, id);
        publisher.getMagazines().size();//хитрість, спеціально команда підвантажує додатково ще і магазини які не завантажилися через lazy:тригерить lazy loading
        session.getTransaction().commit();
        return publisher;
    }


    //task5
    public Publisher findPublisherWithMagazinesAndFetching (Long ID) {
        Session session = factory.getCurrentSession();
        session.beginTransaction();
        Publisher publisher = session.createQuery(
         "SELECT p FROM Publisher p JOIN FETCH p.magazines WHERE p.id = :id",
                        Publisher.class)
                .setParameter("id", ID)
                .uniqueResult();
        session.getTransaction().commit();
        return publisher;
    }

    //task6
    public List <Publisher> findAllpublisher () {
        Session session = factory.getCurrentSession();
        session.beginTransaction();
        List <Publisher> list = session.createQuery("FROM Publisher", Publisher.class).getResultList();
        for (Publisher p : list) {
            p.getMagazines().size(); // підвантажили журнали
        }
        session.getTransaction().commit();
        return list;
    }
}