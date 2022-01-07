package com.learning.selenium.lesson9;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.URL;

public class Main {

    public static void main(String args[]) {
        WebDriver driver = null;
        try {
            FirefoxOptions firefoxOptions = new FirefoxOptions();
            driver = new RemoteWebDriver(new URL("http://192.168.0.102:4444"), firefoxOptions);
            driver.get("http://www.ya.ru");

            Thread.sleep(5000);
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
