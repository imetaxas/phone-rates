package rocks.appconcept.rebtel.parser;

import org.jsoup.Jsoup;

/**
 * Created by imeta on 08-Apr-17.
 */
public abstract class HtmlParser implements Parser {

  protected static final String HTML_TAG = "<html>";
  protected static final String BODY_TAG = "<body>";
  protected static final String TD = "td";

  public String validate(String htmlString) {
    return Jsoup.parse(htmlString).html();
  }
}
