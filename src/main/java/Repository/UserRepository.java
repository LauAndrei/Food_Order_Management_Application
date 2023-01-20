package Repository;

import Domain.User;
import Domain.UserType;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class UserRepository implements IUserRepository{
    private static final String username = "postgres";
    private static final String password = "user";
    private static final String url = "jdbc:postgresql://localhost:5432/postgres";

    public int signUp(User user) {
        var sqlString = "INSERT INTO users(username, password_digest, account_type) VALUES (?, ?, ?);";
        try (var connection = DriverManager.getConnection(url, username, password);
             var preparedStatement = connection.prepareStatement(sqlString)) {
            preparedStatement.setString(1, user.getUsername());
            preparedStatement.setString(2, user.getPasswordDigest());
            preparedStatement.setInt(3, user.getUserType().ordinal());
            try {
                preparedStatement.executeUpdate();
                return 1;
            } catch (SQLException exception) {
                System.out.println("Error signUp in UserRepository: " + exception.getMessage());
                return 0;
            }
        } catch (SQLException exception) {
            System.out.println("Couldn't connect to the database");
            System.out.println(exception.getMessage());
            System.exit(-1);
        }
        return 0;
    }

    public Optional<User> signIn(User user) {
        var sqlString = "SELECT * FROM users WHERE username = ? AND password_digest = ?;";
        try (var connection = DriverManager.getConnection(url, username, password);
             var preparedStatement = connection.prepareStatement(sqlString)) {
            String username = user.getUsername();
            String passwordDigest = user.getPasswordDigest();
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, passwordDigest);
            ResultSet rs = preparedStatement.executeQuery();
            if (!rs.next()) {
                return Optional.empty();
            }
            UserType userType = UserType.values()[rs.getInt("account_type")];
            Integer id = rs.getInt("id");
            return Optional.of(new User(id, username, passwordDigest, userType));
        } catch (SQLException exception) {
            System.out.println("Couldn't connect to database");
            System.exit(-1);
            return Optional.empty();
        }
    }

    public Optional<User> getUserById(Integer id){
        var sqlString = "SELECT * FROM users WHERE id = ?";
        try (var connection = DriverManager.getConnection(url, username, password);
             var preparedStatement = connection.prepareStatement(sqlString)){
            preparedStatement.setInt(1, id);
            ResultSet rs = preparedStatement.executeQuery();
            if (!rs.next()) {
                return Optional.empty();
            }
            UserType userType = UserType.values()[rs.getInt("account_type")];
            String username = rs.getString("username");
            String passwordDigest = rs.getString("password_digest");
            return Optional.of(new User(id, username, passwordDigest, userType));
        }
        catch (SQLException exception){
            System.out.println("Couldn't connect to database");
            System.exit(-1);
            return Optional.empty();
        }
    }
}
