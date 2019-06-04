package app.whatsdone.android.ui.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import app.whatsdone.android.R;

// TODO: check if this class is a duplicate of PhoneNoVerificationActivity and remove
public class CodeVerificationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_code_verification);
    }
}
