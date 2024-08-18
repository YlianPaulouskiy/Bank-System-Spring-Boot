package by.aston.bank.controller;

import by.aston.bank.service.UserService;
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

@WebMvcTest(controllers = UserController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    private static final String USERS_URL = "/api/v1/users";

    @Test
    void Should_Return200_WhenFindById() throws Exception {
        var response = CommonTestData.createUserDto();
        when(userService.findById(anyLong())).thenReturn(Optional.of(response));

        MvcResult mvcResult = mockMvc.perform(get(USERS_URL.concat("/1"))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String expectedJson = objectMapper.writeValueAsString(response);
        String resultJson = mvcResult.getResponse().getContentAsString();
        assertEquals(expectedJson, resultJson);
    }

    @Test
    void Should_Return204_WhenFindIdNotExists() throws Exception {
        mockMvc.perform(get(USERS_URL.concat("/1"))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    void Should_Return201AndExpectedJson_WhenFindAll() throws Exception {
        var response = List.of(CommonTestData.createUserDto());
        when(userService.findAll()).thenReturn(response);

        MvcResult mvcResult = mockMvc.perform(get(USERS_URL)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String expectedJson = objectMapper.writeValueAsString(response);
        String resultJson = mvcResult.getResponse().getContentAsString();
        assertEquals(expectedJson, resultJson);
    }

    @Test
    void Should_Return201_WhenCreateNewEntity() throws Exception {
        var request = CommonTestData.createClientWriteDto();
        var response = CommonTestData.createUserDto();
        when(userService.create(any())).thenReturn(Optional.of(response));

        MvcResult mvcResult = mockMvc.perform(post(USERS_URL)
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
        var request = CommonTestData.createClientWriteDto();
        var response = CommonTestData.createUserDto();
        when(userService.update(anyLong(), any())).thenReturn(Optional.of(response));

        MvcResult mvcResult = mockMvc.perform(put(USERS_URL)
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
        when(userService.delete(anyLong())).thenReturn(true);
        mockMvc.perform(delete(USERS_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("id", "1"))
                .andExpect(status().isOk());
    }
    @Test
    void Should_Return404_WhenIdIncorrect() throws Exception {
        when(userService.delete(anyLong())).thenReturn(false);
        mockMvc.perform(delete(USERS_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("id", "1"))
                .andExpect(status().isNotFound());
    }

}
