package pl.wat.dietanalyst.control;

public record ReportSummary(double kcal, double protein, double carbs, double fat, long mealCount, int target) {
    public int progress() {
        if (target <= 0) return 0;
        return Math.min(100, (int) Math.round(kcal * 100.0 / target));
    }
}
