package com.tommy.restaurant.entity;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.util.List;

public class RestaurantService {
    SessionFactory factory = HibernateUtil.getFactory();

    public void createMenu (Menu menu) {
        Session session = factory.getCurrentSession();
        session.beginTransaction();
        session.save(menu);
        session.getTransaction().commit();
    }

    public void createDish(Dish dish) {
        Session session = factory.getCurrentSession();
        session.beginTransaction();
        session.save(dish);
        session.getTransaction().commit();
    }

    public List<Dish> getAllDishes (long ID) {
        Session session = factory.getCurrentSession();
        session.beginTransaction();
        List <Dish> list = session
                .createQuery("FROM DISH d WHERE d.id = :id", Dish.class)
                .setParameter("id", ID)
                .getResultList();
        session.getTransaction().commit();
        return list;
    }

    public Menu findById (long ID) {
        Session session = factory.getCurrentSession();
        session.beginTransaction();
        Menu menu = session.get(Menu.class, ID);
        //menu.getDishes().size(); // читрість, виконуємо трігєр для витягування рецептів які є lazy і не витягуються автоматично
        session.getTransaction().commit();
        return menu;
    }

    //5
    public Menu getMenuWtRecepiesAndFetch (long id) {
        Session session = factory.getCurrentSession();
        session.getTransaction().commit();
        Menu menu = session.createQuery("SELECT m FROM MENU m JOIN FETCH m.dishes where id = :id", Menu.class)
                .setParameter("id", id).uniqueResult();
        session.getTransaction().commit();
        return menu;
    }

    //6
    public List <Menu> getAllMenusWtRecepies () {
        Session session = factory.getCurrentSession();
        session.getTransaction().commit();
        List <Menu> list = session.createQuery("FROM Menu", Menu.class).getResultList();

        for (Menu m : list) {
            list.size(); // підвантажили рецепти
        }
        session.getTransaction().commit();
        return list;
    }

    //7 problem n+1

    public List <Menu> NplusOneProblemDessision () {
        Session session = factory.getCurrentSession();
        session.getTransaction().commit();
        List <Menu> list = session
                .createQuery("Select distinct m FROM Menu m JOIN FETCH m.dishes", Menu.class)
                .getResultList();

        session.getTransaction().commit();
        return list;
    }
}