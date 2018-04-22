package rocks.appconcept.rebtel.converter;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import rocks.appconcept.rebtel.exception.PhoneRatesConversionException;
import rocks.appconcept.rebtel.provider.KeepCallingProvider;

/**
 * Created by imeta on 15-May-17.
 */

/**
 * <html>
 * <head></head>
 * <body>
 * <img width="130" height="30" src="https://keepcalling.com/images/logo.png">
 * <div class="vertical_center rate_description_top">
 * Greece
 * </div>
 * <div class="vertical_center rate_currency margin_top10">
 * <span> 2?¢</span>
 * <span class="font-size16">/min </span>
 * </div>
 * <div class="hidden-phone vertical_center rate_description_bottom">
 * 500 min for ?10
 * </div>
 * <br>
 * <div class="vertical_center rate_description_top">
 * Mobile
 * </div>
 * <div class="vertical_center rate_currency margin_top10">
 * <span> 6.3?¢</span>
 * <span class="font-size16">/min </span>
 * </div>
 * <div class="hidden-phone vertical_center rate_description_bottom">
 * 158 min for ?10
 * </div>
 * <br>
 * <div class="vertical_center rate_description_top">
 * SMS
 * </div>
 * <div class="vertical_center rate_currency margin_top10">
 * <span> 7?¢</span>
 * <span class="font-size16"> </span>
 * </div>
 * <div class="hidden-phone vertical_center rate_description_bottom">
 * 142 SMS for ?10
 * </div>
 * <br>
 * </body>
 * </html>
 */
public class KeepCallingJsonConverter extends JsonConverter {

  @Override
  public String convert(String stringToConvert) throws PhoneRatesConversionException {
    try {
      Document doc = Jsoup.parse(stringToConvert);

      Elements divs = doc.select("div");

      putToJson(NAME, KeepCallingProvider.NAME);
      putToJson(LOGO, KeepCallingProvider.IMG_URL);
      putToJson(PHONE, divs.get(1).select("span").get(0).text());
      putToJson(CELL, divs.get(4).select("span").get(0).text());
    } catch (Exception e) {
      throw new PhoneRatesConversionException(e);
    }
    return getValidatedJson();
  }
}
