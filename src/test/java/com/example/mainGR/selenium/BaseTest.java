package com.example.mainGR.selenium;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import java.time.Duration;

public class BaseTest {
    
    // WebDriver instance to be used by all test classes
    protected WebDriver driver;
    
    // Base URL of the Spring Boot application
    protected final String BASE_URL = "http://localhost:8080/";

    @BeforeEach
    public void setUp() {
        // Automatically download and setup ChromeDriver
        WebDriverManager.chromedriver().setup();
        
        // Initialize ChromeDriver
        driver = new ChromeDriver();
        
        // Maximize the browser window for better visibility
        driver.manage().window().maximize();
        
        // Add an implicit wait for elements to appear
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
    }

    @AfterEach
    public void tearDown() {
        // Close the browser and quit WebDriver session after each test
        if (driver != null) {
            driver.quit();
        }
    }
}
