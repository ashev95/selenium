package com.learning.selenium.lesson11.test;

import com.learning.selenium.lesson11.application.Application;
import com.learning.selenium.lesson11.model.DataProviders;
import com.learning.selenium.lesson11.page.Basket;
import com.learning.selenium.lesson11.page.Product;
import com.learning.selenium.lesson11.page.ProductList;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.java.junit.dataprovider.UseDataProvider;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

@RunWith(DataProviderRunner.class)
public class ProductTest {

    private static final String URL = "http://localhost:8080/litecart";
    private static final int PRODUCT_COUNT = 3;

    private WebDriver driver;

    @Before
    public void before() {
        driver = new ChromeDriver();
    }

    @Test
    @UseDataProvider(value = "products", location = DataProviders.class)
    public void productTest(int quantity) {
        Application app = new Application(driver, URL);
        ProductList productList = new ProductList(driver);
        Product product = new Product(driver);
        Basket basket = new Basket(driver);
        app.open();
        for (int i = 0; i < PRODUCT_COUNT; i++) {
            productList.openFirstProduct();
            int productCount = app.getProductCount();
            product.fill(quantity);
            product.save();
            app.waitUpdate(productCount);
        }
        basket.open();
        basket.clear();
    }

    @After
    public void after() {
        driver.quit();
        driver = null;
    }

}
