package rocks.appconcept.rebtel.provider;

import java.io.IOException;
import rocks.appconcept.rebtel.exception.PhoneRatesConversionException;
import rocks.appconcept.rebtel.exception.PhoneRatesParseException;

/**
 * Created by imeta on 07-May-17.
 */
public interface Provider {

  String getFares() throws IOException, PhoneRatesParseException, PhoneRatesConversionException;
}
