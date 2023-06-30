package ru.netology.page;

import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.Keys;
import ru.netology.data.DataHelper;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public class LoginPage {
    private final SelenideElement LoginField = $("[data-test-id='login'] input");
    private final SelenideElement passwordField = $("[data-test-id='password'] input");
    private final SelenideElement LoginButton = $("[data-test-id='action-login']");
    private final SelenideElement errorNotification = $("[data-test-id='error-notification']");


    public VerificationPage validLogin(DataHelper.AuthInfo info) {
        LoginField.setValue(info.getLogin());
        passwordField.setValue(info.getPassword());
        LoginButton.click();
        return new VerificationPage();
    }
    public void errorMessage() {
        errorNotification.shouldBe(visible);
    }

    public void cleaningFields() {
        LoginField.sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
        passwordField.sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
    }

    public void login(DataHelper.AuthInfo info) {
        LoginField.setValue(info.getLogin());
        passwordField.setValue(info.getPassword());
        LoginButton.click();
    }
}
