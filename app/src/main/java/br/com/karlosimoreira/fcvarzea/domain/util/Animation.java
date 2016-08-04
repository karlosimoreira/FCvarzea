package br.com.karlosimoreira.fcvarzea.domain.util;

import android.view.View;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

/**
 * Created by Carlos on 04/08/2016.
 */
public class Animation {

    public  static void setAnimation(View v, int dutation , Techniques effects){
        try {
            YoYo.with(effects)
                    .duration(dutation)
                    .playOn(v);
        }catch (Exception e){

        }
    }
}
