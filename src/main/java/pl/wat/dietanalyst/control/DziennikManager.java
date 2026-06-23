package pl.wat.dietanalyst.control;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.wat.dietanalyst.boundary.form.MealForm;
import pl.wat.dietanalyst.entity.Meal;
import pl.wat.dietanalyst.entity.Product;
import pl.wat.dietanalyst.entity.UserAccount;
import pl.wat.dietanalyst.interfaces.IZarzadzanieDziennikiem;
import pl.wat.dietanalyst.repository.MealRepository;
import pl.wat.dietanalyst.repository.ProductRepository;

import java.util.List;

@Service
public class DziennikManager implements IZarzadzanieDziennikiem {
    private final MealRepository meals;
    private final ProductRepository products;
    private final AuditManager audit;

    public DziennikManager(MealRepository meals, ProductRepository products, AuditManager audit) {
        this.meals = meals; this.products = products; this.audit = audit;
    }

    @Override
    @Transactional
    public Meal addMeal(UserAccount user, MealForm form) {
        Product product = products.findById(form.getProductId()).orElseThrow(() -> new IllegalArgumentException("Nie znaleziono produktu."));
        double factor = form.getGrams() / 100.0;
        Meal meal = new Meal();
        meal.setUser(user); meal.setProduct(product); meal.setDate(form.getDate()); meal.setType(form.getType()); meal.setGrams(form.getGrams());
        meal.setKcal(round(product.getKcal() * factor));
        meal.setProtein(round(product.getProtein() * factor));
        meal.setCarbs(round(product.getCarbs() * factor));
        meal.setFat(round(product.getFat() * factor));
        Meal saved = meals.save(meal);
        audit.log(user.getEmail(), "DODANIE_POSILKU", product.getName() + ", " + form.getGrams() + " g");
        return saved;
    }

    @Override public List<Meal> getMeals(UserAccount user) { return meals.findAllByUserIdOrderByDateDescIdDesc(user.getId()); }

    @Override
    @Transactional
    public void deleteMeal(UserAccount user, Long mealId) {
        Meal meal = meals.findById(mealId).orElseThrow(() -> new IllegalArgumentException("Nie znaleziono posiłku."));
        if (!meal.getUser().getId().equals(user.getId())) throw new SecurityException("Brak dostępu do posiłku.");
        meals.delete(meal);
        audit.log(user.getEmail(), "USUNIECIE_POSILKU", String.valueOf(mealId));
    }

    private double round(double v) { return Math.round(v * 10.0) / 10.0; }
}
