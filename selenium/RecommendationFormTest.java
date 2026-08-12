import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.Select;

// Fills the form and checks that the results table is displayed.
public class RecommendationFormTest {
    public static void main(String[] args) {
        WebDriver driver = new ChromeDriver();
        try {
            String homeUrl = "file:///C:/Users/jay23/Desktop/mainGR/web/index.html";
            driver.get(homeUrl);
            driver.findElement(By.id("name")).sendKeys("Rahul Sharma");
            driver.findElement(By.name("age")).sendKeys("22");
            driver.findElement(By.id("income")).sendKeys("200000");
            new Select(driver.findElement(By.id("gender"))).selectByVisibleText("Male");
            new Select(driver.findElement(By.id("occupation"))).selectByVisibleText("Student");
            new Select(driver.findElement(By.id("category"))).selectByVisibleText("SC");
            new Select(driver.findElement(By.id("state"))).selectByVisibleText("All India (Central)");
            driver.findElement(By.id("submitBtn")).click();

            // Wait for results.html to load and fetch data from server
            Thread.sleep(3000);

            WebElement table = driver.findElement(By.xpath("//table[@id='resultsTable']"));
            System.out.println("Results table visible: " + table.isDisplayed());
            System.out.println(table.getText());
        } catch (Exception exception) {
            System.out.println("Test error: " + exception.getMessage());
        } finally {
            driver.quit();
        }
    }
}
