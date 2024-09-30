package pages.yandex;

import helpers.Assertions;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static helpers.CustomWaits.isWebElementDisplayed;
import static helpers.CustomWaits.waitInvisibleIfLocated;

/**
 * Класс PO для веб-страницы с товарами и фильтрами
 * @author Нуреев Андрей
 */
public class YMGoodsPage {

    public WebDriver driver;

    //Список фильтров поиска
    protected List<YMSearchFilter> searchFilters;

    //Xpath-локатор для результатов поиска
    protected String searchResults = "//div[contains(@data-zone-name,'searchPage')]//div[contains(@data-zone-name,'searchResults')]//div[contains(@data-auto-themename,'list')]";

    protected String moreGoodsButton = "//div[contains(@data-zone-name,'SearchPager')]//*[contains(text(),'Вперёд')]";

    protected String searchInput = "//input[@data-auto='search-input' and @id='header-search']";

    protected String searchButton = "//button[@type='submit' and @data-auto='search-button']";

    protected String loaderElement = "//*[@data-auto='spinner' and @role='progressbar']";

    public YMGoodsPage(WebDriver driver) {
        this.driver = driver;
        //инициализация фильтров
        this.searchFilters = initializeFilters("//*[@data-grabber='SearchFilters']/div");
    }

    /**
     * Функция для создания списка фильтров поиска
     * @param Xpath Xpath-локатор фильтров
     * @return
     * @author Нуреев Андрей
     */
    private List<YMSearchFilter> initializeFilters(String Xpath) {
        List<YMSearchFilter> filterList = new ArrayList<>();
        for (WebElement element : driver.findElements(By.xpath(Xpath))) {
            filterList.add(new YMSearchFilter(driver, element));
        }
        return filterList;
    }

    /**
     * Функция для создания списка результатов поиска
     * @param Xpath Xpath-локатор элемента результатов поиска
     * @return
     * @author Нуреев Андрей
     */
    private List<YMSearchResult> initializeSearchResults(String Xpath) {
        List<YMSearchResult> resultList = new ArrayList<>();
        for (WebElement element : driver.findElements(By.xpath(Xpath))) {
            resultList.add(new YMSearchResult(driver,element));
        }
        return resultList;
    }

    /**
     * Установка фильтров в поля класа YMSearchFilter
     * @param params фильтры, которые необходимо установить
     * @author Нуреев Андрей
     */
    public void setFiltersParams(Map<String, List<String>> params) {
        for (Map.Entry<String, List<String>> entry : params.entrySet()) {
            String filterName = entry.getKey();
            for (YMSearchFilter element : searchFilters) {
                if (filterName.equals(element.getFilterName())) {
                    List<String> filterParams = entry.getValue();
                    element.setFilterParams(filterParams);
                }
            }
        }
    }

    /**
     * Функция для перебора результатов поиска
     * @author Нуреев Андрей
     */
    public void proceedSearchResults() {
        Actions actions = new Actions(driver);
        int listCounter = 0;
        String searchResultSlice;
        List<YMSearchResult> searchList;
        if (isWebElementDisplayed(driver, moreGoodsButton, 3)) {
            while (isWebElementDisplayed(driver, moreGoodsButton, 3)) {

                searchResultSlice = String.format("(%s)[position() > %s]",this.searchResults,listCounter);
                searchList = initializeSearchResults(searchResultSlice);
                for (int i = 0; i < searchList.size(); i++) {
                    //System.out.println(checkSearchResult(searchList.get(i)));
                    Assertions.assertTrueSoft(validateSearchResult(searchList.get(i)),
                            String.format("Найденный товар '%s' не соответствует высталенным фильтрам для поиска", searchList.get(i).getTitleText()));
                }
                listCounter += searchList.size();
                actions.moveToElement(searchList.get(searchList.size() - 1).getResultElement()).perform();
            }
        }

        searchResultSlice = String.format("(%s)[position() > %s]",this.searchResults,listCounter);
        searchList = initializeSearchResults(searchResultSlice);
        for (int i = 0; i < searchList.size(); i++) {
            //System.out.println(checkSearchResult(searchList.get(i)));
            Assertions.assertTrueSoft(validateSearchResult(searchList.get(i)),
                    String.format("Найденный товар '%s' не соответствует высталенным фильтрам для поиска", searchList.get(i).getTitleText()));
        }

        if (searchList.size()!= 0){
            actions.moveToElement(searchList.get(searchList.size() - 1).getResultElement()).perform();
        }
    }

