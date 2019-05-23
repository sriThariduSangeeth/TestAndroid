package whatsdone.app.whatsdone;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;

import whatsdone.app.whatsdone.model.Group;

public class CreateNewGroupActivity extends AppCompatActivity {

    private EditText groupNameEditText;
    private Button createGroupButton;
    private List<Group> groupList = new ArrayList<>();
    private String group;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_group);

        groupNameEditText = findViewById(R.id.group_name_edit_text);
        createGroupButton = findViewById(R.id.create_new_group_btn);


        findViewById(R.id.create_new_group_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //add to group_recycler_view
            }
        });
    }
}
