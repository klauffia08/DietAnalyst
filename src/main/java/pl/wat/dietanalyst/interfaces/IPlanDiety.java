package pl.wat.dietanalyst.interfaces;

import pl.wat.dietanalyst.entity.DietPlan;
import pl.wat.dietanalyst.entity.UserAccount;
import java.util.List;

public interface IPlanDiety {
    DietPlan generate(UserAccount user);
    List<DietPlan> getPlans(UserAccount user);
    void submit(UserAccount user, Long planId);
    void archive(UserAccount user, Long planId);
}
