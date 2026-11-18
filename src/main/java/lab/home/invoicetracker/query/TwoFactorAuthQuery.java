package lab.home.invoicetracker.query;

public class TwoFactorAuthQuery {
    public static final String DELETE_TWO_FACTOR_CODE_BY_USER_ID = "DELETE FROM TwoFactorVerifications WHERE user_id = :id";
    public static final String INSERT_TWO_FACTOR_CODE = "INSERT INTO TwoFactorVerification (user_id, code, expirationDate) VALUES (:userId, :code, :expirationDate)";
}
