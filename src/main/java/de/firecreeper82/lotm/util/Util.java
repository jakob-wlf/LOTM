package de.firecreeper82.lotm.util;

public class Util {

    @SuppressWarnings("unused")
    public static boolean isInteger(String s) {
        try {
            int ignored = Integer.parseInt(s);
            return true;
        }
        catch (NumberFormatException e) {
            return false;
        }
    }

    //return -1 if string is not an integer
    public static Integer parseInt(String s) {
        try {
            return Integer.parseInt(s);
        }
        catch (NumberFormatException exception) {
            throw new NumberFormatException();
        }
    }
}
