package br.com.karlosimoreira.fcvarzea.domain.util;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

/**
 * Created by Carlos on 12/08/2016.
 */
public class Uteis  {


    public static int calcularIdade(String date){
        int idade = 0;
        int anoCorrente;
        Calendar cal = GregorianCalendar.getInstance();
        anoCorrente = cal.get(Calendar.YEAR);
        idade = anoCorrente - getYear(date);
        return idade;
    }

    public static int getYear(String date){
        int nDate;
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
        Date myDate = null;
        try {
            myDate = dateFormat.parse(date);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        assert myDate != null;
        nDate = myDate.getYear();

        return nDate;
    }

}
