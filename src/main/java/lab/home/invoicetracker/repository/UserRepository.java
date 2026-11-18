package lab.home.invoicetracker.repository;

import java.util.Collection;

import lab.home.invoicetracker.dto.UserDTO;
import lab.home.invoicetracker.model.User;

public interface UserRepository<T extends User> {
    /* Basic CRUD Operations */
    T create(T data);
    Collection<T> list(int page, int pageSize);
    T get(Long id);
    T update(T data);
    Boolean delete(Long id);

    // more operations to follow
    User getUserByEmail(String email);
    void sendVerificationCode(UserDTO userDTO);
}
