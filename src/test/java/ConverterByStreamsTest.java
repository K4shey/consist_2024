import homework_1.Converter;
import homework_1.ConverterByStreams;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

class ConverterByStreamsTest {

    private Converter converter;

    @BeforeEach
    public void Initialization() {
        converter = new ConverterByStreams();
    }

    @Test
    public void convertStringToDictionary_StringIsNull_ThrowsException() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            converter.convertStringToDictionary(null);
        });
    }

    @Test
    public void convertStringToDictionary_ReturnsEmptyMap() {
        assertThat(converter.convertStringToDictionary(""))
                .as("Ожидается пустой словарь")
                .isEmpty();
    }

    @Test
    public void convertStringToDictionary_Returns_OneKey() {

        Map<String, List<String>> groupByFirstLetterWordsLists =
                converter.convertStringToDictionary("АрБуЗ аНаНас");

        Map<String, List<String>> expectedMap = new LinkedHashMap<>();
        expectedMap.put("а", new ArrayList<>(List.of("ананас", "арбуз")));

        assertThat(groupByFirstLetterWordsLists)
                .as("Ожидается словарь c одной записью")
                .hasSize(1)
                .isExactlyInstanceOf(LinkedHashMap.class)
                .containsExactlyEntriesOf(expectedMap);
    }

    @Test
    public void convertStringToDictionary_ReturnsMap_OneKey_SortedByDescLen() {

        Map<String, List<String>> groupByFirstLetterWordsLists =
                converter.convertStringToDictionary("АрбУз аБрикос АстрономиЯ аРа");

        Map<String, List<String>> expectedMap = new LinkedHashMap<>();
        expectedMap.put("а", new ArrayList<>(Arrays.asList("астрономия", "абрикос", "арбуз", "ара")));

        assertThat(groupByFirstLetterWordsLists)
                .as("Ожидается словарь c одной записью")
                .hasSize(1)
                .isExactlyInstanceOf(LinkedHashMap.class)
                .containsExactlyEntriesOf(expectedMap);
    }

    @Test
    public void convertStringToDictionary_ReturnsMap_TwoKeys_SortedByDescLen() {

        Map<String, List<String>> groupByFirstLetterWordsLists =
                converter.convertStringToDictionary("АрбУз бб аБрикос АстрономиЯ боБ аРа брЕзЕнт бРандМауЭр");

        Map<String, List<String>> expectedMap = new LinkedHashMap<>();
        expectedMap.put("а", new ArrayList<>(Arrays.asList("астрономия", "абрикос", "арбуз", "ара")));
        expectedMap.put("б", new ArrayList<>(Arrays.asList("брандмауэр", "брезент", "боб", "бб")));

        assertThat(groupByFirstLetterWordsLists)
                .as("Ожидается словарь c двумя записями")
                .hasSize(2)
                .isExactlyInstanceOf(LinkedHashMap.class)
                .containsExactlyEntriesOf(expectedMap);
    }

    @Test
    public void convertStringToDictionary_ReturnsMap_TwoKeys_SortedByDescLen_EqualLen() {

        Map<String, List<String>> groupByFirstLetterWordsLists =
                converter.convertStringToDictionary("астра АрбУз бб аБрикос АстрономиЯ боБ алтын аРа брЕзЕнт" +
                        " бРандМауЭр аврал");

        Map<String, List<String>> expectedMap = new LinkedHashMap<>();
        expectedMap.put("а", new ArrayList<>(Arrays.asList("астрономия", "абрикос", "аврал", "алтын",
                "арбуз", "астра", "ара")));
        expectedMap.put("б", new ArrayList<>(Arrays.asList("брандмауэр", "брезент", "боб", "бб")));

        assertThat(groupByFirstLetterWordsLists)
                .as("Ожидается словарь c двумя записями")
                .hasSize(2)
                .isExactlyInstanceOf(LinkedHashMap.class)
                .containsExactlyEntriesOf(expectedMap);
    }

    @Test
    public void convertStringToDictionary_ReturnsMap_ThreeKeys_MoreThanOneWord() {

        Map<String, List<String>> groupByFirstLetterWordsLists =
                converter.convertStringToDictionary("Волна астра АрбУз бб аБрикос АстрономиЯ боБ алтын" +
                " ВоЛаН аРа брЕзЕнт бРандМауЭр аврал ГОРОД");

        Map<String, List<String>> expectedMap = new LinkedHashMap<>();
        expectedMap.put("а", new ArrayList<>(Arrays.asList("астрономия", "абрикос", "аврал", "алтын", "арбуз", "астра", "ара")));
        expectedMap.put("б", new ArrayList<>(Arrays.asList("брандмауэр", "брезент", "боб", "бб")));
        expectedMap.put("в", new ArrayList<>(List.of("волан", "волна")));

        assertThat(groupByFirstLetterWordsLists)
                .as("Ожидается словарь c тремя записями")
                .hasSize(3)
                .isExactlyInstanceOf(LinkedHashMap.class)
                .containsExactlyEntriesOf(expectedMap);
    }

    @Test
    public void convertStringToDictionary_ReturnsMap_SortedKeysAsc() {

        Map<String, List<String>> groupByFirstLetterWordsLists =
                converter.convertStringToDictionary("Наряд Волна астра АрбУз яд ВоЛаН Якорь номер");

        Map<String, List<String>> expectedMap = new LinkedHashMap<>();
        expectedMap.put("а", new ArrayList<>(Arrays.asList("арбуз", "астра")));
        expectedMap.put("в", new ArrayList<>(List.of("волан", "волна")));
        expectedMap.put("н", new ArrayList<>(List.of("наряд", "номер")));
        expectedMap.put("я", new ArrayList<>(Arrays.asList("якорь", "яд")));

        assertThat(groupByFirstLetterWordsLists)
                .isExactlyInstanceOf(LinkedHashMap.class)
                .containsExactlyEntriesOf(expectedMap);
    }
}