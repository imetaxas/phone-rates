package rocks.appconcept.rebtel.provider;

import java.io.IOException;
import rocks.appconcept.rebtel.converter.VonageJsonCorverter;
import rocks.appconcept.rebtel.exception.PhoneRatesConversionException;
import rocks.appconcept.rebtel.exception.PhoneRatesParseException;
import rocks.appconcept.rebtel.parser.VonageParser;
import rocks.appconcept.rebtel.util.HttpRequest;
import rocks.appconcept.rebtel.util.StringUtil;

/**
 * Created by imeta on 08-Apr-17.
 */
public class VonageProvider extends AbstractProvider {

  public static final String NAME = "Vonage";
  public static final String IMG_URL = "https://www.vonage.co.uk/img/logo/logo.svg";
  private static final String URL = "https://www.vonage.co.uk/inc/ajax/international_rates.php";
  private String country;

  public VonageProvider(String country) {
    super(new VonageParser(), new VonageJsonCorverter());
    this.country = country;
  }

  @Override
  public String getFares()
      throws IOException, PhoneRatesParseException, PhoneRatesConversionException {
    String result = HttpRequest.sendPost(URL, country);
    String parsedString = getParser().parse(result);
    return getConverter().convert(parsedString);
  }
}
