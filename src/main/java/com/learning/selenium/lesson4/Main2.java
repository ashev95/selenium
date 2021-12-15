package com.learning.selenium.lesson4;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import java.util.List;

public class Main2 {

    private static final String URL = "http://localhost:8080/litecart";
    private static final String LOGIN = "admin";
    private static final String PASSWORD = "admin";

    //Получить все блоки с товарами
    private static final String XPATH_PRODUCTS = "//li[@class='product column shadow hover-light']";

    //Получить название продукта
    private static final String XPATH_PRODUCT_TITLE = ".//div[@class='name']";

    //Получить все стикеры у выбранного товара
    private static final String XPATH_STICKERS = ".//div[@class='sticker new' or @class='sticker sale']";

    public static void main(String args[]) {
        WebDriver driver = null;
        try {
            driver = new ChromeDriver();
            //авторзация
            driver.navigate().to(URL);

            List<WebElement> products = driver.findElements(By.xpath(XPATH_PRODUCTS));
            for (WebElement product : products) { //пробег по продуктам
                String title = product.findElement(By.xpath(XPATH_PRODUCT_TITLE)).getText(); //получаем заголовок продукта
                List<WebElement> stickers = product.findElements(By.xpath(XPATH_STICKERS)); //получаем стикеры из текущего продукта
                System.out.println("Проверяемый продукт: " + title);
                if (stickers.size() != 1) {
                    System.out.println("Для продукта с названием " + title + " количество стикеров не равно одному");
                }
            }
        } finally {
            if (driver != null) {
                driver.quit();
                driver = null;
            }
        }
    }
}
