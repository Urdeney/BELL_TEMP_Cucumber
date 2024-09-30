package pages.yandex;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

import static helpers.CustomWaits.isWebElementDisplayed;

/**
 * Класс для результатов поиска по фильтрам
 * @author Нурее Андрей
 */
public class YMSearchResult {

    private WebDriver driver;
    private WebElement resultElement;

    private String titleText;

    private String priceText;

    private String goodDescriptionText = ".//*[text() and not(@class='ds-visuallyHidden')]";

    public YMSearchResult(WebDriver driver, WebElement element){
        this.driver = driver;
        this.resultElement = element;
        this.titleText = resultElement.findElement(By.xpath(".//*[contains(@data-baobab-name,'title')]//*[text()]")).getText();
        this.priceText = resultElement.findElement(By.xpath(".//*[contains(@data-auto,'price')]//*[text()]")).getText();
    }

    public WebElement getResultElement() {
        return resultElement;
    }

    public String getTitleText() {
        return titleText;
    }

    public String getPriceText() {
        return priceText;
    }

    /**
     * Получение описания из карточки результата поиска
     * @return
     * @author Нуреев Андрей
     */
    public String getGoodDescriptionText() {
        StringBuilder searchText = new StringBuilder();

        for (WebElement textElement : resultElement.findElements(By.xpath(goodDescriptionText))) {
            searchText.append(textElement.getText());
        }

        return searchText.toString();
    }

    public boolean isEqualByTitle(YMSearchResult otherSearchResult){
        return (this.titleText.equals(otherSearchResult.titleText));
    }
}
