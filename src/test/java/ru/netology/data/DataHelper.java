package ru.netology.data;

import com.github.javafaker.Faker;
import lombok.SneakyThrows;
import lombok.Value;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Locale;

public class DataHelper {
    private static Faker faker = new Faker(new Locale("en"));
    private static QueryRunner runner = new QueryRunner();

    private DataHelper() {
    }

    @SneakyThrows
    private static Connection getConn() {
        return DriverManager.getConnection("jdbc:mysql://localhost:3306/app", "app", "pass");
    }

    @SneakyThrows
    public static void cleanDatabase() {
        var connection = getConn();
        runner.execute(connection,"DELETE FROM auth_codes");
        runner.execute(connection,"DELETE FROM card_transactions");
        runner.execute(connection,"DELETE FROM cards");
        runner.execute(connection,"DELETE FROM users");
    }

    @SneakyThrows
    public static DataHelper.VerificationCode getVerificationCode() {
        var codeSQL = "SELECT code FROM auth_codes ORDER BY created DESC LIMIT 1";
        var conn = getConn();
        var code = runner.query(conn, codeSQL, new ScalarHandler<String>());
        return new DataHelper.VerificationCode(code);
    }

    @SneakyThrows
    public static String getBlockedUser() {
        var codeSQL = "SELECT status FROM users LIMIT 1";
        var conn = getConn();
        var result = runner.query(conn, codeSQL, new ScalarHandler<String>());
        return result;
    }

    @Value
    public static class AuthInfo {
        private String login;
        private String password;

    }

    public static AuthInfo getValidAuthInfo() {
        return new AuthInfo("vasya", "qwerty123");
    }

    public static String generateRandomLogin() {
        return faker.name().username();
    }
    public static String generateRandomPassword() {
        return faker.internet().password();
    }
    public static VerificationCode generateRandomVerificationCode() {
        return new VerificationCode(faker.numerify("#####"));
    }

    public static AuthInfo getGenerateRandomUser() {
        return new AuthInfo(generateRandomLogin(), generateRandomPassword());
    }

    public static AuthInfo getInvalidRandomUser() {
        return new AuthInfo(getValidAuthInfo().getLogin(),generateRandomPassword());
    }

    @Value
    public static class VerificationCode {
        String code;
    }
}
