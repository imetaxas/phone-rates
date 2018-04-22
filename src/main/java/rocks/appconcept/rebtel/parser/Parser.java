package rocks.appconcept.rebtel.parser;

import rocks.appconcept.rebtel.exception.PhoneRatesParseException;

/**
 * Created by imeta on 08-Apr-17.
 */
public interface Parser {

  String parse(String stringToParse) throws PhoneRatesParseException;

  String validate(String stringToParse);
}
