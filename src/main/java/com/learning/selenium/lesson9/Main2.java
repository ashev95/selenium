package com.learning.selenium.lesson9;

import io.percy.selenium.Percy;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.URL;

public class Main2 {

    /*
    mvn clean package

    npx percy exec -- java -jar target/percy-test/dependencies/lessons-1.0-SNAPSHOT.jar
     */

    public static void main(String args[]) {
        WebDriver driver = null;
        Percy percy = null;
        try {
            FirefoxOptions options = new FirefoxOptions();
            options.setHeadless(true);
            driver = new FirefoxDriver(options);
            percy = new Percy(driver);

            driver.get("https://ya.ru");
            percy.snapshot("ya welcome");

            driver.findElement(By.name("text")).sendKeys("Что такое Яндекс?");
            driver.findElement(By.tagName("button")).click();

            percy.snapshot("ya result");

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (driver != null) {
                driver.quit();
                driver = null;
            }
        }
    }

}
