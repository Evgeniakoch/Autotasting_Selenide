import com.codeborne.selenide.Configuration;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.openqa.selenium.Keys.BACK_SPACE;


public class cardOrderForm {
    public String TodayPlus3Days() {
        return LocalDate.now().plusDays(3).format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));

    }


    @Test
    void shouldSendCardOrderFormWithValidInformation() {
        Configuration.holdBrowserOpen = true;
        open("http://localhost:9999");
        $("[data-test-id='city']").click();
        $("[data-test-id='city']").find("[type=text]").setValue("Нижний Новгород");
        $("[data-test-id='date']").find("[type=tel]").doubleClick().sendKeys(BACK_SPACE);
        $("[data-test-id='date']").find("[type=tel]").setValue(TodayPlus3Days());
        $("[data-test-id='name']").find("[type=text]").setValue("Иванов");
        $("[data-test-id='phone']").find("[type=tel]").setValue("+79515255555");
        $("[data-test-id='agreement']").click();
        $(By.className("button")).click();
        $("[data-test-id = notification]").shouldHave(text("Успешно!"),
                Duration.ofSeconds(15)).shouldBe(visible);
        $(".notification__content").shouldHave(text("Встреча успешно забронирована на " + TodayPlus3Days()),
                Duration.ofSeconds(15)).shouldBe(visible);
    }

    @Test
    void shouldSendCardOrderFormWithUnvalidCity() {
        Configuration.holdBrowserOpen = true;
        open("http://localhost:9999");
        $("[data-test-id='city']").click();
        $("[data-test-id='city']").find("[type=text]").setValue("Сан-Паулу");
        $("[data-test-id='date']").find("[type=tel]").doubleClick().sendKeys(BACK_SPACE);
        $("[data-test-id='date']").find("[type=tel]").setValue(TodayPlus3Days());
        $("[data-test-id='name']").find("[type=text]").setValue("Иванов");
        $("[data-test-id='phone']").find("[type=tel]").setValue("+79515255555");
        $("[data-test-id='agreement']").click();
        $(By.className("button")).click();
        String expected = "Доставка в выбранный город недоступна";
        String actual = $(By.cssSelector("[data-test-id='city'].input_invalid .input__sub")).getText();
        assertEquals(expected, actual);
    }
    
}
