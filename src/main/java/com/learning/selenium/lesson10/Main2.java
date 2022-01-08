package com.learning.selenium.lesson10;

import net.lightbody.bmp.BrowserMobProxy;
import net.lightbody.bmp.BrowserMobProxyServer;
import net.lightbody.bmp.client.ClientUtil;
import net.lightbody.bmp.core.har.Har;
import org.openqa.selenium.By;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.LoggingPreferences;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.util.logging.Level;

public class Main2 {

    private static final String URL = "http://localhost:8080/litecart/admin/";
    private static final String LOGIN = "admin";
    private static final String PASSWORD = "admin";
    public static void main(String args[]) {
        WebDriver driver = null;
        BrowserMobProxy proxy = null;
        try {

            proxy = new BrowserMobProxyServer();
            proxy.start(0);
            Proxy seleniumProxy = ClientUtil.createSeleniumProxy(proxy);
            //seleniumProxy.setHttpProxy("127.0.0.1:8888");
            ChromeOptions chromeOptions = new ChromeOptions();
            chromeOptions.setCapability(CapabilityType.PROXY, seleniumProxy);
            driver = new ChromeDriver(chromeOptions);

            proxy.newHar();
            driver.navigate().to(URL);
            Har har = proxy.endHar();
            har.getLog().getEntries().forEach(e -> System.out.println(e.getResponse().getStatus() + " : " + e.getRequest().getUrl()));

            driver.findElement(By.name("username")).sendKeys(LOGIN);
            driver.findElement(By.name("password")).sendKeys(PASSWORD);
            driver.findElement(By.name("remember_me")).click();
            driver.findElement(By.name("login")).click();

        } finally {
            if (proxy != null) {
                try {
                    proxy.stop();
                    proxy = null;
                } catch (Exception e) {
                }
            }
            if (driver != null) {
                driver.quit();
                driver = null;
            }
        }
    }

}
