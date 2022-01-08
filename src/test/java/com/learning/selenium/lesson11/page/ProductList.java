package com.learning.selenium.lesson11.page;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class ProductList {

    public ProductList(WebDriver driver) {
        PageFactory.initElements(driver, this);
    }

    @FindBy(css = "li.product")
    WebElement firstProduct;

    public void openFirstProduct() {
        firstProduct.click();
    }

}
