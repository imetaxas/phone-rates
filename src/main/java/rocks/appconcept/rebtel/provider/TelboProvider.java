package rocks.appconcept.rebtel.provider;

import java.io.IOException;
import rocks.appconcept.rebtel.converter.JsonConverter;
import rocks.appconcept.rebtel.exception.PhoneRatesConversionException;
import rocks.appconcept.rebtel.exception.PhoneRatesParseException;
import rocks.appconcept.rebtel.parser.TelboParser;
import rocks.appconcept.rebtel.util.HttpRequest;

/**
 * Created by imeta on 13-Apr-17.
 */
public class TelboProvider extends AbstractProvider {

  public static final String NAME = "Telbo";
  private static String url = "https://www.telbo.com/calling_rates";

  public TelboProvider(String country) {
    super(new TelboParser(country), new JsonConverter() {
      @Override
      public String convert(String stringToConvert) throws PhoneRatesConversionException {
        return null;
      }
    });
  }

  @Override
  public String getFares()
      throws IOException, PhoneRatesParseException, PhoneRatesConversionException {
    String result = HttpRequest.sendGet(url);
    return getParser().parse(result);
  }
}
