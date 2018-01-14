import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.WebDriverRunner;
import org.assertj.core.api.SoftAssertions;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.chrome.ChromeDriver;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.confirm;
import static com.codeborne.selenide.Selenide.open;
import static com.codeborne.selenide.WebDriverRunner.url;

public class OptionsPresentMealServices {
    public ChromeDriver driver;

    CommonCode commonCode = new CommonCode();
    private SoftAssertions softAssertions;

    @Before
    public void setUp() {
        System.setProperty("webdriver.chrome.driver", "C:\\Automation\\chromedriver.exe");
        driver = new ChromeDriver();
        driver.manage().window().maximize();

        softAssertions = new SoftAssertions();
    }

    @Test
    public void test1(){

        WebDriverRunner.setWebDriver(driver);
        Configuration selenideConfig = new Configuration();
        selenideConfig.timeout = 30000;

        Properties props=new Properties();
        try {
            props.load(new InputStreamReader(new FileInputStream("target\\test-classes\\application.properties"), "UTF-8"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.print("Получил проперти ");
        System.out.println(props.getProperty("baseURL"));
        System.out.print("[-] Открываем URL: "+props.getProperty("baseURL"));
        open(props.getProperty("baseURL"));
        commonCode.WaitForPageToLoad(driver);
        System.out.println(CommonCode.OK);

        //Ждём пока загрузится страница и проподёт "Loading..."
        commonCode.WaitForPageToLoad(driver);
        CommonCode.WaitForProgruzkaSilent();

        System.out.print("[-] Открываем URL: "+props.getProperty("baseURL")+"/application/rfq.rfq");
        open(props.getProperty("baseURL")+"/application/rfq.rfq");
        commonCode.WaitForPageToLoad(driver);
        System.out.println(CommonCode.OK);

        //Вводим логин с паролем и кликаем Логин
        System.out.print("[-] Вводим логин с паролем и кликаем Логин");
        $(By.id("username")).setValue("test1@test.com");
        $(By.id("password")).setValue("password");
        $(By.cssSelector("button[type=\"submit\"]")).click();
        System.out.println(CommonCode.OK);

        //Ждём пока загрузится страница и проподёт "Loading..."
        commonCode.WaitForPageToLoad(driver);
        CommonCode.WaitForProgruzkaSilent();
        System.out.println("[-] страница загружена - URL: " + url());

        //Создаём новый Quotation
        System.out.print("[-] Создаём новый Quotation, ID = ");
        $(By.cssSelector(QuotationListPage.newQuotationButton)).click();
        CommonCode.WaitForProgruzkaSilent();

        //Получаем Id новой квотации
        String newQuotationID = $(By.cssSelector(NewQuotationPage.quotationId)).getText();
        newQuotationID = newQuotationID.substring(1, newQuotationID.length());
        System.out.println(CommonCode.ANSI_GREEN+newQuotationID+CommonCode.ANSI_RESET);

        //Выставляем имя клиента
        System.out.print("[-] Выставляем имя клиента, как "+"Test Client: ");
        $(By.cssSelector(NewQuotationPage.clientName)).click();
        $(By.cssSelector(NewQuotationPage.chooseClientNamePopup)).shouldBe(Condition.visible);
        //$(By.cssSelector(NewQuotationPage.ChooseClientNamePopup.searchField)).sendKeys("Test Client\n");
        $(By.cssSelector(NewQuotationPage.chooseClientNamePopup
                + " div[class=\"check-list scroll-pane\"] div[class=\"jspContainer\"] div[class=\"jspPane\"]"
                + " div[group-value=\"T\"] div[class=\"check-wrap\"] span")).click();
        CommonCode.WaitForProgruzkaSilent();
        System.out.println(CommonCode.OK);

        //Выставляем Nights
        System.out.print("[-] Выставляем 1 ночь: ");
        $(By.xpath(NewQuotationPage.Options.nightsButtonXP)).click();
        $(By.xpath(NewQuotationPage.Options.nightsInputXP)).shouldBe(Condition.visible);
        $(By.xpath(NewQuotationPage.Options.nightsInputXP)).setValue("1");
        CommonCode.WaitForProgruzkaSilent();
        System.out.println(CommonCode.OK);

        System.out.print("[-] Выставляем Preset Meal Services - FB: ");
        $(By.cssSelector(NewQuotationPage.Options.presentMealServicesButton)).click();
        $(By.cssSelector(NewQuotationPage.Options.presentMealServicesButton)).click();
        $(By.cssSelector(NewQuotationPage.Options.presentMealServicesSelectors)).shouldBe(Condition.visible);
        $(By.cssSelector(NewQuotationPage.Options.presentMealServiceFullBoard)).click();
        //$(By.cssSelector(NewQuotationPage.Options.presentMealServiceNO)).click();
        CommonCode.WaitForProgruzkaSilent();
        System.out.println(CommonCode.OK);

        //Заполняем даты
        DateTimeFormatter formatForDate = DateTimeFormatter.ofPattern("dd-MM-yyyy")
                .withLocale(Locale.UK).withZone(ZoneOffset.UTC);
        LocalDate nowDate = LocalDate.now();
        System.out.print("[-] Заполняем дату From: " +formatForDate.format(nowDate)+ " ");
        //Кликаем на поле для ввода даты
        $(By.cssSelector(NewQuotationPage.Dates.firstIntervalFromInput)).click();

        //System.out.println("Текущая дата: " + formatForDateNow.format(nowDate));
        //Вводим дату в поле
        $(By.cssSelector(NewQuotationPage.Dates.firstIntervalFromInput)).setValue(formatForDate.format(nowDate)).pressEnter();
        CommonCode.WaitForProgruzkaSilent();
        System.out.println(CommonCode.OK);

        //Проверяем, что типы питания выставляются корректно и происходит автогенерация сервисов
        System.out.println("[-] Проверяем, что типы питания выставляются корректно и происходит автогенерация сервисов:");
        String[] presentMealServices = {"BB", "HB", "NO", "FB"};
        String errorText;
        //boolean additionIsCorrect;
        for(int i=0; i<presentMealServices.length; i++){

            //additionIsCorrect=true;
            errorText = "none";
            //Выбираем Present Meal Service
            System.out.println("[-] Выставляем Preset Meal Services - "+presentMealServices[i]+": ");
            $(By.cssSelector(NewQuotationPage.Options.presentMealServicesButton)).scrollTo().click();
            $(By.cssSelector(NewQuotationPage.Options.presentMealServicesButton)).click();
            $(By.cssSelector(NewQuotationPage.Options.presentMealServicesSelectors)).shouldBe(Condition.visible);
            $(By.cssSelector(NewQuotationPage.Options.presentMealServicesSelectors+ " div[data-value=\""+presentMealServices[i]+"\"]")).click();
            //$(By.cssSelector(NewQuotationPage.Options.presentMealServiceNO)).click();
            CommonCode.WaitForProgruzkaSilent();
            //System.out.println(CommonCode.OK);

            //Выбираем Москву
            //System.out.print("[-] Добавляем размещение - Москва: ");
            $(By.xpath("//div[@id=\"accommodationsBlock\"]//div[@class=\"info-row empty-accommodation\"]")).click();
            //$(By.cssSelector(NewQuotationPage.Accommodations.cityAddButton)).click();
            CommonCode.WaitForProgruzkaSilent();
            $(By.xpath("//div[@id=\"accommodationsBlock\"]//div[@class=\"info-row empty-accommodation\"]//div[@class=\"check-wrapper city-selector\"]")).shouldBe(Condition.visible);
            //$(By.cssSelector(NewQuotationPage.Accommodations.cityList)).shouldBe(Condition.visible);
            $(By.xpath("//div[@id=\"accommodationsBlock\"]//div[@class=\"info-row empty-accommodation\"]//div[@class=\"check-wrapper city-selector\"]/div/div/div/div/div")).click();
            //$(By.cssSelector(NewQuotationPage.Accommodations.moscowButton)).click();
            errorText = commonCode.GetJSErrorText(driver);
            //System.out.println(errorText);
            CommonCode.WaitForProgruzkaSilent();

            if(errorText.equals("none")){
                System.out.println(CommonCode.ANSI_GREEN+"      Базовая генерация прошла + "+CommonCode.ANSI_RESET);
                //Удаляем город из Accomodations
                $(By.xpath("//div[@id=\"accommodationsBlock\"]//div[@class=\"info-row\"]")).hover();
                $(By.xpath("//div[@id=\"accommodationsBlock\"]//div[@class=\"info-row\"]//div[@class=\"delete-btn\"]")).hover().click();
                confirm();
                CommonCode.WaitForProgruzkaSilent();

                //Проверяем что добавились нужные автосервисы

            }
            else{
                softAssertions.assertThat(errorText)
                        .as("Try to use "+ presentMealServices[i] +" for quotation generation: ")
                        .isEqualTo("Ok");
                System.out.println(CommonCode.ANSI_RED +"      Добавление: " + presentMealServices[i] + " упало -"+ CommonCode.ANSI_RESET);
                CommonCode.WaitForProgruzkaSilent();
            }



        }


    }

    @After
    public void close() {
        driver.quit();
        softAssertions.assertAll();
    }

}
