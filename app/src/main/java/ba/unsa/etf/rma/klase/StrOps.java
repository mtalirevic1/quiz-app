package ba.unsa.etf.rma.klase;

public class StrOps {

    public static String ArrayToString(String[] array) {
        String str = "";
        for (int i = 0; i < array.length; i++) {
            str = str + array[i];
            if (i < array.length - 1) {
                str = str + ",";
            }
        }
        return str;
    }

    public static String[] StringToArray(String str) {
        return str.split(",");
    }
}
