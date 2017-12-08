import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.WebDriverRunner;
import org.assertj.core.api.SoftAssertions;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.chrome.ChromeDriver;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static com.codeborne.selenide.WebDriverRunner.url;

public class BaseScenario1 {

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

        //Выбираем Currency
        System.out.print("[-] Выставляем валюту - RUB: ");
        $(By.cssSelector(NewQuotationPage.Options.currencyButton)).click();
        $(By.cssSelector(NewQuotationPage.Options.currencySelectors)).shouldBe(Condition.visible);
        $(By.cssSelector(NewQuotationPage.Options.currencyRUB)).click();
        CommonCode.WaitForProgruzkaSilent();
        System.out.println(CommonCode.OK);

        //Выставляем Nights
        System.out.print("[-] Выставляем 2 ночи: ");
        $(By.cssSelector(NewQuotationPage.Options.nightsButton)).click();
        $(By.cssSelector(NewQuotationPage.Options.nightsInput)).shouldBe(Condition.visible);
        $(By.cssSelector(NewQuotationPage.Options.nightsInput)).setValue("2");
        CommonCode.WaitForProgruzkaSilent();
        System.out.println(CommonCode.OK);

        //Выбираем Present Meal Service
        System.out.print("[-] Выставляем Preset Meal Services - FB: ");
        $(By.cssSelector(NewQuotationPage.Options.presentMealServicesButton)).click();
        $(By.cssSelector(NewQuotationPage.Options.presentMealServicesButton)).click();
        $(By.cssSelector(NewQuotationPage.Options.presentMealServicesSelectors)).shouldBe(Condition.visible);
        $(By.cssSelector(NewQuotationPage.Options.presentMealServiceFullBoard)).click();
        CommonCode.WaitForProgruzkaSilent();
        System.out.println(CommonCode.OK);

        //Выбираем Additional Service
        /*System.out.print("[-] Выставляем Additional Service - Headphones: ");
        $(By.cssSelector(NewQuotationPage.Options.additionalServicesButton)).click();
        $(By.cssSelector(NewQuotationPage.Options.additionalServicesSelectors)).shouldBe(Condition.visible);
        $(By.cssSelector(NewQuotationPage.Options.additionalServicesHeadphones)).click();
        CommonCode.WaitForProgruzkaSilent();
        System.out.println(CommonCode.OK);*/

        //Выбираем Guides language
        System.out.print("[-] Выставляем Guides language - English: ");
        $(By.cssSelector(NewQuotationPage.Options.guidesLanguageButton)).click();
        $(By.cssSelector(NewQuotationPage.Options.guidesLanguageArea)).shouldBe(Condition.visible);
        $(By.cssSelector(NewQuotationPage.Options.guidesLanguageEnglish)).click();
        CommonCode.WaitForProgruzkaSilent();
        System.out.println(CommonCode.OK);

        //Выставляем Free Tour Leaders
        System.out.print("[-] Выставляем Free Tour Leaders - 1:");
        $(By.cssSelector(NewQuotationPage.Options.freeTourLeadersButton)).click();
        $(By.cssSelector(NewQuotationPage.Options.freeTourLeadersInput)).shouldBe(Condition.visible);
        $(By.cssSelector(NewQuotationPage.Options.freeTourLeadersInput)).setValue("1");
        CommonCode.WaitForProgruzkaSilent();
        System.out.println(CommonCode.OK);

        //Заполняем даты
        Date nowDate = new Date();
        SimpleDateFormat formatForDateNow = new SimpleDateFormat("dd-MM-yyyy");
        System.out.print("[-] Заполняем " +formatForDateNow.format(nowDate)+ " и плюс 4 дня: ");
        //Кликаем на поле для ввода даты
        $(By.cssSelector(NewQuotationPage.Dates.firstIntervalFromInput)).click();

        //System.out.println("Текущая дата: " + formatForDateNow.format(nowDate));
        //Вводим дату в поле
        $(By.cssSelector(NewQuotationPage.Dates.firstIntervalFromInput)).setValue(formatForDateNow.format(nowDate)).pressEnter();
        CommonCode.WaitForProgruzkaSilent();
        System.out.println(CommonCode.OK);

        //Выбираем Москву
        System.out.print("[-] Добавляем размещение - Москва: ");
        $(By.cssSelector(NewQuotationPage.Accommodations.cityAddButton)).click();
        $(By.cssSelector(NewQuotationPage.Accommodations.cityList)).shouldBe(Condition.visible);
        $(By.cssSelector(NewQuotationPage.Accommodations.moscowButton)).click();
        CommonCode.WaitForProgruzkaSilent();
        System.out.println(CommonCode.OK);

