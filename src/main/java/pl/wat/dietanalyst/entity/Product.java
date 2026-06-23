package pl.wat.dietanalyst.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 140)
    private String name;
    private String category;
    private double kcal;
    private double protein;
    private double carbs;
    private double fat;

    public Product() {}
    public Product(String name, String category, double kcal, double protein, double carbs, double fat) {
        this.name = name; this.category = category; this.kcal = kcal;
        this.protein = protein; this.carbs = carbs; this.fat = fat;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public double getKcal() { return kcal; }
    public void setKcal(double kcal) { this.kcal = kcal; }
    public double getProtein() { return protein; }
    public void setProtein(double protein) { this.protein = protein; }
    public double getCarbs() { return carbs; }
    public void setCarbs(double carbs) { this.carbs = carbs; }
    public double getFat() { return fat; }
    public void setFat(double fat) { this.fat = fat; }
}
