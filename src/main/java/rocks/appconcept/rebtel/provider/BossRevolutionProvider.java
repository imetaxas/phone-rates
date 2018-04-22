package rocks.appconcept.rebtel.provider;

import java.io.IOException;
import rocks.appconcept.rebtel.converter.BossRevolutionJsonConverter;
import rocks.appconcept.rebtel.exception.PhoneRatesConversionException;
import rocks.appconcept.rebtel.exception.PhoneRatesParseException;
import rocks.appconcept.rebtel.parser.BossRevolutionHtmlParser;
import rocks.appconcept.rebtel.util.HttpRequest;

/**
 * Created by imeta on 08-Apr-17.
 */
public class BossRevolutionProvider extends AbstractProvider {

  public static final String NAME = "BossRevolution";
  public static final String URL = "https://www.bossrevolution.com/en-us/rates/";
  public static final String IMG_URL = "https://www.bossrevolution.com/Content/img/logo-bossrevolution.png";
  private String country;

  public BossRevolutionProvider(String country) {
    super(new BossRevolutionHtmlParser(), new BossRevolutionJsonConverter());
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
