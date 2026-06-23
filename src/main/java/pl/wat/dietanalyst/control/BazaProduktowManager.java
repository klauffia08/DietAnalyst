package pl.wat.dietanalyst.control;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.wat.dietanalyst.boundary.form.ProductForm;
import pl.wat.dietanalyst.entity.Product;
import pl.wat.dietanalyst.interfaces.IBazaProduktow;
import pl.wat.dietanalyst.repository.MealRepository;
import pl.wat.dietanalyst.repository.ProductRepository;

import java.util.List;

@Service
public class BazaProduktowManager implements IBazaProduktow {
    private final ProductRepository products;
    private final MealRepository meals;
    private final AuditManager audit;

    public BazaProduktowManager(ProductRepository products, MealRepository meals, AuditManager audit) {
        this.products = products;
        this.meals = meals;
        this.audit = audit;
    }

    @Override
    public List<Product> findAll() {
        return products.findAllByOrderByNameAsc();
    }

    @Override
    @Transactional
    public Product add(ProductForm form, String actorEmail) {
        if (products.existsByNameIgnoreCase(form.getName())) {
            throw new IllegalArgumentException("Taki produkt już istnieje.");
        }
        Product p = new Product(form.getName().trim(), form.getCategory().trim(), form.getKcal(),
                form.getProtein(), form.getCarbs(), form.getFat());
        Product saved = products.save(p);
        audit.log(actorEmail, "DODANIE_PRODUKTU", saved.getName());
        return saved;
    }

    @Transactional
    public Product update(Long id, ProductForm form, String actorEmail) {
        Product product = products.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Nie znaleziono produktu."));
        product.setName(form.getName().trim());
        product.setCategory(form.getCategory().trim());
        product.setKcal(form.getKcal());
        product.setProtein(form.getProtein());
        product.setCarbs(form.getCarbs());
        product.setFat(form.getFat());
        Product saved = products.save(product);
        audit.log(actorEmail, "EDYCJA_PRODUKTU", saved.getName());
        return saved;
    }

    @Transactional
    public void delete(Long id, String actorEmail) {
        Product product = products.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Nie znaleziono produktu."));
        if (meals.countByProductId(id) > 0) {
            throw new IllegalArgumentException("Nie można usunąć produktu używanego w dzienniku posiłków.");
        }
        products.delete(product);
        audit.log(actorEmail, "USUNIECIE_PRODUKTU", product.getName());
    }
}
