package app.whatsdone.android.ui.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import app.whatsdone.android.R;
import app.whatsdone.android.databinding.ActivityCodeVerificationBinding;
import app.whatsdone.android.ui.presenter.CodeVerifyPresenter;
import app.whatsdone.android.ui.presenter.CodeVerifyPresenterImpl;
import app.whatsdone.android.ui.view.CodeVerifyView;
import app.whatsdone.android.ui.viewmodel.CodeVerificationViewModel;
import app.whatsdone.android.utils.Constants;

// TODO: check if this class is a duplicate of PhoneNoVerificationActivity and remove
public class CodeVerificationActivity extends AppCompatActivity implements CodeVerifyView {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityCodeVerificationBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_code_verification);
        CodeVerificationViewModel model = new CodeVerificationViewModel();
        model.setCode("");
        CodeVerifyPresenter presenter = new CodeVerifyPresenterImpl();
        presenter.setContext(this);
        presenter.init(this, getIntent().getStringExtra(Constants.ARG_VERIFICATION_ID));
        binding.setPresenter(presenter);
        binding.setViewModel(model);
    }

    @Override
    public void onVerified() {
        Intent intent = new Intent(CodeVerificationActivity.this,ProfileCreationActivity.class);
        startActivity(intent);
        finish();
    }
}
