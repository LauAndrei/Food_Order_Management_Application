package Service;

import Domain.User;
import Domain.UserType;
import Repository.IUserRepository;
import Repository.UserRepository;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;

public class LoginService implements ILoginService{
    private final IUserRepository userRepository;

    public LoginService(IUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    private Optional<String> getPasswordDigest(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] encodedPassword = digest.digest(password.getBytes(StandardCharsets.UTF_8));
            //replace all "\u0000" with "" because NULL is not supported
            return Optional.of(new String(encodedPassword, StandardCharsets.UTF_8).replaceAll("\u0000", ""));
        } catch (NoSuchAlgorithmException e) {
            return Optional.empty();
        }
    }

    public void register(String username, String password) {
        String passwordDigest = getPasswordDigest(password).get();
        User user = new User(null, username, passwordDigest, UserType.CLIENT);
        int success = userRepository.signUp(user);
        if (success == 1) {
            System.out.println("User created successfully");
        } else {
           throw new RuntimeException();
        }
    }

    public User logIn(String username, String password) {
        String passwordDigest = getPasswordDigest(password).get();
        User user = new User(null, username, passwordDigest, UserType.CLIENT);
        Optional<User> currentUser = userRepository.signIn(user);
        if (currentUser.isEmpty()) {
            throw new RuntimeException("Account does not exist!");
        }
        return currentUser.get();
    }
}
