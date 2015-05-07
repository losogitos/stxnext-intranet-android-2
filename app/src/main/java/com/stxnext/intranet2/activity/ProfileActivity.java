package com.stxnext.intranet2.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;

import com.stxnext.intranet2.R;

/**
 * Created by Tomasz Konieczny on 2015-04-22.
 */
public class ProfileActivity extends ActionBarActivity {

    private static final String TAG = "ProfileActivity";
    private static final int LOGIN_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acitivty_profile);
        Intent loginIntent = new Intent(this, LoginActivity.class);
        if (isLogged()) {
            loadProfile();
        } else {
            startActivityForResult(loginIntent, LOGIN_REQUEST);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case LOGIN_REQUEST:
                if (resultCode == LoginActivity.LOGIN_OK) {
                    loadProfile();
                } else {

                }
                break;
            default:
                break;
        }
    }

    //TODO
    private void loadProfile() {
        Log.d(TAG, "loadProfile()");
    }
    //TODO
    private boolean isLogged() {
        return false;
    }
}
