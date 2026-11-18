package lab.home.invoicetracker.service;

import lab.home.invoicetracker.dto.UserDTO;
import lab.home.invoicetracker.model.User;

public interface UserService {
    UserDTO createUser(User user);
    UserDTO getUserByEmail(String email);

}
