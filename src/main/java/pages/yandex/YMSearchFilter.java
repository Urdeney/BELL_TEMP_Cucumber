package pages.yandex;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;


import java.util.ArrayList;
import java.util.List;

import static helpers.CustomWaits.*;
import static helpers.CustomWaits.isWebElementDisplayed;

/**
 * Класс для поисковых фильтров товаров
 * @author Нуреев Андрей
 */
public class YMSearchFilter {

    private WebDriver driver;

    private WebElement filterElement;

    private String filterName;

    public  static enum FilterType {Range,Boolean,Radio,Enum};

    private FilterType filterType;

    public List<String> filterParams;

    public YMSearchFilter(WebDriver driver, WebElement webElement){
        this.driver = driver;
        this.filterElement = webElement;
        this.filterName = this.initializeName();
        this.filterType = this.initializeType();
        this.filterParams = new ArrayList<>();
    }

    /**
     * Инициализация заголовка для результа поиска по фильтрам
     * @return
     * @author Нуреев Андрей
     */
    public String initializeName(){
        isWebElementDisplayed(driver,filterElement,".//*[text()]",3);
        return filterElement.findElement(By.xpath(".//*[text()]")).getText();
    }

    /**
     * Инициализация типа фильтра для результа поиска по фильтрам
     * @return
     * @author Нуреев Андрей
     */
    public FilterType initializeType(){
        String typeStr = filterElement.getAttribute("data-filter-type");
        switch (typeStr) {
            case "range":
                return FilterType.Range;
            case "boolean":
                return FilterType.Boolean;
            case "radio":
                return FilterType.Radio;
            case "enum":
                return FilterType.Enum;
            default:
                throw new IllegalStateException("Не удалось определить тип фильтра");
        }
    }

    public WebDriver getDriver() {
        return driver;
    }

    public FilterType getFilterType() {
        return filterType;
    }

    public String getFilterName() {
        return filterName;
    }

    public List<String> getFilterParams() {
        return filterParams;
    }

    public WebElement getFilterElement() {
        return filterElement;
    }

    /**
     * Функция для установки параметров для фильтров нв веб-странице
     * @param params параметры фильтра
     * @author Нуреев Андрей
     */
    public void setFilterParams(List<String> params){
        switch (filterType){
            case Range:
                SetRangeFilter(params);
                break;
            case Boolean:
                SetBooleanFilter(params);
                break;
            case Radio:
                SetRadioFilter(params);
                break;
            case Enum:
                SetEnumFilter(params);
                break;
            default:
                throw new IllegalStateException("Не удалось установить значения фильтра");
        }
    }

    /**
     * Функция для установки парамтеров для range-фильтра
     * @param params параметры фильтра
     * @author Нуреев Андрей
     */
    public void SetRangeFilter(List<String> params){
        List<WebElement> rangeValues = filterElement.findElements(By.xpath(".//input[contains(@type,'text')]"));
        if (rangeValues.size() != 2) {throw new IllegalArgumentException("Не удалось найти поля для фильтра-диапазона");}
        if (params.size() != 2) {throw new IllegalArgumentException("Неверное количество аргументов для фильтра-диапазона");}
        rangeValues.get(0).sendKeys(params.get(0)); // значение "от" диапазона
        rangeValues.get(1).sendKeys(params.get(1)); // значение "до" диапазона
        this.filterParams = params;
    }

    /**
     * Функция для установки парамтеров для boolean-фильтра
     * @param params параметры фильтра
     * @author Нуреев Андрей
     */
    public void SetBooleanFilter(List<String> params){
        if (params.size()!= 1){throw new IllegalArgumentException("Неверное количество аргументов для фильтра-переключателя");}
        if (params.get(0).equals("Да")){
            filterElement.findElement(By.xpath(".//input[contains(@type,'checkbox')]")).click();
        }
        this.filterParams = params;
    }

    /**
     * Функция для установки парамтеров для radio-фильтра
     * @param params параметры фильтра
     * @author Нуреев Андрей
     */
    public void SetRadioFilter(List<String> params){
        if (params.size()!=1) {throw new IllegalArgumentException("Неправильное количество параметров для радио-фильтра");}
        filterElement.findElement(By.xpath(String.format(".//*[contains(text(),'%s')]//ancestor::label",params.get(0)))).click();
        this.filterParams = params;
    }

    /**
     * Функция для установки парамтеров для enum-фильтра
     * @param params параметры фильтра
     * @author Нуреев Андрей
     */
    public void SetEnumFilter(List<String> params){
        for (String param : params){
            boolean isFilterValueVisible= isWebElementDisplayed(driver,filterElement,String.format(".//*[contains(text(),'%s')]//ancestor::label",param),2);
            if (isFilterValueVisible)
                filterElement.findElement(By.xpath(String.format(".//*[contains(text(),'%s')]//ancestor::label",param))).click();
            else {
                isWebElementDisplayed(driver,filterElement,".//button",3);
                this.filterElement.findElement(By.xpath(".//button")).click();
                filterElement.findElement(By.xpath(".//input[contains(@type,'text')]")).sendKeys(param);
                isWebElementDisplayed(driver,filterElement,String.format(".//label//*[text()='%s']",param),5);
                filterElement.findElement(By.xpath(String.format(".//label//*[text()='%s']",param))).click();
            }
            this.filterParams = params;
        }
    }
}
