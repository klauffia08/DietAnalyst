package pl.wat.dietanalyst.entity;

public enum PlanStatus {
    GENERATED("Wygenerowany"),
    SUBMITTED("Do weryfikacji"),
    APPROVED("Zatwierdzony"),
    REJECTED("Odrzucony"),
    ARCHIVED("Zarchiwizowany");
    private final String label;
    PlanStatus(String label) { this.label = label; }
    public String getLabel() { return label; }
}
