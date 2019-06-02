package app.whatsdone.android.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import app.whatsdone.android.R;

// TODO: check if this class is a duplicate of PhoneNoVerificationActivity and remove
public class CodeVerificationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_code_verification);

        final int maxLength = 6;
        final EditText code = (EditText)findViewById(R.id.editText_Code);
        Button verify = (Button)findViewById(R.id.button_verify);

        verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(code.getText().toString().length() == maxLength)
                {
                    Intent i = new Intent(CodeVerificationActivity.this,ProfileCreationActivity.class);
                    startActivity(i);
                }
                else
                {
                    Toast.makeText(getApplicationContext(),"Verification Code has to be 6 Digits",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
