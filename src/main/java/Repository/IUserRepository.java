package Repository;

import Domain.User;

import java.util.Optional;

public interface IUserRepository {
    int signUp(User user);
    Optional<User> signIn(User user);
    Optional<User> getUserById(Integer id);
}
