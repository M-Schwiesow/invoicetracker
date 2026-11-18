package lab.home.invoicetracker.repository.implementation;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Repository;

import lab.home.invoicetracker.model.Role;
import lab.home.invoicetracker.model.User;
import lab.home.invoicetracker.model.UserPrincipal;
import lab.home.invoicetracker.repository.RoleRepository;
import lab.home.invoicetracker.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j 
@Repository 
@RequiredArgsConstructor 
public class CustomUserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository<User> userRepository;
    private final RoleRepository<Role> roleRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        try {
            User user = userRepository.getUserByEmail(email);
            if(user == null) {
                log.error("user not found");
                throw new UsernameNotFoundException("user not found in the database");

            } else {
                log.info("User found in DB: {}", user.getId());
                String permissions = roleRepository.getRoleByUserId(user.getId()).getPermission();
                return new UserPrincipal(user, permissions);
            } 
        } catch (Exception exception) {
            log.error("Exception getting user by email: {}", exception.getMessage());
            throw exception;
        }
    }
}
