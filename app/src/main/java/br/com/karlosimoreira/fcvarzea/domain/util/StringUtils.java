package br.com.karlosimoreira.fcvarzea.domain.util;

/**
 * Created by Carlos on 01/08/2016.
 */
public class StringUtils {
    // Verifica se a String é null ou vazia ou só tem espaços em branco
    public static boolean isNullOrBlank(String s) {
        return (s == null || s.trim().equals(""));
    }

    // Verifica se a String é null ou vazia
    // Pode ser utilizado como suporte em APIs menores que 9 do android onde não está disponivel o metódo de String isEmpty()
    public static boolean isNullOrEmpty(String s) {
        return (s == null || s.equals(""));
    }

    public static int subString(String mString){
        int result = Integer.parseInt(mString.substring(6));
        return result;
    }
}
