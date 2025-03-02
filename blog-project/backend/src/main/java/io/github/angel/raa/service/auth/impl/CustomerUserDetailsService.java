package io.github.angel.raa.service.auth.impl;

import io.github.angel.raa.persistence.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CustomerUserDetailsService implements UserDetailsService {
    private final UserRepository repository;

    public CustomerUserDetailsService(UserRepository repository) {
        this.repository = repository;
    }
    @Transactional(readOnly = true)
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return repository.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));
    }
}
