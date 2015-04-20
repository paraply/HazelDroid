package se.evinja.hazeldroid;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;


public class Activity_Login extends ActionBarActivity implements HazelEvents {
    EditText login_username, login_password;
    CheckBox login_remember;
    SharedPreferences sharedPref;
    protected Hazel hazel;
    Boolean from_saved_info = false;
    ProgressDialog progress;
    Button login_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        hazel = (Hazel) getApplication();
        login_username = (EditText) findViewById(R.id.login_text_username);
        login_password = (EditText) findViewById(R.id.login_text_password);
        login_remember = (CheckBox) findViewById(R.id.login_chk_remember);
        login_btn = (Button) findViewById(R.id.login_btn_login);
        sharedPref = getPreferences(Context.MODE_PRIVATE);
        String username = sharedPref.getString("username", "");
        login_username.setText(username);
        String password = sharedPref.getString("password","");
        login_password.setText(password);

        if (!username.isEmpty() || !password.isEmpty()){
            from_saved_info = true;
            if (!hazel.user_has_logged_out()) {
                login(username, password);
            }
        }
    }

    public void login_btn_clicked(View v){
        String username = login_username.getText().toString();
        String password = login_password.getText().toString();
        if (username.isEmpty()){
            Toast.makeText(v.getContext(), getString(R.string.username_is_missing), Toast.LENGTH_LONG).show();
            login_username.requestFocus();
        }else if (password.isEmpty()){
            Toast.makeText(v.getContext(), getString(R.string.password_is_missing), Toast.LENGTH_LONG).show();
            login_password.requestFocus();
        }else{
            login(username, password);
        }
    }


    private void login(String username, String password){
        progress = ProgressDialog.show(
                Activity_Login.this,
                getString(R.string.please_wait),
                getString(R.string.connecting),
                true,
                true,
                new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        hazel.cancel();
                        Activity_Login.this.restoreUI();
                    }
                }
        );

        login_username.setEnabled(false);
        login_password.setEnabled(false);
        login_remember.setEnabled(false);
        login_btn.setEnabled(false);
        hazel.login(username, password, this);
    }

    private void restoreUI(){
        login_btn.setEnabled(true);
        login_username.setEnabled(true);
        login_password.setEnabled(true);
        login_remember.setEnabled(true);
    }

    @Override
    public void onError(Hazel.HazelCommand duringCommand, String errorMsg) {
        switch (duringCommand){
            case LOGIN:
                Toast.makeText(this, getString(R.string.connection_failed) + ": " + errorMsg, Toast.LENGTH_LONG).show();
                break;
            case DOWNLOAD_PERSONNEL:
                Toast.makeText(this, getString(R.string.download_failed) + ": " + errorMsg, Toast.LENGTH_LONG).show();
            //    hazel.disconnect_on_fail();
                break;
            case DOWNLOAD_USER_SCHEDULE:
                Toast.makeText(this, getString(R.string.download_user_schedule_fail) + ": " + errorMsg, Toast.LENGTH_LONG).show();
                break;
            case DOWNLOAD_STAFF_SCHEDULE:
                Toast.makeText(this, getString(R.string.download_staff_schedule_fail) + ": " + errorMsg, Toast.LENGTH_LONG).show();
                break;
        }
        restoreUI();
    }


    @Override
    public void onConnected() {
        if (login_remember.isChecked() && !from_saved_info){ //Ska spara datan
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString("username", login_username.getText().toString());
            editor.putString("password", login_password.getText().toString());
            editor.commit();
        }else if (!login_remember.isChecked() && from_saved_info){
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.remove("username");
            editor.remove("password");
            editor.commit();
        }
        progress.setMessage(getString(R.string.downloading_personnel));

    }

    @Override
    public void onStaffDownloaded() {
        progress.setMessage(getString(R.string.downloading_schedules));
    }



    @Override
    public void onUserSchedule() {}

    @Override
    public void onStaffSchedule() {
        restoreUI();
        Intent intent = new Intent(this,Activity_Main.class);
        startActivity(intent);
        finish();
    }


    @Override
    protected void onStop() {
        super.onStop();

        if(progress!= null)
            progress.dismiss();
    }

    @Override
    protected void onPause() {
        super.onPause();

        if(progress!= null)
            progress.dismiss();
    }


}
