package ru.netology.testmode.test;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Configuration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Selenide.$x;
import static com.codeborne.selenide.Selenide.open;
import static ru.netology.testmode.data.DataGenerator.*;
import static ru.netology.testmode.data.DataGenerator.Registration.getRegisteredUser;
import static ru.netology.testmode.data.DataGenerator.Registration.getUser;

class AuthTest {

    @BeforeEach
    void setup() {
        Configuration.holdBrowserOpen = true;
        open("http://localhost:9999");
    }

    @Test
    @DisplayName("Should successfully login with active registered user")
    void shouldSuccessfulLoginIfRegisteredActiveUser() {
        var registeredUser = getRegisteredUser("active");

        $x("//*[@data-test-id='login']//input").setValue(registeredUser.getLogin());
        $x("//*[@data-test-id='password']//input").setValue(registeredUser.getPassword());
        $x("//*[@data-test-id='action-login']").click();
        $x("//h2[contains (text(), 'Личный кабинет')]").shouldBe(Condition.visible);
    }

    @Test
    @DisplayName("Should get error message if login with not registered user")
    void shouldGetErrorIfNotRegisteredUser() {
        var notRegisteredUser = getUser("active");

        $x("//*[@data-test-id='login']//input").setValue(notRegisteredUser.getLogin());
        $x("//*[@data-test-id='password']//input").setValue(notRegisteredUser.getPassword());
        $x("//*[@data-test-id='action-login']").click();
        $x("//*[@class='notification__title']").shouldHave(Condition.text("Ошибка")).shouldBe(Condition.visible);
        $x("//*[@class='notification__content']").shouldHave(Condition.text("Ошибка! " + "Неверно указан логин или пароль")).shouldBe(Condition.visible);
    }

    @Test
    @DisplayName("Should get error message if login with blocked registered user")
    void shouldGetErrorIfBlockedUser() {
        var blockedUser = getRegisteredUser("blocked");

        $x("//*[@data-test-id='login']//input").setValue(blockedUser.getLogin());
        $x("//*[@data-test-id='password']//input").setValue(blockedUser.getPassword());
        $x("//*[@data-test-id='action-login']").click();
        $x("//*[@class='notification__title']").shouldHave(Condition.text("Ошибка")).shouldBe(Condition.visible);
        $x("//*[@class='notification__content']").shouldHave(Condition.text("Ошибка! " + "Пользователь заблокирован")).shouldBe(Condition.visible);
    }

    @Test
    @DisplayName("Should get error message if login with wrong login")
    void shouldGetErrorIfWrongLogin() {
        var registeredUser = getRegisteredUser("active");
        var wrongLogin = getRandomLogin();

        $x("//*[@data-test-id='login']//input").setValue(wrongLogin);
        $x("//*[@data-test-id='password']//input").setValue(registeredUser.getPassword());
        $x("//*[@data-test-id='action-login']").click();
        $x("//*[@class='notification__title']").shouldHave(Condition.text("Ошибка")).shouldBe(Condition.visible);
        $x("//*[@class='notification__content']").shouldHave(Condition.text("Ошибка! " + "Неверно указан логин или пароль")).shouldBe(Condition.visible);
    }

    @Test
    @DisplayName("Should get error message if login with wrong password")
    void shouldGetErrorIfWrongPassword() {
        var registeredUser = getRegisteredUser("active");
        var wrongPassword = getRandomPassword();

        $x("//*[@data-test-id='login']//input").setValue(registeredUser.getLogin());
        $x("//*[@data-test-id='password']//input").setValue(wrongPassword);
        $x("//*[@data-test-id='action-login']").click();
        $x("//*[@class='notification__title']").shouldHave(Condition.text("Ошибка")).shouldBe(Condition.visible);
        $x("//*[@class='notification__content']").shouldHave(Condition.text("Ошибка! " + "Неверно указан логин или пароль")).shouldBe(Condition.visible);
    }
}