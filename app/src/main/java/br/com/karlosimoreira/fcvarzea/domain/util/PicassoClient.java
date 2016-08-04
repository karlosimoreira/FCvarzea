package br.com.karlosimoreira.fcvarzea.domain.util;

import android.content.Context;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import br.com.karlosimoreira.fcvarzea.R;

/**
 * Created by Carlos on 17/06/2016.
 */
public class PicassoClient {

    public static void downloadImage(Context c, String url, ImageView img)
    {
        if(url != null && url.length()>0)
        {
            Picasso.with(c).load(url).placeholder(R.drawable.placeholder).into(img);
        }else {
            Picasso.with(c).load(R.drawable.placeholder).into(img);
        }
    }

}
