package com.kisahy.commerce.service.customer.service;

import com.kisahy.commerce.service.customer.domain.Customer;
import com.kisahy.commerce.service.customer.repository.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CustomerServiceCreateTest {
    private CustomerRepository customerRepository;
    private CustomerService customerService;

    @BeforeEach
    void setUp() {
        customerRepository = Mockito.mock(CustomerRepository.class);
        customerService = new CustomerService(customerRepository);
    }

    @Nested
    class SuccessCases {
        @Test
        void createCustomer_Success() {
            String email = "test@example.com";
            String name = "김상현";
            String password = "password1234";

            when(customerRepository.findByEmail(email)).thenReturn(Optional.empty());
            ArgumentCaptor<Customer> captor = ArgumentCaptor.forClass(Customer.class);
            when(customerRepository.save(captor.capture())).thenAnswer(invocation -> invocation.getArgument(0));

            Customer created = customerService.createCustomer(email, name, password);

            assertNotNull(created);
            assertEquals(email, created.getEmail());
            assertEquals(name, created.getName());
            assertEquals(password, created.getPassword());
            verify(customerRepository, times(1)).save(any(Customer.class));
        }
    }

    @Nested
    class FailureCases {
        @Test
        void createCustomer_duplicateEmail() {
            String email = "test@example.com";

            when(customerRepository.findByEmail(email)).thenReturn(Optional.of(new Customer()));

            IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> customerService.createCustomer(email, "김상현", "password1234")
            );

            assertEquals("이미 존재하는 이메일입니다.", ex.getMessage());
        }
    }
}
