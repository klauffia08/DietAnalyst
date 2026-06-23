package pl.wat.dietanalyst.control;

import org.springframework.stereotype.Service;
import pl.wat.dietanalyst.entity.Meal;
import pl.wat.dietanalyst.entity.UserAccount;
import pl.wat.dietanalyst.interfaces.IRaportowanie;
import pl.wat.dietanalyst.repository.MealRepository;
import java.time.LocalDate;
import java.util.List;

@Service
public class RaportManager implements IRaportowanie {
    private final MealRepository meals;
    public RaportManager(MealRepository meals) { this.meals = meals; }

    @Override
    public ReportSummary buildSummary(UserAccount user) {
        List<Meal> today = meals.findAllByUserIdAndDate(user.getId(), LocalDate.now());
        double kcal = today.stream().mapToDouble(Meal::getKcal).sum();
        double protein = today.stream().mapToDouble(Meal::getProtein).sum();
        double carbs = today.stream().mapToDouble(Meal::getCarbs).sum();
        double fat = today.stream().mapToDouble(Meal::getFat).sum();
        return new ReportSummary(round(kcal), round(protein), round(carbs), round(fat), today.size(), user.getCaloriesTarget());
    }
    private double round(double v) { return Math.round(v * 10.0) / 10.0; }
}
