package com.learning.selenium.lesson11.page;

import com.learning.selenium.lesson11.util.Commands;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.util.List;

public class Product {

    public Product(WebDriver driver) {
        PageFactory.initElements(driver, this);
    }

    @FindBy(name = "quantity")
    WebElement quantity;

    @FindBy(name = "add_cart_product")
    WebElement addToBasketButton;

    @FindBy(name = "options[Size]")
    List<WebElement> optionsSize;

    public void fill(int quantityValue) {
        Commands.selectFirstOptionSafety(optionsSize);
        Commands.setText(quantity, Integer.toString(quantityValue));
    }

    public void save() {
        addToBasketButton.click();
    }

}
