package com.rohit.pragnesh.myb2b.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.rohit.pragnesh.myb2b.R;
import com.rohit.pragnesh.myb2b.fragments.LoginFragment;
import com.rohit.pragnesh.myb2b.fragments.RegisterFragment;

public class LoginActivity extends AppCompatActivity implements LoginFragment.FragmentMover,
        RegisterFragment.jumpToLogin {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getSupportFragmentManager().beginTransaction()
                .add(R.id.login_fragment_container, new LoginFragment()).commit();
    }

    @Override
    public void activityListener(String choice) {
        switch (choice) {
            case "POST":
                Intent intent = new Intent(this, PostActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                break;

            case "REGISTER":
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.login_fragment_container, new RegisterFragment()).commit();
        }
    }

    @Override
    public void loginFragment() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.login_fragment_container, new LoginFragment()).commit();
    }
}
