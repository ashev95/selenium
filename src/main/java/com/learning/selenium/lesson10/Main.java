package com.learning.selenium.lesson10;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.logging.LogEntry;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.LoggingPreferences;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;

public class Main {

    private static final String URL = "http://localhost:8080/litecart/admin/";
    private static final String LOGIN = "admin";
    private static final String PASSWORD = "admin";
    public static void main(String args[]) {
        WebDriver driver = null;
        try {
            LoggingPreferences loggingPreferences = new LoggingPreferences();
            loggingPreferences.enable(LogType.BROWSER, Level.ALL);
            ChromeOptions chromeOptions = new ChromeOptions();
            chromeOptions.setCapability(CapabilityType.LOGGING_PREFS, loggingPreferences);
            driver = new ChromeDriver(chromeOptions);

            driver.navigate().to(URL);
            driver.findElement(By.name("username")).sendKeys(LOGIN);
            driver.findElement(By.name("password")).sendKeys(PASSWORD);
            driver.findElement(By.name("remember_me")).click();
            driver.findElement(By.name("login")).click();

            driver.findElement(By.cssSelector("#box-apps-menu>li>a[href*=catalog]")).click();

            driver.findElement(By.xpath(".//table[@class='dataTable']/tbody/tr[td[input]]/td[3]/a")).click();

            String mainWindowHandle = driver.getWindowHandle();
            Set<String> allWindowHandles = driver.getWindowHandles();

            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));

            //очистим накопившийся лог с админки
            driver.manage().logs().get(LogType.BROWSER).forEach(e -> {return;});

            for (WebElement link : driver.findElements(By.xpath(".//table[@class='dataTable']/tbody/tr/td[5]/a[contains(@href,'product_id')]"))) {
                String url = link.getAttribute("href");

                ((JavascriptExecutor)driver).executeScript("window.open()");

                Set<String> newWindowHandles = wait.until(anyWindowOtherThan(allWindowHandles));
                if (newWindowHandles.size() != 1) {
                    throw new RuntimeException("Ожидаемое кол-во новых окон: 1. Получено: " + newWindowHandles.size());
                }
                driver.switchTo().window(newWindowHandles.iterator().next());
                driver.navigate().to(url);
                //Дождёмся полной загрузки страницы
                new WebDriverWait(driver, Duration.ofSeconds(30)).until(webDriver -> ((JavascriptExecutor) webDriver).executeScript("return document.readyState").equals("complete"));

                driver.close();
                driver.switchTo().window(mainWindowHandle);

                List<LogEntry> logEntries = driver.manage().logs().get(LogType.BROWSER).getAll();
                if (logEntries.size() > 0) {
                    System.out.println("Есть записи лога на странице: " + url);
                    for (LogEntry logEntry : logEntries) {
                        System.out.println(logEntry);
                    }
                }

            }
        } finally {
            if (driver != null) {
                driver.quit();
                driver = null;
            }
        }
    }

    public static ExpectedCondition<Set<String>> anyWindowOtherThan(final Set<String> allWindowHandles) {
        return new ExpectedCondition<Set<String>>() {
            public Set<String> apply(WebDriver driver) {
                try {
                    Set<String> newWindowHandles = driver.getWindowHandles();
                    if (newWindowHandles.size() > allWindowHandles.size()) {
                        newWindowHandles.removeAll(allWindowHandles);
                        return newWindowHandles;
                    }
                } catch (WebDriverException var3) {
                    return null;
                }
                return null;
            }
            public String toString() {
                return "allWindowHandles: " + allWindowHandles;
            }
        };
    }

}
