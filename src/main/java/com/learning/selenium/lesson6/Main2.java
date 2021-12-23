package com.learning.selenium.lesson6;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.Select;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class Main2 {

    private static final String URL = "http://localhost:8080/litecart/admin/";
    private static final String LOGIN = "admin";
    private static final String PASSWORD = "admin";

    private static final String CSS_CATALOG_LINK = "#box-apps-menu > li > a[href$=catalog]";
    private static final String CSS_CREATE_PRODUCT_LINK = ".button[href$=edit_product]";

    private static final String CSS_TAB_INFORMATION_LINK = "div.tabs > ul > li > a[href='#tab-information']";
    private static final String CSS_TAB_PRICES_LINK = "div.tabs > ul > li > a[href='#tab-prices']";

    public static void main(String args[]) {
        WebDriver driver = null;
        try {
            driver = new ChromeDriver();
            driver.navigate().to(URL);
            driver.findElement(By.name("username")).sendKeys(LOGIN);
            driver.findElement(By.name("password")).sendKeys(PASSWORD);
            driver.findElement(By.name("remember_me")).click();
            driver.findElement(By.name("login")).click();

            driver.findElement(By.cssSelector(CSS_CATALOG_LINK)).click();
            driver.findElement(By.cssSelector(CSS_CREATE_PRODUCT_LINK)).click();

            String id = UUID.randomUUID().toString().replace("{", "").replace("}", "").replace("-", "");
            String duckName = "DuckToy_" + id;

            ProductForm form = new ProductForm(true, duckName, 1, Arrays.asList(new String[]{"Root", "Rubber Ducks", "Subcategory"}),
                    "Rubber Ducks", Arrays.asList(new String[]{"1-1", "1-2", "1-3"}), 3.10, "pcs", "3-5 days",
                    "Temporary sold out", "src/main/resources/image/duck.jpg", "20.12.2021", "30.12.2021" ,
                    "ACME Corp.", "", "Duck,Toy", "Cute duck", "It's a very cute duck. Buy it!",
                    "Duck Toy", "Toy", 6.95, "Euros", "", 2, 1);

            //General
            List<WebElement> statuses = driver.findElements(By.cssSelector("div#tab-general input[name=status]"));
            WebElement statusEnabledElement = statuses.get(0);
            WebElement statusDisabledElement = statuses.get(1);
            if (form.isEnabled() && statusEnabledElement.getAttribute("checked") == null) {
                statusEnabledElement.click();
            } else if (!form.isEnabled() && statusEnabledElement.getAttribute("checked") != null) {
                statusDisabledElement.click();
            }
            driver.findElement(By.name("name[en]")).sendKeys(form.getName());
            driver.findElement(By.name("code")).sendKeys(Integer.toString(form.getCode()));

            for (WebElement element : driver.findElements(By.name("categories[]"))) {
                if (form.getCategories().contains(element.getAttribute("data-name")) && element.getAttribute("checked") == null) {
                    element.click();
                }
            }

            new Select(driver.findElement(By.name("default_category_id"))).selectByVisibleText(form.getDefaultCategory());

            for (WebElement element : driver.findElements(By.name("product_groups[]"))) {
                if (form.getGenders().contains(element.getAttribute("value"))) {
                    element.click();
                }
            }

            driver.findElement(By.name("quantity")).clear();
            driver.findElement(By.name("quantity")).sendKeys(Double.toString(form.getQuantity()));

            new Select(driver.findElement(By.name("quantity_unit_id"))).selectByVisibleText(form.getQuantityUnit());
            new Select(driver.findElement(By.name("delivery_status_id"))).selectByVisibleText(form.getDeliveryStatus());
            new Select(driver.findElement(By.name("sold_out_status_id"))).selectByVisibleText(form.getSoldOutStatus());

            driver.findElement(By.name("new_images[]")).sendKeys(new File(form.getImagePath()).getAbsolutePath());

            driver.findElement(By.name("date_valid_from")).sendKeys(form.getDateValidFrom());
            driver.findElement(By.name("date_valid_to")).sendKeys(form.getDateValidTo());

            //Information
            driver.findElement(By.cssSelector(CSS_TAB_INFORMATION_LINK)).click();

            while (!driver.findElement(By.name("manufacturer_id")).isDisplayed()) {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            new Select(driver.findElement(By.name("manufacturer_id"))).selectByVisibleText(form.getManufacturer());

            Select supplierSelect = new Select(driver.findElement(By.name("supplier_id")));
            if (supplierSelect.getOptions().size() > 1) {
                supplierSelect.selectByVisibleText(form.getSupplier());
            }

            driver.findElement(By.name("keywords")).sendKeys(form.getKeywords());
            driver.findElement(By.name("short_description[en]")).sendKeys(form.getShortDescription());

            driver.findElement(By.cssSelector(".trumbowyg-editor")).sendKeys(form.getDescription());

            driver.findElement(By.name("head_title[en]")).sendKeys(form.getHeadTitle());

            driver.findElement(By.name("meta_description[en]")).sendKeys(form.getMetaDescription());

            //Prices
            driver.findElement(By.cssSelector(CSS_TAB_PRICES_LINK)).click();

            while (!driver.findElement(By.name("purchase_price")).isDisplayed()) {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            driver.findElement(By.name("purchase_price")).clear();
            driver.findElement(By.name("purchase_price")).sendKeys(Double.toString(form.getPurchasePrice()));

            new Select(driver.findElement(By.name("purchase_price_currency_code"))).selectByVisibleText(form.getPurchasePriceCurrency());

            Select taxClassIdElement = new Select(driver.findElement(By.name("tax_class_id")));
            if (taxClassIdElement.getOptions().size() > 1) {
                taxClassIdElement.selectByVisibleText(form.getTaxClass());
            }

            driver.findElement(By.name("prices[USD]")).sendKeys(Integer.toString(form.getPriceTaxUSD()));
            driver.findElement(By.name("prices[EUR]")).sendKeys(Integer.toString(form.getPriceTaxEUR()));

            driver.findElement(By.cssSelector("button[value=Save]")).click();

            for (WebElement webElement : driver.findElements(By.cssSelector(".dataTable a[href*=edit_product]"))) {
                if (webElement.getText().equals(duckName)) {
                    System.out.println("Нашли добавленный товар!");
                    try {
                        Thread.sleep(15000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    return;
                }
            }

            System.out.println("Добавленный товар не нашли!");

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

    private static class ProductForm {
        private boolean enabled;
        private String name;
        private int code;
        private List<String> categories;
        private String defaultCategory;
        private List<String> genders;
        private double quantity;
        private String quantityUnit;
        private String deliveryStatus;
        private String soldOutStatus;
        private String imagePath;
        private String dateValidFrom;
        private String dateValidTo;

        private String manufacturer;
        private String supplier;
        private String keywords;
        private String shortDescription;
        private String description;
        private String headTitle;
        private String metaDescription;

        private double purchasePrice;
        private String purchasePriceCurrency;
        private String taxClass;
        private int priceTaxUSD;
        private int priceTaxEUR;

        public ProductForm(boolean enabled, String name, int code, List<String> categories, String defaultCategory,
                           List<String> genders, double quantity, String quantityUnit, String deliveryStatus,
                           String soldOutStatus, String imagePath, String dateValidFrom, String dateValidTo,
                           String manufacturer, String supplier, String keywords, String shortDescription,
                           String description, String headTitle, String metaDescription, double purchasePrice,
                           String purchasePriceCurrency, String taxClass, int priceTaxUSD, int priceTaxEUR) {
            this.enabled = enabled;
            this.name = name;
            this.code = code;
            this.categories = categories;
            this.defaultCategory = defaultCategory;
            this.genders = genders;
            this.quantity = quantity;
            this.quantityUnit = quantityUnit;
            this.deliveryStatus = deliveryStatus;
            this.soldOutStatus = soldOutStatus;
            this.imagePath = imagePath;
            this.dateValidFrom = dateValidFrom;
            this.dateValidTo = dateValidTo;
            this.manufacturer = manufacturer;
            this.supplier = supplier;
            this.keywords = keywords;
            this.shortDescription = shortDescription;
            this.description = description;
            this.headTitle = headTitle;
            this.metaDescription = metaDescription;
            this.purchasePrice = purchasePrice;
            this.purchasePriceCurrency = purchasePriceCurrency;
            this.taxClass = taxClass;
            this.priceTaxUSD = priceTaxUSD;
            this.priceTaxEUR = priceTaxEUR;
        }

        public boolean isEnabled() {
            return enabled;
        }

        public String getName() {
            return name;
        }

        public int getCode() {
            return code;
        }

        public List<String> getCategories() {
            return categories;
        }

        public String getDefaultCategory() {
            return defaultCategory;
        }

        public List<String> getGenders() {
            return genders;
        }

        public double getQuantity() {
            return quantity;
        }

        public String getQuantityUnit() {
            return quantityUnit;
        }

        public String getDeliveryStatus() {
            return deliveryStatus;
        }

        public String getSoldOutStatus() {
            return soldOutStatus;
        }

        public String getImagePath() {
            return imagePath;
        }

        public String getDateValidFrom() {
            return dateValidFrom;
        }

        public String getDateValidTo() {
            return dateValidTo;
        }

        public String getManufacturer() {
            return manufacturer;
        }

        public String getSupplier() {
            return supplier;
        }

        public String getKeywords() {
            return keywords;
        }

        public String getShortDescription() {
            return shortDescription;
        }

        public String getDescription() {
            return description;
        }

        public String getHeadTitle() {
            return headTitle;
        }

        public String getMetaDescription() {
            return metaDescription;
        }

        public double getPurchasePrice() {
            return purchasePrice;
        }

        public String getPurchasePriceCurrency() {
            return purchasePriceCurrency;
        }

        public String getTaxClass() {
            return taxClass;
        }

        public int getPriceTaxUSD() {
            return priceTaxUSD;
        }

        public int getPriceTaxEUR() {
            return priceTaxEUR;
        }
    }

}
