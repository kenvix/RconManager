package com.kenvix.rconmanager.ui.addserver;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kenvix.rconmanager.R;
import com.kenvix.rconmanager.database.dao.ServerModel;
import com.kenvix.rconmanager.ui.base.BaseActivity;
import com.kenvix.rconmanager.ui.main.MainActivity;
import com.kenvix.rconmanager.utils.Invoker;
import com.kenvix.utils.annotation.ErrorPrompt;
import com.kenvix.utils.annotation.ViewAutoLoad;
import com.kenvix.utils.annotation.form.FormNotEmpty;
import com.kenvix.utils.annotation.form.FormNumberLessOrEqual;
import com.kenvix.utils.annotation.form.FormNumberMoreOrEqual;

public class AddServerActivity extends BaseActivity {
    public static final int ActivityRequestCode = 0xac00;
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
    @FormNumberLessOrEqual(65535)
    @FormNumberMoreOrEqual(1)
    @ErrorPrompt("Invalid port number")
    public EditText addServerPort;
    @ViewAutoLoad
    public EditText addServerPassword;
    @ViewAutoLoad
    public Button addServerSubmit;
    @ViewAutoLoad
    public Toolbar addServerToolbar;
    @ViewAutoLoad
    public LinearLayout addServerEditMode;
    @ViewAutoLoad
    public TextView addServerEditModeTargetId;

    @Override
    @SuppressWarnings("SetTextI18n")
    protected void onInitialize() {
        editTargetId = getIntent().getIntExtra(ParamEditTargetId, -1);

        setSupportActionBar(addServerToolbar);

        addServerToolbar.setNavigationOnClickListener(view -> finish());

        addServerSubmit.setOnClickListener(this::onServerFormSubmit);

        serverModel = new ServerModel(this);

        if (isEditMode()) {
            addServerEditMode.setVisibility(View.VISIBLE);
            addServerEditModeTargetId.setText("#" + String.valueOf(editTargetId));

            try (Cursor currentData = serverModel.getBySid(editTargetId)) {

                if (currentData.getCount() <= 0)
                    throw new IllegalArgumentException(getString(R.string.error_invalid_sid) + editTargetId);

                addServerPort.setText(currentData.getString(currentData.getColumnIndexOrThrow(ServerModel.FieldPort)));
                addServerName.setText(currentData.getString(currentData.getColumnIndexOrThrow(ServerModel.FieldName)));
                addServerHost.setText(currentData.getString(currentData.getColumnIndexOrThrow(ServerModel.FieldHost)));
                addServerPassword.setText(currentData.getString(currentData.getColumnIndexOrThrow(ServerModel.FieldPassword)));
            } catch (Exception ex) {
                exceptionToastPrompt(ex);
                ex.printStackTrace();
                setResult(RESULT_CANCELED);
                finish();
            }
        } else {
            addServerEditMode.setVisibility(View.INVISIBLE);
        }

    }

    private boolean isEditMode() {
        return editTargetId > -1;
    }

    @Override
    protected int getBaseLayout() {
        return R.layout.activity_add_server;
    }

    @Override
    protected int getBaseContainer() {
        return R.id.add_server_container;
    }

    private boolean checkForm() {
        return Invoker.invokeFormChecker(this);
    }

    private void onServerFormSubmit(View view) {
        if (!checkForm())
            return;

        try {
            if (isEditMode()) {
                serverModel.updateBySid(editTargetId, addServerName.getText().toString(),
                        addServerHost.getText().toString(),
                        Integer.parseInt(addServerPort.getText().toString()),
                        addServerPassword.getText().toString());
            } else {
                serverModel.add(addServerName.getText().toString(),
                        addServerHost.getText().toString(),
                        Integer.parseInt(addServerPort.getText().toString()),
                        addServerPassword.getText().toString());
            }

            Intent intent = new Intent();
            intent.putExtra(MainActivity.ExtraRequestReload, true);
            intent.putExtra(MainActivity.ExtraPromptText, isEditMode() ? getString(R.string.success_edit, addServerName.getText().toString()) : getString(R.string.success_add, addServerName.getText().toString()));
            setResult(RESULT_OK, intent);
            finish();
        } catch (Exception ex) {
            exceptionToastPrompt(ex);
        }
    }

    public static void startActivity(Activity activity) {
        Intent intent = new Intent(activity, AddServerActivity.class);
        activity.startActivityForResult(intent, ActivityRequestCode);
    }

    public static void startActivity(Activity activity, int editTargetId) {
        Intent intent = new Intent(activity, AddServerActivity.class);
        intent.putExtra(AddServerActivity.ParamEditTargetId, editTargetId);
        activity.startActivityForResult(intent, ActivityRequestCode);
    }
}