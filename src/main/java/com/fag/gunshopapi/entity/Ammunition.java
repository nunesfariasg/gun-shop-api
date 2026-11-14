package com.fag.gunshopapi.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "ammunitions")
public class Ammunition {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 100)
    private String brand;

    @Column(nullable = false, length = 50)
    private String caliber;

    @Column(name = "quantity_per_box", nullable = false)
    private Integer quantityPerBox;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @Column(nullable = false)
    private Integer stock;

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getBrand() { return brand; }
    public void setBrand(String brand) { this.brand = brand; }
    public String getCaliber() { return caliber; }
    public void setCaliber(String caliber) { this.caliber = caliber; }
    public Integer getQuantityPerBox() { return quantityPerBox; }
    public void setQuantityPerBox(Integer quantityPerBox) { this.quantityPerBox = quantityPerBox; }
    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }
    public Integer getStock() { return stock; }
    public void setStock(Integer stock) { this.stock = stock; }
}
