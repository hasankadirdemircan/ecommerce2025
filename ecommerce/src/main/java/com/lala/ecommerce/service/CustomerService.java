package com.lala.ecommerce.service;

import com.lala.ecommerce.enums.RoleEnum;
import com.lala.ecommerce.dto.CustomerDto;
import com.lala.ecommerce.mapper.CustomerMapper;
import com.lala.ecommerce.model.Customer;
import com.lala.ecommerce.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final PasswordEncoder passwordEncoder;
    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;

    public CustomerDto createCustomer(Customer customer) {
        if(Objects.isNull(customer.getRoles())) {
            customer.setRoles(RoleEnum.ROLE_USER.name());
        }
        customer.setPassword(passwordEncoder.encode(customer.getPassword()));
        return customerMapper.customerToCustomerDto(customerRepository.save(customer));

    }
}
