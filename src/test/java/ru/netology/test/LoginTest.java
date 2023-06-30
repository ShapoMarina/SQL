package ru.netology.test;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.netology.data.DataHelper;
import ru.netology.page.LoginPage;


import static com.codeborne.selenide.Selenide.open;
import static ru.netology.data.DataHelper.cleanDatabase;

public class LoginTest {

    @AfterAll
    static void clearing() {
        cleanDatabase();
    }

    @Test
    void shouldSuccessfulLogin() {
        var loginPage = open("http://localhost:9999", LoginPage.class);
        var loginData = DataHelper.getValidAuthInfo();
        var verificationPage = loginPage.validLogin(loginData);
        var verificationCode = DataHelper.getVerificationCode();
        verificationPage.validVerify(verificationCode.getCode());
    }

    @Test
    void shouldErrorMessageWhenEnteringRandomVerificationCode() {
        var loginPage = open("http://localhost:9999", LoginPage.class);
        var loginData = DataHelper.getValidAuthInfo();
        var verificationPage = loginPage.validLogin(loginData);
        var verificationCode = DataHelper.generateRandomVerificationCode();
        verificationPage.verify(verificationCode.getCode());
        verificationPage.verifyErrorNotification();
    }

    @Test
    void shouldErrorMessageWhenEnteringRandomPasswordRegisteredUser() {
        var loginPage = open("http://localhost:9999", LoginPage.class);
        var firstLoginData = DataHelper.getInvalidRandomUser();
        loginPage.login(firstLoginData);
        loginPage.cleaningFields();

        var secondLoginData = DataHelper.getInvalidRandomUser();
        loginPage.login(secondLoginData);
        loginPage.cleaningFields();

        var thirdLoginData = DataHelper.getInvalidRandomUser();
        loginPage.login(thirdLoginData);
        loginPage.cleaningFields();

        var loginData = DataHelper.getValidAuthInfo();
        loginPage.validLogin(loginData);
        var actual = DataHelper.getBlockedUser();
        var expected = "blocked";
        Assertions.assertEquals(expected, actual);

    }
}
