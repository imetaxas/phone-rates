package rocks.appconcept.rebtel.converter;

import java.util.StringTokenizer;
import rocks.appconcept.rebtel.exception.PhoneRatesConversionException;
import rocks.appconcept.rebtel.provider.LycaMobileProvider;

/**
 * Created by imeta on 15-May-17.
 */
public class LycaMobileJsonConverter extends JsonConverter {

  @Override
  public String convert(String stringToConvert) throws PhoneRatesConversionException {
    StringTokenizer tokenizer = new StringTokenizer(stringToConvert.replaceAll("<sup></sup>", "|"), "|");
    tokenizer.nextToken();
    String phone = tokenizer.nextToken();
    String cell = tokenizer.nextToken();
    try {
      putToJson(NAME, LycaMobileProvider.NAME);
      putToJson(LOGO, LycaMobileProvider.IMG_URL);
      putToJson(PHONE, phone);
      putToJson(CELL, cell);
    } catch (Exception e) {
      throw new PhoneRatesConversionException(e);
    }
    return getValidatedJson();
  }
}
