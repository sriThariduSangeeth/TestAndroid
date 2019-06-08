package app.whatsdone.android.ui.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.hbb20.CountryCodePicker;

import app.whatsdone.android.R;
import app.whatsdone.android.databinding.ActivityLogingScreenBinding;
import app.whatsdone.android.databinding.ActivityPhoneNoVerificationBinding;
import app.whatsdone.android.ui.presenter.LoginPresenter;
import app.whatsdone.android.ui.presenter.LoginPresenterImpl;
import app.whatsdone.android.ui.view.LoginView;
import app.whatsdone.android.utils.Constants;
import app.whatsdone.android.ui.viewmodel.LoginViewModel;

public class LoginActivity extends AppCompatActivity implements LoginView {

    private CountryCodePicker CCP;
    private EditText txtNumber;
    private Button nextButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityPhoneNoVerificationBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_phone_no_verification);
        LoginPresenter presenter = new LoginPresenterImpl(LoginActivity.this, LoginActivity.this);
        LoginViewModel model = new LoginViewModel();
        binding.setPresenter(presenter);
        binding.setModel(model);

        txtNumber = (EditText)findViewById(R.id.phoneNumber);
        nextButton = (Button)findViewById(R.id.nextButton);

        CCP = (CountryCodePicker)findViewById(R.id.countryCodePicker);
        model.setCountryCode("+"+CCP.getSelectedCountryCode());
        CCP.setOnCountryChangeListener(() -> model.setCountryCode(CCP.getSelectedCountryCodeWithPlus()));

    }

    @Override
    public void onCodeSent(String verificationId) {
        Intent intent = new Intent(LoginActivity.this,CodeVerificationActivity.class);
        intent.putExtra(Constants.ARG_VERIFICATION_ID, verificationId);
        startActivity(intent);
    }

    @Override
    public void onVerificationCompleted(String token) {
        Intent intent = new Intent(LoginActivity.this,ProfileCreationActivity.class);
        intent.putExtra(Constants.ARG_VERIFICATION_ID, token);
        startActivity(intent);
    }

    @Override
    public void onValidationFailed() {

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Error");
            builder.setMessage("Please enter a valid Phone Number");

            builder.setPositiveButton("OK",null);
            AlertDialog dialog = builder.create();
            dialog.show();

    }
    public void disableButton()
    {
        nextButton.setEnabled(false);
        nextButton.setBackgroundColor(Color.GRAY);
        nextButton.setTextColor(0xFFFFFFFF);
    }
}
