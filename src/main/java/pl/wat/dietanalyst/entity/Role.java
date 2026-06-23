package pl.wat.dietanalyst.entity;

public enum Role {
    USER("Użytkownik"),
    DIETITIAN("Dietetyk"),
    ADMIN("Administrator");

    private final String label;

    Role(String label) { this.label = label; }
    public String getLabel() { return label; }
}
