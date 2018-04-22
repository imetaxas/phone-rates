package rocks.appconcept.rebtel.converter;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import rocks.appconcept.rebtel.exception.PhoneRatesConversionException;
import rocks.appconcept.rebtel.provider.VonageProvider;

/**
 * Created by imeta on 07-May-17.
 */
public class VonageJsonCorverter extends JsonConverter {

  @Override
  public String convert(String stringToConvert) throws PhoneRatesConversionException {
    try {
      Document doc = Jsoup.parse(stringToConvert);

      Elements trs = doc.select("tr");

      putToJson(NAME, VonageProvider.NAME);
      putToJson(LOGO, VonageProvider.IMG_URL);
      putToJson(PHONE, trs.get(1).select("td").get(1).text());
      putToJson(CELL, trs.get(2).select("td").get(1).text());
    } catch (Exception e) {
      throw new PhoneRatesConversionException(e);
    }
    return getValidatedJson();
  }
}
