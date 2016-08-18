package br.com.karlosimoreira.fcvarzea.domain.util;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

/**
 * Created by Carlos on 12/08/2016.
 */
public class Uteis  {

    public static int calculaIdade(String dataNasc, String pattern){

        DateFormat sdf = new SimpleDateFormat(pattern, Locale.ENGLISH);

        Date dataNascInput = null;

        try {

            dataNascInput = sdf.parse(dataNasc);


        } catch (Exception e) {}

        Calendar dateOfBirth = new GregorianCalendar();

        dateOfBirth.setTime(dataNascInput);

        // Cria um objeto calendar com a data atual

        Calendar today = Calendar.getInstance();

        // Obtém a idade baseado no ano

        int age = today.get(Calendar.YEAR) - dateOfBirth.get(Calendar.YEAR);

        dateOfBirth.add(Calendar.YEAR, age);

        if (today.before(dateOfBirth)) {
            age--;
        }
        return age;

    }

}
