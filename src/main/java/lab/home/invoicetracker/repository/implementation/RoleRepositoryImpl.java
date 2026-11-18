package lab.home.invoicetracker.repository.implementation;

import static lab.home.invoicetracker.query.RoleQuery.*;

import java.util.Collection;
import java.util.Map;
import java.util.Objects;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import lab.home.invoicetracker.exception.ApiException;
import lab.home.invoicetracker.model.Role;
import lab.home.invoicetracker.repository.RoleRepository;
import lab.home.invoicetracker.repository.rowmapper.RoleRowMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Repository 
@RequiredArgsConstructor 
@Slf4j 
public class RoleRepositoryImpl implements RoleRepository<Role> {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    @Override
    public Role create(Role data) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'create'");
    }

    @Override
    public Collection<Role> list(int page, int pageSize) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'list'");
    }

    @Override
    public Role get(Long id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'get'");
    }

    @Override
    public Role update(Role data) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'update'");
    }

    @Override
    public Boolean delete(Long id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'delete'");
    }

    @Override
    public void addRoleToUser(Long userId, String roleName) {
        log.info("Adding role {} to user id: {}", roleName, userId);

        try {
            Role role = jdbcTemplate.queryForObject(SELECT_ROLE_BY_NAME_QUERY, Map.of("name", roleName), new RoleRowMapper());
            jdbcTemplate.update(INSERT_ROLE_TO_USER_QUERY, Map.of("userId", userId, "roleId", Objects.requireNonNull(role).getId()));
        } catch (Exception exception) {
            log.error("Error in RoleRepository.addRoleToUser: {}", exception.getMessage());
            throw new ApiException("An error occured.  Please try again.");
        }
    }

    @Override
    public Role getRoleByUserId(Long userId) {
        try {
            return jdbcTemplate.queryForObject(SELECT_ROLE_BY_USER_ID_QUERY, Map.of("userId", userId), new RoleRowMapper());
        } catch (Exception exception) {
            exception.printStackTrace();
            log.error("Error in RoleRepository.getRoleByUserId: {}", exception.getMessage());
            throw new ApiException("An error occured.  Please try again.");
        }
    }

    @Override
    public Role getRoleByUserEmail(String email) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getRoleByUserEmail'");
    }

    @Override
    public void updateUserRole(Long userId, String roleName) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateUserRole'");
    }
    
}
