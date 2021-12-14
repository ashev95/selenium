package com.learning.selenium.lesson4;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class Main {

    private static final String URL = "http://localhost:8080/litecart/admin";
    private static final String LOGIN = "admin";
    private static final String PASSWORD = "admin";

    //Выражение состоит из 2 подвыражений:
    //  1: Получить первый элемент списка, который идёт следующим за выделенным элементом
    //  2: Получить первый элемент списка, если во всем меню нет выделенного элемента
    private static final String XPATH_MAIN_PANES = "//*[@id='box-apps-menu']/li[@class='selected']/following-sibling::li[1] | //*[@id='box-apps-menu']/li[count(//*[@id='box-apps-menu']/li[@class='selected'])=0][1]";

    //Получить первый элемент дочернего списка, который идёт следующим за выделенным элементом
    private static final String XPATH_SUB_PANES = "//li[@class='selected']/ul/li[@class='selected']/following-sibling::li[1]";

    //Получить первый выделенный элемент дочернего списка
    private static final String XPATH_SUB_SELECTED = "//li[@class='selected']/ul/li[@class='selected'][1]";

    //Получить элемент, который содержит название открытой страницы
    private static final String XPATH_PAGE_TITLE = "//h1[1]";

    public static void main(String args[]) {
        WebDriver driver = null;
        try {
            driver = new ChromeDriver();
            //авторзация
            driver.navigate().to(URL);
            driver.findElement(By.name("username")).sendKeys(LOGIN);
            driver.findElement(By.name("password")).sendKeys(PASSWORD);
            driver.findElement(By.name("remember_me")).click();
            driver.findElement(By.name("login")).click();
            //Проход по всем разделам админки:
            //1. Если у родительского элемента нет дочернего элемента,
            // то сверяется название элемента родительского элемента с заголовком страницы
            //2. Если в ходе сопоставления имени элемента и заголовка страницы выявлены несоответствия,
            // выводится сообщение в консоль и программа продолжает проходить по остальным элементам
            List<WebElement> mainElements = getMainElements(driver); //выбрал список, чтобы избежать исключения
            while(mainElements.size() > 0) { //пробег по основным элементам
                String paneName = mainElements.get(0).getText();
                String pageTitle = null;
                mainElements.get(0).click();
                List<WebElement> selectedSubElements = getSelectedSubElements(driver); //выбрал список, чтобы избежать исключения
                if (selectedSubElements.size() > 0) { //если есть дочерние элементы
                    paneName = selectedSubElements.get(0).getText();
                    selectedSubElements.get(0).click();
                    pageTitle = getPageTitle(driver);
                    assertEquals(paneName, pageTitle, false);
                    //sleepMs(1000); //чтобы успеть проследить за каждым действием
                    List<WebElement> childElements = getSubElements(driver); //выбрал список, чтобы избежать исключения
                    while(childElements.size() > 0) { //пробег по дочерним элементам
                        paneName = childElements.get(0).getText();
                        childElements.get(0).click();
                        pageTitle = getPageTitle(driver);
                        assertEquals(paneName, pageTitle, false);
                        //sleepMs(1000); //чтобы успеть проследить за каждым действием
                        childElements = getSubElements(driver);
                    }
                } else { //если нет дочерних элементов
                    pageTitle = getPageTitle(driver);
                    assertEquals(paneName, pageTitle, false);
                }
                mainElements = getMainElements(driver);
            }
        } finally {
            if (driver != null) {
                driver.quit();
                driver = null;
            }
        }
    }

    private static List<WebElement> getMainElements(WebDriver driver) {
        return driver.findElements(By.xpath(XPATH_MAIN_PANES));
    }

    private static List<WebElement> getSubElements(WebDriver driver) {
        return driver.findElements(By.xpath(XPATH_SUB_PANES));
    }

    private static List<WebElement> getSelectedSubElements(WebDriver driver) {
        return driver.findElements(By.xpath(XPATH_SUB_SELECTED));
    }

    private static String getPageTitle(WebDriver driver) {
        return driver.findElement(By.xpath(XPATH_PAGE_TITLE)).getText();
    }

    private static void sleepMs(long ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static void assertEquals(String paneName, String pageTitle, boolean throwException) {
        if (!paneName.equals(pageTitle)) {
            String msg = "Был нажат элемент: " + paneName + ". Получена страница: " + pageTitle;
            if (throwException) {
                throw new RuntimeException(msg);
            } else {
                System.out.println(msg);
            }
        }
    }

}
