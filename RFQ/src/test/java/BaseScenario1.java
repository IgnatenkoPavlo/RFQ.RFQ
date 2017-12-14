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
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;

import static com.codeborne.selenide.Condition.visible;
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

        class PeriodsCollection{
            public LocalDate dateFrom;
            public LocalDate dateTo;
            public int priceSGL;
            public int priceDBL;
            public int priceSGLWE;
            public int priceDBLWE;

            PeriodsCollection(String ... data) {
                if(data.length>=1)
                    dateFrom = LocalDate.of(Integer.valueOf(data[0].substring(6,data[0].length())), Integer.valueOf(data[0].substring(3,5)), Integer.valueOf(data[0].substring(0,2)));
                if(data.length>=2)
                    dateTo = LocalDate.of(Integer.valueOf(data[1].substring(6,data[1].length())), Integer.valueOf(data[1].substring(3,5)), Integer.valueOf(data[1].substring(0,2)));
                if(data.length>=3)
                    priceSGL = Integer.valueOf(data[2]);
                if(data.length>=4)
                    priceDBL = Integer.valueOf(data[3]);
                if(data.length>=5)
                    priceSGLWE = Integer.valueOf(data[4]);
                if(data.length>=6)
                    priceDBLWE = Integer.valueOf(data[5]);
            }
        }

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
        $(By.id("username")).setValue("test");
        $(By.id("password")).setValue("password");
        $(By.cssSelector("button[type=\"submit\"]")).click();
        System.out.println(CommonCode.OK);

        //Ждём пока загрузится страница и проподёт "Loading..."
        commonCode.WaitForPageToLoad(driver);
        CommonCode.WaitForProgruzkaSilent();

        //Открываем Quotation приложение
        System.out.print("[-] Открываем приложение Prices");
        open(String.valueOf(props.getProperty("baseURL"))+"/application/olta.prices");
        //Ждём пока загрузится страница и проподёт "Loading..."
        commonCode.WaitForPageToLoad(driver);
        CommonCode.WaitForProgruzkaSilent();
        System.out.println(CommonCode.OK);


        //Открываем групповые цены на отели
        System.out.print("[-] Открываем групповые цены");
        $(By.id("group")).click();
        //Открываем текущий день
        DateTimeFormatter formatForDate = DateTimeFormatter.ofPattern("dd-MM-yyyy")
                .withLocale(Locale.UK).withZone(ZoneOffset.UTC);
        DateTimeFormatter formatForPrices = DateTimeFormatter.ofPattern("yyyy-MM-dd")
                .withLocale(Locale.UK).withZone(ZoneOffset.UTC);
        LocalDate nowDate = LocalDate.now();
        LocalDate tommorrow = nowDate.plus(1, ChronoUnit.DAYS);
        System.out.println(CommonCode.OK);

        //Открываем Москву
        System.out.print("[-] Открываем Москву");
        $(By.xpath("//div[@id=\"switch-city\"]//button[@data-switch-value=\"MSK\"]")).click();
        CommonCode.WaitForProgruzkaSilent();
        System.out.println(CommonCode.OK);

        //Открываем текущий год
        System.out.print("[-] Открываем текущий год");
        $(By.xpath("//div[@id=\"switch-year\"]//button[contains(text(),'2017')]")).click();
        CommonCode.WaitForProgruzkaSilent();
        System.out.println(CommonCode.OK);

        //Выставляем тип отеля - Hotel 4* central
        System.out.print("[-] Выставляем тип отеля - Hotel 4* central");
        $(By.xpath("//select[@id=\"hotel-type-filter\"]")).selectOptionContainingText("Hotel 4* central");
        CommonCode.WaitForProgruzkaSilent();
        System.out.println(CommonCode.OK);

        //Открываем попап с ценами
        System.out.print("[-] Открываем попап с ценами");
        $(By.xpath("//div[@id=\"content\"]//div[@id=\"hotel-calendar\"]//div[@data-year=\"2017\"]" +
                "//div//table//tbody//tr" +
                "//td[@data-date=\""+nowDate.format(formatForPrices)+"\"]")).click();
        System.out.println(CommonCode.OK);

        //Сохраняем цены
        System.out.print("[-] Сохраняем цены");
        ArrayList<PeriodsCollection> prices = new ArrayList<>();

        $(By.xpath("//div[@class=\"modal-dialog\"]//div[@class=\"modal-content\"]")).shouldBe(visible);
        //Сохраняем значения из попапа
        String dateFrom = $(By.xpath("//div[@class=\"modal-dialog\"]//div[@class=\"modal-content\"]//form[@id=\"form-update-group-hotel-price\"]//input[@id=\"u-dateFrom\"]")).getValue();
        String dateTo = $(By.xpath("//div[@class=\"modal-dialog\"]//div[@class=\"modal-content\"]//form[@id=\"form-update-group-hotel-price\"]//input[@id=\"u-dateTo\"]")).getValue();
        String priceSGL = $(By.xpath("//div[@class=\"modal-dialog\"]//div[@class=\"modal-content\"]//form[@id=\"form-update-group-hotel-price\"]//input[@id=\"u-priceSgl\"]")).getValue();
        String priceDBL = $(By.xpath("//div[@class=\"modal-dialog\"]//div[@class=\"modal-content\"]//form[@id=\"form-update-group-hotel-price\"]//input[@id=\"u-priceDbl\"]")).getValue();
        String priceSGLWE = $(By.xpath("//div[@class=\"modal-dialog\"]//div[@class=\"modal-content\"]//form[@id=\"form-update-group-hotel-price\"]//input[@id=\"u-priceSglWe\"]")).getValue();
        String priceDBLWE = $(By.xpath("//div[@class=\"modal-dialog\"]//div[@class=\"modal-content\"]//form[@id=\"form-update-group-hotel-price\"]//input[@id=\"u-priceDblWe\"]")).getValue();

        //Сохраняем значения в новый элемент списка
        prices.add(new PeriodsCollection(dateFrom, dateTo, priceSGL, priceDBL, priceSGLWE, priceDBLWE));


        //System.out.println(dateFrom+" "+dateTo+" "+priceSGL+" "+priceDBL+" "+priceSGLWE+" "+priceDBLWE);
        //Закрываем попап
        $(By.xpath("//div[@class=\"modal-dialog\"]//div[@class=\"modal-content\"]//div[@class=\"modal-footer\"]//button[3]")).click();
        $(By.xpath("//div[@class=\"modal-dialog\"]//div[@class=\"modal-content\"]")).shouldNotBe(visible);
        System.out.println(CommonCode.OK);

        //Открываем Экскурсии
        System.out.print("[-] Открываем Экскурсии");
        $(By.id("excursions")).click();
        System.out.println(CommonCode.OK);

        //Открываем Москву
        System.out.print("[-] Открываем Москву");
        $(By.xpath("//div[@id=\"switch-city\"]//button[@data-switch-value=\"MSK\"]")).click();
        CommonCode.WaitForProgruzkaSilent();
        System.out.println(CommonCode.OK);

        //Открываем текущий год
        System.out.print("[-] Открываем текущий год");
        $(By.xpath("//div[@id=\"switch-year\"]//button[contains(text(),'2017')]")).click();
        CommonCode.WaitForProgruzkaSilent();
        System.out.println(CommonCode.OK);

        //Сохраняем цену за экскурсию - Бункер 42
        System.out.print("[-] Сохраняем цену за экскурсию - Бункер 42");
        String priceForBunker42 = $(By.xpath("//table[@id=\"service-prices\"]//tbody//tr[@data-excursion-id=\"3\"]" +
                "//td[@class=\"editable editable-service-price price\"]")).getText();
        System.out.println(" "+priceForBunker42+" ");
        System.out.println(CommonCode.OK);

        //Открываем Шоу
        System.out.print("[-] Открываем Шоу");
        $(By.id("shows")).click();
        System.out.println(CommonCode.OK);

        //Открываем Москву
        System.out.print("[-] Открываем Москву");
        $(By.xpath("//div[@id=\"switch-city\"]//button[@data-switch-value=\"MSK\"]")).click();
        CommonCode.WaitForProgruzkaSilent();
        System.out.println(CommonCode.OK);

        //Открываем текущий год
        System.out.print("[-] Открываем текущий год");
        $(By.xpath("//div[@id=\"switch-year\"]//button[contains(text(),'2017')]")).click();
        CommonCode.WaitForProgruzkaSilent();
        System.out.println(CommonCode.OK);

        //Сохраняем цену за Шоу - Большой Театр
        System.out.print("[-] Сохраняем цену за Шоу - Большой Театр");
        String priceForBolshoiTheatre = $(By.xpath("//table[@id=\"service-prices\"]//tbody//tr[@data-excursion-id=\"75\"]" +
                "//td[@class=\"editable editable-service-price price\"]")).getText();
        System.out.println(" "+priceForBolshoiTheatre+" ");
        System.out.println(CommonCode.OK);

        //Выходим из Prices
        System.out.print("[-] Выходим из Prices");
        $(By.xpath("//div[@id=\"profile\"]")).click();
        $(By.xpath("//button[@id=\"btn-logout\"]")).shouldBe(Condition.visible).click();
        System.out.println(CommonCode.OK);

        //Открываем клиентский RFQ
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

        //Выбираем Currency
        System.out.print("[-] Выставляем валюту - RUB: ");
        $(By.cssSelector(NewQuotationPage.Options.currencyButton)).click();
        $(By.cssSelector(NewQuotationPage.Options.currencySelectors)).shouldBe(Condition.visible);
        $(By.xpath(NewQuotationPage.Options.currencyRUBXP)).click();
        $(By.xpath(NewQuotationPage.Options.currencyRUBXP)).click();
        $(By.xpath(NewQuotationPage.Options.currencyRUBXP)).shouldNotBe(Condition.visible);
        CommonCode.WaitForProgruzkaSilent();

        System.out.println(CommonCode.OK);

        //Выставляем Nights
        System.out.print("[-] Выставляем 1 ночь: ");
        $(By.xpath(NewQuotationPage.Options.nightsButtonXP)).click();
        $(By.xpath(NewQuotationPage.Options.nightsInputXP)).shouldBe(Condition.visible);
        $(By.xpath(NewQuotationPage.Options.nightsInputXP)).setValue("1");
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
        System.out.print("[-] Заполняем " +formatForDate.format(nowDate)+ " и плюс 4 дня: ");
        //Кликаем на поле для ввода даты
        $(By.cssSelector(NewQuotationPage.Dates.firstIntervalFromInput)).click();

        //System.out.println("Текущая дата: " + formatForDateNow.format(nowDate));
        //Вводим дату в поле
        $(By.cssSelector(NewQuotationPage.Dates.firstIntervalFromInput)).setValue(formatForDate.format(nowDate)).pressEnter();
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
                + "//div[@group-value=\"B\"]//div[@data-value=\"BUNKER-42\"]")).click();
        CommonCode.WaitForProgruzkaSilent();
        System.out.println(CommonCode.OK);

        //В второй день добавить экскурсию Большой театр
        /*System.out.print("[-] Во второй день добавляем экскурсию - Большой Театр: ");
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
                + "//div[@data-value=\"BOLSHOI THEATRE\"]")).click();
        CommonCode.WaitForProgruzkaSilent();
        System.out.println(CommonCode.OK);*/



        //Запускаем расчёт
        System.out.print("[-] Запускаем расчёт: ");
        $(By.xpath(NewQuotationPage.Results.calculateButton)).scrollTo().click();
        CommonCode.WaitForProgruzkaSilent();
        System.out.println(CommonCode.OK);
        /*
        Заполнить все данные для расчета (маршрут с программой тура)
        Расчитать стоимость тура
        Сгенерировать программу (web-версия + doc-версия)
        */

        $(By.id("result")).scrollTo();
        Double hotelsWE;
        System.out.println("[-] Проверяем, что цены в Totals верные:");

            //hotelsWE = (Double.valueOf(prices.get(0).priceDBLWE)+Double.valueOf(prices.get(1).priceDBLWE))/4.0;
        if(nowDate.getDayOfWeek().getValue() >= 1 & nowDate.getDayOfWeek().getValue()<=4){
            hotelsWE = Double.valueOf((new BigDecimal(Double.valueOf(prices.get(0).priceDBL)/2.0).setScale(0, RoundingMode.DOWN).floatValue())
                    + (new BigDecimal(Double.valueOf(prices.get(0).priceSGL)/15.0).setScale(0, RoundingMode.DOWN).floatValue()));
        }
        else{
            hotelsWE = Double.valueOf((new BigDecimal(Double.valueOf(prices.get(0).priceDBLWE)/2.0).setScale(0, RoundingMode.DOWN).floatValue())
                    + (new BigDecimal(Double.valueOf(prices.get(0).priceSGLWE)/15.0).setScale(0, RoundingMode.DOWN).floatValue()));
        }
            /*hotelsWE = hotelsWE + Double.valueOf(priceForBunker42) +  Double.valueOf(priceForBunker42)/15.0
                    + Double.valueOf(priceForBolshoiTheatre) + Double.valueOf(priceForBolshoiTheatre)/15.0
                    + 2500.0/15.0 + 850.0/15.0
                    + 2500.0/15.0 + 850.0/15.0;*/

        hotelsWE = hotelsWE + Double.valueOf(priceForBunker42) + Double.valueOf(priceForBunker42)/15.0
                + 2500.0/15.0 + 850.0/15.0;
        hotelsWE = Double.valueOf(new BigDecimal(hotelsWE).setScale(0, RoundingMode.DOWN).floatValue());
            //hotelsWE = hotelsWE / rubEur;
            hotelsWE = hotelsWE / 0.85;
            String priceDBLDS = String.valueOf((int) new BigDecimal(hotelsWE).setScale(0, RoundingMode.DOWN).floatValue());

            String result = $(By.cssSelector("table#table-result-totals tbody tr:nth-of-type(2) td:nth-of-type(2)")).getText();
            result = result.substring(0, result.indexOf(' '));
        //System.out.println("Из Prices получили:"+priceDBLDS+" в Totals:"+ result);
           if (result.equals(priceDBLDS)){
                System.out.println(CommonCode.ANSI_GREEN+"      Ошибки нет, значение для группы 15 корректное + "+CommonCode.ANSI_RESET);
            } else {
                softAssertions.assertThat(result)
                        .as("Check that value in Hotels for 15, is correct")
                        .isEqualTo(priceDBLDS);
                System.out.println(CommonCode.ANSI_RED +"      Значение для группы 15 не некорректные: " + CommonCode.ANSI_RESET
                        + result + " -");
            }


    }


    @After
    public void close() {
        driver.quit();
        softAssertions.assertAll();
    }
}