    /**
     * Функция проверки результата поиска на соответсвие фильтров
     * @param searchElement  проверяемый YMSearchResult
     * @return boolean - удовлетворяет ли searchElement всем элементам
     * @author Нуреев Андрей
     */
    private boolean validateSearchResult(YMSearchResult searchElement) {
        List<Boolean> isChecked = new ArrayList<>();
        for (YMSearchFilter filterElem : this.getActiveSearchFilters()) {
            switch (filterElem.getFilterType()) {
                case Range:
                    List<Integer> rangeList = filterElem.filterParams.stream().map(x -> Integer.parseInt(x.replaceAll("\\D+", ""))).collect(Collectors.toList());
                    Integer elemInt = Integer.parseInt(searchElement.getPriceText().replaceAll("\\D+", ""));
                    boolean isRangePresent = (rangeList.get(0) <= elemInt) && (elemInt <= rangeList.get(1));
                    isChecked.add(isRangePresent);
                    break;
                case Boolean:
                    break;
                case Radio:
                    String radioStr = filterElem.filterParams.get(0);
                    boolean isRadioPresent = (searchElement.getTitleText().toLowerCase().contains(radioStr.toLowerCase())) || (searchElement.getGoodDescriptionText().contains(radioStr));
                    isChecked.add(isRadioPresent);
                    break;
                case Enum:
                    boolean isEnumPresent = false;
                    for (String param : filterElem.filterParams) {
                        isEnumPresent = (searchElement.getTitleText().toLowerCase().contains(param.toLowerCase()) || searchElement.getGoodDescriptionText().contains(param.toLowerCase()));
                        if(isEnumPresent) {
                            break;
                        }
                    }
                    isChecked.add(isEnumPresent);
                    break;
                default:
                    throw new IllegalStateException("Не удалось определить тип фильтра");
            }
        }
        return !isChecked.contains(false);
    }

    /**
     * Функция для получения установленных фильтров
     * @return список установленных фильтров
     * @author Нуреев Андрей
     */
    private List<YMSearchFilter> getActiveSearchFilters() {
        return this.searchFilters.stream().filter(x -> (!x.filterParams.isEmpty())).collect(Collectors.toList());
    }

    /**
     * Функция, которая возращает первый результат по установленным фильтрам
     * @return экземпляр YMSearchResult
     * @author Нуреев Андрей
     */
    public YMSearchResult firstSearchResult(){
        Actions actions = new Actions(driver);
        WebElement element = driver.findElement(By.xpath(searchResults));
        actions.moveToElement(element).perform();
        return new YMSearchResult(driver, element);
    }

    /**
     * Функция для поиска товаров по исходному тексту
     * @param text исходный текст
     * @author Нуреев Андрей
     */
    public void searchText(String text){
        driver.findElement(By.xpath(searchInput)).sendKeys(text);
        driver.findElement(By.xpath(searchButton)).click();
    }

    /**
     * Функция для проверки наличия 12 товаров в результатах поиска по фильтрам
     * @author Нуреев Андрей
     */
    public void checkGoodsQuantity(){
        checkGoodsQuantity(12);
    }

    /**
     * Функция для проверки наличия заданного количества товаров в результатах поиска по фильтрам
     * @param quantity количество товаров
     * @author Нуреев Андрей
     */
    public void checkGoodsQuantity(int quantity){
        waitInvisibleIfLocated(driver, loaderElement,2,5);
        boolean result;
        result = (driver.findElements(By.xpath(this.searchResults)).stream().count() > quantity);
        Assertions.assertTrue(result,"Не удалось найти " + quantity + " товаров на странице результатов поиска товаров");
    }

    /**
     * Функция для нахождения товара в результатах поиска по заголовку
     * @param title искомый текст
     * @author Нуреев Андрей
     */
    public void findSearchResultByTitle(String title){
        List<WebElement> webElement = driver.findElements(By.xpath(searchResults + String.format("//span[contains(text(),'%s')]",title)));
        if (webElement.size()!=0){
            Assertions.assertTrue(webElement.get(0).getText().contains(title),"Не удалось найти элемент с названием " + title);
        }
        else{
            Assertions.fail("Не удалось найти элемент с названием " + title);
        }
    }
}
