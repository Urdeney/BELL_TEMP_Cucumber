package helpers;

import org.junit.jupiter.params.provider.Arguments;

import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class DataProvider {

    /**
     * Тестовый набор для Яндекс Маркета
     * @author Нуреев Андрей
     */
    public static Stream<Arguments> providerCheckingYMNotebooksV1() {
        return Stream.of(
                Arguments.of("Электроника", "Ноутбуки",
                        Map.of("Цена, ₽",
                                List.of("10000", "30000"), "Производитель", List.of("HP", "Lenovo"))));
    }

    /**
     * Тестовый набор для Яндекс Маркета
     * @author Нуреев Андрей
     */
    public static Stream<Arguments> providerCheckingYMNotebooksV2() {
        return Stream.of(
                Arguments.of("Электроника", "Ноутбуки",
                        Map.of("Цена, ₽",
                                List.of("10000", "30000"), "Производитель", List.of("Apple"))));
    }

}
