import java.util.HashMap;

public class LoginManger {
    private final HashMap<String, String> users;

    public LoginManger(HashMap<String, String> users) {
        this.users = users;
    }

    public static LoginManger createDefault() {
        return new LoginManger(new HashMap<>());
    }

    public void addUser(String username, String password) {
        users.put(username, password);
    }

    public boolean isLogin(String username, String password) {
        return users.containsKey(username) && users.get(username).equals(password);
    }

    public boolean isNotLogin(String username, String password) {
        return !isLogin(username, password);
    }
}
