// A citizen who checks scheme eligibility.
public class User extends Person implements Eligible {
    private double income;
    private String occupation;
    private String category;
    private String state;

    public User(int id, String name, int age, String gender, double income,
            String occupation, String category, String state) {
        super(id, name, age, gender);
        this.income = income;
        this.occupation = occupation;
        this.category = category;
        this.state = state;
    }

    @Override
    public void displayRole() {
        System.out.println("Role: Citizen");
    }

    @Override
    public void showEligibleSchemes() {
        System.out.println("Eligible schemes for " + getName() + ":");
    }

    public double getIncome() { return income; }
    public void setIncome(double income) { this.income = income; }
    public String getOccupation() { return occupation; }
    public void setOccupation(String occupation) { this.occupation = occupation; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public String getState() { return state; }
    public void setState(String state) { this.state = state; }
}
