package com.Anmol.UserService.Security;

import com.Anmol.UserService.Entity.User;
import com.Anmol.UserService.Repository.UserRepository;
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
