package com.tommy.bookstore.entity;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="publishers")
public class Publisher {



    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    @OneToMany (mappedBy = "publisher", cascade = CascadeType.ALL, fetch = FetchType.LAZY)   ///  хто володар relation --> publisher!
    private List<Magazine> magazines;

    public Publisher() {}

    public Publisher(String name) {
        this.name = name;
    }

    public void addMagazine(Magazine magazine) {

        if (magazines == null) {
            magazines = new ArrayList<>();
        }

        magazines.add(magazine);

        magazine.setPublisher(this);
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

    public void setMagazines (Magazine newMagazine) {
        if (magazines == null) {
            magazines = new ArrayList<>();
        }
        magazines.add(newMagazine);
    }

    @Override
    public String toString() {
        return "Publisher{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
