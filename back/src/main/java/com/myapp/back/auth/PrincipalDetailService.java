package com.myapp.back.auth;

import com.myapp.back.model.User;
import com.myapp.back.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

//http://localhost:8080 /login 호출시 (스프링 시큐리티 자동 uri) -> 동직을 하지 않는다. formLogin사용 안하니 => SpringSecuriyFilter를 extends해서 해결
@Service
@RequiredArgsConstructor
public class PrincipalDetailService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        System.out.println("PrincipalDetailService loadUserByUsername 실행중");

        User findUser = userRepository.findByEmail(email);

        return new PrincipalDetails(findUser);
    }
}
