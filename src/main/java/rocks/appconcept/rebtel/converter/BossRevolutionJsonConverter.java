package rocks.appconcept.rebtel.converter;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import rocks.appconcept.rebtel.exception.PhoneRatesConversionException;
import rocks.appconcept.rebtel.provider.BossRevolutionProvider;

/**
 * Created by imeta on 29-Apr-17.
 *
 * <html>
 * <head></head>
 * <body>
 * <img src="https://www.bossrevolution.com/Content/img/logo-bossrevolution.png">
 * <table>
 * <tbody>
 * <tr>
 * <td>Amount:&nbsp;&nbsp;&nbsp;$3</td>
 * </tr>
 * <tr>
 * <td>Greece</td>
 * <td>2¢</td>
 * <td>150</td>
 * <td></td>
 * </tr>
 * <tr>
 * <td>Greece Cellular</td>
 * <td>7.1¢</td>
 * <td>42</td>
 * <td></td>
 * </tr>
 * <tr>
 * <td>Amount:&nbsp;&nbsp;&nbsp;$5</td>
 * </tr>
 * <tr>
 * <td>Greece</td>
 * <td>2¢</td>
 * <td>250</td>
 * <td></td>
 * </tr>
 * <tr>
 * <td>Greece Cellular</td>
 * <td>7.1¢</td>
 * <td>70</td>
 * <td></td>
 * </tr>
 * <tr>
 * <td>Amount:&nbsp;&nbsp;&nbsp;$10</td>
 * </tr>
 * <tr>
 * <td>Greece</td>
 * <td>2¢</td>
 * <td>500</td>
 * <td></td>
 * </tr>
 * <tr>
 * <td>Greece Cellular</td>
 * <td>7.1¢</td>
 * <td>140</td>
 * <td></td>
 * </tr>
 * <tr>
 * <td>Amount:&nbsp;&nbsp;&nbsp;$20</td>
 * </tr>
 * <tr>
 * <td>Greece</td>
 * <td>2¢</td>
 * <td>1000</td>
 * <td></td>
 * </tr>
 * <tr>
 * <td>Greece Cellular</td>
 * <td>7.1¢</td>
 * <td>281</td>
 * <td></td>
 * </tr>
 * </tbody>
 * </table>
 * </body>
 * </html>
 */
public class BossRevolutionJsonConverter extends JsonConverter {


  @Override
  public String convert(String stringToConvert) throws PhoneRatesConversionException {
    try {
      Document doc = Jsoup.parse(stringToConvert);

      Elements trs = doc.select("tr");

      putToJson(NAME, BossRevolutionProvider.NAME);
      putToJson(LOGO, BossRevolutionProvider.IMG_URL);
      putToJson(PHONE, trs.get(1).select("td").get(1).text());
      putToJson(CELL, trs.get(2).select("td").get(1).text());
    } catch (Exception e) {
      throw new PhoneRatesConversionException(e);
    }
    return getValidatedJson();
  }
}
