package pages.yandex;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.WebDriverWait;

import static org.openqa.selenium.support.ui.ExpectedConditions.visibilityOfElementLocated;

/**
 * Класс PO для главной страницы Яндекс Маркета
 * @author Нуреев Андрей
 */
public class YMMainPage {

    private WebDriver driver;

    private WebDriverWait wait;

    private Actions actions;

    private String catalogButton = "//div[@id='MainHeader']//*[contains(@data-apiary-widget-id,'catalogEntrypoint')]";

    private String catalogNames = "//ul[@role='tablist']/li[@role='tab' and @data-zone-name='category-link']";

    private String goodsNames = "//div[@role='heading']//div[@data-auto='category']/ul[@data-autotest-id='subItems']";


    public YMMainPage(WebDriver driver){
        this.driver = driver;
        wait = new WebDriverWait(driver,10);
        actions = new Actions(driver);
    }

    /**
     * Открытие вкладки "Каталог"
     * @author Нуреев Андрей
     */
    public void openCategoryTab() {
        wait.until(visibilityOfElementLocated(By.xpath(catalogButton)));
        driver.findElement(By.xpath(catalogButton)).click();
    }

    /**
     * Переход в указанный раздел
     * @param categoryName имя раздела
     * @author Нуреев Андрей
     */
    public void moveToCategoryName(String categoryName){
        String xpath = String.format(catalogNames +"//*[contains(text(),'%s')]/../..",categoryName);
        wait.until(visibilityOfElementLocated(By.xpath(xpath)));
        actions.moveToElement(driver.findElement(By.xpath(xpath))).perform();
        for (int i = 0; i < 10; i++){
            if(driver.findElement(By.xpath(xpath)).getAttribute("aria-selected").equals("true")){break;}
            actions.moveToElement(driver.findElement(By.xpath(xpath))).perform();
        }
    }

    /**
     * Переход на вкладку с указанным именем
     * @param goodsName имя вкладки
     * @author Нуреев Андрей
     */
    public void goGoodsPage(String goodsName){
        String xpath = String.format(goodsNames +"//a[text()='%s']",goodsName);
        wait.until(visibilityOfElementLocated(By.xpath(xpath)));
        actions.moveToElement(driver.findElement(By.xpath(xpath))).perform();
        driver.findElement(By.xpath(xpath)).click();
    }


}
