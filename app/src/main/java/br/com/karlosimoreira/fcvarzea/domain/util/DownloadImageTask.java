package br.com.karlosimoreira.fcvarzea.domain.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import java.io.InputStream;

/**
 * A simple task that download an image from a given url into an Image View
 * Based on http://stackoverflow.com/a/9288544/967435
 */
public class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
    ImageView bmImage;

    public DownloadImageTask(ImageView bmImage) {
        this.bmImage = bmImage;
    }

    protected Bitmap doInBackground(String... urls) {
        String urldisplay = urls[0];
        Bitmap bitmap = null;
        try {
            InputStream in = new java.net.URL(urldisplay).openStream();
            bitmap = ImagemProcess.getResizedBitmap(BitmapFactory.decodeStream(in), 200, 200);

        } catch (Exception e) {
            Log.i(e.getMessage(), "Error fetching image");
        }
        return bitmap;
    }

    protected void onPostExecute(Bitmap result) {
        bmImage.setImageBitmap(result);
    }

}