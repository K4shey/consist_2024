import homework_1.StringToDictionaryConverter;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class StringToDictionaryConverterTest {

    @Test
    public void convertStringToDictionary_ReturnEmptyMap() {
        String stringToProcess = "";
        assertEquals(new LinkedHashMap<String, ArrayList<String>>(),
                StringToDictionaryConverter.convertStringToDictionary(stringToProcess));
    }

    @Test
    public void convertStringToDictionary_Return_OneKey() {
        String stringToProcess = "АрБуЗ аНаНас";
        Map<String, List<String>> expectedMap = new LinkedHashMap<>();
        List<String> expectedList = new ArrayList<>(List.of("ананас", "арбуз"));
        expectedMap.put("а", expectedList);
        assertEquals(expectedMap, StringToDictionaryConverter.convertStringToDictionary(stringToProcess));
    }

    @Test
    public void convertStringToDictionary_ReturnMap_OneKey_SortedByDescLen() {
        String stringToProcess = "АрбУз аБрикос АстрономиЯ аРа";
        List<String> expectedList = new ArrayList<>(Arrays.asList("астрономия", "абрикос", "арбуз", "ара"));
        Map<String, List<String>> expectedMap = new LinkedHashMap<>();
        expectedMap.put("а", expectedList);
        assertEquals(expectedMap, StringToDictionaryConverter.convertStringToDictionary(stringToProcess));
    }

    @Test
    public void convertStringToDictionary_ReturnMap_TwoKeys_SortedByDescLen() {
        Map<String, List<String>> expectedMap = new LinkedHashMap<>();
        String stringToProcess = "АрбУз бб аБрикос АстрономиЯ боБ аРа брЕзЕнт бРандМауЭр";
        List<String> expectedList = new ArrayList<>(Arrays.asList("астрономия", "абрикос", "арбуз", "ара"));
        expectedMap.put("а", expectedList);
        expectedList = new ArrayList<>(Arrays.asList("брандмауэр", "брезент", "боб", "бб"));
        expectedMap.put("б", expectedList);
        assertEquals(expectedMap, StringToDictionaryConverter.convertStringToDictionary(stringToProcess));
    }

    @Test
    public void convertStringToDictionary_ReturnMap_TwoKeys_SortedByDescLen_EqualLen() {
        Map<String, List<String>> expectedMap = new LinkedHashMap<>();
        String stringToProcess = "астра АрбУз бб аБрикос АстрономиЯ боБ алтын аРа брЕзЕнт бРандМауЭр аврал";
        List<String> expectedList = new ArrayList<>(Arrays.asList("астрономия", "абрикос", "аврал", "алтын",
                "арбуз", "астра", "ара"));
        expectedMap.put("а", expectedList);
        expectedList = new ArrayList<>(Arrays.asList("брандмауэр", "брезент", "боб", "бб"));
        expectedMap.put("б", expectedList);
        assertEquals(expectedMap, StringToDictionaryConverter.convertStringToDictionary(stringToProcess));
    }

    @Test
    public void convertStringToDictionary_ReturnMap_FourKeys_MoreThanOneWord() {
        Map<String, List<String>> expectedMap = new LinkedHashMap<>();
        String stringToProcess = "Волна астра АрбУз бб аБрикос АстрономиЯ боБ алтын ВоЛаН аРа брЕзЕнт бРандМауЭр аврал ГОРОД";
        List<String> expectedList = new ArrayList<>(Arrays.asList("астрономия", "абрикос", "аврал", "алтын",
                "арбуз", "астра", "ара"));
        expectedMap.put("а", expectedList);
        expectedList = new ArrayList<>(Arrays.asList("брандмауэр", "брезент", "боб", "бб"));
        expectedMap.put("б", expectedList);
        expectedList = new ArrayList<>(List.of("волан", "волна"));
        expectedMap.put("в", expectedList);
        assertEquals(expectedMap, StringToDictionaryConverter.convertStringToDictionary(stringToProcess));
    }

    @Test
    public void convertStringToDictionary_ReturnMap_SortedKeysAsc() {
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
        assertEquals(expectedMap, StringToDictionaryConverter.convertStringToDictionary(stringToProcess));
    }
}
