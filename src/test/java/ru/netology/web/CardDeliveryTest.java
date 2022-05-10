package ru.netology.web;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Configuration;
import org.junit.jupiter.api.AfterEach;
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

    @BeforeEach

    public void openPage() {
        open("http://localhost:9999/");
    }

   // @AfterEach
    void tearDown() {
        closeWindow();
    }

    @Test
    public void test() {
        //Configuration.holdBrowserOpen = true;
        String date = generateDate(3);
        $("[data-test-id=city] input").setValue("Москва");
        $("[data-test-id=date] input").doubleClick();
        $("[data-test-id=date] input")
                .sendKeys(Keys.chord(BACK_SPACE, date));
        $("[data-test-id=name] input").setValue("Романов Роман");
        $("[data-test-id=phone] input").setValue("+79998887766");
        $$(".checkbox__box").find(Condition.visible).click();
        $$("button").find(Condition.exactText("Забронировать")).click();
        $(withText("Встреча успешно забронирована"))
                .shouldBe(Condition.visible, Duration.ofSeconds(15));
        $(withText("Встреча успешно забронирована"))
                .shouldBe(Condition.text(date));

    }


}
