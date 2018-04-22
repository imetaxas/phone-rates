package rocks.appconcept.rebtel.parser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

/**
 * Created by imeta on 09-Apr-17.
 */
public class LycaMobileParser extends HtmlParser {

  @Override
  public String parse(String stringToParse) {
    Document doc = Jsoup.parse(stringToParse);
    doc.select("p").remove();

    StringBuilder result = new StringBuilder();
    result
        .append("<img src=\"http://www.lycamobile.se/templates/template2/images/logo.gif\"></img>");
    result.append(doc.toString());

    return Jsoup.parse(result.toString()).html();
  }

  public String parseFromUrl1(String stringToParse, String country) {
    Document doc = Jsoup.parse(stringToParse);
    Element aImg = doc.select("a").attr("title", country).first().select("img").first();
    String aImgId = aImg.attr("id");
    return aImgId.split("_")[1];
  }
}
