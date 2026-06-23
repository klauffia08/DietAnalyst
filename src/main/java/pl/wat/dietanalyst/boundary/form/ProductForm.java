package pl.wat.dietanalyst.boundary.form;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;

public class ProductForm {
    @NotBlank private String name;
    @NotBlank private String category;
    @PositiveOrZero private double kcal;
    @PositiveOrZero private double protein;
    @PositiveOrZero private double carbs;
    @PositiveOrZero private double fat;

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
