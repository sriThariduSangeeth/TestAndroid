package app.whatsdone.android.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;

import app.whatsdone.android.R;


public class PhoneNoVerificationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_no_verification);


        final int maxLength = 7;
        final EditText phoneNumber = (EditText)findViewById(R.id.phoneNumber);
        final Button nextButton = (Button)findViewById(R.id.Nextbutton);
        nextButton.setEnabled(false);


        Locale[] locale = Locale.getAvailableLocales();
        ArrayList<String> countries = new ArrayList<String>();
        String country;
        for( Locale loc : locale ){
            country = loc.getDisplayCountry();
            if( country.length() > 0 && !countries.contains(country) ){
                countries.add( country );
            }
        }
        Collections.sort(countries, String.CASE_INSENSITIVE_ORDER);

        Spinner citizenship = (Spinner)findViewById(R.id.spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, countries);
        citizenship.setAdapter(adapter);


        phoneNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.toString().length()<maxLength)
                {
                    nextButton.setEnabled(false);
                }else
                {
                    nextButton.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(phoneNumber.getText().toString().length()>maxLength)
                {
                    Toast.makeText(getApplicationContext(),"Phone Number cannot be more than 7 Digits",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    startActivity(new Intent(PhoneNoVerificationActivity.this,CodeVerificationActivity.class));
                }
            }
        });
    }


}
