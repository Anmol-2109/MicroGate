package com.anmol.userservice.Security;

import com.anmol.userservice.Entity.User;
import com.anmol.userservice.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = userRepository.findByUsername(username).orElseThrow(()->
                new RuntimeException(("User Not Found")));

        if(user.isDeleted()==true){
            throw new RuntimeException("User is unactivated or deleted");
        }

        return user;
    }
}
