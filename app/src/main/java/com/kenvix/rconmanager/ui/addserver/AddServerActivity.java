package com.kenvix.rconmanager.ui.addserver;

import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.kenvix.rconmanager.R;
import com.kenvix.rconmanager.ui.base.BaseActivity;
import com.kenvix.rconmanager.utils.Invoker;
import com.kenvix.utils.annotation.form.FormNotEmpty;
import com.kenvix.utils.annotation.ViewAutoLoad;

public class AddServerActivity extends BaseActivity {
    private final String tag = "AddServerActivity";

    @ViewAutoLoad @FormNotEmpty(tag) public EditText addServerName;
    @ViewAutoLoad @FormNotEmpty(tag) public EditText addServerHost;
    @ViewAutoLoad @FormNotEmpty(tag) public EditText addServerPort;
    @ViewAutoLoad                    public EditText addServerPassword;
    @ViewAutoLoad                    public Button   addServerSubmit;
    @ViewAutoLoad                    public Button   addServerTest;
    @ViewAutoLoad                    public Toolbar  addServerToolbar;

    @Override
    protected void initializeElements() {
        setSupportActionBar(addServerToolbar);

        addServerToolbar.setNavigationOnClickListener(view -> finish());

        addServerSubmit.setOnClickListener(this::onServerFormSubmit);
        addServerTest.setOnClickListener(this::onServerFormTest);
    }

    public void test(String promptText) {
        //EditText fuck = findViewById()
        //if(target.addServerName.getText().toString().isEmpty())
        //    target.addServerName.setError("fuck you");
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_add_server;
    }

    private void checkForm(View view) {
        Invoker.invokeFormChecker(tag, this);
    }

    private void onServerFormTest(View view) {
        checkForm(view);
    }

    private void onServerFormSubmit(View view) {
        checkForm(view);
    }
}