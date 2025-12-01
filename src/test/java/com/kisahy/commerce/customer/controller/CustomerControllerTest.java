package com.kisahy.commerce.customer.controller;

import com.kisahy.commerce.customer.domain.Customer;
import com.kisahy.commerce.customer.service.CustomerService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CustomerController.class)
public class CustomerControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private CustomerService customerService;

    @Test
    void create_customer_api_success() throws Exception {
        Customer request = Customer.builder()
                .email("test@example.com")
                .name("김상현")
                .password("password1234")
                .build();

        Customer created = Customer.builder()
                .id(1L)
                .email(request.getEmail())
                .name(request.getName())
                .password(request.getPassword())
                .build();

        when(customerService.createCustomer(anyString(), anyString(), anyString())).thenReturn(created);

        mockMvc.perform(post("/api/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.email", is("test@example.com")))
                .andExpect(jsonPath("$.name", is("김상현")))
                .andExpect(jsonPath("$.password", is("password1234")));

        verify(customerService, times(1)).createCustomer(
                eq(request.getEmail()), eq(request.getName()), eq(request.getPassword())
        );
    }
}
