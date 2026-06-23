package pl.wat.dietanalyst.interfaces;

import pl.wat.dietanalyst.boundary.form.RegistrationForm;
import pl.wat.dietanalyst.entity.UserAccount;

public interface IAutoryzacja {
    UserAccount register(RegistrationForm form);
}
