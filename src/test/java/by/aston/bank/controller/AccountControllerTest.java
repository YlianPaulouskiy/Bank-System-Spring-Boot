package by.aston.bank.controller;

import by.aston.bank.service.AccountService;
import by.aston.bank.util.CommonTestData;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = AccountController.class)
public class AccountControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AccountService accountService;

    private static final String ACCOUNT_URL = "/api/v1/accounts";

    @Test
    void Should_Return200_WhenFindById() throws Exception {
        var response = CommonTestData.createAccountReadDto();
        when(accountService.findById(anyLong())).thenReturn(Optional.of(response));

        MvcResult mvcResult = mockMvc.perform(get(ACCOUNT_URL.concat("/1"))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String expectedJson = objectMapper.writeValueAsString(response);
        String resultJson = mvcResult.getResponse().getContentAsString();
        assertEquals(expectedJson, resultJson);
    }

    @Test
    void Should_Return204_WhenFindIdNotExists() throws Exception {
        mockMvc.perform(get(ACCOUNT_URL.concat("/1"))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    void Should_Return201AndExpectedJson_WhenFindAll() throws Exception {
        var response = List.of(CommonTestData.createAccountReadDto());
        when(accountService.findAll()).thenReturn(response);

        MvcResult mvcResult = mockMvc.perform(get(ACCOUNT_URL)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String expectedJson = objectMapper.writeValueAsString(response);
        String resultJson = mvcResult.getResponse().getContentAsString();
        assertEquals(expectedJson, resultJson);
    }

    @Test
    void Should_Return201_WhenCreateNewEntity() throws Exception {
        var request = CommonTestData.createAccountWriteDto();
        var response = CommonTestData.createAccountReadDto();
        when(accountService.create(any())).thenReturn(Optional.of(response));

        MvcResult mvcResult = mockMvc.perform(post(ACCOUNT_URL)
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
        var request = CommonTestData.createAccountWriteDto();
        var response = CommonTestData.createAccountReadDto();
        when(accountService.update(anyLong(), any())).thenReturn(Optional.of(response));

        MvcResult mvcResult = mockMvc.perform(put(ACCOUNT_URL)
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
        when(accountService.delete(anyLong())).thenReturn(true);
        mockMvc.perform(delete(ACCOUNT_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("id", "1"))
                .andExpect(status().isOk());
    }
    @Test
    void Should_Return404_WhenIdIncorrect() throws Exception {
        when(accountService.delete(anyLong())).thenReturn(false);
        mockMvc.perform(delete(ACCOUNT_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("id", "1"))
                .andExpect(status().isNotFound());
    }


}
