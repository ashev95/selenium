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
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

public class Main {

    private static final String URL = "http://localhost:8080/litecart";

    public static void main(String args[]) {
        WebDriver driver = null;
        try {
            driver = new ChromeDriver();
            driver.navigate().to(URL);

            List<String> productElementLinks = new LinkedList<>();
            for (WebElement webElement : driver.findElements(By.cssSelector("li.product"))) {
                if (productElementLinks.size() > 2) {
                    break;
                }
                productElementLinks.add(webElement.findElement(By.tagName("a")).getAttribute("href"));
            }

            for (String link : productElementLinks) {
                addProduct(driver, link);
            }

            driver.findElement(By.id("cart")).click();

            Wait wait = new WebDriverWait(driver, Duration.ofMillis(500));

            for (int i = 0; i < productElementLinks.size(); i++) {
                //выбрать первый элемент, если больше 1 наименования
                List<WebElement> tableElements = driver.findElements(By.className("dataTable")); //если таблица есть - больше 1 наименования
                if (tableElements.size() > 0) {
                    tableElements.get(0).click();
                }
                //удалить первый продукт, если больше одного наименования, или удалить текущий продукт, если одно наименование
                driver.findElement(By.cssSelector("button[name=remove_cart_item]")).click();
                if (tableElements.size() > 0) {
                    wait.until(ExpectedConditions.stalenessOf(tableElements.get(0)));
                }
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

    private static void addProduct(WebDriver driver, String link) {
        driver.navigate().to(link);

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
