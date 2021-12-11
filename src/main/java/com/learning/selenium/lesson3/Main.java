package com.learning.selenium.lesson3;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxBinary;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.ie.InternetExplorerOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.time.Duration;

public class Main {

    private static final String URL = "http://localhost:8080/litecart/admin";
    private static final String LOGIN = "admin";
    private static final String PASSWORD = "admin";

    public static void main(String args[]) {
        //Прописал в системной переменной окружения Path
        //System.setProperty("webdriver.chrome.driver", "src/main/resources/chromedriver.exe");
        runCase(new ChromeDriver());
        runCase(new FirefoxDriver());
        runCase(new EdgeDriver());
        //IE11
        //вынужден чистить куки, т.к. при повторном запуске самостоятельно это не делает
        InternetExplorerOptions internetExplorerOptions = new InternetExplorerOptions();
        internetExplorerOptions.setCapability(InternetExplorerDriver.IE_ENSURE_CLEAN_SESSION, true);
        InternetExplorerDriver ieDriver = new InternetExplorerDriver(internetExplorerOptions);
        runCase(ieDriver);
        //Nightly
        FirefoxBinary firefoxBinary = new FirefoxBinary(new File("C:\\Program Files\\Firefox Nightly\\firefox.exe"));
        FirefoxOptions firefoxOptions = new FirefoxOptions();
        firefoxOptions.setBinary(firefoxBinary);
        firefoxOptions.setProfile(new FirefoxProfile());
        FirefoxDriver nightlyDriver = new FirefoxDriver(firefoxOptions);
        runCase(nightlyDriver);
    }

    private static void runCase(RemoteWebDriver driver) {
        try {
            navigateAndLoginWithSleep(driver, 2000);
        } finally {
            if (driver != null) {
                driver.quit();
            }
        }
    }

    private static void navigateAndLoginWithSleep(WebDriver driver, long sleepMs) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(3));
        driver.navigate().to(URL);
        driver.findElement(By.name("username")).sendKeys(LOGIN);
        driver.findElement(By.name("password")).sendKeys(PASSWORD);
        driver.findElement(By.name("remember_me")).click();
        driver.findElement(By.name("login")).click();
        wait.until(webDriver -> ((JavascriptExecutor) webDriver).executeScript("return document.readyState").equals("complete"));
        try {
            Thread.sleep(sleepMs);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
