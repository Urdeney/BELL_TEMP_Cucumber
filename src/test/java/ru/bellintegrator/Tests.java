package ru.bellintegrator;

import io.qameta.allure.Feature;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import pages.yandex.YMGoodsPage;
import pages.yandex.YMMainPage;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static steps.StepsAll.*;
import static helpers.Properties.testsProperties;

public class Tests extends BaseTest{

    @Feature("Проверка UI Яндекс Маркета")
    @DisplayName("Проверка UI Яндекс Маркета - все в степах")
    @ParameterizedTest(name = "{displayName}: {arguments}")
    @MethodSource("helpers.DataProvider#providerCheckingYMNotebooksV1")
    public void checkYandexMarketStepsAll(String category, String goods, Map<String,List<String>> filters){
        openSite(testsProperties.YandexMarketUrl(),"Яндекс Маркет",chromeDriver);
        openYMCatalog(category,goods);
        checkCurrentSiteTitle(goods);
        searchYMGoods(filters);
    }

    @Feature("Проверка UI Яндекс Маркета")
    @DisplayName("Проверка UI Яндекс Маркета - базовая")
    @ParameterizedTest(name = "{displayName}: {arguments}")
    @MethodSource("helpers.DataProvider#providerCheckingYMNotebooksV2")
    public void checkYandexMarketBase(String category, String goods, Map<String,List<String>> filters) {
        chromeDriver.get(testsProperties.YandexMarketUrl());

        //инициализация класса главной страницы
        YMMainPage mainPage = new YMMainPage(chromeDriver);
        //отркрытие вкладки категорий
        mainPage.openCategoryTab();
        //выбор категорий в каталоге с помощью наведения мыши
        mainPage.moveToCategoryName(category);
        //переход на нужный раздел товаров
        mainPage.goGoodsPage(goods);


        //инициализация класса страницы поиска товаров
        YMGoodsPage goodsPage = new YMGoodsPage(chromeDriver);
        Map<String, List<String>> dataProvider = new LinkedHashMap<>(filters);
        //Установка фильтров
        goodsPage.setFiltersParams(dataProvider);
        //Проверка того, что на старнице находится более 12 элементов (с загрузкой)
        goodsPage.checkGoodsQuantity();
        //Проверка соответвия результатов
        goodsPage.proceedSearchResults();
        //Вбить в поиск первый найденный элемент
        goodsPage.searchText(goodsPage.firstSearchResult().getTitleText());
        //Проверка нахождения нужного элемента
        goodsPage.findSearchResultByTitle(goodsPage.firstSearchResult().getTitleText());

    }

}
