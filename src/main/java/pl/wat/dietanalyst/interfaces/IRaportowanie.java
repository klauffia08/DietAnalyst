package pl.wat.dietanalyst.interfaces;

import pl.wat.dietanalyst.control.ReportSummary;
import pl.wat.dietanalyst.entity.UserAccount;

public interface IRaportowanie {
    ReportSummary buildSummary(UserAccount user);
}
