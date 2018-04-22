package rocks.appconcept.rebtel.converter;

import org.json.simple.JSONObject;

/**
 * Created by imeta on 29-Apr-17.
 */
public abstract class JsonConverter implements Converter {

  private JSONObject jsonObject;

  protected static final String NAME = "name";
  protected static final String LOGO = "logo";
  protected static final String PHONE = "phone";
  protected static final String CELL = "cell";

  public JsonConverter() {
    jsonObject = new JSONObject();
  }

  @SuppressWarnings("unchecked")
  protected void putToJson(String key, String value) {
    jsonObject.put(key, value);
  }

  protected String getValidatedJson() {
    return jsonObject.toJSONString();
  }
}
