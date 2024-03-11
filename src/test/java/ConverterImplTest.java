import homework_1.AbstractConverter;
import homework_1.ConverterImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

class ConverterImplTest {

    private static AbstractConverter converter;

    @BeforeAll
    public static void Initialization() {
        converter = new ConverterImpl();
    }

    @Test
    public void convertStringToDictionary_StringIsNull_ThrowsException() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            converter.convertStringToDictionary(null);
        });
    }

    @Test
    public void convertStringToDictionary_ReturnsEmptyMap() {
        assertThat(converter.convertStringToDictionary("")).isEmpty();
    }

    @Test
    public void convertStringToDictionary_Returns_OneKey() {
        String stringToProcess = "АрБуЗ аНаНас";
        Map<String, List<String>> expectedMap = new LinkedHashMap<>();
        List<String> expectedList = new ArrayList<>(List.of("ананас", "арбуз"));
        expectedMap.put("а", expectedList);
        assertThat(converter.convertStringToDictionary(stringToProcess)).isEqualTo(expectedMap);
    }

    @Test
    public void convertStringToDictionary_ReturnsMap_OneKey_SortedByDescLen() {
        String stringToProcess = "АрбУз аБрикос АстрономиЯ аРа";
        List<String> expectedList = new ArrayList<>(Arrays.asList("астрономия", "абрикос", "арбуз", "ара"));
        Map<String, List<String>> expectedMap = new LinkedHashMap<>();
        expectedMap.put("а", expectedList);
        assertThat(converter.convertStringToDictionary(stringToProcess)).isEqualTo(expectedMap);
    }

    @Test
    public void convertStringToDictionary_ReturnsMap_TwoKeys_SortedByDescLen() {
        Map<String, List<String>> expectedMap = new LinkedHashMap<>();
        String stringToProcess = "АрбУз бб аБрикос АстрономиЯ боБ аРа брЕзЕнт бРандМауЭр";
        List<String> expectedList = new ArrayList<>(Arrays.asList("астрономия", "абрикос", "арбуз", "ара"));
        expectedMap.put("а", expectedList);
        expectedList = new ArrayList<>(Arrays.asList("брандмауэр", "брезент", "боб", "бб"));
        expectedMap.put("б", expectedList);
        assertThat(converter.convertStringToDictionary(stringToProcess)).isEqualTo(expectedMap);
    }

    @Test
    public void convertStringToDictionary_ReturnsMap_TwoKeys_SortedByDescLen_EqualLen() {
        Map<String, List<String>> expectedMap = new LinkedHashMap<>();
        String stringToProcess = "астра АрбУз бб аБрикос АстрономиЯ боБ алтын аРа брЕзЕнт бРандМауЭр аврал";
        List<String> expectedList = new ArrayList<>(Arrays.asList("астрономия", "абрикос", "аврал", "алтын",
                "арбуз", "астра", "ара"));
        expectedMap.put("а", expectedList);
        expectedList = new ArrayList<>(Arrays.asList("брандмауэр", "брезент", "боб", "бб"));
        expectedMap.put("б", expectedList);
        assertThat(converter.convertStringToDictionary(stringToProcess)).isEqualTo(expectedMap);
    }

    @Test
    public void convertStringToDictionary_ReturnsMap_FourKeys_MoreThanOneWord() {
        Map<String, List<String>> expectedMap = new LinkedHashMap<>();
        String stringToProcess = "Волна астра АрбУз бб аБрикос АстрономиЯ боБ алтын ВоЛаН аРа брЕзЕнт бРандМауЭр аврал ГОРОД";
        List<String> expectedList = new ArrayList<>(Arrays.asList("астрономия", "абрикос", "аврал", "алтын",
                "арбуз", "астра", "ара"));
        expectedMap.put("а", expectedList);
        expectedList = new ArrayList<>(Arrays.asList("брандмауэр", "брезент", "боб", "бб"));
        expectedMap.put("б", expectedList);
        expectedList = new ArrayList<>(List.of("волан", "волна"));
        expectedMap.put("в", expectedList);
        assertThat(converter.convertStringToDictionary(stringToProcess)).isEqualTo(expectedMap);
    }

    @Test
    public void convertStringToDictionary_ReturnsMap_SortedKeysAsc() {
        Map<String, List<String>> expectedMap = new LinkedHashMap<>();
        String stringToProcess = "Наряд Волна астра АрбУз яд ВоЛаН Якорь номер";
        List<String> expectedList = new ArrayList<>(Arrays.asList("арбуз", "астра"));
        expectedMap.put("а", expectedList);
        expectedList = new ArrayList<>(List.of("волан", "волна"));
        expectedMap.put("в", expectedList);
        expectedList = new ArrayList<>(List.of("наряд", "номер"));
        expectedMap.put("н", expectedList);
        expectedList = new ArrayList<>(Arrays.asList("якорь", "яд"));
        expectedMap.put("я", expectedList);
        assertThat(converter.convertStringToDictionary(stringToProcess)).isEqualTo(expectedMap);
    }
}