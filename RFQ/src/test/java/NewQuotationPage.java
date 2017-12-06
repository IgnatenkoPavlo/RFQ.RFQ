public class NewQuotationPage extends QuotationListPage{

    public final static String quotationId = "#content span[class=\"grey-title\"] span";
    //public final static String clientName = "#content div[class=\"container-olta title-row\"] div[class=\"left-col\"] #clientName div[class=\"hover-block\"]";
    public final static String clientName = "#clientName";
    public final static String chooseClientNamePopup = "#clientName div[class=\"clients-list check-wrapper\"]";

    public static class ChooseClientNamePopup{

        public final static String searchField = chooseClientNamePopup + " div[class=\"check-list-top-row\"] form input[class=\"super-check-list-filter\"]";

    }
}
