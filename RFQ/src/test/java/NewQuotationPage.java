public class NewQuotationPage extends QuotationListPage{

    public final static String quotationId = "#content span[class=\"grey-title\"] span";
    //public final static String clientName = "#content div[class=\"container-olta title-row\"] div[class=\"left-col\"] #clientName div[class=\"hover-block\"]";
    public final static String clientName = "#clientName";
    public final static String chooseClientNamePopup = "#clientName div[class=\"clients-list check-wrapper\"]";

    public static class ChooseClientNamePopup{

        public final static String searchField = chooseClientNamePopup + " div[class=\"check-list-top-row\"] form input[class=\"super-check-list-filter\"]";

    }

    public final static String optionsArea = "#options div[class=\"options-row\"]";
    public static class Options{

        public final static String currencyButton = optionsArea+ " div[class=\"options-col have-list value-null\"]";
        public final static String currencySelectors = optionsArea+ " div[class=\"check-list\"]";
        public final static String currencyRUB = currencySelectors+ " div[data-value=\"RUB\"]";

        public final static String nightsButton = optionsArea+ ":nth-child(3) div[class=\"options-col have-string value-null\"]";
        public final static String nightsInput = optionsArea+ ":nth-child(3) div input[data-optionkey=\"number_of_nights\"]";

        public final static String presentMealServicesButton = optionsArea+ ":nth-child(4) div[class=\"options-col have-list value-null\"]";
        public final static String presentMealServicesSelectors = presentMealServicesButton+ " div[class=\"check-list\"]";
        public final static String presentMealServiceFullBoard = presentMealServicesSelectors+ " div[data-value=\"FB\"]";

        public final static String additionalServicesButton = optionsArea+ ":nth-child(5) div";
        public final static String additionalServicesSelectors = additionalServicesButton+" div[class=\"radio-list\"]";
        public final static String additionalServicesHeadphones = additionalServicesSelectors+" div[class=\"radio-row\"] div[title=\"Headphones\"]";

        public final static String guidesLanguageButton = optionsArea+" div:nth-child(2)";
        public final static String guidesLanguageArea = guidesLanguageButton+" div[class=\"check-list country-list\"]";
        public final static String guidesLanguageEnglish = guidesLanguageArea+" div[data-value=\"English\"]";

        public final static String freeTourLeadersButton = optionsArea+ ":nth-child(3) div:nth-child(2)";
        public final static String freeTourLeadersInput = optionsArea+ ":nth-child(3) div:nth-child(2) input[data-optionkey=\"ftl_number\"]";
    }

    public static class Dates{


    }

    public static class Groups{


    }

    public static class Accommodations{


    }

    public static class Itinerary{


    }
}
