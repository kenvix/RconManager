// Rcon Manager for Android
// Copyright (c) 2019. Kenvix <i@kenvix.com>
//
// Licensed under GNU Affero General Public License v3.0

package com.kenvix.rconmanager.ui.main.view.quickcommands;

import android.support.annotation.NonNull;
import android.view.ContextMenu;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kenvix.rconmanager.R;
import com.kenvix.rconmanager.database.dao.QuickCommandModel;
import com.kenvix.rconmanager.meta.QuickCommand;
import com.kenvix.rconmanager.ui.addquickcommand.AddQuickCommandActivity;
import com.kenvix.rconmanager.ui.base.view.IconManager;
import com.kenvix.rconmanager.ui.base.view.base.BaseHolder;
import com.kenvix.rconmanager.ui.main.MainActivity;
import com.kenvix.utils.annotation.ViewAutoLoad;

public class QuickCommandsHolder extends BaseHolder<QuickCommand> implements View.OnCreateContextMenuListener {
    public ImageView imageView;
    private QuickCommand quickCommand;
    @ViewAutoLoad public TextView quickCommandName;
    @ViewAutoLoad public TextView quickCommandValue;
    @ViewAutoLoad public LinearLayout quickCommandItem;

    public QuickCommandsHolder(@NonNull View itemView) {
        super(itemView);
        imageView = IconManager.getInstance().getIcon(R.drawable.ic_storage_list);
        itemView.setOnCreateContextMenuListener(this);
    }

    @Override
    public void bindView(QuickCommand quickCommand) {
        this.quickCommand = quickCommand;
        quickCommandName.setText(quickCommand.getName());
        quickCommandValue.setText(quickCommand.getValue());

        quickCommandItem.setOnClickListener(view -> getActivityByView(view).openContextMenu(quickCommandItem));
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        MainActivity activity = (MainActivity) getActivityByView(v);
        activity.getMenuInflater().inflate(R.menu.menu_quick_command_item, menu);
        QuickCommandModel quickCommandModel = new QuickCommandModel(activity);

        menu.findItem(R.id.action_quick_command_item_delete).setOnMenuItemClickListener(view -> {
            activity.confirmDialog(activity.getString(R.string.confirm_server_delete, quickCommand.getName()), result -> {
                if(result) {
                    try {
                        quickCommandModel.deleteByCid(quickCommand.getCid());
                        activity.snackbar(activity.getString(R.string.prompt_deleted, quickCommand.getName()));
                        activity.reloadQuickCommandRecyclerView();
                    } catch (Exception ex) {
                        activity.exceptionSnackbarPrompt(ex);
                    }
                }
            });
            return true;
        });

        menu.findItem(R.id.action_quick_command_item_duplicate).setOnMenuItemClickListener(view -> {
            try {
                QuickCommand newInstance = quickCommand.clone();
                newInstance.setName(newInstance.getName() + activity.getString(R.string.name_duplicated));
                quickCommandModel.add(newInstance);

                activity.snackbar(activity.getString(R.string.success_duplicated, quickCommand.getName()));
                activity.reloadQuickCommandRecyclerView();
            } catch (Exception ex) {
                activity.exceptionSnackbarPrompt(ex);
            }
            return true;
        });

        menu.findItem(R.id.action_quick_command_item_edit).setOnMenuItemClickListener(view -> {
            AddQuickCommandActivity.startActivity(activity, quickCommand.getCid());
            return true;
        });
    }
}
