package sit.int222.poc.services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import sit.int222.poc.user_account.User;
import sit.int222.poc.user_account.UserRepository;

import java.util.List;

@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
}
