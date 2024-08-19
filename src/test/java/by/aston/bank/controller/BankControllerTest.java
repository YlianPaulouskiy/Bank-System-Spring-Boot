package by.aston.bank.controller;

import by.aston.bank.service.BankService;
import by.aston.bank.util.CommonTestData;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = BankController.class)
public class BankControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private BankService bankService;

    private static final String BANK_URL = "/api/v1/banks";

    @Test
    void Should_Return200_WhenFindById() throws Exception {
        var response = CommonTestData.createBankReadDto();
        when(bankService.findById(anyLong())).thenReturn(Optional.of(response));

        MvcResult mvcResult = mockMvc.perform(get(BANK_URL.concat("/1"))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String expectedJson = objectMapper.writeValueAsString(response);
        String resultJson = mvcResult.getResponse().getContentAsString();
        assertEquals(expectedJson, resultJson);
    }

    @Test
    void Should_Return204_WhenFindIdNotExists() throws Exception {
        mockMvc.perform(get(BANK_URL.concat("/1"))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    void Should_Return201AndExpectedJson_WhenFindAll() throws Exception {
        var response = List.of(CommonTestData.createBankReadDto());
        when(bankService.findAll()).thenReturn(response);

        MvcResult mvcResult = mockMvc.perform(get(BANK_URL)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String expectedJson = objectMapper.writeValueAsString(response);
        String resultJson = mvcResult.getResponse().getContentAsString();
        assertEquals(expectedJson, resultJson);
    }

    @Test
    void Should_Return201_WhenCreateNewEntity() throws Exception {
        var request = CommonTestData.createBankWriteDto();
        var response = CommonTestData.createBankUserDto();
        when(bankService.create(any())).thenReturn(Optional.of(response));

        MvcResult mvcResult = mockMvc.perform(post(BANK_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andReturn();

        String expectedJson = objectMapper.writeValueAsString(response);
        String resultJson = mvcResult.getResponse().getContentAsString();
        assertEquals(expectedJson, resultJson);
    }

    @Test
    void Should_Return200_WhenUpdateEntity() throws Exception {
        var request = CommonTestData.createBankWriteDto();
        var response = CommonTestData.createBankReadDto();
        when(bankService.update(anyLong(), any())).thenReturn(Optional.of(response));

        MvcResult mvcResult = mockMvc.perform(put(BANK_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("id", "1")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andReturn();

        String expectedJson = objectMapper.writeValueAsString(response);
        String resultJson = mvcResult.getResponse().getContentAsString();
        assertEquals(expectedJson, resultJson);
    }

    @Test
    void Should_Return200_WhenDeleteById() throws Exception {
        when(bankService.delete(anyLong())).thenReturn(true);
        mockMvc.perform(delete(BANK_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .param("id", "1"))
                .andExpect(status().isOk());
    }
    @Test
    void Should_Return404_WhenIdIncorrect() throws Exception {
        when(bankService.delete(anyLong())).thenReturn(false);
        mockMvc.perform(delete(BANK_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .param("id", "1"))
                .andExpect(status().isNotFound());
    }
}
