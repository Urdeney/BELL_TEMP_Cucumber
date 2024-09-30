package steps;

import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import pages.yandex.YMGoodsPage;
import pages.yandex.YMMainPage;

import java.util.List;
import java.util.Map;

public class StepsAll {

    private static WebDriverWait wait;
    private static WebDriver driver;

    @Step("Переход на сайт: {url}")
    public static void openSite(String url, String title, WebDriver currentDriver) {
        driver = currentDriver;
        driver.get(url);
        wait = new WebDriverWait(driver, 30);
        wait.until(ExpectedConditions.titleContains(title));
    }

    @Step("Открытие каталога сайта переход в на страницу раздела: {category}->{goods}")
    public static void openYMCatalog(String category, String goods) {
        YMMainPage mainPage = new YMMainPage(driver);
        mainPage.openCategoryTab();
        mainPage.moveToCategoryName(category);
        mainPage.goGoodsPage(goods);
    }

    @Step("Проверка, что заголовок текущей страницы содержит: {name}")
    public static void checkCurrentSiteTitle(String name){
        wait = new WebDriverWait(driver, 30);
        wait.until(ExpectedConditions.titleContains(name));
    }

    @Step("Поиск товаров по установленным фильтрам {filters}")
    public static void searchYMGoods(Map<String, List<String>> filters) {
        YMGoodsPage goodsPage = new YMGoodsPage(driver);
        goodsPage.setFiltersParams(filters);
        goodsPage.checkGoodsQuantity();
        goodsPage.proceedSearchResults();
        goodsPage.searchText(goodsPage.firstSearchResult().getTitleText());
        goodsPage.findSearchResultByTitle(goodsPage.firstSearchResult().getTitleText());
    }
}