        //В первый день добавить экскурсию Бункер-42
        System.out.print("[-] В первый день добавляем экскурсию - Бункер-42: ");
        $(By.xpath(NewQuotationPage.Itinerary.DayCityByNumberXP(1,1)
                + NewQuotationPage.Itinerary.ServiceAddButton)).scrollTo().click();
        $(By.xpath(NewQuotationPage.Itinerary.DayCityByNumberXP(1,1)
                + NewQuotationPage.Itinerary.ServiceAddButton
                + "/div/div[@class=\"icons-block\"]")).shouldBe(Condition.visible);
        $(By.xpath(NewQuotationPage.Itinerary.DayCityByNumberXP(1,1)
                + NewQuotationPage.Itinerary.ServiceAddButton
                + "/div/div[@class=\"icons-block\"]//div[@data-service-type-id=\"3\"]")).click();
        CommonCode.WaitForProgruzkaSilent();
        $(By.xpath(NewQuotationPage.Itinerary.DayCityByNumberXP(1,1)
                + NewQuotationPage.Itinerary.serviceByNumberXP(4)
                + "//div[@class=\"click-service-area\"]")).scrollTo().click();
        $(By.xpath(NewQuotationPage.Itinerary.DayCityByNumberXP(1,1)
                + NewQuotationPage.Itinerary.serviceByNumberXP(4)
                + "//div[@class=\"service-names-list check-wrapper\"]")).shouldBe(Condition.visible);
        $(By.xpath(NewQuotationPage.Itinerary.DayCityByNumberXP(1,1)
                + NewQuotationPage.Itinerary.serviceByNumberXP(4)
                + "//div[@class=\"service-names-list check-wrapper\"]"
                + "//div[@class=\"check-list scroll-pane jspScrollable\"]/div[@class=\"jspContainer\"]"
                + "//div[@group-value=\"B\"]//div[@data-value=\"Bunker-42\"]")).click();
        CommonCode.WaitForProgruzkaSilent();
        System.out.println(CommonCode.OK);

        //В второй день добавить экскурсию Большой театр
        System.out.print("[-] Во второй день добавляем экскурсию - Большой Театр: ");
        $(By.xpath(NewQuotationPage.Itinerary.DayCityByNumberXP(2,1)
                + NewQuotationPage.Itinerary.ServiceAddButton)).scrollTo().click();
        $(By.xpath(NewQuotationPage.Itinerary.DayCityByNumberXP(2,1)
                + NewQuotationPage.Itinerary.ServiceAddButton
                + "/div/div[@class=\"icons-block\"]")).shouldBe(Condition.visible);
        $(By.xpath(NewQuotationPage.Itinerary.DayCityByNumberXP(2,1)
                + NewQuotationPage.Itinerary.ServiceAddButton
                + "/div/div[@class=\"icons-block\"]//div[@data-service-type-id=\"7\"]")).click();
        CommonCode.WaitForProgruzkaSilent();
        $(By.xpath(NewQuotationPage.Itinerary.DayCityByNumberXP(2,1)
                + NewQuotationPage.Itinerary.serviceByNumberXP(4)
                + "//div[@class=\"click-service-area\"]")).scrollTo().click();
        $(By.xpath(NewQuotationPage.Itinerary.DayCityByNumberXP(2,1)
                + NewQuotationPage.Itinerary.serviceByNumberXP(4)
                + "//div[@class=\"check-wrapper service-names-list\"]")).shouldBe(Condition.visible);
        $(By.xpath(NewQuotationPage.Itinerary.DayCityByNumberXP(2,1)
                + NewQuotationPage.Itinerary.serviceByNumberXP(4)
                + "//div[@class=\"check-wrapper service-names-list\"]"
                + "//div[@class=\"check-list scroll-pane\"]/div[@class=\"jspContainer\"]/div[@class=\"jspPane\"]"
                + "//div[@data-value=\"Bolshoi theatre\"]")).click();
        CommonCode.WaitForProgruzkaSilent();
        System.out.println(CommonCode.OK);



        //Запускаем расчёт
        System.out.print("Запускаем расчёт: ");
        $(By.xpath(NewQuotationPage.Results.calculateButton)).scrollTo().click();
        CommonCode.WaitForProgruzkaSilent();
        System.out.println(CommonCode.OK);
        /*
        Заполнить все данные для расчета (маршрут с программой тура)
        Расчитать стоимость тура
        Сгенерировать программу (web-версия + doc-версия)
        */

    }


    @After
    public void close() {
        driver.quit();
        softAssertions.assertAll();
    }
}
