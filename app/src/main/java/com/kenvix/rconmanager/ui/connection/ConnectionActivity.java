package com.kenvix.rconmanager.ui.connection;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.kenvix.rconmanager.ApplicationEnvironment;
import com.kenvix.rconmanager.DefaultPreferences;
import com.kenvix.rconmanager.R;
import com.kenvix.rconmanager.rcon.meta.RconServer;
import com.kenvix.rconmanager.rcon.protocol.RconConnect;
import com.kenvix.rconmanager.ui.base.BaseActivity;
import com.kenvix.rconmanager.ui.main.MainActivity;
import com.kenvix.utils.annotation.ViewAutoLoad;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ConnectionActivity extends BaseActivity {
    public static final String ExtraRconServer = "rcon_server";
    public static final String ExtraPreFilledCommandText = "rcon_prefill_command";
    public static final String ExtraPreFilledCommandResultAreaText = "rcon_prefill_result_area";

    private RconServer rconServer;
    private RconConnect rconConnect = null;
    private int historyPosition = 0;
    private List<String> commandHistory = new ArrayList<>();

    @ViewAutoLoad public Button connectionCommandPrev;
    @ViewAutoLoad public Button connectionCommandNext;
    @ViewAutoLoad public Button connectionCommandRun;
    @ViewAutoLoad public Button connectionCommandHistory;
    @ViewAutoLoad public Button connectionQuickCommand;
    @ViewAutoLoad public TextView connectionCommandArea;
    @ViewAutoLoad public EditText connectionCommandText;
    @ViewAutoLoad public Toolbar connectionToolbar;
    @ViewAutoLoad public ScrollView connectionScroll;
    @ViewAutoLoad public LinearLayout connectionCommandLayout;

    @Override
    protected void onInitialize() {
        try {

            Intent intent = getIntent();
            rconServer = intent.getParcelableExtra(ExtraRconServer);

            if(rconServer == null)
                throw new IllegalArgumentException("ExtraRconServer CAN'T be null");

            connectionToolbar.setTitle(rconServer.getName());
            setSupportActionBar(connectionToolbar);

            String preFilledCommandText = intent.getStringExtra(ExtraPreFilledCommandText);
            if(preFilledCommandText != null)
                connectionCommandText.setText(preFilledCommandText);

            String preFilledCommandResultAreaText = intent.getStringExtra(ExtraPreFilledCommandResultAreaText);
            if(preFilledCommandResultAreaText != null) {
                connectionCommandArea.setText(preFilledCommandResultAreaText);
                scrollCommandAreaToBottom();
                Log.d("Connection Activity","rescued activity from intent");
            }

            connectionCommandPrev.setOnClickListener(this::onPreviousCommandButtonClick);
            connectionCommandNext.setOnClickListener(this::onNextCommandButtonClick);

            connectionCommandArea.setText("");
            connectionCommandArea.setTextSize(Integer.parseInt(Objects.requireNonNull(getPreferences().getString(DefaultPreferences.KeyTerminalTextSize, DefaultPreferences.DefaultTerminalTextSize))));
            connectionCommandText.setImeActionLabel(getString(R.string.action_run), KeyEvent.KEYCODE_ENTER);
            connectionCommandText.setOnKeyListener((view, keyCode, event) -> {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    connectionCommandRun.performClick();
                    connectionCommandText.requestFocus();
                    return true;
                }
                return false;
            });

            connectionScroll.addOnLayoutChangeListener((v, left, top, right, bottom, oldLeft, oldTop, oldRight, oldBottom) -> scrollCommandAreaToBottom());

            RconServerConnectorAsyncTask rconServerConnectorAsyncTask = new RconServerConnectorAsyncTask(this);
            rconServerConnectorAsyncTask.execute();

        } catch (Exception ex) {
            exceptionToastPrompt(ex);
            //setResult(RESULT_CANCELED);
            finish();
        }
    }

    public void onRconConnectionEstablished(RconConnect connect) {
        rconConnect = connect;
        connectionCommandRun.setEnabled(true);
    }

    @Override
    protected void onPause() {
        super.onPause();

        if(rconConnect != null) {
            int notifyCode = makeConnectionNotification();
            Log.d("Rcon Connection", "Frontend paused, hashcode: " + notifyCode);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        if(rconConnect != null) {
            int notifyCode = rconServer.hashCode();
            Log.d("Rcon Connection", "Frontend started, hashcode: " + notifyCode);
            cleanConnectionNotification(notifyCode);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_connection, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case R.id.connection_command_area:
                return true;

            case R.id.connection_exit:
                return true;

            case R.id.connection_back:
                return true;

            case R.id.connection_clean_command_history:
                return true;

            case R.id.connection_connection_info:
                return true;

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected int getBaseLayout() {
        return R.layout.activity_connection;
    }

    @Override
    protected int getBaseContainer() {
        return R.id.connection_container;
    }

    public RconServer getRconServer() {
        return rconServer;
    }

    private void cleanConnectionNotification(int notifyCode) {
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(ApplicationEnvironment.NotificationChannelID.RconConnection, notifyCode);
    }

    private int makeConnectionNotification() {
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        int notifyCode = rconServer.hashCode();

        Intent openActivityIntent = new Intent(this, ConnectionActivity.class);
        openActivityIntent.putExtra(ExtraRconServer, rconServer);
        openActivityIntent.putExtra(ExtraPreFilledCommandText, connectionCommandText.getText().toString());
        openActivityIntent.putExtra(ExtraPreFilledCommandResultAreaText, connectionCommandArea.getText().toString());
        openActivityIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);

        PendingIntent openActivityPendingIntent = PendingIntent.getActivity(this, 0, openActivityIntent, PendingIntent.FLAG_CANCEL_CURRENT);


        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, ApplicationEnvironment.NotificationChannelID.RconConnection)
                .setContentTitle(getString(R.string.prompt_connectied_to) + rconServer.getName())
                .setContentText(rconServer.getHostAndPort())
                .setContentIntent(openActivityPendingIntent)
                .setCategory(Notification.CATEGORY_SERVICE)
                .setSmallIcon(R.drawable.ic_server_3)
                .setWhen(System.currentTimeMillis())
                .setPriority(NotificationCompat.PRIORITY_LOW);

        Notification notification = notificationBuilder.build();

        notification.flags = Notification.FLAG_NO_CLEAR | Notification.FLAG_ONGOING_EVENT;

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(ApplicationEnvironment.NotificationChannelID.RconConnection, getString(R.string.title_connection), NotificationManager.IMPORTANCE_LOW);
            notificationChannel.setDescription(getString(R.string.title_connection));

            notificationManager.createNotificationChannel(notificationChannel);
        }

        notificationManager.notify(ApplicationEnvironment.NotificationChannelID.RconConnection, notifyCode, notification);

        return notifyCode;
    }

    public SpannableStringBuilder getCommandPrompt() {
        String commandPromptString = getPreferences().getString(DefaultPreferences.KeyCommandPrompt, DefaultPreferences.DefaultCommandPrompt);
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(commandPromptString);
        ForegroundColorSpan colorPrimaryDark = new ForegroundColorSpan(getColor(R.color.colorPrimaryDark));

        assert commandPromptString != null;
        spannableStringBuilder.setSpan(colorPrimaryDark, 0, commandPromptString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spannableStringBuilder;
    }


    public void onRunButtonClick(View view) {
        String commandText = connectionCommandText.getText().toString();

        if(!commandText.isEmpty()) {
            RconCommanderAsyncTask rconCommanderAsyncTask = new RconCommanderAsyncTask(rconConnect, this);
            appendCommandEcho(commandText);
            pushCommandHistory(commandText);
            rconCommanderAsyncTask.execute(commandText);
            connectionCommandText.setText("");
            historyPosition = 0;
        }
    }

    private void scrollCommandAreaToBottom() {
        connectionScroll.fullScroll(View.FOCUS_DOWN);
    }

    public void appendCommandEcho(String command) {
        connectionCommandArea.append(getCommandPrompt());
        connectionCommandArea.append(command);
        connectionCommandArea.append("\n");
        scrollCommandAreaToBottom();
    }

    @SuppressLint("SetTextI18n")
    public void appendCommandResult(String result) {
        connectionCommandArea.append(result + "\n");
        scrollCommandAreaToBottom();
    }

    public void pushCommandHistory(String command) {
        commandHistory.add(command);
    }

    public void onHistoryButtonClick(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        String[] stringArray = commandHistory.toArray(new String[0]);
        builder.setItems(stringArray, ((dialog, which) -> connectionCommandText.append(stringArray[which])));
        builder.create().show();
    }

    public void onPreviousCommandButtonClick(View view) {
        if(commandHistory.size() > historyPosition) {
            historyPosition++;
            connectionCommandText.setText(commandHistory.get(commandHistory.size() - historyPosition));
        }
    }

    public void onNextCommandButtonClick(View view) {
        if(historyPosition > 0) {
            historyPosition--;

            if(historyPosition > 0)
                connectionCommandText.setText(commandHistory.get(commandHistory.size() - historyPosition));
            else
                connectionCommandText.setText("");
        }
    }

    public static void startActivity(Activity activity, RconServer rconServer) {
        Intent intent = new Intent(activity, ConnectionActivity.class);
        intent.putExtra(ConnectionActivity.ExtraRconServer, rconServer);
        activity.startActivity(intent);
    }
}
