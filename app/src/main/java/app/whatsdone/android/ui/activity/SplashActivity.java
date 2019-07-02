package app.whatsdone.android.ui.activity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import app.whatsdone.android.R;
import app.whatsdone.android.utils.ConfigLoader;
import app.whatsdone.android.utils.Constants;
import app.whatsdone.android.utils.SharedPreferencesUtil;
import timber.log.Timber;

public class SplashActivity extends AppCompatActivity {

    private ProgressBar progressBar = null;
    private ImageView bgImage;
    private Animation uptodown, downtoup, logopopup;
    private LinearLayout layoutcenter , layoutWelcome;
    private Handler mHandler;
    private TextView txt1;
    private Button but1;

    public SplashActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        new ConfigLoader().fetch();
        Bundle bundle = getIntent().getExtras();
            if (bundle != null) {
                for (String key : bundle.keySet()) {
                    Object value = bundle.get(key);
                    Timber.d("%s %s (%s", key,
                            value.toString(), value.getClass().getName());
                }

                if(bundle.containsKey(Constants.ARG_TYPE)){
                    String type = bundle.getString(Constants.ARG_TYPE);
                    if(type.equals(Constants.ARG_TASK)){
                        Intent activeIntent = new Intent(getBaseContext(), GroupsActivity.class);
                        activeIntent.putExtra(Constants.ARG_ACTION, Constants.ACTION_VIEW_TASK);
                        startActivity(activeIntent);
                        finish();
                    }
                }
            }


        progressBar = findViewById(R.id.progressBar);
        bgImage = findViewById(R.id.bg_image);
        layoutcenter = findViewById(R.id.layoutCenter);
        layoutWelcome = findViewById(R.id.txtWelcome);
        downtoup = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.downtoup);
        uptodown = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.uptodowm);
        logopopup = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.logopopup);
        txt1 = findViewById(R.id.lbl1);
        but1 = findViewById(R.id.mainbutton);
        txt1.setVisibility(View.GONE);
        but1.setVisibility(View.GONE);
        layoutWelcome.setVisibility(View.GONE);
        mHandler = new Handler();
        StartProgress startProgress = new StartProgress();
        startProgress.execute();


    }

    private class StartProgress extends AsyncTask<String, String, String> {

        float screHeight;
        float screWidth;
        float halfscreehight;
        float halftohalfhight;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            layoutcenter.setAnimation(logopopup);
            progressBar.setVisibility(View.VISIBLE);
            progressBar.setProgress(0);

            DisplayMetrics displayMetrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            screHeight = displayMetrics.heightPixels;
            screWidth = displayMetrics.widthPixels;
            halftohalfhight =(screHeight* 5)/100;
            halfscreehight = halftohalfhight*15;
        }

        @Override
        protected String doInBackground(String... strings) {
            // wait(1000);
            try {
                Thread.sleep(100);
                progressBar.setProgress(25);
                // wait(2000);
                Thread.sleep(100);
                progressBar.setProgress(50);
                //  wait(1500);
                Thread.sleep(100);
                progressBar.setProgress(80);
                Thread.sleep(50);
                progressBar.setProgress(100);
            } catch (InterruptedException e) {
                Thread.interrupted();
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);

        }

        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
        @Override
        protected void onPostExecute(String name) {
            super.onPostExecute(name);

            layoutcenter.animate().setStartDelay(55).translationY(-halftohalfhight*2).scaleX(0.8f).scaleY(0.8f);
            progressBar.animate().setStartDelay(40).translationY(1000).alpha(0);

            if (!SharedPreferencesUtil.getString(Constants.SHARED_TOKEN).equals("")) {
                // change activity to chat screen

                Runnable changeChatWindow = () -> {
                    Intent activeIntent = new Intent(getBaseContext(), GroupsActivity.class);
                    startActivity(activeIntent);
                    finish();

                };

                mHandler.postDelayed(changeChatWindow, 60);

            } else {
                // change activity logging part

                txt1.setVisibility(View.VISIBLE);
                but1.setVisibility(View.VISIBLE);
                layoutWelcome.setVisibility(View.VISIBLE);

                txt1.startAnimation(downtoup);
                but1.startAnimation(downtoup);
                layoutWelcome.setAnimation(downtoup);


                bgImage.animate().setStartDelay(50).translationY(-(halftohalfhight*5)).setDuration(80).withEndAction(new Runnable() {
                    @Override
                    public void run() {

                        final Intent logingIntent = new Intent(getBaseContext(), LoginActivity.class);
                        but1.setOnClickListener(v -> {
                            startActivity(logingIntent);
                            finish();
                        });
                    }
                });

            }

        }

    }

}
