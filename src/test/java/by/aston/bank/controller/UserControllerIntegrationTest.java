package by.aston.bank.controller;

import by.aston.bank.config.IT;
import by.aston.bank.service.dto.*;
import com.github.dockerjava.zerodep.shaded.org.apache.hc.core5.net.URIBuilder;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;

import java.net.URISyntaxException;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@IT
@SqlGroup({
        @Sql(scripts = "classpath:testdata/add_user_test_data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
        @Sql(scripts = "classpath:testdata/clear_user_test_data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
})
public class UserControllerIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    private static final String USERS_URL = "/api/v1/users";

    @Test
    void Should_Return200_WhenFindAll() {
        ResponseEntity<String> responseEntity = restTemplate.getForEntity(USERS_URL, String.class);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(MediaType.APPLICATION_JSON, responseEntity.getHeaders().getContentType());
    }

    @ParameterizedTest
    @ValueSource(ints = {5, 5})
    void Should_Return200_WhenFindByIdIsCorrect(int id) {
        ResponseEntity<UserDto> responseEntity =
                restTemplate.getForEntity(USERS_URL.concat("/{id}"), UserDto.class, id);

        assertNotNull(responseEntity.getBody());
        assertEquals(responseEntity.getBody().getId(), id);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(MediaType.APPLICATION_JSON, responseEntity.getHeaders().getContentType());
    }

    @ParameterizedTest
    @ValueSource(ints = 10)
    void Should_ReturnNoContentAndBodyIsNull_WhenCantFindEntityById(int id) {
        ResponseEntity<UserDto> responseEntity =
                restTemplate.getForEntity(USERS_URL.concat("/{id}"), UserDto.class, id);

        assertNull(responseEntity.getBody());
        assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode());
        assertNull(responseEntity.getHeaders().getContentType());
    }

    @ParameterizedTest
    @MethodSource("provideUserWriteDto")
    void Should_ReturnCreated_WhenSaveIsOk(UserWriteDto writeDto) {
        HttpEntity<UserWriteDto> requestEntity = new HttpEntity<>(writeDto);

        ResponseEntity<UserDto> responseEntity = restTemplate.exchange(
                USERS_URL,
                HttpMethod.POST,
                requestEntity,
                UserDto.class);

        assertNotNull(responseEntity.getBody());
        assertEquals(responseEntity.getBody().getName(), writeDto.name());
        assertEquals(responseEntity.getBody().getLastName(), writeDto.lastName());
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertEquals(MediaType.APPLICATION_JSON, responseEntity.getHeaders().getContentType());
    }

    private static Stream<UserWriteDto> provideUserWriteDto() {
        return Stream.of(
                new UserWriteDto("Julia",
                        "Podolsakaya",
                        null,
                        null));
    }

    @ParameterizedTest
    @MethodSource("provideIdAndUserWriteDto")
    void Should_ReturnOk_WhenUpdateIsOk(Long id, UserWriteDto writeDto) throws URISyntaxException {
        HttpEntity<UserWriteDto> requestEntity = new HttpEntity<>(writeDto);
        String updateUrl = new URIBuilder().addParameter("id", id.toString()).setPath(USERS_URL).build().toString();
        ResponseEntity<UserDto> responseEntity = restTemplate.exchange(
                updateUrl,
                HttpMethod.PUT,
                requestEntity,
                UserDto.class);

        assertNotNull(responseEntity.getBody());
        assertEquals(responseEntity.getBody().getName(), writeDto.name());
        assertEquals(responseEntity.getBody().getLastName(), writeDto.lastName());
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(MediaType.APPLICATION_JSON, responseEntity.getHeaders().getContentType());
    }

    private static Stream<Arguments> provideIdAndUserWriteDto() {
        return Stream.of(
                Arguments.of(5L,
                        new UserWriteDto("Name",
                                "LastName",
                                "2024-07-24",
                                null)));
    }

    @ParameterizedTest
    @ValueSource(ints = {5,6})
    void Should_NotThrow_WhenDeleteEntityIsOk(Integer id) throws URISyntaxException {
        String deleteUrl = new URIBuilder().addParameter("id", id.toString()).setPath(USERS_URL).build().toString();
        assertDoesNotThrow(() -> restTemplate.delete(deleteUrl));
    }

    @ParameterizedTest
    @ValueSource(ints = {1,2})
    void Should_NotThrow_WhenDeletedIdNotFound(Integer id) throws URISyntaxException {
        String deleteUrl = new URIBuilder().addParameter("id", id.toString()).setPath(USERS_URL).build().toString();
        assertDoesNotThrow(() -> restTemplate.delete(deleteUrl));
    }

}
