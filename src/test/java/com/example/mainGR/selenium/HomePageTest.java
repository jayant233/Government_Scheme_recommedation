package com.example.mainGR.selenium;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class HomePageTest extends BaseTest {

    @Test
    public void testTC01_VerifyHomePageLaunchesSuccessfully() {
        // Open the application
        driver.get(BASE_URL);
        
        // Verify the page title matches the expected title
        String pageTitle = driver.getTitle();
        assertEquals("Government Scheme Recommendation System", pageTitle, "Page title does not match");
    }

    @Test
    public void testTC02_VerifyPageUrl() {
        // Open the application
        driver.get(BASE_URL);
        
        // Verify the current URL is the base URL
        String currentUrl = driver.getCurrentUrl();
        assertEquals(BASE_URL, currentUrl, "Application URL is incorrect");
    }

    @Test
    public void testTC03_VerifyAllRecommendationFormFieldsAreDisplayed() {
        // Open the application
        driver.get(BASE_URL);
        
        // Verify all necessary input fields and buttons are displayed on the screen
        assertTrue(driver.findElement(By.id("name")).isDisplayed(), "Name field is missing");
        assertTrue(driver.findElement(By.id("age")).isDisplayed(), "Age field is missing");
        assertTrue(driver.findElement(By.id("gender")).isDisplayed(), "Gender dropdown is missing");
        assertTrue(driver.findElement(By.id("income")).isDisplayed(), "Income field is missing");
        assertTrue(driver.findElement(By.id("occupation")).isDisplayed(), "Occupation dropdown is missing");
        assertTrue(driver.findElement(By.id("category")).isDisplayed(), "Category dropdown is missing");
        assertTrue(driver.findElement(By.id("state")).isDisplayed(), "State dropdown is missing");
        assertTrue(driver.findElement(By.id("submitBtn")).isDisplayed(), "Submit button is missing");
    }

    @Test
    public void testTC04_VerifyNavigationToAdminPortal() {
        // Open the application
        driver.get(BASE_URL);
        
        // Find and click the 'Go to Admin Portal' link
        WebElement adminLink = driver.findElement(By.id("adminLink"));
        adminLink.click();
        
        // Verify the URL changes to the admin page
        assertTrue(driver.getCurrentUrl().contains("/admin"), "Did not navigate to Admin Portal");
        
        // Verify the admin page heading is displayed
        WebElement adminTitle = driver.findElement(By.id("adminTitle"));
        assertTrue(adminTitle.isDisplayed(), "Admin title is not displayed");
    }
}
