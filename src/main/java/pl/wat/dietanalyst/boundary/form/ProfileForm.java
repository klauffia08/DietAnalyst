package pl.wat.dietanalyst.boundary.form;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public class ProfileForm {
    @NotBlank private String name;
    @Email private String email;
    @Min(12) private Integer age;
    @NotBlank private String goal;
    @NotBlank private String activity;
    @Min(1000) private Integer caloriesTarget;
    private String notes;

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public Integer getAge() { return age; }
    public void setAge(Integer age) { this.age = age; }
    public String getGoal() { return goal; }
    public void setGoal(String goal) { this.goal = goal; }
    public String getActivity() { return activity; }
    public void setActivity(String activity) { this.activity = activity; }
    public Integer getCaloriesTarget() { return caloriesTarget; }
    public void setCaloriesTarget(Integer caloriesTarget) { this.caloriesTarget = caloriesTarget; }
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
}
