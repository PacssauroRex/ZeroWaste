package com.zerowaste.models.promotion;

import com.zerowaste.models.product.Product;

import java.time.LocalDate;
import java.util.Set;

import jakarta.persistence.*;

@Table(name = "promotions")
@Entity(name = "promotions")
public class Promotion {

    // Constructors
    public Promotion() {
    }

    public Promotion(Long id, Set<Product> products, String name, Double percentage, LocalDate startsAt, LocalDate endsAt) {
        this.id = id;
        this.products = products;
        this.name = name;
        this.percentage = percentage;
        this.startsAt = startsAt;
        this.endsAt = endsAt;
    }

    // Attributes
    @Id
    @SequenceGenerator(name = "promotions_id_seq", sequenceName = "promotions_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "promotions_id_seq")
    private Long id;

    @Column(name = "name", length = 100, nullable = false)
    private String name;

    @Column(name = "percentage", nullable = false)
    private Double percentage;

    @Column(name = "starts_at", nullable = false)
    private LocalDate startsAt;

    @Column(name = "ends_at", nullable = false)
    private LocalDate endsAt;

    @ManyToMany
    @JoinTable(name = "products_promotions", joinColumns = @JoinColumn(name = "promotion_id"), inverseJoinColumns = @JoinColumn(name = "product_id"))
    private Set<Product> products;

    // Getters and Setters
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

    public Double getPercentage() {
        return percentage;
    }

    public void setPercentage(Double percentage) {
        this.percentage = percentage;
    }

    public LocalDate getStartsAt() {
        return startsAt;
    }

    public void setStartsAt(LocalDate startsAt) {
        this.startsAt = startsAt;
    }

    public LocalDate getEndsAt() {
        return endsAt;
    }

    public void setEndsAt(LocalDate endsAt) {
        this.endsAt = endsAt;
    }

    public Set<Product> getProducts() {
        return products;
    }

    public void setProducts(Set<Product> products) {
        this.products = products;
    }

    // Complementary Methods
    public void addProduct(Product product) {
        this.products.add(product);
    }

    public void removeProduct(Product product) {
        this.products.remove(product);
    }
}
