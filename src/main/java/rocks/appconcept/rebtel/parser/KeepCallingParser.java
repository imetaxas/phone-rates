package rocks.appconcept.rebtel.parser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * Created by imeta on 09-Apr-17.
 */
public class KeepCallingParser extends HtmlParser {

  @Override
  public String parse(String stringToParse) {

    Document doc = Jsoup.parse(stringToParse);
    Element rateTable = doc.getElementById("searched_rates");
    Elements as = rateTable.select("a");

    StringBuilder result = new StringBuilder();
    result.append(
        "<img width='130' height='30' src=\"https://keepcalling.com/images/logo.png\"></img>");

    for (Element a : as) {
      result.append(a.select("div").first().html());
      result.append("<br>");
    }
    return Jsoup.parse(result.toString()).html();
  }
}
