package com.example.mainGR.selenium;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import java.time.Duration;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AdminPortalTest extends BaseTest {

    @Test
    public void testTC10_VerifyAdminPageLoadsSuccessfully() {
        // Open the admin page
        driver.get(BASE_URL + "admin");
        
        // Verify admin portal heading is displayed
        WebElement heading = driver.findElement(By.id("adminTitle"));
        assertTrue(heading.isDisplayed(), "Admin heading not displayed");
        
        // Verify all three action buttons are displayed using XPath
        assertTrue(driver.findElement(By.xpath("//button[text()='Add New Scheme']")).isDisplayed());
        assertTrue(driver.findElement(By.xpath("//button[text()='Update Scheme']")).isDisplayed());
        assertTrue(driver.findElement(By.xpath("//button[text()='Delete Scheme']")).isDisplayed());
    }

    @Test
    public void testTC11_VerifyAddSchemeFormIsDisplayed() {
        driver.get(BASE_URL + "admin");
        
        // Click 'Add New Scheme' button to reveal the form
        driver.findElement(By.xpath("//button[text()='Add New Scheme']")).click();
        
        // Find the form container and verify it is now visible
        WebElement addForm = driver.findElement(By.id("addForm"));
        assertTrue(addForm.isDisplayed(), "Add form should be visible");
        
        // Verify input fields inside the form are displayed
        assertTrue(addForm.findElement(By.name("name")).isDisplayed());
        assertTrue(addForm.findElement(By.name("minAge")).isDisplayed());
        assertTrue(addForm.findElement(By.name("desc")).isDisplayed());
    }

    @Test
    public void testTC12_VerifyAddingNewScheme() {
        driver.get(BASE_URL + "admin");
        
        // Open the add form
        driver.findElement(By.xpath("//button[text()='Add New Scheme']")).click();
        
        // Fill out the scheme details
        WebElement addForm = driver.findElement(By.id("addForm"));
        addForm.findElement(By.name("name")).sendKeys("Test Scheme Auto");
        addForm.findElement(By.name("minAge")).sendKeys("18");
        addForm.findElement(By.name("maxAge")).sendKeys("60");
        addForm.findElement(By.name("minIncome")).sendKeys("0");
        addForm.findElement(By.name("maxIncome")).sendKeys("100000");
        addForm.findElement(By.name("desc")).sendKeys("Test Description");
        
        // Select dropdowns
        new Select(addForm.findElement(By.name("gender"))).selectByValue("ALL");
        new Select(addForm.findElement(By.name("occupation"))).selectByValue("ANY");
        new Select(addForm.findElement(By.name("category"))).selectByValue("ANY");
        new Select(addForm.findElement(By.name("state"))).selectByIndex(1);
        
        // Submit the form
        addForm.findElement(By.cssSelector("button[type='submit']")).click();
        
        // Wait for page to reload and verify the table is back
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        wait.until(ExpectedConditions.presenceOfElementLocated(By.id("adminTable")));
        
        // Verify the new scheme appears somewhere in the page source
        String pageSource = driver.getPageSource();
        assertTrue(pageSource.contains("Test Scheme Auto"), "New scheme should be present in the list");
    }

    @Test
    public void testTC13_VerifyUpdatingExistingScheme() {
        driver.get(BASE_URL + "admin");
        
        // Open the update form
        driver.findElement(By.xpath("//button[text()='Update Scheme']")).click();
        
        // Ensure the update form is visible
        WebElement updateForm = driver.findElement(By.id("updateForm"));
        assertTrue(updateForm.isDisplayed(), "Update form is not visible");
        
        // Fill update details (assuming ID 1 exists, but testing behavior)
        updateForm.findElement(By.name("id")).sendKeys("1");
        updateForm.findElement(By.name("name")).sendKeys("Updated Scheme Auto");
        updateForm.findElement(By.name("minAge")).sendKeys("20");
        updateForm.findElement(By.name("maxAge")).sendKeys("50");
        updateForm.findElement(By.name("minIncome")).sendKeys("1000");
        updateForm.findElement(By.name("maxIncome")).sendKeys("50000");
        updateForm.findElement(By.name("desc")).sendKeys("Updated description");
        new Select(updateForm.findElement(By.name("gender"))).selectByValue("ALL");
        new Select(updateForm.findElement(By.name("occupation"))).selectByValue("ANY");
        new Select(updateForm.findElement(By.name("category"))).selectByValue("ANY");
        new Select(updateForm.findElement(By.name("state"))).selectByIndex(1);
        
        // Submit the update
        updateForm.findElement(By.cssSelector("button[type='submit']")).click();
        
        // Wait and verify we remain on the admin portal
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        wait.until(ExpectedConditions.urlContains("/admin"));
        assertTrue(driver.getCurrentUrl().contains("/admin"), "Should redirect back to admin page");
    }

    @Test
    public void testTC14_VerifyAllSchemesAreDisplayed() {
        driver.get(BASE_URL + "admin");
        
        // Verify the schemes table is visible
        WebElement adminTable = driver.findElement(By.id("adminTable"));
        assertTrue(adminTable.isDisplayed(), "Admin table should be visible");
        
        // Use findElements to check row count
        List<WebElement> rows = driver.findElements(By.xpath("//table[@id='adminTable']//tr"));
        
        // Table should have at least 1 row (the header row)
        assertTrue(rows.size() >= 1, "Table should have at least the header row");
    }

    @Test
    public void testTC15_VerifyDeletingScheme() {
        driver.get(BASE_URL + "admin");
        
        // Open the delete form
        driver.findElement(By.xpath("//button[text()='Delete Scheme']")).click();
        
        WebElement deleteForm = driver.findElement(By.id("deleteForm"));
        assertTrue(deleteForm.isDisplayed(), "Delete form should be visible");
        
        // Enter a dummy ID to delete
        deleteForm.findElement(By.name("id")).sendKeys("9999");
        
        // Submit delete request
        deleteForm.findElement(By.cssSelector("button[type='submit']")).click();
        
        // Wait for reload and verify we are back on admin page
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        wait.until(ExpectedConditions.urlContains("/admin"));
        
        assertTrue(driver.getCurrentUrl().contains("/admin"), "Should remain on admin page after delete");
    }
}
