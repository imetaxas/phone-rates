package rocks.appconcept.rebtel.parser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import rocks.appconcept.rebtel.exception.PhoneRatesParseException;
import rocks.appconcept.rebtel.provider.BossRevolutionProvider;

/**
 * Created by imeta on 08-Apr-17.
 */
public class BossRevolutionHtmlParser extends HtmlParser {

  @Override
  public String parse(String stringToParse) throws PhoneRatesParseException {
    try {
      Document doc = Jsoup.parse(stringToParse);
      Element rateTable = doc.getElementById("ratesel_rate_row");

      //Rate Amount
      Element rateAmount = doc.getElementById("ratesel_amount");
      Elements options = rateAmount.children();
      //Rates
      Elements trs = rateTable.select("tr");

      StringBuilder result = new StringBuilder();
      result.append(HTML_TAG);
      result.append(BODY_TAG);
      result.append("<img src=\"" + BossRevolutionProvider.IMG_URL + "\"></img>");
      result.append("<table>");
      for (Element option : options) {
        result.append("<tr>");
        result.append("<td>");
        result.append("Amount:");
        result.append(option.text());
        result.append("</td>");
        result.append("</tr>");
        for (Element tr : trs) {
          result.append("<tr>");
          Elements tds = tr.select(TD);
          for (Element td : tds) {
            result.append(getHtmlValue(option, td));
          }
          result.append("</tr>");
        }
      }
      result.append("</table>");

      return validate(result.toString());
    } catch (Exception e) {
      throw new PhoneRatesParseException(e);
    }
  }

  private StringBuilder getHtmlValue(Element option, Element td) {
    StringBuilder result = new StringBuilder();
    result.append("<td>");
    try {
      Double val =
          (100 / Double.parseDouble(td.text().substring(0, td.text().length() - 1))) * Double
              .parseDouble(option.getElementsByAttribute("value").first().val());
      result.append(td.text());
      result.append("</td>");
      result.append("<td>");
      result.append(val.intValue());
    } catch (Exception e) {
      result.append(td.text());
    }
    result.append("</td>");
    return result;
  }
}
