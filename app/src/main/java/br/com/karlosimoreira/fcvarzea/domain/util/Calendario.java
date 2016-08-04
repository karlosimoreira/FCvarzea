package br.com.karlosimoreira.fcvarzea.domain.util;

import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;


import java.util.LinkedList;
import java.util.List;
import java.util.Calendar;

/**
 * Created by Carlos on 05/07/2016.
 */
public class Calendario extends AppCompatActivity implements DatePickerDialog.OnDateSetListener,  DialogInterface.OnCancelListener {

    public static int year;
    public static int month;
    public static int day;
    public static int hour;
    public static int minute;


    public Calendario(int year, int month, int day, int hour, int minute) {
        this.year = year;
        this.month = month;
        this.day = day;
        this.hour = hour;
        this.minute = minute;
    }

    public Calendario(int year, int month, int day) {
        this.year = year;
        this.month = month;
        this.day = day;
    }

    public Calendario() {}


    public void selectBirthDate(){

        initDateTimeData();
        Calendar cDefault = Calendar.getInstance();
        cDefault.set(year, month, day);

        DatePickerDialog datePickerDialog = DatePickerDialog.newInstance(
                this,
                cDefault.get(Calendar.YEAR),
                cDefault.get(Calendar.MONTH),
                cDefault.get(Calendar.DAY_OF_MONTH)
        );

        Calendar  cMin = Calendar.getInstance();
        Calendar cMax = Calendar.getInstance();
        cMin.set(1930, 11, 31 );
        cMax.set( cMax.get(Calendar.YEAR), 11, 31 );
        datePickerDialog.setMinDate(cMin);
        datePickerDialog.setMaxDate(cMax);

        List<Calendar> daysList = new LinkedList<>();
        Calendar[] daysArray;
        Calendar cAux = Calendar.getInstance();

        while( cAux.getTimeInMillis() <= cMax.getTimeInMillis() ){
            if( cAux.get( Calendar.DAY_OF_WEEK ) >= 1 && cAux.get( Calendar.DAY_OF_WEEK ) <= 7 ){
                Calendar c = Calendar.getInstance();
                c.setTimeInMillis( cAux.getTimeInMillis() );

                daysList.add( c );
            }
            cAux.setTimeInMillis( cAux.getTimeInMillis() + ( 24 * 60 * 60 * 1000 ) );
        }
        daysArray = new Calendar[ daysList.size() ];
        for( int i = 0; i < daysArray.length; i++ ){
            daysArray[i] = daysList.get(i);
        }

        datePickerDialog.setSelectableDays( daysArray );
        datePickerDialog.setOnCancelListener(this);
        datePickerDialog.show( getFragmentManager(), "DatePickerDialog" );
    }

    public static void initDateTimeData(){
        if( year == 0 ){
            Calendar c = Calendar.getInstance();
            year = c.get(Calendar.YEAR);
            month = c.get(Calendar.MONTH);
            day = c.get(Calendar.DAY_OF_MONTH);
            hour = c.get(Calendar.HOUR_OF_DAY);
            minute = c.get(Calendar.MINUTE);
        }
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        year = month = day = hour = minute = 0;
    }

    @Override
    public void onDateSet(DatePickerDialog datePickerDialog, int i, int i1, int i2) {
        Calendar tDefault = Calendar.getInstance();
        tDefault.set(year, month, day, hour, minute);

        year = i;
        month = i1;
        day = i2;
    }

    public void selectBirthDateHora(View view){
        Calendar tDefault = Calendar.getInstance();
        tDefault.set(year, month, day, hour, minute);
        TimePickerDialog timePickerDialog = TimePickerDialog.newInstance(
                (TimePickerDialog.OnTimeSetListener) Calendario.this,
                tDefault.get(Calendar.HOUR_OF_DAY),
                tDefault.get(Calendar.MINUTE),
                true
        );
        timePickerDialog.setOnCancelListener(this);
        timePickerDialog.show(getFragmentManager(), "timePickerDialog");
        timePickerDialog.setTitle("Horário");

        timePickerDialog.setThemeDark(true);
    }

    public String setTextDate(){
        String data = (day < 10 ? "0"+day : day)+"/"+
                (month+1 < 10 ? "0"+(month+1) : month+1)+"/"+ year;
        return data;
    }
}
