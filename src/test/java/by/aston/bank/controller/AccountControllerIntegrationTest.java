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

import java.math.BigDecimal;
import java.net.URISyntaxException;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@IT
@SqlGroup({
        @Sql(scripts = "classpath:testdata/add_account_test_data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
        @Sql(scripts = "classpath:testdata/clear_account_test_data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD),
})
public class AccountControllerIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    private static final String ACCOUNT_URL = "/api/v1/accounts";

    @Test
    void Should_Return200_WhenFindAll() {
        ResponseEntity<String> responseEntity = restTemplate.getForEntity(ACCOUNT_URL, String.class);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(MediaType.APPLICATION_JSON, responseEntity.getHeaders().getContentType());
    }

    @ParameterizedTest
    @ValueSource(ints = {5, 6, 7, 8})
    void Should_Return200_WhenFindByIdIsCorrect(int id) {
        ResponseEntity<AccountReadDto> responseEntity =
                restTemplate.getForEntity(ACCOUNT_URL.concat("/{id}"), AccountReadDto.class, id);

        assertNotNull(responseEntity.getBody());
        assertEquals(responseEntity.getBody().id(), id);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(MediaType.APPLICATION_JSON, responseEntity.getHeaders().getContentType());
    }

    @ParameterizedTest
    @ValueSource(ints = 10)
    void Should_ReturnNoContentAndBodyIsNull_WhenCantFindEntityById(int id) {
        ResponseEntity<AccountReadDto> responseEntity =
                restTemplate.getForEntity(ACCOUNT_URL.concat("/{id}"), AccountReadDto.class, id);

        assertNull(responseEntity.getBody());
        assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode());
        assertNull(responseEntity.getHeaders().getContentType());
    }

    @ParameterizedTest
    @MethodSource("provideAccountWriteDto")
    void Should_ReturnCreated_WhenSaveIsOk(AccountWriteDto writeDto) {
        HttpEntity<AccountWriteDto> requestEntity = new HttpEntity<>(writeDto);

        ResponseEntity<AccountReadDto> responseEntity = restTemplate.exchange(
                ACCOUNT_URL,
                HttpMethod.POST,
                requestEntity,
                AccountReadDto.class);

        assertNotNull(responseEntity.getBody());
        assertEquals(responseEntity.getBody().number(), writeDto.number());
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertEquals(MediaType.APPLICATION_JSON, responseEntity.getHeaders().getContentType());
    }

    private static Stream<AccountWriteDto> provideAccountWriteDto() {
        return Stream.of(
                new AccountWriteDto("123123123",
                        new BigDecimal("999.99"),
                        1L,
                        1L));
    }

    @ParameterizedTest
    @MethodSource("provideIdAndAccountWriteDto")
    void Should_ReturnOk_WhenUpdateIsOk(Long id, AccountWriteDto writeDto) throws URISyntaxException {
        HttpEntity<AccountWriteDto> requestEntity = new HttpEntity<>(writeDto);
        String updateUrl = new URIBuilder().addParameter("id", id.toString()).setPath(ACCOUNT_URL).build().toString();
        ResponseEntity<AccountReadDto> responseEntity = restTemplate.exchange(
                updateUrl,
                HttpMethod.PUT,
                requestEntity,
                AccountReadDto.class);

        assertNotNull(responseEntity.getBody());
        assertEquals(responseEntity.getBody().number(), writeDto.number());
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(MediaType.APPLICATION_JSON, responseEntity.getHeaders().getContentType());
    }

    private static Stream<Arguments> provideIdAndAccountWriteDto() {
        return Stream.of(
                Arguments.of(1L,
                        new AccountWriteDto("999999999",
                                new BigDecimal("999.99"),
                                2L,
                                2L)));
    }

    @ParameterizedTest
    @ValueSource(ints = {5, 6, 7, 8})
    void Should_NoThrow_WhenDeleteEntityIsOk(Integer id) throws URISyntaxException {
        String deleteUrl = new URIBuilder().addParameter("id", id.toString()).setPath(ACCOUNT_URL).build().toString();
        assertDoesNotThrow(() -> restTemplate.delete(deleteUrl));
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3})
    void Should_NoThrow_WhenDeletedIdNotFound(Integer id) throws URISyntaxException {
        String deleteUrl = new URIBuilder().addParameter("id", id.toString()).setPath(ACCOUNT_URL).build().toString();
        assertDoesNotThrow(() -> restTemplate.delete(deleteUrl));
    }


}
