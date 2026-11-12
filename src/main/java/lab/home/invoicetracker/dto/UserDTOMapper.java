package lab.home.invoicetracker.dto;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import lab.home.invoicetracker.model.User;

@Component 
public class UserDTOMapper {
    public static UserDTO fromUser(User user) {
        UserDTO userDTO = new UserDTO();
        BeanUtils.copyProperties(user, userDTO);
        return userDTO;
    }

    public static User fromUserDTO(UserDTO userDTO) {
        User user = new User();
        BeanUtils.copyProperties(userDTO, user);
        return user;
    }
}
