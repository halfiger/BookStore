package com.tommy.restaurant.entity;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateUtil {
    private static final SessionFactory factory;

    static {
        factory = new Configuration()
                .configure("hibernate.cfg.xml")
                .addAnnotatedClass(Menu.class)
                .addAnnotatedClass(Dish.class)
                .buildSessionFactory();
    }

    public static SessionFactory getFactory () {
        return factory;
    }
}