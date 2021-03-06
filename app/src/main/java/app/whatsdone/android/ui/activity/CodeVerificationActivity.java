package app.whatsdone.android.ui.activity;

import android.content.Intent;
import android.content.pm.ActivityInfo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import android.os.Bundle;

import app.whatsdone.android.R;
import app.whatsdone.android.databinding.ActivityCodeVerificationBinding;
import app.whatsdone.android.services.AuthServiceImpl;
import app.whatsdone.android.ui.presenter.CodeVerifyPresenter;
import app.whatsdone.android.ui.presenter.CodeVerifyPresenterImpl;
import app.whatsdone.android.ui.view.CodeVerifyView;
import app.whatsdone.android.ui.viewmodel.CodeVerificationViewModel;
import app.whatsdone.android.utils.Constants;

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
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    @Override
    public void onVerified() {
        if(AuthServiceImpl.getCurrentUser().getDisplayName().isEmpty()){
            Intent intent = new Intent(CodeVerificationActivity.this,ProfileCreationActivity.class);
            startActivity(intent);
        }else {
            Intent intent = new Intent(CodeVerificationActivity.this,GroupsActivity.class);
            startActivity(intent);
        }
        finish();
    }
}
