package rocks.appconcept.rebtel.provider;

import rocks.appconcept.rebtel.converter.Converter;
import rocks.appconcept.rebtel.parser.Parser;

/**
 * Created by imeta on 08-Apr-17.
 */
public abstract class AbstractProvider implements Provider {

  private Parser parser;
  private Converter converter;

  protected AbstractProvider(Parser parser, Converter converter) {
    this.parser = parser;
    this.converter = converter;
  }

  Parser getParser() {
    return parser;
  }

  public Converter getConverter() {
    return converter;
  }

}
