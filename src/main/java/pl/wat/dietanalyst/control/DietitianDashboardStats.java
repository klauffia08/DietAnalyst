package pl.wat.dietanalyst.control;

public record DietitianDashboardStats(
        long patientCount,
        long pendingPlans,
        long approvedPlans,
        long mealsLast7Days
) {}
