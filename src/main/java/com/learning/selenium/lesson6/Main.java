package com.learning.selenium.lesson6;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import java.util.LinkedList;
import java.util.List;

public class Main {

    private static final String URL = "http://localhost:8080/litecart";

    private static final String CSS_REG_LINK = "form > table > tbody > tr > td > a";
    private static final String CSS_LOGOUT_LINK = "#box-account .list-vertical a[href$=logout]";

    public static void main(String args[]) {
        WebDriver driver = null;
        try {
            driver = new ChromeDriver();
            driver.navigate().to(URL);
            driver.findElement(By.cssSelector(CSS_REG_LINK)).click();
            Form form = new Form(1, "InstanceCompany", "Андрей", "Юрченко",
                    "г. Москва, Комарова 13", "г. Москва, Комарова 14", 12345, "Москва",
                    "Российский Федерация", "andrey_yur12@yandex.ru", "+37689379041",
                    false, "12345");

            driver.findElement(By.name("tax_id")).sendKeys(Integer.toString(form.getTaxId()));
            driver.findElement(By.name("company")).sendKeys(form.getCompany());
            driver.findElement(By.name("firstname")).sendKeys(form.getFirstName());
            driver.findElement(By.name("lastname")).sendKeys(form.getLastName());
            driver.findElement(By.name("address1")).sendKeys(form.getAddr1());
            driver.findElement(By.name("address2")).sendKeys(form.getAddr2());
            driver.findElement(By.name("postcode")).sendKeys(Integer.toString(form.getPostCode()));
            driver.findElement(By.name("city")).sendKeys(form.getCity());

            driver.findElement(By.cssSelector("span.selection > span[role]")).click();
            for (WebElement li : driver.findElements(By.cssSelector("span.select2-results > ul > li"))) {
                if (li.getText().equals("United States")) {
                    li.click();
                    break;
                }
            }

            driver.findElement(By.name("email")).sendKeys(form.getEmail());
            driver.findElement(By.name("phone")).sendKeys(form.getPhoneNumber());

            WebElement newsLetterElement = driver.findElement(By.name("newsletter"));
            if ((form.getSubscribeOnNewsletter() && newsLetterElement.getAttribute("checked")  == null) ||
                    (!form.getSubscribeOnNewsletter() && newsLetterElement.getAttribute("checked")  != null)) {
                newsLetterElement.click();
            }

            driver.findElement(By.name("password")).sendKeys(form.getPassword());
            driver.findElement(By.name("confirmed_password")).sendKeys(form.getPassword());

            driver.findElement(By.name("create_account")).click();

            //выход из учётной записи
            driver.findElement(By.cssSelector(CSS_LOGOUT_LINK)).click();

            //вход под учётной записью
            driver.findElement(By.name("email")).sendKeys(form.getEmail());
            driver.findElement(By.name("password")).sendKeys(form.getPassword());
            driver.findElement(By.name("login")).click();
            try {
                Thread.sleep(15000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } finally {
            if (driver != null) {
                driver.quit();
                driver = null;
            }
        }
    }

    private static class Form {
        private int taxId;
        private String company;
        private String firstName;
        private String LastName;
        private String addr1;
        private String addr2;
        private int postCode;
        private String city;
        private String country;
        private String email;
        private String phoneNumber;
        private boolean subscribeOnNewsletter;
        private String password;

        public Form(int taxId, String company, String firstName, String lastName, String addr1, String addr2,
                    int postCode, String city, String country, String email, String phoneNumber,
                    boolean subscribeOnNewsletter, String password) {
            this.taxId = taxId;
            this.company = company;
            this.firstName = firstName;
            LastName = lastName;
            this.addr1 = addr1;
            this.addr2 = addr2;
            this.postCode = postCode;
            this.city = city;
            this.country = country;
            this.email = email;
            this.phoneNumber = phoneNumber;
            this.subscribeOnNewsletter = subscribeOnNewsletter;
            this.password = password;
        }

        public int getTaxId() {
            return taxId;
        }

        public String getCompany() {
            return company;
        }

        public String getFirstName() {
            return firstName;
        }

        public String getLastName() {
            return LastName;
        }

        public String getAddr1() {
            return addr1;
        }

        public String getAddr2() {
            return addr2;
        }

        public int getPostCode() {
            return postCode;
        }

        public String getCity() {
            return city;
        }

        public String getCountry() {
            return country;
        }

        public String getEmail() {
            return email;
        }

        public String getPhoneNumber() {
            return phoneNumber;
        }

        public boolean getSubscribeOnNewsletter() {
            return subscribeOnNewsletter;
        }

        public String getPassword() {
            return password;
        }
    }

}
