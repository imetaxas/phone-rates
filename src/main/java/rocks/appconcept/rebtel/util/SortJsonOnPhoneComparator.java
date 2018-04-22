package rocks.appconcept.rebtel.util;

import java.util.Comparator;
import org.json.simple.JSONObject;

/**
 * Created by imeta on 18-Jun-17.
 */
public class SortJsonOnPhoneComparator implements Comparator<JSONObject> {

  private String attr;

  public SortJsonOnPhoneComparator(String attr) {
    this.attr = attr;
  }

  @Override
  public int compare(JSONObject o1, JSONObject o2) {

    try {

      String phonePriceStr1 = o1.get(attr).toString();
      String phonePriceStr2 = o2.get(attr).toString();
      int index = StringUtil.getIndexOfFirstChar(phonePriceStr1);
      Double phonePrice1 = Double.parseDouble(phonePriceStr1.substring(0, index).trim());

      index = StringUtil.getIndexOfFirstChar(phonePriceStr2);
      Double phonePrice2 = Double.parseDouble(phonePriceStr2.substring(0, index).trim());

      return phonePrice1 > phonePrice2 ? 1 : (phonePrice1 < phonePrice2 ? -1 : 0);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return 0;
  }
}
