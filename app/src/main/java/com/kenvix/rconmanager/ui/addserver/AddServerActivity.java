package com.kenvix.rconmanager.ui.addserver;

import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.kenvix.rconmanager.R;
import com.kenvix.rconmanager.ui.base.BaseActivity;
import com.kenvix.utils.annotation.form.FormNotEmpty;
import com.kenvix.utils.annotation.ViewAutoLoad;

public class AddServerActivity extends BaseActivity {
    @ViewAutoLoad @FormNotEmpty private EditText addServerName;
    @ViewAutoLoad @FormNotEmpty private EditText addServerHost;
    @ViewAutoLoad @FormNotEmpty private EditText addServerPort;
    @ViewAutoLoad               private EditText addServerPassword;
    @ViewAutoLoad               private Button   addServerSubmit;
    @ViewAutoLoad               private Button   addServerTest;
    @ViewAutoLoad               private Toolbar  addServerToolbar;

    @Override
    protected void initializeElements() {
        setSupportActionBar(addServerToolbar);

        addServerToolbar.setNavigationOnClickListener(view -> finish());

        addServerSubmit.setOnClickListener(this::onServerFormSubmit);
        addServerTest.setOnClickListener(this::onServerFormTest);
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_add_server;
    }

    private void checkForm(View view) {
        if(addServerName.getText().toString().isEmpty())
            addServerName.setError("fuck you");
    }

    private void onServerFormTest(View view) {
        checkForm(view);
    }

    private void onServerFormSubmit(View view) {
        checkForm(view);
    }
}