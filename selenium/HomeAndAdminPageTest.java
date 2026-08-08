import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

// Opens the home page, then checks the admin page.
public class HomeAndAdminPageTest {
    public static void main(String[] args) {
        WebDriver driver = new ChromeDriver();
        try {
            // Change this path only if the project folder is moved.
            String homeUrl = "file:///C:/Users/jay23/Desktop/GovernmentSchemeRecommendationSystem/web/index.html";
            driver.get(homeUrl);
            System.out.println("Home title: " + driver.getTitle());

            driver.findElement(By.cssSelector("#adminLink")).click();
            WebElement addButton = driver.findElement(By.id("addSchemeBtn"));
            System.out.println("Admin title: " + driver.getTitle());
            System.out.println("Add Scheme button visible: " + addButton.isDisplayed());
        } catch (Exception exception) {
            System.out.println("Test error: " + exception.getMessage());
        } finally {
            driver.close();
        }
    }
}
