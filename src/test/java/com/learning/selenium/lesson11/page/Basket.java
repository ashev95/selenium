package com.learning.selenium.lesson11.page;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class Basket {

    private WebDriver driver;

    public Basket(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    private static final String CLASS_NAME_TABLE = "dataTable";
    private static final String CSS_SHORTCUTS = "li.shortcut";

    @FindBy(css = "button[name=remove_cart_item]")
    List<WebElement> removeButtons;

    @FindBy(id = "cart")
    WebElement basketLink;

    public void open() {
        basketLink.click();
    }

    public void clear() {
        Wait wait = new WebDriverWait(driver, Duration.ofMillis(500));
        while (removeButtons.size() > 0) {
            // не поддается переделке на аннотации из-за пересоздания экземпляров элементов
            WebElement tableElement = driver.findElement(By.className(CLASS_NAME_TABLE));
            List<WebElement> shortcuts = driver.findElements(By.cssSelector(CSS_SHORTCUTS));
            if (shortcuts.size() > 0) {
                shortcuts.get(0).click();
            }
            removeButtons.get(0).click();
            wait.until(ExpectedConditions.stalenessOf(tableElement));
        }
    }

}
