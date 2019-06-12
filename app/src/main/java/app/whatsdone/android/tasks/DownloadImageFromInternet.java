package app.whatsdone.android.tasks;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import java.io.InputStream;

import timber.log.Timber;

public class DownloadImageFromInternet extends AsyncTask<String, Void, Bitmap> {
    private ImageView imageView;

//    public DownloadImageFromInternet(ImageView imageView) {
//        this.imageView = imageView;
//    }

    protected Bitmap doInBackground(String... urls) {
        String imageURL = urls[0];
        Bitmap bimage = null;
        try {
            InputStream in = new java.net.URL(imageURL).openStream();
            bimage = BitmapFactory.decodeStream(in);

        } catch (Exception e) {
            Timber.e(e);
            e.printStackTrace();
        }
        return bimage;
    }

    protected void onPostExecute(Bitmap result) {
        imageView.setImageBitmap(result);
        Thread.interrupted();
    }
}

