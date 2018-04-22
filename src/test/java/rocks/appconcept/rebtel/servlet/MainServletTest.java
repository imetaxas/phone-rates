package rocks.appconcept.rebtel.servlet;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Collections;
import junit.framework.TestCase;
import org.apache.commons.io.IOUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import rocks.appconcept.rebtel.util.SortJsonOnPhoneComparator;
import rocks.appconcept.rebtel.util.StringUtil;

/**
 * Created by imeta on 18-Jun-17.
 */
public class MainServletTest extends TestCase {

  public void setUp() throws Exception {

  }

  public void testSortJSONObject() throws Exception {
    InputStream is = MainServletTest.class.getClassLoader().getResourceAsStream("fares.json");
    Reader reader = new InputStreamReader(is, "UTF-8");

    JSONParser parser = new JSONParser();
    JSONArray jsonArray = (JSONArray) parser.parse(IOUtils.toString(reader));

    System.out.println("jsonFares=" + jsonArray.toJSONString());
    Collections.sort(jsonArray, new SortJsonOnPhoneComparator("phone"));

    System.out.println("Sorted jsonFares=" + jsonArray.toJSONString());

    assertTrue(extractPhonePrice(jsonArray, 0, "phone") < extractPhonePrice(jsonArray, 1, "phone"));
  }

  private static Double extractPhonePrice(JSONArray jsonArray, int index, String key) {
    String phonePrice = ((JSONObject)jsonArray.get(index)).get(key).toString();
    return Double.parseDouble(phonePrice.substring(0, StringUtil.getIndexOfFirstChar(phonePrice)));
  }
}