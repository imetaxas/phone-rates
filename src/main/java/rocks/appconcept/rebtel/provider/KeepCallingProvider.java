package rocks.appconcept.rebtel.provider;

import java.io.IOException;
import rocks.appconcept.rebtel.converter.KeepCallingJsonConverter;
import rocks.appconcept.rebtel.exception.PhoneRatesConversionException;
import rocks.appconcept.rebtel.exception.PhoneRatesParseException;
import rocks.appconcept.rebtel.parser.KeepCallingParser;
import rocks.appconcept.rebtel.util.HttpRequest;

/**
 * Created by imeta on 09-Apr-17.
 */
public class KeepCallingProvider extends AbstractProvider {

  public static final String NAME = "KeepCalling";
  public static final String IMG_URL = "https://keepcalling.com/images/logo.png";
  private static final String URL = "https://keepcalling.com/international-calls/";
  private String country;

  public KeepCallingProvider(String country) {
    super(new KeepCallingParser(), new KeepCallingJsonConverter());
    this.country = country;
  }

  @Override
  public String getFares()
      throws IOException, PhoneRatesParseException, PhoneRatesConversionException {
    String result = HttpRequest.sendGet(URL + country);
    String parsedString = getParser().parse(result);
    return getConverter().convert(parsedString);
  }
}
