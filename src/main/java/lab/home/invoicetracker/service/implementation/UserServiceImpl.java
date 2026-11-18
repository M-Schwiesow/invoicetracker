package lab.home.invoicetracker.service.implementation;

import org.springframework.stereotype.Service;

import lab.home.invoicetracker.dto.UserDTO;
import lab.home.invoicetracker.dto.UserDTOMapper;
import lab.home.invoicetracker.model.User;
import lab.home.invoicetracker.repository.UserRepository;
import lab.home.invoicetracker.service.UserService;
import lombok.RequiredArgsConstructor;

@Service 
@RequiredArgsConstructor 
public class UserServiceImpl implements UserService {
    private final UserRepository<User> userRepository;

    @Override
    public UserDTO createUser(User user) {
        return UserDTOMapper.fromUser(userRepository.create(user));
    }

    @Override 
    public UserDTO getUserByEmail(String email) {
        User user = userRepository.getUserByEmail(email);
        return UserDTOMapper.fromUser(user);
    }

}
