package com.example.mainGR.selenium;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import java.time.Duration;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class RecommendationFormTest extends BaseTest {

    @Test
    public void testTC05_VerifyRecommendationFormSubmissionWithValidData() {
        driver.get(BASE_URL);
        
        // Fill all text fields
        driver.findElement(By.id("name")).sendKeys("Jane Doe");
        driver.findElement(By.id("age")).sendKeys("28");
        driver.findElement(By.id("income")).sendKeys("45000");
        
        // Select options from dropdowns by index (1 is usually the first actual option)
        new Select(driver.findElement(By.id("gender"))).selectByIndex(1);
        new Select(driver.findElement(By.id("occupation"))).selectByIndex(1);
        new Select(driver.findElement(By.id("category"))).selectByIndex(1);
        new Select(driver.findElement(By.id("state"))).selectByIndex(1);
        
        // Click Check Eligible Schemes button
        driver.findElement(By.id("submitBtn")).click();
        
        // Explicitly wait until the URL changes to the results page
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        wait.until(ExpectedConditions.urlContains("/results"));
        
        // Verify navigation was successful
        assertTrue(driver.getCurrentUrl().contains("/results"), "Did not navigate to results page");
    }

    @Test
    public void testTC06_VerifyResultsPageIsDisplayed() {
        driver.get(BASE_URL);
        
        // Fill form with valid data to trigger a result
        driver.findElement(By.id("name")).sendKeys("John Smith");
        driver.findElement(By.id("age")).sendKeys("35");
        driver.findElement(By.id("income")).sendKeys("60000");
        new Select(driver.findElement(By.id("gender"))).selectByIndex(1);
        new Select(driver.findElement(By.id("occupation"))).selectByIndex(1);
        new Select(driver.findElement(By.id("category"))).selectByIndex(1);
        new Select(driver.findElement(By.id("state"))).selectByIndex(1);
        
        // Submit form
        driver.findElement(By.id("submitBtn")).click();
        
        // Verify the 'Eligible Government Schemes' header appears using XPath
        WebElement resultsHeader = driver.findElement(By.xpath("//h2[text()='Eligible Government Schemes']"));
        assertTrue(resultsHeader.isDisplayed(), "Results header is not displayed");
    }

    @Test
    public void testTC07_VerifyEligibleSchemesAreDisplayed() {
        driver.get(BASE_URL);
        
        // Fill form
        driver.findElement(By.id("name")).sendKeys("Test User");
        driver.findElement(By.id("age")).sendKeys("22");
        driver.findElement(By.id("income")).sendKeys("10000");
        new Select(driver.findElement(By.id("gender"))).selectByIndex(1);
        new Select(driver.findElement(By.id("occupation"))).selectByIndex(1);
        new Select(driver.findElement(By.id("category"))).selectByIndex(1);
        new Select(driver.findElement(By.id("state"))).selectByIndex(1);
        
        // Submit
        driver.findElement(By.id("submitBtn")).click();
        
        // Wait and verify that the results table is visible on the screen
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        WebElement table = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("table.styled-table")));
        assertTrue(table.isDisplayed(), "Schemes table should be displayed");
    }

    @Test
    public void testTC08_VerifyFormBehaviorWithEmptyFields() {
        driver.get(BASE_URL);
        
        // Attempt to submit the form without entering any data
        driver.findElement(By.id("submitBtn")).click();
        
        // HTML5 'required' attribute should block submission, so URL stays the same
        String currentUrl = driver.getCurrentUrl();
        assertEquals(BASE_URL, currentUrl, "Form should not submit with empty fields");
    }

    @Test
    public void testTC09_VerifyDropdownSelection() {
        driver.get(BASE_URL);
        
        // Initialize Select class for the gender dropdown
        Select genderDropdown = new Select(driver.findElement(By.id("gender")));
        
        // Select an option
        genderDropdown.selectByIndex(1);
        
        // Verify the selected option is retained
        WebElement selectedOption = genderDropdown.getFirstSelectedOption();
        assertTrue(selectedOption.getText().length() > 0, "Selected value should not be empty");
    }
}
