package com.tejas.service.impl;

import com.tejas.model.Customer;
import com.tejas.repository.CustomerRepository;
import com.tejas.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {
    private final CustomerRepository customerRepository;

    @Override
    public Customer createCustomer(Customer customer) {
        return customerRepository.save(customer);
    }

    @Override
    public Customer updateCustomer(Long id, Customer customer) {
        Customer existingCustomer = customerRepository.findById(id).orElseThrow(() -> new RuntimeException("customer not found"));
        if (customer.getFullName() != null) existingCustomer.setFullName(customer.getFullName());
        if (customer.getEmail() != null) existingCustomer.setEmail(customer.getEmail());
        if (customer.getPhone() != null) existingCustomer.setPhone(customer.getPhone());
        return customerRepository.save(existingCustomer);
    }

    @Override
    public void deleteCustomer(Long id) {
        Customer existingCustomer = customerRepository.findById(id).orElseThrow(() -> new RuntimeException("customer not found"));
        customerRepository.delete(existingCustomer);
    }

    @Override
    public Customer getCustomer(Long id) {
        return customerRepository.findById(id).orElseThrow(() -> new RuntimeException("customer not found"));
    }

    @Override
    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    @Override
    public List<Customer> searchCustomer(String keyword) {
        return customerRepository.findByFullNameContainingIgnoreCaseOrEmailContainingIgnoreCase(keyword, keyword);
    }
}
