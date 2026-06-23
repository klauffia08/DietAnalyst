package pl.wat.dietanalyst.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class UserAccount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 120)
    private String name;

    @Column(nullable = false, unique = true, length = 160)
    private String email;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private Role role = Role.USER;

    @Column(nullable = false)
    private boolean active = true;

    private Integer age;
    private String goal = "Zdrowe odżywianie";
    private String activity = "Umiarkowana";

    @Column(name = "calories_target", nullable = false)
    private Integer caloriesTarget = 2000;

    @Lob
    private String notes;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "assigned_dietitian_id")
    private UserAccount assignedDietitian;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }
    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
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
    public UserAccount getAssignedDietitian() { return assignedDietitian; }
    public void setAssignedDietitian(UserAccount assignedDietitian) { this.assignedDietitian = assignedDietitian; }
}
