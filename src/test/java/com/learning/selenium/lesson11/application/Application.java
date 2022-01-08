package com.learning.selenium.lesson11.application;

import com.learning.selenium.lesson11.util.Commands;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class Application {

    private WebDriver driver;
    private String mainPageUrl;

    @FindBy(css = "#cart .quantity")
    WebElement basketCount;

    public Application(WebDriver driver, String mainPageUrl) {
        this.driver = driver;
        this.mainPageUrl = mainPageUrl;
        PageFactory.initElements(driver, this);
    }

    public void open() {
        driver.navigate().to(mainPageUrl);
    }

    public int getProductCount() {
        return Commands.getTextAsInt(basketCount);
    }

    public void waitUpdate(int productCount) {
        //кастомная проверка сравнения, т.к. у Selenium нет функционала ожидания с логикой сравнения текста элемента
        int productCountNew = productCount;
        int attempts = 7;
        while (attempts-- > 0) {
            productCountNew = getProductCount();
            if (productCount != productCountNew) break;
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
            }
        }
        if (productCount == productCountNew) throw new RuntimeException("Не смогли дождаться обновления корзины");
    }

}
