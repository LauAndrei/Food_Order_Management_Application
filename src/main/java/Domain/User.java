package Domain;

public class User {
    private final Integer id;
    private final String username;
    private final String passwordDigest;
    private final UserType userType;

    public User(Integer id, String username, String passwordDigest, UserType userType) {
        this.id = id;
        this.username = username;
        this.passwordDigest = passwordDigest;
        this.userType = userType;
    }

    public String getUsername() {
        return username;
    }

    public String getPasswordDigest() {
        return passwordDigest;
    }

    public UserType getUserType() {
        return userType;
    }

    public Integer getId() {
        return id;
    }

    @Override
    public String toString() {
        return "User: id= " + id + ", username= " + username + '\n' +'\n';
    }
}
