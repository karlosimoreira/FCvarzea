package br.com.karlosimoreira.fcvarzea.domain.util;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;

import com.cloudinary.Cloudinary;
import com.cloudinary.Transformation;
import com.cloudinary.android.Utils;
import com.cloudinary.utils.ObjectUtils;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ExecutionException;

import br.com.karlosimoreira.fcvarzea.activitys.Home.Jogador.JogadoresSearchActivity;


/**
 * Created by Carlos on 20/06/2016.
 */
public class ImagemProcess extends AppCompatActivity {

    private static final String CLOUD_NAME = "onedesigner";
    private static final String API_KEY = "378753226363159";
    private static final String API_SECRET = "Pbt4SJ5G0gaxdJPPv7DzjUDugzg";
    public String url_cloudinary ;
    private Target loadtarget;
    Bitmap bitmapCovert;
    Context context;
    private Bitmap b;

    public static Map config = new HashMap();
    public static Cloudinary cloudinary;

    public ImagemProcess() {}

    public ImagemProcess(Context context) {
        this.context = context;
    }

    public static void ConfigCloudinary(){

        config.put("cloud_name", CLOUD_NAME);
        config.put("api_key", API_KEY);
        config.put("api_secret", API_SECRET);

        Log.i("ConfigCloudinary", "Cloudinary config");

        cloudinary = new Cloudinary(config);
    }

    public static void initCloudinary(Context context) {
        cloudinary = new Cloudinary(Utils.cloudinaryUrlFromContext(context));
        Log.i("initCloudinary", "Cloudinary initialized");
    }

    public String uploadCloudinary( String[] pathFile)  {
        try {
            url_cloudinary =  new UploadGenereteUrl().execute(pathFile).get();
            Log.i("uploadCloudinary", "url_cloudinary: " + url_cloudinary);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return url_cloudinary;
    }


    public class UploadGenereteUrl extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {

            String url = null;
            if (params.length > 0)
                try {
                    String public_id = getRandomVariante() + params[1];
                    cloudinary.uploader().upload(params[0], ObjectUtils.asMap("public_id",public_id ));

                    url = cloudinary.url().transformation(new Transformation().width(300).height(300).crop("fill")).generate(public_id);

                    Log.i("doInBackground", "pathFile" + params[0]);
                    Log.i("doInBackground", "url" + url);

                } catch (Exception e) {
                    Log.i("uploadCloudinary", "Upload failed: " + e.getMessage());
                    throw new RuntimeException(e);

                }
            Log.i("doInBackground", "Url: " + url);


            return url;
        }
    }

    private String getRandomVariante(){
        Random random = new Random();
        int variante = random.nextInt(10001);
        String resullt = String.valueOf(variante);

        return resullt ;
    }

    public static Bitmap getResizedBitmap(Bitmap bm, int newHeight, int newWidth) {

        int width = bm.getWidth();
        int height = bm.getHeight();

        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;

        // CREATE A MATRIX FOR THE MANIPULATION

        Matrix matrix = new Matrix();

        // RESIZE THE BIT MAP

        matrix.postScale(scaleWidth, scaleHeight);

        // RECREATE THE NEW BITMAP

        Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, false);

        return resizedBitmap;

    }

    public static int calculateInSampleSize( BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Altura Raw e largura da imagem
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calcula o maior valor inSampleSize que é uma potência de 2 e mantém tanto
            // Altura e largura maior do que a altura e largura desejada.
            while ((halfHeight / inSampleSize) > reqHeight && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId, int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }

    private void setupImage(String Url, int id) {

        Log.i("setupImage","UrlUp: " + Url);
        new DownloadImageTask((ImageView) findViewById(id)).execute(Url);
    }

    public Bitmap convetBitmap(int Id) {
        return bitmapCovert = ImagemProcess.decodeSampledBitmapFromResource(JogadoresSearchActivity.mContext.getResources(), Id, 30, 30);
    }

    public static void downloadImage(Context context, ImageView imageView, String url){
       Picasso picasso = new Picasso.Builder(context).listener(new Picasso.Listener() {
           @Override
           public void onImageLoadFailed(Picasso picasso, Uri uri, Exception exception) {
               exception.printStackTrace();
           }
       }).build();
        picasso.load(url).resize(200, 200).centerCrop().into(imageView);
    }

    public Bitmap loadBitmap(String url) {

        if (loadtarget == null) loadtarget = new Target() {

            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                // do something with the Bitmap
                b = bitmap ;
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {
            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {
            }
        };

        Picasso.with(JogadoresSearchActivity.mContext).load(url).into(loadtarget);
        return b;
    }

}
