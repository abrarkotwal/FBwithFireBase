package com.abrarkotwal.facebookdemo.Activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.abrarkotwal.facebookdemo.Fragment.LoginFragment;
import com.abrarkotwal.facebookdemo.Other.Utils;
import com.abrarkotwal.facebookdemo.R;
import com.abrarkotwal.facebookdemo.SessionManager.LoginSessionManager;


public class LoginActivity extends AppCompatActivity {
    private static FragmentManager fragmentManager;
    private LoginSessionManager sessionManager;
    private String category;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        fragmentManager = getSupportFragmentManager();

        sessionManager = new LoginSessionManager(this);

        // If savedinstnacestate is null then replace login fragment
        if (savedInstanceState == null) {
            fragmentManager
                    .beginTransaction()
                    .replace(R.id.frameContainer, new LoginFragment(),
                            Utils.Login_Fragment).commit();
        }
    }

    public void replaceLoginFragment(String pointer, Fragment fragment) {
        fragmentManager
                .beginTransaction()
                .setCustomAnimations(R.anim.left_enter, R.anim.right_out)
                .replace(R.id.frameContainer, fragment,
                        pointer).commit();
    }



    @Override
    public void onBackPressed() {

        Fragment SignUp_Fragment = fragmentManager
                .findFragmentByTag(Utils.SignUp_Fragment);

        if (SignUp_Fragment != null)
            replaceLoginFragment(Utils.Login_Fragment,new LoginFragment());
        else
            super.onBackPressed();

        overridePendingTransition(R.anim.slide_in_down, R.anim.slide_out_down);
    }
}
