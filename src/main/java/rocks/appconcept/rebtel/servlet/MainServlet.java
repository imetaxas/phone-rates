package rocks.appconcept.rebtel.servlet;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Collections;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import rocks.appconcept.rebtel.provider.AbstractProvider;
import rocks.appconcept.rebtel.provider.BossRevolutionProvider;
import rocks.appconcept.rebtel.provider.KeepCallingProvider;
import rocks.appconcept.rebtel.provider.LycaMobileProvider;
import rocks.appconcept.rebtel.provider.Provider;
import rocks.appconcept.rebtel.provider.VonageProvider;
import rocks.appconcept.rebtel.util.SortJsonOnPhoneComparator;

/**
 * Created by imeta on 18-Mar-17.
 */
public class MainServlet extends HttpServlet implements Servlet {

  private static final Logger logger = Logger.getLogger(MainServlet.class);

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException {
    process(req, resp);
  }

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException {
    process(req, resp);
  }

  private static void process(HttpServletRequest req, HttpServletResponse resp) {
    resp.setContentType("application/json; charset=UTF-8");

    PrintWriter writer = null;
    try {
      BufferedReader br = new BufferedReader(new InputStreamReader(req.getInputStream()));

      JSONParser jsonParser = new JSONParser();
      JSONObject jsonObject = (JSONObject) jsonParser.parse(br.readLine());
      String country = (String) jsonObject.get("name");
      Provider[] providers = initProviders(country);

      writer = resp.getWriter();
      writer.println(getAllFares(providers).toJSONString());
    } catch (Exception e) {
      logger.error("MainServlet servlet error: ", e);
      e.printStackTrace();
    } finally {
      if (writer != null) {
        writer.close();
      }
    }
  }

  private static Provider[] initProviders(String country) {
    return new AbstractProvider[]{
        new BossRevolutionProvider(country),
        new VonageProvider(country),
        new KeepCallingProvider(country),
        new LycaMobileProvider(country)/*,
          new TelboProvider(country)*/
    };
  }

  private static JSONArray getAllFares(Provider[] providers) {
    JSONArray jsonFares = new JSONArray();
    for (Provider provider : providers) {
      getFares(jsonFares, provider);
    }

    Collections.sort(jsonFares, new SortJsonOnPhoneComparator("cell"));
    setLowest(jsonFares, "lowest_cell", "check");
    Collections.sort(jsonFares, new SortJsonOnPhoneComparator("phone"));
    setLowest(jsonFares, "lowest_phone", "check");

    return jsonFares;
  }

  private static void getFares(JSONArray jsonFares, Provider provider) {
    try {
      String fares = provider.getFares();
      jsonFares.add(new JSONParser().parse(fares));
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private static void setLowest(JSONArray jsonFares, String attr, String val) {
    int i = 0;
    for(Object obj: jsonFares) {
      if(i == 0) {
        ((JSONObject) obj).put(attr, val);
      } else {
        ((JSONObject) obj).put(attr, "");
      }
      i++;
    }
  }
}
