package pl.wat.dietanalyst.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "meals")
public class Meal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "meal_date", nullable = false)
    private LocalDate date;

    @Enumerated(EnumType.STRING)
    @Column(name = "meal_type", nullable = false)
    private MealType type;

    private double grams;
    private double kcal;
    private double protein;
    private double carbs;
    private double fat;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    private UserAccount user;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    private Product product;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }
    public MealType getType() { return type; }
    public void setType(MealType type) { this.type = type; }
    public double getGrams() { return grams; }
    public void setGrams(double grams) { this.grams = grams; }
    public double getKcal() { return kcal; }
    public void setKcal(double kcal) { this.kcal = kcal; }
    public double getProtein() { return protein; }
    public void setProtein(double protein) { this.protein = protein; }
    public double getCarbs() { return carbs; }
    public void setCarbs(double carbs) { this.carbs = carbs; }
    public double getFat() { return fat; }
    public void setFat(double fat) { this.fat = fat; }
    public UserAccount getUser() { return user; }
    public void setUser(UserAccount user) { this.user = user; }
    public Product getProduct() { return product; }
    public void setProduct(Product product) { this.product = product; }
}
