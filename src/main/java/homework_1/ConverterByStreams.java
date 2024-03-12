package homework_1;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Содержит метод для конвертации строки из слов в словарь.
 */
public class ConverterByStreams implements Converter {

    /**
     * Предназначен для конвертации строки из слов в словарь (Map)
     *
     * @param stringToProcess Строка к обработке, содержащая слова, разделенные пробелами
     * @return Словарь (Map) ключом является буква, на которую начинаются слова из списка. В значении словаря содержится
     * список слов, начинающихся на букву из ключа.
     */
    public Map<String, List<String>> convertStringToDictionary(String stringToProcess) {

        if (stringToProcess == null) {
            throw new IllegalArgumentException("Must be not null!");
        }

        if (stringToProcess.isEmpty() || stringToProcess.isBlank()) {
            return new LinkedHashMap<>();
        }

        return Arrays.stream(stringToProcess.split(" "))
                .map(String::toLowerCase)
                .sorted(Comparator.comparing(String::length).reversed().thenComparing(String::compareTo))
                .collect(Collectors.groupingBy(word -> word.substring(0, 1)))
                .entrySet()
                .stream()
                .filter(entry -> entry.getValue().size() > 1)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (exist, newVal) -> newVal, LinkedHashMap::new));
    }
}
