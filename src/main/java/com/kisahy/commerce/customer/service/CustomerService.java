package com.kisahy.commerce.customer.service;

import com.kisahy.commerce.customer.domain.Customer;
import com.kisahy.commerce.customer.repository.CustomerRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class CustomerService {
    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public Customer createCustomer(
            String email,
            String name,
            String password
    ) {
        if (customerRepository.findByEmail(email).isPresent()) {
            throw new IllegalArgumentException("이미 존재하는 이메일입니다.");
        }

        Customer customer = Customer.builder()
                .email(email)
                .name(name)
                .password(password)
                .build();

        return customerRepository.save(customer);
    }
}
