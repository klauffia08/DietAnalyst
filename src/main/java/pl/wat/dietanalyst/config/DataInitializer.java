package pl.wat.dietanalyst.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import pl.wat.dietanalyst.entity.*;
import pl.wat.dietanalyst.repository.DietPlanRepository;
import pl.wat.dietanalyst.repository.MealRepository;
import pl.wat.dietanalyst.repository.ProductRepository;
import pl.wat.dietanalyst.repository.UserRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Configuration
public class DataInitializer {
    @Bean
    CommandLineRunner seed(UserRepository users, ProductRepository products, MealRepository meals,
                           DietPlanRepository plans, PasswordEncoder encoder) {
        return args -> {
            users.findByEmailIgnoreCase("admin@dietanalyst.pl")
                    .orElseGet(() -> createUser(users, encoder, "Administrator DietAnalyst",
                            "admin@dietanalyst.pl", "Admin123!", Role.ADMIN));

            UserAccount dietitian = users.findByEmailIgnoreCase("dietetyk@dietanalyst.pl")
                    .orElseGet(() -> createUser(users, encoder, "Anna Kowalska",
                            "dietetyk@dietanalyst.pl", "Diet123!", Role.DIETITIAN));

            UserAccount user = users.findByEmailIgnoreCase("user@dietanalyst.pl")
                    .orElseGet(() -> createUser(users, encoder, "Klaudia Nowak",
                            "user@dietanalyst.pl", "User123!", Role.USER));

            if (user.getAssignedDietitian() == null) {
                user.setAssignedDietitian(dietitian);
                user.setAge(23);
                user.setGoal("Redukcja masy ciała");
                user.setActivity("Umiarkowana");
                user.setCaloriesTarget(2000);
                user.setNotes("Preferowane szybkie posiłki, zwiększona podaż białka.");
                users.save(user);
            }

            seedProduct(products, new Product("Płatki owsiane", "Zbożowe", 389, 17, 66, 7));
            seedProduct(products, new Product("Jogurt naturalny", "Nabiał", 60, 4, 5, 3));
            seedProduct(products, new Product("Banan", "Owoce", 89, 1.1, 23, 0.3));
            seedProduct(products, new Product("Pierś z kurczaka", "Mięso", 165, 31, 0, 3.6));
            seedProduct(products, new Product("Ryż biały", "Zbożowe", 130, 2.7, 28, 0.3));
            seedProduct(products, new Product("Jajko", "Nabiał", 155, 13, 1.1, 11));
            seedProduct(products, new Product("Awokado", "Warzywa/Owoce", 160, 2, 9, 15));

            if (meals.countByUserId(user.getId()) == 0) {
                Product oats = products.findByNameIgnoreCase("Płatki owsiane").orElseThrow();
                Product chicken = products.findByNameIgnoreCase("Pierś z kurczaka").orElseThrow();
                meals.save(meal(user, oats, MealType.BREAKFAST, 70, LocalDate.now()));
                meals.save(meal(user, chicken, MealType.LUNCH, 150, LocalDate.now()));
                meals.save(meal(user, oats, MealType.BREAKFAST, 60, LocalDate.now().minusDays(1)));
            }

            if (plans.findAllByUserIdOrderByCreatedAtDesc(user.getId()).isEmpty()) {
                DietPlan plan = new DietPlan();
                plan.setName("Plan – Redukcja masy ciała");
                plan.setStatus(PlanStatus.SUBMITTED);
                plan.setCreatedAt(LocalDateTime.now().minusHours(2));
                plan.setUser(user);
                plan.setContent("""
                        DZIEŃ 1
                        Śniadanie: płatki owsiane, jogurt naturalny i banan
                        Obiad: pierś z kurczaka, ryż i warzywa
                        Kolacja: jajka, awokado i sałatka

                        DZIEŃ 2
                        Śniadanie: jogurt naturalny z owocami
                        Obiad: kurczak z ryżem i warzywami
                        Kolacja: lekki posiłek białkowy
                        """);
                plans.save(plan);
            }
        };
    }

    private UserAccount createUser(UserRepository users, PasswordEncoder encoder, String name,
                                   String email, String password, Role role) {
        UserAccount user = new UserAccount();
        user.setName(name);
        user.setEmail(email);
        user.setPassword(encoder.encode(password));
        user.setRole(role);
        user.setActive(true);
        user.setCaloriesTarget(2000);
        return users.save(user);
    }

    private void seedProduct(ProductRepository products, Product product) {
        if (!products.existsByNameIgnoreCase(product.getName())) {
            products.save(product);
        }
    }

    private Meal meal(UserAccount user, Product product, MealType type, double grams, LocalDate date) {
        double factor = grams / 100.0;
        Meal meal = new Meal();
        meal.setUser(user);
        meal.setProduct(product);
        meal.setType(type);
        meal.setDate(date);
        meal.setGrams(grams);
        meal.setKcal(round(product.getKcal() * factor));
        meal.setProtein(round(product.getProtein() * factor));
        meal.setCarbs(round(product.getCarbs() * factor));
        meal.setFat(round(product.getFat() * factor));
        return meal;
    }

    private double round(double value) {
        return Math.round(value * 10.0) / 10.0;
    }
}
