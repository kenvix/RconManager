package com.kenvix.rconmanager.ui.addserver;

import android.content.Intent;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kenvix.rconmanager.R;
import com.kenvix.rconmanager.database.dao.ServerModel;
import com.kenvix.rconmanager.ui.base.BaseActivity;
import com.kenvix.rconmanager.utils.Invoker;
import com.kenvix.utils.annotation.form.FormNotEmpty;
import com.kenvix.utils.annotation.ViewAutoLoad;
import com.kenvix.utils.annotation.form.FormNumberLessOrEqual;
import com.kenvix.utils.annotation.form.FormNumberMoreOrEqual;

public class AddServerActivity extends BaseActivity {
    public static final String ParamEditTargetId = "edit_target_id";
    public static final String ParamRequestReload = "request_reload";

    private int editTargetId = -1;
    private ServerModel serverModel;

    @ViewAutoLoad
    @FormNotEmpty
    public EditText addServerName;
    @ViewAutoLoad
    @FormNotEmpty
    public EditText addServerHost;
    @ViewAutoLoad
    @FormNotEmpty
    @FormNumberLessOrEqual(25565)
    @FormNumberMoreOrEqual(1)
    public EditText addServerPort;
    @ViewAutoLoad
    public EditText addServerPassword;
    @ViewAutoLoad
    public Button addServerSubmit;
    @ViewAutoLoad
    public Button addServerTest;
    @ViewAutoLoad
    public Toolbar addServerToolbar;
    @ViewAutoLoad
    public LinearLayout addServerEditMode;
    @ViewAutoLoad
    public TextView addServerEditModeTargetId;

    @Override
    @SuppressWarnings("SetTextI18n")
    protected void initializeElements() {
        editTargetId = getIntent().getIntExtra(ParamEditTargetId, -1);

        setSupportActionBar(addServerToolbar);

        addServerToolbar.setNavigationOnClickListener(view -> finish());

        addServerSubmit.setOnClickListener(this::onServerFormSubmit);
        addServerTest.setOnClickListener(this::onServerFormTest);

        serverModel = new ServerModel(this);

        if (isEditMode()) {
            addServerEditMode.setVisibility(View.VISIBLE);
            addServerEditModeTargetId.setText("#" + String.valueOf(editTargetId));


        } else {
            addServerEditMode.setVisibility(View.INVISIBLE);
        }

    }

    public void test(String promptText) {
        //EditText fuck = findViewById()
        //if(target.addServerName.getText().toString().isEmpty())
        //    target.addServerName.setError("fuck you");
    }

    private boolean isEditMode() {
        return editTargetId > -1;
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_add_server;
    }

    private boolean checkForm(View view) {
        if(!Invoker.invokeFormChecker(this))
            return false;

        int port = Integer.parseInt(addServerPort.getText().toString());
        return port >= 1 && port <= 25565;
    }

    private void onServerFormTest(View view) {
        if(!checkForm(view))
            return;


    }

    private void onServerFormSubmit(View view) {
        if(!checkForm(view))
            return;

        try {
            if (isEditMode()) {

            } else {
                serverModel.add(addServerName.getText().toString(),
                        addServerHost.getText().toString(),
                        Integer.parseInt(addServerPort.getText().toString()),
                        addServerPort.getText().toString());
            }

            Intent intent = new Intent();
            intent.putExtra(ParamEditTargetId, true);
            setResult(RESULT_OK, intent);
            finish();
        } catch (Exception ex) {
            makeExceptionPrompt(ex);
        }
    }
}