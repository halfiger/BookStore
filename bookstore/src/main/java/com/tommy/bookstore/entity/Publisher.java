package com.tommy.bookstore.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;

import java.util.List;


@Entity
public class Publisher {
    Long id;
    String name;
    @OneToMany
    @JoinColumn(table="my_db.magazine")
    List<Magazine> magazines;

    public Publisher() {}

    public Publisher(String name, List<Magazine> magazines) {
        this.name = name;
        this.magazines = magazines;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Magazine> getMagazines() {
        return magazines;
    }

    public void setMagazines(List<Magazine> magazines) {
        this.magazines = magazines;
    }

    @Override
    public String toString() {
        return "Publisher{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", magazines=" + magazines +
                '}';
    }
}
