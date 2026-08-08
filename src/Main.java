import java.sql.SQLException;
import java.util.ArrayList;

// Starts the console demonstration.
public class Main {
    public static void main(String[] args) {
        User user = new User(1, "Rahul Sharma", 22, "Male", 200000,
                "Student", "SC", "All India (Central)");
        Admin admin = new Admin(2, "Priya Patel", 35, "Female");
        ArrayList<Person> people = new ArrayList<Person>();
        people.add(user);
        people.add(admin);

        System.out.println("--- Runtime Polymorphism ---");
        for (Person person : people) {
            person.displayRole();
        }

        SchemeDAO dao = new SchemeDAO();
        try {
            user.showEligibleSchemes();
            for (Scheme scheme : dao.getEligibleSchemes(user)) {
                System.out.println("- " + scheme.getSchemeName());
            }

            System.out.println("--- Schemes With States ---");
            for (String scheme : dao.listSchemes()) {
                System.out.println("- " + scheme);
            }

            // Intentionally searches for an ID that does not exist.
            dao.getSchemeById(999);
        } catch (SchemeNotFoundException exception) {
            System.out.println("Custom exception: " + exception.getMessage());
        } catch (SQLException exception) {
            System.out.println("Database error: " + exception.getMessage());
        } finally {
            System.out.println("Program finished.");
        }
    }
}
