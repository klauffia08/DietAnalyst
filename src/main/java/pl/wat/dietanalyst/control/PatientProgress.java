package pl.wat.dietanalyst.control;

import pl.wat.dietanalyst.entity.UserAccount;

public record PatientProgress(
        UserAccount patient,
        ReportSummary today,
        long mealsLast7Days,
        long totalMeals,
        long totalPlans,
        long pendingPlans,
        String latestPlanStatus
) {}
