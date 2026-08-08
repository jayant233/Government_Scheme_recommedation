// Demonstrates inheritance and method overriding.
public class Admin extends Person {
    public Admin(int id, String name, int age, String gender) {
        super(id, name, age, gender);
    }

    @Override
    public void displayRole() {
        System.out.println("Role: Administrator");
    }
}
