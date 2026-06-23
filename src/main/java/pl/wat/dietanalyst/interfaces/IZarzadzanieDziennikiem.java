package pl.wat.dietanalyst.interfaces;

import pl.wat.dietanalyst.boundary.form.MealForm;
import pl.wat.dietanalyst.entity.Meal;
import pl.wat.dietanalyst.entity.UserAccount;
import java.util.List;

public interface IZarzadzanieDziennikiem {
    Meal addMeal(UserAccount user, MealForm form);
    List<Meal> getMeals(UserAccount user);
    void deleteMeal(UserAccount user, Long mealId);
}
