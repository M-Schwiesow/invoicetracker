package lab.home.invoicetracker.repository.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import lab.home.invoicetracker.model.Role;
import lombok.NoArgsConstructor;

@NoArgsConstructor 
public class RoleRowMapper implements RowMapper<Role> {@Override
    public Role mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        return Role.builder()
        .id(resultSet.getLong("id"))
        .name(resultSet.getString("name"))
        .permission(resultSet.getString("permission"))
        .build();
    }
    
}
