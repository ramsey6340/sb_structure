package ml.yelen.yelen.security;

import lombok.extern.slf4j.Slf4j;
import ml.yelen.yelen.entities.Administrator;
import ml.yelen.yelen.exceptions.BadRequestException;
import ml.yelen.yelen.repositories.AdminRepository;
import ml.yelen.yelen.resources.ErrorMessageValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@Slf4j
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private AdminRepository adminRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            Optional<Administrator> adminOptional = adminRepository.findByUsername(username);
            if(adminOptional.isEmpty()){
                throw new UsernameNotFoundException(ErrorMessageValue.USER_NOT_FOUND);
            }
            return new CustomUserDetails(adminOptional.get());
        }catch (Exception e) {
            throw new BadRequestException(e.getMessage());
        }
    }
}