package rocks.appconcept.rebtel.util;

/**
 * Created by imeta on 09-Apr-17.
 */
public class StringUtil {

  public static String uppercaseFirstChar(String input) {
    return input.substring(0, 1).toUpperCase() + input.substring(1);
  }

  public static int getIndexOfFirstChar(String str) {
    int i = 0;
    for(Character c: str.trim().toCharArray()){
      try{
        if(!isIgnoredChar(c)) {
          Double.parseDouble(c.toString());
        }
        i++;
      } catch (Exception e){
        return i;
      }
    }
    return i;
  }

  public static boolean isIgnoredChar(Character c) {
    if(c == '.') {
      return true;
    }
    return false;
  }
}
