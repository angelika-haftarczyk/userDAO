package pl.coderslab.entity;

import org.apache.commons.lang3.ArrayUtils;
import org.mindrot.jbcrypt.BCrypt;
import pl.coderslab.DbUtil;

import java.sql.*;

public class UserDao {

    private static final String createUserQuery =  "INSERT INTO users(username, email, password) VALUES (?, ?, ?)";
    private static final String readUserQuery = "SELECT * FROM users WHERE id = ?";
    private static final String updateUserQuery = "UPDATE users SET username = ?, email = ?, password = ? WHERE id = ?";
    private static final String deleteUserQuery = "DELETE FROM users WHERE id = ?";
    private static final String findAllUserQuery = "SELECT * FROM users";

    public static void main(String[] args) {

      User user1 = createUser("Jan","jan@gmail.com","hasło");
      User user2 = createUser("Stasiu","stasiu@gmail.com","password");
      User user3 = createUser("Czesio","czesio@wp.pl","hasło123");
      User user4 = createUser("Ala","ala@gmail.com","123hasło");
      User user5 = createUser("Kasia","kasia@vp.pl","123345");


        User[] users = findAllUser();
        for (int i = 0; i < users.length; i++) {
            System.out.println(users[i]);
        }
        System.out.println("-------");
        User user = readUser(1);

        System.out.println(user);
        updateUser(1, "Jasiu","jasiu@o2.pl", user.getPassword());
        User userUpdate = readUser(1);
        System.out.println("po update");
        System.out.println(userUpdate);
        System.out.println("-------");

        users = findAllUser();
        for (int i = 0; i < users.length; i++) {
            System.out.println(users[i]);
        }
        System.out.println("-------");

        deleteUser(3);
        System.out.println("po usunięciu");
        users = findAllUser();
        for (int i = 0; i < users.length; i++) {
            System.out.println(users[i]);
        }
        System.out.println("-------");

    }

    public static User createUser(String username, String email, String password) {
        User user = new User();
        try (Connection conn = DbUtil.getConnection()) {
            PreparedStatement prepStmt = conn.prepareStatement(createUserQuery, PreparedStatement.RETURN_GENERATED_KEYS);
            prepStmt.setString(1, username);
            prepStmt.setString(2, email);
            prepStmt.setString(3, hashPassword(password));
            prepStmt.executeUpdate();
            ResultSet rs = prepStmt.getGeneratedKeys();
            if (rs.next()) {
                int id = rs.getInt(1);
                System.out.println("Inserted ID: " + id);
                user.setId(id);
            }
            user.setUserName(username);
            user.setEmail(email);
            user.setPassword(password);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return user;
    }

    public static User readUser(int id) {
        User user = new User();
        try (Connection conn = DbUtil.getConnection()) {
            PreparedStatement prepStmt = conn.prepareStatement(readUserQuery);
            prepStmt.setInt(1, id);
            ResultSet rs = prepStmt.executeQuery();
            if (rs.next()) {
                user.setId(id);
                user.setUserName(rs.getString(2));
                user.setEmail(rs.getString(3));
                user.setPassword(rs.getString(4));
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return user;
    }

    public static User updateUser(int id, String username, String email, String password) {
        User user = new User();
        try (Connection conn = DbUtil.getConnection()) {
            PreparedStatement prepStmt = conn.prepareStatement(updateUserQuery);
            prepStmt.setString(1, username);
            prepStmt.setString(2, email);
            prepStmt.setString(3, hashPassword(password));
            prepStmt.setInt(4, id);
            prepStmt.executeUpdate();
            user.setId(id);
            user.setUserName(username);
            user.setEmail(email);
            user.setPassword(password);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return user;
    }

    public static void deleteUser(int id) {
        try (Connection conn = DbUtil.getConnection()) {
            PreparedStatement prepStmt = conn.prepareStatement(deleteUserQuery );
            prepStmt.setInt(1, id);
            prepStmt.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public static User[] findAllUser() {
        User[] users = new User[0];
        try (Connection conn = DbUtil.getConnection()) {
            PreparedStatement prepStmt = conn.prepareStatement(findAllUserQuery);
            ResultSet rs = prepStmt.executeQuery();
            while (rs.next()) {
                User user = new User();
                user.setId(rs.getInt(1));
                user.setUserName(rs.getString(2));
                user.setEmail(rs.getString(3));
                user.setPassword(rs.getString(4));
                users = ArrayUtils.add(users, user);
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return users;
    }

    public static String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }
}
