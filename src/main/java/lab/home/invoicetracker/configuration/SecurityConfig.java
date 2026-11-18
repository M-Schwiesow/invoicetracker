package lab.home.invoicetracker.configuration;

import lab.home.invoicetracker.handler.CustomAccessDeniedHandler;
import lab.home.invoicetracker.handler.CustomAuthenticationEntryPoint;
import lombok.RequiredArgsConstructor;

import static org.springframework.http.HttpMethod.DELETE;
import static org.springframework.http.HttpMethod.OPTIONS;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration 
@EnableWebSecurity 
@EnableMethodSecurity 
@RequiredArgsConstructor 
public class SecurityConfig {

    private static final String[] PUBLIC_URLS = {"/user/login/**", "/user/register/**"};

    private final CustomAccessDeniedHandler customAccessDeniedHandler;
    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;
    private final UserDetailsService userDetailsService;
    private final BCryptPasswordEncoder encoder;

    @Bean 
    public SecurityFilterChain securityFilterChain(HttpSecurity http) {

        http
            .csrf(csrf -> csrf.disable())
            .cors(cors -> cors.disable())
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(request -> 
                request.requestMatchers(PUBLIC_URLS).permitAll()
                    .requestMatchers(OPTIONS).permitAll()
                    .requestMatchers(DELETE, "/user/delete/**").hasAnyAuthority("DELETE:USER")
                    .requestMatchers(DELETE, "/customer/delete/**").hasAnyAuthority("DELETE:CUSTOMER")
                    .anyRequest().authenticated())
            .exceptionHandling(exception -> 
                exception.accessDeniedHandler(customAccessDeniedHandler)
                    .authenticationEntryPoint(customAuthenticationEntryPoint));

        return http.build();
    }

    @Bean 
    public AuthenticationManager authenticationManager() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider(userDetailsService);
        authProvider.setPasswordEncoder(encoder);
        return new ProviderManager(authProvider);
    }

}
