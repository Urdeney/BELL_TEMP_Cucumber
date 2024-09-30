package helpers;

import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import io.qameta.allure.model.Status;
import io.qameta.allure.model.StepResult;
import java.util.List;
import java.util.stream.Collectors;

public class Assertions {

    @Step("Проверяем что нет ошибки: {message}")
    public static void assertTrue(boolean condition, String message) {
        org.junit.jupiter.api.Assertions.assertTrue(condition, message);
    }

    @Step("Проверяем что нет ошибки: {message}")
    public static void fail(String message) {
        org.junit.jupiter.api.Assertions.fail(message);
    }

    /**
     * Мягкая проверка - Маркирование Step если условие не выполняется
     * Работает с failEndMethod
     * @author Нуреев Андрей
     * @param isCorrect выполняется ли условие из вызывающей функции
     * @param message сообщение при возникновении ошибки
     */
    @Step("Мягкая проверка: {message}")
    public static void assertTrueSoft(boolean isCorrect, String message) {
        if (!isCorrect) {
            Allure.getLifecycle().updateStep(step -> {
                step.setStatus(Status.FAILED);
            });
            Allure.getLifecycle().stopStep();
        }
    }

    /**
     * Мягкая проверка - Проверка всех Step на их правильность
     * Работает с asssertTrueSoft
     * @author Нуреев Андрей
     */
    @Step("Проверяем, были ли шагах теста ошибки")
    public static void failEndMethod() {
        Allure.getLifecycle().updateTestCase(testResult -> {
            List<StepResult> failedSteps = testResult.getSteps().stream().filter(stepResult -> stepResult.getStatus() == Status.FAILED).collect(Collectors.toList());
            if (failedSteps.size() != 0) {
                org.junit.jupiter.api.Assertions.fail("В шагах теста присутвуют ошибки");
            }
        });
    }
}
