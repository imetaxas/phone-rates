package rocks.appconcept.rebtel.util;

import junit.framework.TestCase;

/**
 * Created by imeta on 18-Jun-17.
 */
public class StringUtilTest extends TestCase {

  public void testUppercaseFirstChar() throws Exception {

  }

  public void testGetIndexOfFirstChar() throws Exception {
    String input = "1.79&nbsp;kr";
    assertEquals(StringUtil.getIndexOfFirstChar(input), 4);

  }

}