package rocks.appconcept.rebtel.parser;

import org.jsoup.Jsoup;

/**
 * Created by imeta on 08-Apr-17.
 */
public class VonageParser extends HtmlParser {

  @Override
  public String parse(String stringToParse) {
    String res =
        "<img width='130' height='30' src=\"https://www.vonage.co.uk/img/logo/logo.svg\"></img>"
            + "<table>"
            + "<thead>"
            + "<tr>"
            + "<th>Description</th>"
            + "<th>Rate / Min</th>"
            + "<th>Call Set-up Fee</th>"
            + "<th>Area Code</th>"
            + "</tr>"
            + "</thead>"
            + "<tbody>"
            + stringToParse
            + "</tbody>"
            + "</table>";
    return Jsoup.parse(res).html();
  }
}
