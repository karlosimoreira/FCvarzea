package br.com.karlosimoreira.fcvarzea.domain.util;

import android.app.Application;
import android.content.Context;

/**
 * Created by Carlos on 20/07/2016.
 */
public class App extends Application {
    public static Context context;

    @Override public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
    }
}