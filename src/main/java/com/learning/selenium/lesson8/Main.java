package com.learning.selenium.lesson8;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.Set;

public class Main {

    private static final String URL = "http://localhost:8080/litecart/admin";
    private static final String LOGIN = "admin";
    private static final String PASSWORD = "admin";

    public static void main(String args[]) {
        WebDriver driver = null;
        try {
            driver = new ChromeDriver();
            driver.navigate().to(URL);
            driver.findElement(By.name("username")).sendKeys(LOGIN);
            driver.findElement(By.name("password")).sendKeys(PASSWORD);
            driver.findElement(By.name("remember_me")).click();
            driver.findElement(By.name("login")).click();

            driver.findElement(By.cssSelector("#box-apps-menu li > a[href$=countries]")).click();

            driver.findElement(By.xpath(".//table[@class='dataTable']/tbody/tr[@class='row']/td[7]")).click();

            String mainWindowHandle = driver.getWindowHandle();
            Set<String> allWindowHandles = driver.getWindowHandles();

            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));

            for (WebElement iconElement : driver.findElements(By.className("fa-external-link"))) {
                iconElement.click();
                //Можно воспользоваться ExpectedConditions.numberOfWindowsToBe,
                // но мне захотелось получить идентификаторы всех новых окон
                Set<String> newWindowHandles = wait.until(anyWindowOtherThan(allWindowHandles));
                if (newWindowHandles.size() != 1) {
                    throw new RuntimeException("Ожидаемое кол-во новых окон: 1. Получено: " + newWindowHandles.size());
                }
                driver.switchTo().window(newWindowHandles.iterator().next());
                //Дождёмся полной загрузки страницы, чтобы визуально оценить результат
                new WebDriverWait(driver, Duration.ofSeconds(30)).until(webDriver -> ((JavascriptExecutor) webDriver).executeScript("return document.readyState").equals("complete"));
                driver.close();
                driver.switchTo().window(mainWindowHandle);
            }

            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
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
