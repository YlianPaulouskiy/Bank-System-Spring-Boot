package by.aston.bank.controller;

import by.aston.bank.config.IT;
import by.aston.bank.service.dto.BankReadDto;
import by.aston.bank.service.dto.BankUserDto;
import by.aston.bank.service.dto.BankWriteDto;
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

@IT
@SqlGroup(value = {
        @Sql(scripts = "classpath:testdata/add_bank_test_data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
        @Sql(scripts = "classpath:testdata/clear_bank_test_data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD),
})
public class BankControllerIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    private static final String BANK_URL = "/api/v1/banks";

    @Test
    void Should_Return200_WhenFindAll() {
        ResponseEntity<String> responseEntity = restTemplate.getForEntity(BANK_URL, String.class);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(MediaType.APPLICATION_JSON, responseEntity.getHeaders().getContentType());
    }

    @ParameterizedTest
    @ValueSource(ints = {5,6,7})
    void Should_Return200_WhenFindByIdIsCorrect(int id) {
        ResponseEntity<BankReadDto> responseEntity =
                restTemplate.getForEntity(BANK_URL.concat("/{id}"), BankReadDto.class, id);

        assertNotNull(responseEntity.getBody());
        assertEquals(responseEntity.getBody().id(), id);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(MediaType.APPLICATION_JSON, responseEntity.getHeaders().getContentType());
    }

    @ParameterizedTest
    @ValueSource(ints = 10)
    void Should_ReturnNoContentAndBodyIsNull_WhenCantFindEntityById(int id) {
        ResponseEntity<BankReadDto> responseEntity =
                restTemplate.getForEntity(BANK_URL.concat("/{id}"), BankReadDto.class, id);

        assertNull(responseEntity.getBody());
        assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode());
        assertNull(responseEntity.getHeaders().getContentType());
    }

    @ParameterizedTest
    @MethodSource("provideBankWriteDto")
    void Should_ReturnCreated_WhenSaveIsOk(BankWriteDto writeDto) {
        HttpEntity<BankWriteDto> requestEntity = new HttpEntity<>(writeDto);

        ResponseEntity<BankUserDto> responseEntity = restTemplate.exchange(
                BANK_URL,
                HttpMethod.POST,
                requestEntity,
                BankUserDto.class);

        assertNotNull(responseEntity.getBody());
        assertEquals(responseEntity.getBody().title(), writeDto.title());
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertEquals(MediaType.APPLICATION_JSON, responseEntity.getHeaders().getContentType());
    }

    private static Stream<BankWriteDto> provideBankWriteDto() {
        return Stream.of(new BankWriteDto("New Bank"));
    }

    @ParameterizedTest
    @MethodSource("provideIdAndBankWriteDto")
    void Should_ReturnOk_WhenUpdateIsOk(Long id, BankWriteDto writeDto) throws URISyntaxException {
        HttpEntity<BankWriteDto> requestEntity = new HttpEntity<>(writeDto);
        String updateUrl = new URIBuilder().addParameter("id", id.toString()).setPath(BANK_URL).build().toString();
        ResponseEntity<BankUserDto> responseEntity = restTemplate.exchange(
                updateUrl,
                HttpMethod.PUT,
                requestEntity,
                BankUserDto.class);

        assertNotNull(responseEntity.getBody());
        assertEquals(responseEntity.getBody().title(), writeDto.title());
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(MediaType.APPLICATION_JSON, responseEntity.getHeaders().getContentType());
    }

    private static Stream<Arguments> provideIdAndBankWriteDto() {
        return Stream.of(
                Arguments.of(1L, new BankWriteDto("Not America Bank!")));
    }

    @ParameterizedTest
    @ValueSource(ints = {5,6,7})
    void Should_NotThrow_WhenDeleteEntityIsOk(Integer id) throws URISyntaxException {
        String deleteUrl = new URIBuilder().addParameter("id", id.toString()).setPath(BANK_URL).build().toString();
        assertDoesNotThrow(() -> restTemplate.delete(deleteUrl));
    }

    @ParameterizedTest
    @ValueSource(ints = {1,2,3})
    void Should_NotThrow_WhenDeletedIdNotFound(Integer id) throws URISyntaxException {
        String deleteUrl = new URIBuilder().addParameter("id", id.toString()).setPath(BANK_URL).build().toString();
        assertDoesNotThrow(() -> restTemplate.delete(deleteUrl));
    }

}
