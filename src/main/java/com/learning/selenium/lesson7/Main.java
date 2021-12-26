package com.learning.selenium.lesson7;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class Main {

    private static final String URL = "http://localhost:8080/litecart";
    private static final int PRODUCT_COUNT = 3;

    public static void main(String args[]) {
        WebDriver driver = null;
        try {
            driver = new ChromeDriver();
            driver.navigate().to(URL);

            for (int i = 0; i < PRODUCT_COUNT; i++) {
                addFirstProduct(driver);
            }

            driver.findElement(By.id("cart")).click();

            Wait wait = new WebDriverWait(driver, Duration.ofMillis(500));

            List<WebElement> removeButtons = getRemoveButtons(driver);
            while (removeButtons.size() > 0) {
                WebElement tableElement = driver.findElement(By.className("dataTable"));
                List<WebElement> shortcuts = driver.findElements(By.cssSelector("li.shortcut"));
                if (shortcuts.size() > 0) {
                    shortcuts.get(0).click();
                }
                removeButtons.get(0).click();
                wait.until(ExpectedConditions.stalenessOf(tableElement));
                removeButtons = getRemoveButtons(driver);
            }

            Thread.sleep(15000);

        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            if (driver != null) {
                driver.quit();
                driver = null;
            }
        }
    }

    private static List<WebElement> getRemoveButtons(WebDriver driver) {
        return driver.findElements(By.cssSelector("button[name=remove_cart_item]"));
    }

    private static void addFirstProduct(WebDriver driver) {
        driver.findElement(By.cssSelector("li.product")).click();

        int productCount = getProductCount(driver); //фиксируем исходное кол-во товаров

        List<WebElement> optionsElements = driver.findElements(By.name("options[Size]"));
        if (optionsElements.size() > 0) {
            Select select = new Select(optionsElements.get(0));
            select.selectByValue(select.getOptions().get(1).getText());
        }

        driver.findElement(By.name("quantity")).clear();
        driver.findElement(By.name("quantity")).sendKeys("1");
        driver.findElement(By.name("add_cart_product")).click();

        //кастомная проверка сравнения, т.к. у Selenium нет функционала ожидания с логикой сравнения текста элемента
        int productCountNew = productCount;
        int attempts = 7;
        while (attempts-- > 0) {
            productCountNew = getProductCount(driver);
            if (productCount != productCountNew) break;
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
            }
        }
        if (productCount == productCountNew) throw new RuntimeException("Не смогли дождаться обновления корзины");
    }

    private static final int getProductCount(WebDriver driver) {
        return Integer.parseInt(driver.findElement(By.cssSelector("#cart .quantity")).getText());
    }

}
