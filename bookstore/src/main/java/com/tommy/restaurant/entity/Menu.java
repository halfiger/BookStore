package com.tommy.restaurant.entity;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name ="menu")
public class Menu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany (mappedBy = "menu",
                cascade = CascadeType.ALL,
                fetch = FetchType.LAZY,
                orphanRemoval = true)
    private List<Dish> dishes;

    private String name;

    public Menu () {}

    public Menu (String name) {
        this.name = name;
    }

    public void setDish (Dish dish) {
        if (dishes == null) {
            dishes = new ArrayList<>();
        }
        dishes.add(dish);
        dish.setMenu(this);
    }

    public List <Dish> getDishes () {
        return dishes;
    }


    public void removeDish (Dish dish) {
        dish.setMenu(null);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
