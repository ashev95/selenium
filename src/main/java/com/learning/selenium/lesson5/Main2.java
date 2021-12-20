package com.learning.selenium.lesson5;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import java.util.LinkedList;
import java.util.List;

public class Main2 {

    private static final String URL = "http://localhost:8080/litecart/admin/?app=geo_zones&doc=geo_zones";
    private static final String LOGIN = "admin";
    private static final String PASSWORD = "admin";

    private static final String CSS_COUNTRY_LINKS = "table.dataTable > tbody > tr.row > td:nth-child(3) > a";

    private static final String XPATH_TIME_ZONES = ".//table[@id='table-zones']/tbody/tr[not(@class='header') and not(./td[@colspan])]/td[3]/select/option[@selected]";

    public static void main(String args[]) {
        WebDriver driver = null;
        try {
            driver = new ChromeDriver();
            driver.navigate().to(URL);
            driver.findElement(By.name("username")).sendKeys(LOGIN);
            driver.findElement(By.name("password")).sendKeys(PASSWORD);
            driver.findElement(By.name("remember_me")).click();
            driver.findElement(By.name("login")).click();
            List<String> links = new LinkedList<>();
            List<WebElement> aTags = driver.findElements(By.cssSelector(CSS_COUNTRY_LINKS));
            for (WebElement aTag : aTags) links.add(aTag.getAttribute("href")); //получим сразу все ссылки
            for (String link : links) {
                System.out.println("Обрабатываем ссылку: " + link);
                driver.navigate().to(link);
                String prevZone = null;
                List<WebElement> timeZones = driver.findElements(By.xpath(XPATH_TIME_ZONES));
                String zone;
                for (WebElement timeZone : timeZones) {
                    zone = timeZone.getText();
                    if (prevZone != null) {
                        if (prevZone.compareTo(zone) > 0) {
                            throw new RuntimeException("Строка со значением " + prevZone + " больше, чем строка cо значением " + zone + ". \nРабота прервана");
                        }
                        System.out.println("Сравнение пройдено: " + zone);
                    }
                    prevZone = zone;
                }
            }
        } finally {
            if (driver != null) {
                driver.quit();
                driver = null;
            }
        }
    }

    private class Country {
        private String name;
        private String link;

        public Country(String name, String link) {
            this.name = name;
            this.link = link;
        }

        public String getName() {
            return name;
        }

        public String getLink() {
            return link;
        }
    }

}
