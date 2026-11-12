package lab.home.invoicetracker.dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data 
public class UserDTO {
    
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String address;
    private String phone;
    private String title;
    private String bio;
    private Boolean enabled;
    private Boolean isNotLocked;
    private Boolean isUsingMfa;
    private LocalDateTime createdAt;
}
