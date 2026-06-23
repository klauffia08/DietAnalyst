package pl.wat.dietanalyst.interfaces;

import pl.wat.dietanalyst.boundary.form.ProductForm;
import pl.wat.dietanalyst.entity.Product;
import java.util.List;

public interface IBazaProduktow {
    List<Product> findAll();
    Product add(ProductForm form, String actorEmail);
}
