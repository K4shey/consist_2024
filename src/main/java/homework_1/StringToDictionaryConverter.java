package homework_1;

import java.util.*;
import java.util.stream.Collectors;

/**
 *  Содержит статический метод для конвертации строки из слов в словарь.
 */
public class StringToDictionaryConverter {

    /**
     * Предназначен для конвертации строки из слов в словарь (Map)
     *
     * @param stringToProcess Строка к обработке, содержащая слова, разделенные пробелами
     * @return Словарь (Map) ключом в котором является буква, на которую начинаются слова из списка. В значении словаря содержится
     * список слов, начинающихся на букву из ключа.
     *
     */
    public static Map<String, List<String>> convertStringToDictionary(String stringToProcess) {
        if (stringToProcess.isEmpty()) {
            return new LinkedHashMap<>();
        }
        return Arrays.stream(stringToProcess.split(" "))
                .map(String::toLowerCase)
                .sorted(Comparator.comparing(String::length).reversed().thenComparing(String::compareTo))
                .collect(Collectors.groupingBy(word -> String.valueOf(word.charAt(0))))
                .entrySet()
                .stream()
                .filter(entry -> entry.getValue().size() > 1)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }
}
