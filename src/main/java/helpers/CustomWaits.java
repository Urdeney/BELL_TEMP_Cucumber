package helpers;

import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.FluentWait;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static helpers.Properties.testsProperties;
import static org.openqa.selenium.support.ui.ExpectedConditions.*;

public class CustomWaits {

    private static int implicitlyWait = testsProperties.defaultTimeout();
    private static int pageLoadTimeout = testsProperties.defaultTimeout();
    private static int setScriptTimeout = testsProperties.defaultTimeout();

    public static void implicitlyWait(WebDriver driver, int defaultTimeout) {
        driver.manage().timeouts().implicitlyWait(defaultTimeout, TimeUnit.SECONDS);
        implicitlyWait = defaultTimeout;
    }

    private static void sleep(int sec) {
        try {
            Thread.sleep(sec * 1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private static void sleep(double sec) {
        try {
            Thread.sleep((int) (sec * 1000));
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * Явное ожидание того, что элемент пропадет в заданный промежуток времени
     * @param driver драйвер
     * @param elementXpath Xpath-локатор проверяемого элемента
     * @param timeWaitLocated время на нахождение элемента
     * @param timeWaitInvisible время на пропажу элемента
     * @author Нуреев Андрей
     */
    public static void waitInvisibleIfLocated(WebDriver driver, String elementXpath, int timeWaitLocated, int timeWaitInvisible) {
        driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);

        for (int i = 0; i < timeWaitLocated; ++i) {
            if (driver.findElements(By.xpath(elementXpath)).size() != 0) {
                for (int j = 0; ; ++j) {
                    if (driver.findElements(By.xpath(elementXpath)).size() == 0)
                        break;
                    if (j + 1 == timeWaitInvisible)
                        Assertions.fail("Элемент " + elementXpath + " не исчез за " + timeWaitInvisible + "секунд");
                    sleep(1);
                }
            }
            sleep(1);
        }

        implicitlyWait(driver, implicitlyWait);
    }

    /**
     * Проверка на то, присутсвует ли элемент с Xpath-локатором в области заданного элемента
     * @param driver драйвер
     * @param parentElement родительский WebElement в котором будет проходить поиск
     * @param childXpath Xpath-локатор дочернего элемента
     * @param timeWaitDisplayed время на нахождение элемента
     * @return boolean - элемент представлен на странице
     * @auhor Нуреев Андрей
     */
    public static boolean isWebElementDisplayed(WebDriver driver, WebElement parentElement, String childXpath, int timeWaitDisplayed) {
        driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
        boolean isDisplayed;
        try {
            getFluentWait(driver, timeWaitDisplayed, 200).until(presenceOfNestedElementLocatedBy(parentElement, By.xpath(childXpath)));
            isDisplayed = true;
        } catch (TimeoutException e) {
            isDisplayed = false;
        }
        implicitlyWait(driver, implicitlyWait);
        return isDisplayed;
    }

    /**
     * Проверка на то, присутсвует ли элемент с Xpath-локатором на всей странице
     * @param driver драйвер
     * @param Xpath Xpath-локатор элемента
     * @param timeWaitDisplayed время на нахождение элемента
     * @return boolean - элемент представлен на странице
     * @author Нуреев Андрей
     */
    public static boolean isWebElementDisplayed(WebDriver driver, String Xpath, int timeWaitDisplayed) {
        driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
        boolean isDisplayed;
        try {
            getFluentWait(driver, timeWaitDisplayed, 200).until(presenceOfElementLocated(By.xpath(Xpath)));
            isDisplayed = true;
        } catch (TimeoutException e) {
            isDisplayed = false;
        }
        implicitlyWait(driver, implicitlyWait);
        return isDisplayed;
    }

    /**
     * Конфигурирование FluentWait c заданными параметрами
     * @param driver драйвер
     * @param timeWaite время проверки FluentWait
     * @param frequency частота опроса FluentWait
     * @return плоученный FluentWait
     * @author Нуреев Андрей
     */
    private static FluentWait<WebDriver> getFluentWait(WebDriver driver, int timeWaite, int frequency) {
        return new FluentWait<>(driver)
                .withTimeout(Duration.ofSeconds(timeWaite))
                .pollingEvery(Duration.ofMillis(frequency))
                .ignoreAll(List.of(
                        NoSuchElementException.class,
                        WebDriverException.class,
                        StaleElementReferenceException.class,
                        ElementClickInterceptedException.class,
                        TimeoutException.class)
                );
    }
}
