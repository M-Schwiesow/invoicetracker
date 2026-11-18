package lab.home.invoicetracker.repository.implementation;

import static java.util.Objects.requireNonNull;
import static lab.home.invoicetracker.query.UserQuery.*;
import static lab.home.invoicetracker.query.TwoFactorAuthQuery.*;
import static lab.home.invoicetracker.enumeration.RoleType.*;
import static lab.home.invoicetracker.enumeration.VerificationType.*;

import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Repository;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import lab.home.invoicetracker.dto.UserDTO;
import lab.home.invoicetracker.exception.ApiException;
import lab.home.invoicetracker.model.Role;
import lab.home.invoicetracker.model.User;
import lab.home.invoicetracker.repository.RoleRepository;
import lab.home.invoicetracker.repository.UserRepository;
import lab.home.invoicetracker.repository.rowmapper.UserRowMapper;
import lab.home.invoicetracker.util.SmsUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Repository 
@RequiredArgsConstructor 
@Slf4j 
public class UserRepositoryImpl implements UserRepository<User> {
    
    private static final String DATE_FORMAT = "yyyy-MM-dd hh:mm:ss";
    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final RoleRepository<Role> roleRepository;
    private final BCryptPasswordEncoder encoder;

    @Override
    public User create(User user) {
        log.info("UserRepository.create called with user: {}", user.getEmail());
        // Check the email is unique
        if(getEmailCount(user.getEmail().trim().toLowerCase()) > 0) throw new ApiException("Email already in use.  Please use a different email and try again.");

        // Save new user
        try {
            KeyHolder holder = new GeneratedKeyHolder();
            SqlParameterSource parameters = getSqlParameterSource(user);
            jdbcTemplate.update(INSERT_USER_QUERY, parameters, holder, new String[]{"id"});
            user.setId(requireNonNull(holder.getKey()).longValue());

            // add role to user
            roleRepository.addRoleToUser(user.getId(), ROLE_USER.name());

            // send verification url
            String verificationUrl = getVerificationUrl(UUID.randomUUID().toString(), ACCOUNT.getType());
            // save URL to table
            jdbcTemplate.update(INSERT_ACCOUNT_VERIFICATION_URL_QUERY, Map.of("userId", user.getId(), "url", verificationUrl));

            // TODO: Marc - send email
            //emailService.sendVerificationUrl(user.getFirstName(), user.getEmail(), verificationUrl, ACCOUNT);
            
            user.setEnabled(false);
            user.setIsNotLocked(true);
            return user;
        } catch (Exception exception) {
            log.error("Exception in UserRepository.create: {}", exception.getMessage());
            throw new ApiException("An error occured.  Please try again. " + exception.getMessage());
        }
    }

    private SqlParameterSource getSqlParameterSource(User user) {
        return new MapSqlParameterSource()
            .addValue("firstName", user.getFirstName())
            .addValue("lastName", user.getLastName())
            .addValue("email", user.getEmail())
            .addValue("password", encoder.encode(user.getPassword()));

    }

    private Integer getEmailCount(String email) {
        return jdbcTemplate.queryForObject(COUNT_USER_EMAIL_QUERY, Map.of("email", email), Integer.class);
    }

    @Override
    public Collection<User> list(int page, int pageSize) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'list'");
    }

    @Override
    public User get(Long id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'get'");
    }

    @Override
    public User update(User data) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'update'");
    }

    @Override
    public Boolean delete(Long id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'delete'");
    }

    private String getVerificationUrl(String key, String type) {
        return ServletUriComponentsBuilder.fromCurrentContextPath().path("/user/verify/" + type + "/" + key).toUriString();
    }

    @Override 
    public User getUserByEmail(String email) {
        log.debug("UserRepositoryImpl.getUserByEmail called with email: {}", email);
        try {
            User user = jdbcTemplate.queryForObject(SELECT_USER_BY_EMAIL_QUERY, Map.of("email", email), new UserRowMapper());

            log.debug("UserRepositoryImpl.getUserByEmail returning with userId: {}", user.getId());
            return user;
        } catch (Exception exception) {
            log.error("Exception getting user by email: {}", exception.getMessage());
            throw new ApiException("Error in getUserByEmail");
        }
    }

    @Override
    public void sendVerificationCode(UserDTO userDTO) {
        String expirationDate = DateFormatUtils.format(DateUtils.addDays(new Date(), 1), DATE_FORMAT);
        String verificationCode = RandomStringUtils.secure().nextAlphabetic(8);
        try {
            jdbcTemplate.update(DELETE_TWO_FACTOR_CODE_BY_USER_ID, Map.of("id", userDTO.getId()));
            jdbcTemplate.update(INSERT_TWO_FACTOR_CODE, Map.of("userId", userDTO.getId(), "code", verificationCode, "expirationDate", expirationDate));
            SmsUtils.sendSMS(userDTO.getPhone(), "From: Invoice Tracker \nVerification Code\n" + verificationCode);
        } catch (Exception exception) {
            log.error("oh no! {}", exception.getMessage());
        }
    }
}
