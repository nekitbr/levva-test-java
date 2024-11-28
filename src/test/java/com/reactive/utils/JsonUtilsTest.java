package com.reactive.utils;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.text.MessageFormat;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

@ExtendWith(MockitoExtension.class)
class JsonUtilsTest {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    private static class Person {
        private String name;
        private int age;
        private Person partner;
        private List<Person> children;
        private boolean deleted;
        private Boolean blocked;
        private Gender gender;
        private FakeEnum fake;
    }

    private enum Gender {
        M, F
    }

    @Getter
    @AllArgsConstructor
    private enum FakeEnum {
        FAKE("yes"),
        NOT_FAKE("no");

        @JsonValue
        private final String value;
    }

    private Person person;
    private String personJson;

    @BeforeEach
    void init() {
        var child1 = Person.builder()
                .name("child1")
                .age(8)
                .fake(FakeEnum.NOT_FAKE)
                .gender(Gender.M)
                .build();
        var child2 = Person.builder()
                .name("child2")
                .age(14)
                .fake(FakeEnum.FAKE)
                .gender(Gender.F)
                .build();
        var partner = Person.builder()
                .name("partner1")
                .age(24)
                .build();

        person = Person.builder()
                .name("test person")
                .age(25)
                .children(List.of(child1, child2))
                .partner(partner)
                .blocked(null) // null
                .deleted(false)
                .build();

        personJson = "{" +
                "\"name\":\"test person\"," +
                "\"age\":25," +
                "\"partner\":{" +
                "\"name\":\"partner1\"," +
                "\"age\":24," +
                "\"deleted\":false" +
                "}," +
                "\"children\":[" +
                "{" +
                "\"name\":\"child1\"," +
                "\"age\":8," +
                "\"deleted\":false," +
                "\"gender\":\"M\"," +
                "\"fake\":\"no\"" +
                "}," +
                "{" +
                "\"name\":\"child2\"," +
                "\"age\":14," +
                "\"deleted\":false," +
                "\"gender\":\"F\"," +
                "\"fake\":\"yes\"" +
                "}" +
                "]," +
                "\"deleted\":false" +
                "}";
    }

    @Test
    @DisplayName("given a DTO with all types from JSON as value, then it should parse into a valid JSON, " +
            "and when an ENUM is present, then it should get the field annotated with @JsonValue or fallback to printing the field as a value")
    void toJson() {
        assertThat(JsonUtils.toJson(person))
                .isEqualTo(personJson);
    }

    @Test
    @DisplayName("given a valid JSON, when passing the correct class, then it should create a new instance of that class with " +
            "identical information as the JSON")
    void testFromJson() {
        assertThat(JsonUtils.fromJson(personJson, Person.class))
                .isEqualTo(person);
    }

    @Test
    @DisplayName("given an invalid JSON, when trying to parse from JSON to a new DTO instance, " +
            "then it should fail with the expected error message")
    void testFromJson_RuntimeException() {
        var invalidJson = "{\"name\":\"John\", \"age\": \"invalid_number\"}";
        var expectedErrorMessage = "Cannot deserialize value of type `int` from String \"invalid_number\": not a valid `int` value\n at " +
                "[Source: REDACTED (`StreamReadFeature.INCLUDE_SOURCE_IN_LOCATION` disabled); line: 1, column: 24] " +
                "(through reference chain: com.reactive.commons.utils.JsonUtilsTest$Person[\"age\"])";

        assertThatExceptionOfType(RuntimeException.class)
                .isThrownBy(() -> JsonUtils.fromJson(invalidJson, Person.class))
                .withMessage(MessageFormat.format("Fail to deserialize the request. {0}", expectedErrorMessage));
    }

}
