package rocks.appconcept.rebtel.converter;

import rocks.appconcept.rebtel.exception.PhoneRatesConversionException;

/**
 * Created by imeta on 07-May-17.
 */
public interface Converter {

  String convert(String stringToConvert) throws PhoneRatesConversionException;
}
