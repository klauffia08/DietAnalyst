package pl.wat.dietanalyst.control;

public record AdminDashboardStats(
        long allAccounts,
        long activeAccounts,
        long users,
        long dietitians,
        long administrators,
        long products,
        long meals,
        long plans,
        long pendingPlans
) {}
