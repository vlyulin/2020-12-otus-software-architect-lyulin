package ru.otus.spring.library.services;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.otus.spring.library.models.User;
import ru.otus.spring.library.repositories.UserRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private UserRepository userRepository;

    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // https://overcoder.net/q/1158292/%D0%B2%D0%BE%D0%B7%D0%B2%D1%80%D0%B0%D1%89%D0%B0%D0%B5%D1%82-%D0%B7%D0%BD%D0%B0%D1%87%D0%B5%D0%BD%D0%B8%D0%B5-%D0%B7%D0%B0%D0%BF%D1%80%D0%BE%D1%81%D0%B0-jpa-%D0%B5%D1%81%D0%BB%D0%B8-%D1%81%D0%BE%D0%B2%D0%BF%D0%B0%D0%B4%D0%B5%D0%BD%D0%B8%D0%B9-%D0%BD%D0%B5-%D0%BD%D0%B0%D0%B9%D0%B4%D0%B5%D0%BD%D0%BE
        User user = userRepository.findByLoginIgnoreCase(username);
        if (user == null) {
            throw new UsernameNotFoundException(username + " not found.");
        }

        org.springframework.security.core.userdetails.User.UserBuilder builder = null;
        builder = org.springframework.security.core.userdetails.User.withUsername(username);
        builder.password(user.getPassword());
        builder.roles(userRepository.findAuthoritiesByLogin(username));
        return builder.build();
    }
}
