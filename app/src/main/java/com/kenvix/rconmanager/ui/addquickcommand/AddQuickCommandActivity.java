package com.kenvix.rconmanager.ui.addquickcommand;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kenvix.rconmanager.R;
import com.kenvix.rconmanager.database.dao.QuickCommandModel;
import com.kenvix.rconmanager.ui.base.BaseActivity;
import com.kenvix.rconmanager.ui.main.MainActivity;
import com.kenvix.rconmanager.utils.Invoker;
import com.kenvix.utils.annotation.ViewAutoLoad;
import com.kenvix.utils.annotation.form.FormNotEmpty;

public class AddQuickCommandActivity extends BaseActivity {
    public static final int ActivityRequestCode = 0xac00;
    public static final String ParamEditTargetId = "edit_target_id";
    public static final String ParamRequestReload = "request_reload";

    private int editTargetId = -1;
    private QuickCommandModel quickCommandModel;

    @ViewAutoLoad
    @FormNotEmpty
    public EditText quickCommandName;
    @ViewAutoLoad
    @FormNotEmpty
    public EditText quickCommandValue;
    @ViewAutoLoad
    public Button quickCommandSubmit;
    @ViewAutoLoad
    public Toolbar quickCommandToolbar;
    @ViewAutoLoad
    public LinearLayout quickCommandEditMode;
    @ViewAutoLoad
    public TextView quickCommandEditModeTargetId;

    @Override
    @SuppressWarnings("SetTextI18n")
    protected void onInitialize() {
        editTargetId = getIntent().getIntExtra(ParamEditTargetId, -1);

        setSupportActionBar(quickCommandToolbar);

        quickCommandToolbar.setNavigationOnClickListener(view -> finish());

        quickCommandSubmit.setOnClickListener(this::onQuickCommandFormSubmit);

        quickCommandModel = new QuickCommandModel(this);

        if (isEditMode()) {
            quickCommandEditMode.setVisibility(View.VISIBLE);
            quickCommandEditModeTargetId.setText("#" + String.valueOf(editTargetId));

            try(Cursor currentData = quickCommandModel.getByCid(editTargetId)) {

                if(currentData.getCount() <= 0)
                    throw new IllegalArgumentException(getString(R.string.error_invalid_sid) + editTargetId);

                quickCommandName.setText(currentData.getString(currentData.getColumnIndexOrThrow(QuickCommandModel.FieldName)));
                quickCommandValue.setText(currentData.getString(currentData.getColumnIndexOrThrow(QuickCommandModel.FieldValue)));
            } catch (Exception ex) {
                exceptionToastPrompt(ex);
                ex.printStackTrace();
                setResult(RESULT_CANCELED);
                finish();
            }
        } else {
            quickCommandEditMode.setVisibility(View.INVISIBLE);
        }

    }

    private boolean isEditMode() {
        return editTargetId > -1;
    }

    @Override
    protected int getBaseLayout() {
        return R.layout.activity_add_quick_command;
    }

    @Override
    protected int getBaseContainer() {
        return R.id.quick_command_container;
    }

    private boolean checkForm() {
        return Invoker.invokeFormChecker(this);
    }


    private void onQuickCommandFormSubmit(View view) {
        if(!checkForm())
            return;

        try {
            if (isEditMode()) {
                quickCommandModel.updateByCid(editTargetId, quickCommandName.getText().toString(),
                        quickCommandValue.getText().toString());
            } else {
                quickCommandModel.add(quickCommandName.getText().toString(),
                        quickCommandValue.getText().toString());
            }

            Intent intent = new Intent();
            intent.putExtra(MainActivity.ExtraRequestReload, true);
            intent.putExtra(MainActivity.ExtraPromptText, isEditMode() ? getString(R.string.success_edit, quickCommandName.getText().toString()) : getString(R.string.success_add, quickCommandName.getText().toString()));
            setResult(RESULT_OK, intent);
            finish();
        } catch (Exception ex) {
            exceptionToastPrompt(ex);
        }
    }

    public static void startActivity(Activity activity) {
        Intent intent = new Intent(activity, AddQuickCommandActivity.class);
        activity.startActivityForResult(intent, ActivityRequestCode);
    }

    public static void startActivity(Activity activity, int editTargetId) {
        Intent intent = new Intent(activity, AddQuickCommandActivity.class);
        intent.putExtra(AddQuickCommandActivity.ParamEditTargetId, editTargetId);
        activity.startActivityForResult(intent, ActivityRequestCode);
    }
}