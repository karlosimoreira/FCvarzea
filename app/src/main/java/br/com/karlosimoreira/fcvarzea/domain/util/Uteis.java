package br.com.karlosimoreira.fcvarzea.domain.util;

import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Created by Carlos on 12/08/2016.
 */
public class Uteis  {


    public static int calcularIdade(int ano){
        int idade = 0;
        int anoCorrente;
        Calendar cal = GregorianCalendar.getInstance();
        anoCorrente = cal.get(Calendar.YEAR);
        idade = anoCorrente - ano;
        return idade;
    }

}
