package ml.yelen.yelen.security;

import ml.yelen.yelen.config.PasswordEncrypt;
import ml.yelen.yelen.exceptions.BadRequestException;
import ml.yelen.yelen.resources.URIs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Autowired
    private JwtAuthFilter jwtAuthFilter;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Bean
    public UserDetailsService userDetailsService() {
        return new UserDetailsServiceImpl();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        try {
            return http.csrf(AbstractHttpConfigurer::disable)
                    .authorizeHttpRequests(
                            authz -> authz
                                    /*.requestMatchers("/api/v1/auths/login/**").permitAll()
                                    .requestMatchers("/api/v1/auths/refreshToken/**").permitAll()*/
                                    /*.requestMatchers(HttpMethod.POST, "/api/v1/auths/createPatient/**").permitAll()*/
                                    /*.requestMatchers(HttpMethod.POST, "/api/v1/auths/createDoctor/**").permitAll()*/
                                    /*.requestMatchers(HttpMethod.POST, "/api/v1/roles/save/**").permitAll()*/
                                    .requestMatchers(URIs.AUTH_PREFIX+URIs.LOGIN_URI+"/**").permitAll()
                                    .requestMatchers("/swagger-ui/**").permitAll()
                                    .requestMatchers("/v3/api-docs/**").permitAll()
                                    .requestMatchers("/swagger-resources/**").permitAll()
                                    .requestMatchers("/webjars/**").permitAll()
                                    .requestMatchers("/api-docs/**").permitAll()
                                    .requestMatchers("/api/v1/**").authenticated()
                    )
                    .sessionManagement(
                            sess -> sess
                                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                    )
                    .authenticationProvider(authenticationProvider())
                    .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class).build();
        }catch (Exception e) {
            throw new BadRequestException(e.getMessage());
        }
    }

    /*@Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }*/

    @Bean
    public AuthenticationProvider authenticationProvider() {
        try {
            DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
            authenticationProvider.setUserDetailsService(userDetailsService());
            authenticationProvider.setPasswordEncoder(passwordEncoder);
            return authenticationProvider;
        }catch (Exception e) {
            throw new BadRequestException(e.getMessage());
        }
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}