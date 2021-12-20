package com.learning.selenium.lesson5;

import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Main3 {

    private static final String URL = "http://localhost:8080/litecart";

    private static final String CSS_MAIN_PAGE_PRODUCTS = "div#box-campaigns  ul > li.product";

    private static final String CSS_PAGE_PRODUCT = "div#box-product";

    public static void main(String args[]) {
        WebDriver driver = null;
        try {
            driver = new ChromeDriver();
            driver.navigate().to(URL);
            WebElement productElement = driver.findElement(By.cssSelector(CSS_MAIN_PAGE_PRODUCTS));
            ProductProps mainPageProduct = parseFromMainPage(productElement);
            driver.navigate().to(mainPageProduct.getLink());
            WebElement productElement1 = driver.findElement(By.cssSelector(CSS_PAGE_PRODUCT));
            ProductProps pageProduct = parseFromProductPage(productElement1);
            mainPageProduct.assertProduct(pageProduct);
        } finally {
            if (driver != null) {
                driver.quit();
                driver = null;
            }
        }
    }

    private static ProductProps parseFromProductPage(WebElement productPage) {
        String name = productPage.findElement(By.cssSelector("h1.title")).getText();
        WebElement infoElement = productPage.findElement(By.xpath("./div[@class='content']/div[@class='information']"));
        WebElement priceSimpleElement = infoElement.findElement(By.xpath("./div[@class='price-wrapper']/s[@class='regular-price']"));
        WebElement pricePromoElement = infoElement.findElement(By.xpath("./div[@class='price-wrapper']/strong[@class='campaign-price']"));
        String priceSimpleText = priceSimpleElement.getText();
        String priceSimpleColor = priceSimpleElement.getCssValue("color");
        boolean priceSimpleCrossedOut = priceSimpleElement.getCssValue("text-decoration").contains("line-through");
        String pricePromoText = pricePromoElement.getText();
        String pricePromoColor = pricePromoElement.getCssValue("color");
        boolean pricePromoCrossedOut = priceSimpleElement.getCssValue("text-decoration").contains("line-through");
        Dimension priceSimpleSize = priceSimpleElement.getSize();
        Dimension pricePromoSize = pricePromoElement.getSize();
        int priceSimpleFontWeight = Integer.parseInt(priceSimpleElement.getCssValue("font-weight"));
        int pricePromoFontWeight = Integer.parseInt(pricePromoElement.getCssValue("font-weight"));
        return new ProductProps(name, null, priceSimpleText, priceSimpleColor, priceSimpleCrossedOut, priceSimpleSize,
                pricePromoText, pricePromoColor, pricePromoCrossedOut, pricePromoSize, priceSimpleFontWeight, pricePromoFontWeight);
    }

    private static ProductProps parseFromMainPage(WebElement productElement) {
        WebElement aTag = productElement.findElement(By.xpath("./a"));
        String link = aTag.getAttribute("href");
        String name = aTag.findElement(By.xpath("./div[@class='name']")).getText();
        WebElement priceSimpleElement = aTag.findElement(By.xpath("./div[@class='price-wrapper']/s[@class='regular-price']"));
        WebElement pricePromoElement = aTag.findElement(By.xpath("./div[@class='price-wrapper']/strong[@class='campaign-price']"));
        String priceSimpleText = priceSimpleElement.getText();
        String priceSimpleColor = priceSimpleElement.getCssValue("color");
        boolean priceSimpleCrossedOut = priceSimpleElement.getCssValue("text-decoration").contains("line-through");
        String pricePromoText = pricePromoElement.getText();
        String pricePromoColor = pricePromoElement.getCssValue("color");
        boolean pricePromoCrossedOut = priceSimpleElement.getCssValue("text-decoration").contains("line-through");
        Dimension priceSimpleSize = priceSimpleElement.getSize();
        Dimension pricePromoSize = pricePromoElement.getSize();
        int priceSimpleFontWeight = Integer.parseInt(priceSimpleElement.getCssValue("font-weight"));
        int pricePromoFontWeight = Integer.parseInt(pricePromoElement.getCssValue("font-weight"));
        return new ProductProps(name, link, priceSimpleText, priceSimpleColor, priceSimpleCrossedOut, priceSimpleSize,
                pricePromoText, pricePromoColor, pricePromoCrossedOut, pricePromoSize, priceSimpleFontWeight, pricePromoFontWeight);
    }

    private static class ProductProps {
        private String name;
        private String link;
        private String priceSimpleText;
        private String priceSimpleColor;
        private boolean priceSimpleCrossedOut;
        private Dimension priceSimpleSize;
        private String pricePromoText;
        private String pricePromoColor;
        private boolean pricePromoCrossedOut;
        private Dimension pricePromoSize;
        private boolean pricesHaveDiffColor;
        private boolean priceSimpleSizeLessThanPromo;
        private int priceSimpleFontWeight;
        private int pricePromoFontWeight;
        private boolean priceSimpleFontWeightLessThanPromo;

        public ProductProps(String name, String link, String priceSimpleText, String priceSimpleColor, boolean priceSimpleCrossedOut,
                            Dimension priceSimpleSize, String pricePromoText, String pricePromoColor,
                            boolean pricePromoCrossedOut, Dimension pricePromoSize, int priceSimpleFontWeight, int pricePromoFontWeight) {
            this.name = name;
            this.link = link;
            this.priceSimpleText = priceSimpleText;
            this.priceSimpleColor = priceSimpleColor;
            this.priceSimpleCrossedOut = priceSimpleCrossedOut;
            this.priceSimpleSize = priceSimpleSize;
            this.pricePromoText = pricePromoText;
            this.pricePromoColor = pricePromoColor;
            this.pricePromoCrossedOut = pricePromoCrossedOut;
            this.pricePromoSize = pricePromoSize;
            this.pricesHaveDiffColor = !this.priceSimpleColor.equals(this.pricePromoColor);
            this.priceSimpleSizeLessThanPromo = priceSimpleSize.getHeight() * priceSimpleSize.getWidth() < pricePromoSize.getHeight() * pricePromoSize.getWidth();
            this.priceSimpleFontWeight = priceSimpleFontWeight;
            this.pricePromoFontWeight = pricePromoFontWeight;
            this.priceSimpleFontWeightLessThanPromo = priceSimpleFontWeight < pricePromoFontWeight;
        }
        public void assertProduct(ProductProps props) {
            if (!name.equals(props.getName())) msg("Название товара", name, props.getName());
            if (!priceSimpleText.equals(props.getPriceSimpleText())) msg("Обычная цена товара", priceSimpleText, props.getPriceSimpleText());
            if (!pricePromoText.equals(props.getPricePromoText())) msg("Акционная цена товара", pricePromoText, props.getPricePromoText());
            if (priceSimpleCrossedOut != props.isPriceSimpleCrossedOut()) msg("Флаг зачёркнутости цвета обычной цены", Boolean.toString(priceSimpleCrossedOut), Boolean.toString(props.isPriceSimpleCrossedOut()));
            if (pricePromoCrossedOut != props.isPricePromoCrossedOut()) msg("Флаг зачёркнутости цвета акционной цены", Boolean.toString(pricePromoCrossedOut), Boolean.toString(props.isPricePromoCrossedOut()));
            if (priceSimpleSizeLessThanPromo != props.isPriceSimpleSizeLessThanPromo()) msg("Флаг \"Размер текста обычной цены меньше акционной\" ", Boolean.toString(priceSimpleSizeLessThanPromo), Boolean.toString(props.isPriceSimpleSizeLessThanPromo()));
            if (pricesHaveDiffColor != props.isPricesHaveDiffColor()) msg("Флаг \"Обычная и акционная цены имеют разный цвет\" ", Boolean.toString(pricesHaveDiffColor), Boolean.toString(props.isPricesHaveDiffColor()));
            if (priceSimpleFontWeightLessThanPromo != props.isPriceSimpleFontWeightLessThanPromo()) msg("Флаг \"Размер обычной цены меньше акционной\" ", Boolean.toString(priceSimpleFontWeightLessThanPromo), Boolean.toString(props.isPriceSimpleFontWeightLessThanPromo()));
        }

        private void msg(String propName, String v1, String v2) {
            System.out.println("Не совпадает " + propName + ". Сравниваемые значения: " + v1 + ", " + v2);
        }

        public String getLink() {
            return link;
        }

        public String getName() {
            return name;
        }

        public String getPriceSimpleText() {
            return priceSimpleText;
        }

        public String getPricePromoText() {
            return pricePromoText;
        }

        public boolean isPriceSimpleCrossedOut() {
            return priceSimpleCrossedOut;
        }

        public boolean isPricePromoCrossedOut() {
            return pricePromoCrossedOut;
        }

        public boolean isPriceSimpleSizeLessThanPromo() {
            return priceSimpleSizeLessThanPromo;
        }

        public boolean isPricesHaveDiffColor() {
            return pricesHaveDiffColor;
        }

        public boolean isPriceSimpleFontWeightLessThanPromo() {
            return priceSimpleFontWeightLessThanPromo;
        }
    }

}
