package com.learning.selenium.lesson11.util;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import java.util.List;

public class Commands {

    public static void setText(WebElement element, String text) {
        element.clear();
        element.sendKeys(text);
    }

    public static void selectFirstOptionSafety(List<WebElement> elements) {
        if (elements != null && elements.size() > 0) {
            Select select = new Select(elements.get(0));
            select.selectByValue(select.getOptions().get(1).getText());
        }
    }

    public static int getTextAsInt(WebElement element) {
        return Integer.parseInt(element.getText());
    }

}
