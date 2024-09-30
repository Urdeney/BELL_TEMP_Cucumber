package ru.bellintegrator;

import helpers.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import static helpers.CustomWaits.implicitlyWait;
import static helpers.Properties.testsProperties;

public class BaseTest {

    protected WebDriver chromeDriver;

    @BeforeEach
    public void before() {
        //System.setProperty("webdriver.chrome.driver",System.getenv("CHROME_DRIVER"));
        System.setProperty("webdriver.chrome.driver","C:\\Users\\Андрей\\.chrome-driver\\chromedriver.exe"); // TODO убрать на релизе
        ChromeOptions options = new ChromeOptions();
        options.setPageLoadStrategy(PageLoadStrategy.NORMAL);
        options.addArguments("--disable-popup-blocking");
        options.addArguments("--accept-insecure-certs");
        chromeDriver = new ChromeDriver(options);
        chromeDriver.manage().window().maximize();
        implicitlyWait(chromeDriver,testsProperties.defaultTimeout());
    }

    @AfterEach
    public void after(){
        chromeDriver.quit();
        //Проверка на ошибки во время выполнения теста
        Assertions.failEndMethod();
    }
}
