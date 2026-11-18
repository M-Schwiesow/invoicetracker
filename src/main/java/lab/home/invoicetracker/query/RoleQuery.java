package lab.home.invoicetracker.query;

public class RoleQuery {
    public static final String INSERT_ROLE_TO_USER_QUERY = "INSERT INTO invoice_tracker.UserRoles (user_id, role_id) VALUES (:userId, :roleId)";
    public static final String SELECT_ROLE_BY_NAME_QUERY = "SELECT * FROM invoice_tracker.Roles WHERE name = :name";
    public static final String SELECT_ROLE_BY_USER_ID_QUERY = "SELECT Roles.* FROM invoice_tracker.Roles JOIN invoice_tracker.UserRoles ON Roles.id = UserRoles.role_id WHERE UserRoles.user_id = :userId";
}
