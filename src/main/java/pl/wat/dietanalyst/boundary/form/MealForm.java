package pl.wat.dietanalyst.boundary.form;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import pl.wat.dietanalyst.entity.MealType;
import java.time.LocalDate;

public class MealForm {
    @NotNull
    private Long productId;
    @NotNull
    private MealType type;
    @Positive(message = "Ilość musi być większa od zera")
    private double grams = 100;
    @NotNull
    private LocalDate date = LocalDate.now();

    public Long getProductId() { return productId; }
    public void setProductId(Long productId) { this.productId = productId; }
    public MealType getType() { return type; }
    public void setType(MealType type) { this.type = type; }
    public double getGrams() { return grams; }
    public void setGrams(double grams) { this.grams = grams; }
    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }
}
