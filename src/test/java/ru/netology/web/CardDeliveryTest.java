package ru.netology.web;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.logevents.LogEventListener;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static com.codeborne.selenide.Selectors.withText;
import static com.codeborne.selenide.Selenide.*;
import static org.openqa.selenium.Keys.BACK_SPACE;

public class CardDeliveryTest {
    public String generateDate(int shift) {
        String date;
        LocalDate localDate = LocalDate.now().plusDays(shift);
        date = DateTimeFormatter.ofPattern("dd.MM.yyyy").format(localDate);
        return date;
    }

    @BeforeAll
    static void setUpAll() {
        SelenideLogger.addListener("allure", new AllureSelenide());}

    @AfterAll
    static void tearDownAll(){
        SelenideLogger.removeListener("allure");
    }

    @BeforeEach

    public void openPage() {
        open("http://localhost:9999/");
    }

    @Test
    public void shouldSuccessOrderIfCorrectFilling() {
        //Configuration.holdBrowserOpen = true;
        String date = generateDate(3);
        $("[data-test-id=city] input").setValue("Москва");
        $("[data-test-id=date] input").doubleClick();
        $("[data-test-id=date] input")
                .sendKeys(Keys.chord(BACK_SPACE, date));
        $("[data-test-id=name] input").setValue("Романов Роман");
        $("[data-test-id=phone] input").setValue("+79998887766");
        $(".checkbox__box").click();
        $(withText("Забронировать")).click();
        $(".notification__content")
                .shouldHave(Condition.text("Встреча успешно забронирована на " + date), Duration.ofSeconds(15));

    }

    @Test
    public void shouldUnsuccessOrderIfIncorrectName() {
        String date = generateDate(3);
        $("[data-test-id=city] input").setValue("Москва");
        $("[data-test-id=date] input").doubleClick();
        $("[data-test-id=date] input")
                .sendKeys(Keys.chord(BACK_SPACE, date));
        $("[data-test-id=name] input").setValue("Tinkoff Oleg");
        $("[data-test-id=phone] input").setValue("+79998887766");
        $(".checkbox__box").click();
        $(withText("Забронировать")).click();
        $(withText("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы."))
                .shouldBe(Condition.visible);

    }

    @Test
    public void shouldUnsuccessOrderIfNoName() {
        String date = generateDate(3);
        $("[data-test-id=city] input").setValue("Москва");
        $("[data-test-id=date] input").doubleClick();
        $("[data-test-id=date] input")
                .sendKeys(Keys.chord(BACK_SPACE, date));
        $("[data-test-id=name] input").setValue("");
        $("[data-test-id=phone] input").setValue("+79998887766");
        $(".checkbox__box").click();
        $(withText("Забронировать")).click();
        $(withText("Поле обязательно для заполнения"))
                .shouldBe(Condition.visible);
    }

}
