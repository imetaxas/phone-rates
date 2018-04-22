package rocks.appconcept.rebtel.provider;

import java.io.IOException;
import rocks.appconcept.rebtel.converter.LycaMobileJsonConverter;
import rocks.appconcept.rebtel.exception.PhoneRatesConversionException;
import rocks.appconcept.rebtel.exception.PhoneRatesParseException;
import rocks.appconcept.rebtel.parser.LycaMobileParser;
import rocks.appconcept.rebtel.util.HttpRequest;

/**
 * Created by imeta on 09-Apr-17 .
 */
public class LycaMobileProvider extends AbstractProvider {

  public static final String NAME = "LycaMobile";
  public static final String IMG_URL = "http://www.lycamobile.se/templates/template2/images/logo.gif";

  private static final String url1 = "http://www.lycamobile.se/templates/template2/designs/hdlGetCountrySearch.ashx?doAction=GetCountrySearch&languagetype=-1&language=3&request=Y";
  private static final String url2 = "http://www.lycamobile.se/en/../templates/Template2/designs/hdlGetRates.ashx?doAction=GetNewInternationalRates&languagetype=-1&language=3&country=";
  private String country;

  public LycaMobileProvider(String country) {
    super(new LycaMobileParser(), new LycaMobileJsonConverter());
    this.country = country;
  }

  @Override
  public String getFares()
      throws IOException, PhoneRatesParseException, PhoneRatesConversionException {
    String result = HttpRequest.sendGet(url1);
    String countryId = ((LycaMobileParser) getParser()).parseFromUrl1(result, country);
    result = HttpRequest.sendGet(url2 + countryId);
    String parsedString = getParser().parse(result);
    return getConverter().convert(parsedString);
  }
}
