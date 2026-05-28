import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class TestOtus extends AbsBaseTest {

    @Test
    @BrowserConfig(mode = BrowserMode.HEADLESS)
    void textInput() {
        WebElement input = driver.findElement(By.id("textInput"));

        input.sendKeys("OTUS");

        Assertions.assertEquals("OTUS", input.getAttribute("value"));
    }

    @Test
    @BrowserConfig(mode = BrowserMode.KIOSK)
    void mailTest() {
        driver.findElement(By.id("name")).sendKeys("Pavel");
        driver.findElement(By.id("email")).sendKeys("pavel_otus@gmail.com");
        driver.findElement(By.cssSelector("button[type='submit']")).click();

        WebElement result = driver.findElement(By.id("messageBox"));

        Assertions.assertEquals(
                "Форма отправлена с именем: Pavel и email: pavel_otus@gmail.com",
                result.getText()
        );
    }

     @Test
     @BrowserConfig(mode = BrowserMode.MAXIMIZED)
     void modalWindow() {
    driver.findElement(By.id("openModalBtn")).click();

    String modalText = driver.findElement(By.id("myModal"))
            .getText()
            .replaceAll("\\s+", " ")
            .trim();

    Assertions.assertTrue(
            modalText.contains("Вы открыли модальное окно. Нажмите на крестик или в любое место вне окна, чтобы закрыть его.")
    );
 }
}
