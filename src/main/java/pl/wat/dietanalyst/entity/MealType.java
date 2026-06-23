package pl.wat.dietanalyst.entity;

public enum MealType {
    BREAKFAST("Śniadanie"), LUNCH("Obiad"), DINNER("Kolacja"), SNACK("Przekąska");
    private final String label;
    MealType(String label) { this.label = label; }
    public String getLabel() { return label; }
}
