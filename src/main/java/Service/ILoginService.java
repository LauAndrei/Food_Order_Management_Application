package Service;

import Domain.User;
import Domain.UserType;

import java.util.Optional;

public interface ILoginService {
    void register(String username, String password);
    User logIn(String username, String password);
}
