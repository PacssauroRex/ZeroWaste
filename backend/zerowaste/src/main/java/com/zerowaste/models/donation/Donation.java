package com.zerowaste.models.donation;

import java.time.LocalDate;
import java.util.List;

import com.zerowaste.models.product.Product;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

@Table(name = "donations")
@Entity(name = "donations")
public class Donation {
    
    //Constructors
    public Donation () {}

    public Donation(Long id, String name, List<Product> products, LocalDate date, LocalDate createdAt, LocalDate updatedAt, LocalDate deletedAt) {
        this.id = id;
        this.name = name;
        this.products = products;
        this.date = date;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.deletedAt = deletedAt;
    }    

    //Attributes
    @Id
    @SequenceGenerator(name = "donation_id_seq", sequenceName = "donation_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "donation_id_seq")
    private Long id;

    @Column(name = "name")
    private String name;

    @ManyToMany
    @JoinTable(
        name = "products_donations", 
        joinColumns = @JoinColumn(name = "donation_id"), 
        inverseJoinColumns = @JoinColumn(name = "product_id")
    )
    private List<Product> products;

    @Column(name = "date")
    private LocalDate date;

    @Column(name = "createdAt")
    private LocalDate createdAt;

    @Column(name = "updatedAt", nullable = true)
    private LocalDate updatedAt;

    @Column(name = "deletedAt", nullable = true)
    private LocalDate deletedAt;

    //Getters and setters
    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Product> getProducts() {
        return this.products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public LocalDate getDate() {
        return this.date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalDate getCreatedAt() {
        return this.createdAt;
    }

    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDate getUpdatedAt() {
        return this.updatedAt;
    }

    public void setUpdatedAt(LocalDate updatedAt) {
        this.updatedAt = updatedAt;
    }

    public LocalDate getDeletedAt() {
        return this.deletedAt;
    }

    public void setDeletedAt(LocalDate deletedAt) {
        this.deletedAt = deletedAt;
    }
}
