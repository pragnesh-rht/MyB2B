package com.rohit.pragnesh.myb2b.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.rohit.pragnesh.myb2b.MainActivity;
import com.rohit.pragnesh.myb2b.R;
import com.rohit.pragnesh.myb2b.fragments.PostFragment;
import com.rohit.pragnesh.myb2b.fragments.ProfileFragment;
import com.rohit.pragnesh.myb2b.models.JWT;
import com.rohit.pragnesh.myb2b.models.Profile;
import com.rohit.pragnesh.myb2b.utility.AuthUtility;
import com.rohit.pragnesh.myb2b.utility.BottomNavigationViewBehavior;
import com.rohit.pragnesh.myb2b.utility.ModelUtils;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PostActivity extends AppCompatActivity implements
        BottomNavigationView.OnNavigationItemSelectedListener {

    private final String TAG = "NEOTag";

    Toolbar toolbar;

    ModelUtils modelUtils = ModelUtils.getInstance();

    Fragment postFragment, profileFragment;

    List<Profile> profiles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        if (findViewById(R.id.post_fragment_container) != null)
            if (savedInstanceState != null)
                return;

        String username = MainActivity.prefConfig.readUserName();
        String password = MainActivity.prefConfig.readPassword();

        getAuth(username, password);
        fetchProfile();

        postFragment = new PostFragment();
        profileFragment = new ProfileFragment();

        getSupportFragmentManager().beginTransaction()
                .add(R.id.post_fragment_container, postFragment).commit();

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Bid 2 Buy");
        }
        toolbar.setTitleMarginStart(64);
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.inflateMenu(R.menu.menu_main);

        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(this);

        CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) navigation.getLayoutParams();
        layoutParams.setBehavior(new BottomNavigationViewBehavior());

        FloatingActionButton fab = findViewById(R.id.fab_cam);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //MainActivity.prefConfig.displayToast("Coming soon!");
                startActivity(new Intent(view.getContext(), NewPostActivity.class));
            }
        });
    }

    private void getAuth(String username, String password) {
        Call<JWT> call = MainActivity.mAPIService.generateToken(username, password);

        call.enqueue(new Callback<JWT>() {
            @Override
            public void onResponse(@NonNull Call<JWT> call, @NonNull Response<JWT> response) {
                JWT jwt = response.body();
                if (response.isSuccessful()) {
                    if (jwt != null) {
                        modelUtils.setJwt(jwt);
                        MainActivity.prefConfig.writeTokens(jwt.getRefresh(), jwt.getAccess());
                    } else {
                        MainActivity.prefConfig.displayToast("No response from the server!");
                        Log.e(TAG, " Error code: " + response.code());
                    }
                } else {
                    AuthUtility.getInstance().getErrorMessage(response.errorBody());
                }
            }

            @Override
            public void onFailure(@NonNull Call<JWT> call, @NonNull Throwable t) {
                Log.e(TAG, "AuthUtility: login: Unable to submit post req to API." + t);
            }
        });
    }

    public void fetchProfile() {
        Call<List<Profile>> call = MainActivity.mAPIService.getProfiles();

        call.enqueue(new Callback<List<Profile>>() {
            @Override
            public void onResponse(@NonNull Call<List<Profile>> call,
                                   @NonNull Response<List<Profile>> response) {
                if (response.isSuccessful()) {
                    profiles = response.body();
                    modelUtils.setProfiles(profiles);
                } else
                    AuthUtility.getInstance().getErrorMessage(response.errorBody());
            }

            @Override
            public void onFailure(@NonNull Call<List<Profile>> call, @NonNull Throwable t) {
                MainActivity.prefConfig
                        .displayToast("Server isn't responding please try again later");
                Log.e(TAG, "ERROR : " + t);
            }
        });
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_discover:
                toolbar.setTitle("Bid 2 Buy");
                getSupportActionBar().show();
                loadFragment(postFragment);
                return true;
            case R.id.action_profile:
                toolbar.setTitle("Profile");
                getSupportActionBar().hide();
                loadFragment(profileFragment);
                return true;
        }
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.logout:
                logout();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void logout() {
        MainActivity.prefConfig.writeLoginStatus(false);
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().
                replace(R.id.post_fragment_container, fragment).commit();
    }
}
