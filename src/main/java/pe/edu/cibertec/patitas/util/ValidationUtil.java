package pe.edu.cibertec.patitas.util;

public class ValidationUtil {

    public static boolean isValid(String cadena) {
        if (cadena != null && cadena.length() > 0) {
            return true;
        }
        return false;
    }

}
