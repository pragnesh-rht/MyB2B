package com.rohit.pragnesh.myb2b.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.rohit.pragnesh.myb2b.MainActivity;
import com.rohit.pragnesh.myb2b.R;
import com.rohit.pragnesh.myb2b.activities.LoginActivity;
import com.rohit.pragnesh.myb2b.models.JWT;
import com.rohit.pragnesh.myb2b.models.Profile;
import com.rohit.pragnesh.myb2b.models.Users;
import com.rohit.pragnesh.myb2b.utility.AuthUtility;
import com.rohit.pragnesh.myb2b.utility.ModelUtils;
import com.squareup.picasso.Picasso;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileFragment extends Fragment implements View.OnClickListener {
    private final String TAG = "NEOTag";
    Users user;
    List<Profile> profiles;
    ModelUtils modelUtils = ModelUtils.getInstance();

    TextView username, email, statistics, accounts, help, logout, id;
    ImageView profile_pic;

    JWT jwt = modelUtils.getJwt();

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        bindData(view);
        user = modelUtils.getUser();
        profiles = modelUtils.getProfiles();

        if (user == null) getUser();
        else if (profiles != null)
            setData();

        return view;
    }

    private void getUser() {
        if (jwt == null) {
            jwt = new JWT();
            jwt.setAccess(MainActivity.prefConfig.readAccess());
            jwt.setRefresh(MainActivity.prefConfig.readRefresh());
        }

        Call<Users> call = MainActivity.mAPIService.whoAmI(jwt.getAuthorization());

        call.enqueue(new Callback<Users>() {
            @Override
            public void onResponse(@NonNull Call<Users> call,
                                   @NonNull Response<Users> response) {
                if (response.isSuccessful()) {
                    user = response.body();
                    if (user != null) {
                        MainActivity.prefConfig.writeUserId(user.getId());
                        modelUtils.setUser(user);
                    } else Log.e(TAG, "User doesn't exist!! code: " + response.code());
                } else {
                    AuthUtility.getInstance().getErrorMessage(response.errorBody());
                }
                user = modelUtils.getUser();
                profiles = modelUtils.getProfiles();
                if (user != null && profiles != null)
                    setData();
                else
                    MainActivity.prefConfig.displayToast("Something went wrong, please login again!");
            }

            @Override
            public void onFailure(@NonNull Call<Users> call,
                                  @NonNull Throwable t) {
                MainActivity.prefConfig
                        .displayToast("Server isn't responding please try again later");
                Log.e(TAG, "ERROR : " + t);
            }
        });
    }

    private void bindData(View view) {
        username = view.findViewById(R.id.profile_username);
        email = view.findViewById(R.id.profile_email);
        id = view.findViewById(R.id.profile_id);
        statistics = view.findViewById(R.id.profile_statistics);
        accounts = view.findViewById(R.id.profile_accounts);
        help = view.findViewById(R.id.profile_help);
        logout = view.findViewById(R.id.profile_logout);
        profile_pic = view.findViewById(R.id.profile_pic);

        statistics.setOnClickListener(this);
        accounts.setOnClickListener(this);
        help.setOnClickListener(this);
        logout.setOnClickListener(this);
    }

    private void setData() {
        username.setText(user.getUsername());
        email.setText(user.getEmail());
        String idText = "Account ID: " + user.getId();
        id.setText(idText);

        if (profiles == null) return;
        for (Profile profile : profiles) {
            if (profile.getUser().equals(user.getUsername())) {
                Picasso.get()
                        .load(profile.getImage())
                        .resize(300, 300)
                        .into(profile_pic);
            }
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.profile_statistics:
                MainActivity.prefConfig.displayToast("High");
                break;
            case R.id.profile_accounts:
                MainActivity.prefConfig.displayToast("You are a SCIENCE Student");
                break;
            case R.id.profile_help:
                MainActivity.prefConfig.displayToast("No one can help you now");
                break;
            case R.id.profile_logout:
                MainActivity.prefConfig.writeLoginStatus(false);
                Context context = view.getContext();
                Intent intent = new Intent(context, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
                break;
        }
    }
}
