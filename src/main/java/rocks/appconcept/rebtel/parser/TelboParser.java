package rocks.appconcept.rebtel.parser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * Created by imeta on 13-Apr-17.
 */
public class TelboParser extends HtmlParser {

  private String country;

  public TelboParser(String country) {
    this.country = country;
  }

  @Override
  public String parse(String stringToParse) {
    Document doc = Jsoup.parse(stringToParse);
    Element rateTable = doc.getElementsByClass("rates-wrapper").first();

    StringBuilder result = new StringBuilder();
    result.append("<html>"
        + "<script src=\"js/telbo.js\"></script>"
        + "<body>"
        + "<img src=\"https://www.telbo.com/public/assets/images/site_logo.png\"></img>");

    // Fix the page anchors for the ABCD...
    Elements as = rateTable.select("a");
    for (Element a : as) {
      if (a.attr("href").startsWith("#/")) {
        a.attr("href", a.attr("href").substring(2));
      }
    }

    // Set country
    Elements countryOptions = rateTable.getElementById("select-rates-country").select("option");
    for (Element option : countryOptions) {
      if (option.text().equalsIgnoreCase(country)) {
        option.attr("selected", "selected");
        break;
      }
    }

    Elements currencyOptions = rateTable.getElementById("select-rates-currency").select("option");
    for (Element option : currencyOptions) {
      if (option.text().equalsIgnoreCase("EURO")) {
        option.attr("selected", "selected");
        break;
      }
    }

    result.append(rateTable.toString());

    return Jsoup.parse(result.toString()).html();
  }
}
