package com.learning.selenium.lesson5;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import java.util.LinkedList;
import java.util.List;

public class Main {

    private static final String URL = "http://localhost:8080/litecart/admin/?app=countries&doc=countries";
    private static final String LOGIN = "admin";
    private static final String PASSWORD = "admin";

    private static final String XPATH_COUNTRY_ROWS = ".//table[@class='dataTable']/tbody/tr[@class='row']";
    private static final String XPATH_COUNTRY_CELL_NAME = "td[5]";
    private static final String XPATH_COUNTRY_CELL_TIME_ZONE = "td[6]";

    private static final String XPATH_GEO_ZONE_ROWS = ".//table[@id='table-zones']/tbody/tr[not(@class='header') and not(./td[text()='\u00A0'][1])]";
    private static final String XPATH_GEO_ZONE_CELL_NAME = "td[3]";

    public static void main(String args[]) {
        WebDriver driver = null;
        try {
            driver = new ChromeDriver();
            driver.navigate().to(URL);
            driver.findElement(By.name("username")).sendKeys(LOGIN);
            driver.findElement(By.name("password")).sendKeys(PASSWORD);
            driver.findElement(By.name("remember_me")).click();
            driver.findElement(By.name("login")).click();
            List<WebElement> rows = driver.findElements(By.xpath(XPATH_COUNTRY_ROWS));
            //для проверки расположения стран в алфавитном порядке достаточно проверять,
            //что каждое последующее наименование больше предыдущего
            String prevName = null;
            List<GeoZone> geoZones = new LinkedList<>(); //по пути сформируем список ссылок, по которым необходимо перейти для сравнения геозон
            for (WebElement row : rows) {
                WebElement cell = getCountryNameCell(row);
                String name = cell.getText();
                if (prevName != null) {
                    if (prevName.compareTo(name) > 0) {
                        throw new RuntimeException("Строка со значением " + prevName + " больше, чем строка cо значением " + name + ". \nРабота прервана");
                    }
                    System.out.println("Сравнение пройдено: " + name);
                }
                int timeZone = getCountryTimeZone(row);
                if (timeZone != 0) {
                    String link = cell.findElement(By.xpath("a")).getAttribute("href");
                    geoZones.add(new GeoZone(name, link));
                    System.out.println("Страна " + name + " добавлена в список обрабокти геозон");
                }
                prevName = name;
            }
            for (GeoZone geoZone : geoZones) { //проходим по геозонам стран, подпадающих под критерий "часовой пояс не равен нулю"
                System.out.println("Проверяем геозону для " + geoZone.getCountry());
                driver.navigate().to(geoZone.getLink());
                prevName = null;
                rows = driver.findElements(By.xpath(XPATH_GEO_ZONE_ROWS));
                for (WebElement row : rows) {
                    String name = getGeoZoneName(row);
                    if (prevName != null) {
                        if (prevName.compareTo(name) > 0) {
                            throw new RuntimeException("Строка со значением " + prevName + " больше, чем строка cо значением " + name + ". \nРабота прервана");
                        }
                        System.out.println("Сравнение пройдено: " + name);
                    }
                    prevName = name;
                }
            }
        } finally {
            if (driver != null) {
                driver.quit();
                driver = null;
            }
        }
    }

    private static WebElement getCountryNameCell(WebElement row) {
        return row.findElement(By.xpath(XPATH_COUNTRY_CELL_NAME));
    }

    private static int getCountryTimeZone(WebElement row) {
        return Integer.parseInt(row.findElement(By.xpath(XPATH_COUNTRY_CELL_TIME_ZONE)).getText());
    }

    private static String getGeoZoneName(WebElement row) {
        return row.findElement(By.xpath(XPATH_GEO_ZONE_CELL_NAME)).getText();
    }

    private static class GeoZone {
        private String country;
        private String link;

        public GeoZone(String country, String link) {
            this.country = country;
            this.link = link;
        }

        public String getCountry() {
            return country;
        }

        public String getLink() {
            return link;
        }
    }

}
