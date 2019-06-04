package app.whatsdone.android.ui.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.res.Resources;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import java.io.InputStream;
import java.util.ArrayList;

import app.whatsdone.android.R;
import app.whatsdone.android.databinding.ActivityLogingScreenBinding;
import app.whatsdone.android.tasks.DownloadImageFromInternet;
import app.whatsdone.android.ui.adapters.CountryListAdapter;
import app.whatsdone.android.ui.presenter.LoginPresenter;
import app.whatsdone.android.ui.presenter.LoginPresenterImpl;
import app.whatsdone.android.utils.ReadCountryJson;
import app.whatsdone.android.viewmodel.LoginViewModel;

import static app.whatsdone.android.utils.ReadCountryJson.countyArray;

public class LoginActivity extends Activity {

    private Animation uptodown, downtoup, logopopup;
    private ImageView appIcon , downArrow , flagImg;
    private Bitmap mIcon_val;
    private EditText selectCountry ,txt_cou_logo_selection , countryCode;
    private ListView list;
    private ArrayList arrayList;
    private ArrayAdapter adapter;
    private Dialog myDialog;
    private String dial_code , country , telephone;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_loging_screen);

        ActivityLogingScreenBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_loging_screen);
        LoginPresenter presenter = new LoginPresenterImpl();
        ((LoginPresenterImpl) presenter).setContext(LoginActivity.this);
        binding.setPresenter(presenter);
        binding.setModel(new LoginViewModel());

        appIcon = (ImageView) findViewById(R.id.logoView);
        flagImg = (ImageView) findViewById(R.id.img_cou_logo_selection);
        uptodown = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.uptodowm);
        logopopup = (Animation) AnimationUtils.loadAnimation(getApplicationContext(), R.anim.logopopup);
        downArrow = (ImageView) findViewById(R.id.arrow_down);
        selectCountry = (EditText) findViewById(R.id.txt_cou_logo_selection);
        countryCode = (EditText) findViewById(R.id.txt_cou_dial_code);
        appIcon.setAnimation(logopopup);
        Resources res = getResources();
        InputStream inList = res.openRawResource(R.raw.country_codes);
        ReadCountryJson readCountryJson = ReadCountryJson.getInstance();
        readCountryJson.getStream(inList);

    }



    public void ShowpopupCountyList (View v){

        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.country_list_view);
        list = (ListView) dialog.findViewById(R.id.count_list);
        CountryListAdapter countyListAdapter = new CountryListAdapter(this, countyArray);
        list.setAdapter(countyListAdapter);


        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                dial_code = countyArray.get(position).getDialCode();
                country = countyArray.get(position).getName();
                selectCountry.setText(country);
                String[] getCode = dial_code.split("\\+");
                countryCode.setText(getCode[1]);
                String url = "http://flagpedia.net/data/flags/normal/"+countyArray.get(position).getCountycode().toLowerCase()+".png";
                //flagImg.setImageBitmap(mIcon_val);
                new DownloadImageFromInternet((ImageView) findViewById(R.id.img_cou_logo_selection)).execute(url);

                dialog.dismiss();

            }
        });
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }



}
