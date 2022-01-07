package com.learning.selenium.lesson9;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.URL;

public class Main1 {

    public static void main(String args[]) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                runFirefoxTest();
            }
        }).start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                runChromeTest();
            }
        }).start();
    }

    private static void runFirefoxTest() {
        WebDriver driver = null;
        try {
            FirefoxOptions firefoxOptions = new FirefoxOptions();
            driver = new RemoteWebDriver(new URL("http://192.168.56.105:4444"), firefoxOptions);
            driver.get("http://192.168.56.105:4444");

            Thread.sleep(15000);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (driver != null) {
                driver.quit();
                driver = null;
            }
        }
    }

    private static void runChromeTest() {
        WebDriver driver = null;
        try {
            ChromeOptions chromeOptions = new ChromeOptions();
            driver = new RemoteWebDriver(new URL("http://192.168.56.105:4444"), chromeOptions);
            driver.get("http://192.168.56.105:4444");

            Thread.sleep(15000);
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
