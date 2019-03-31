package com.rohit.pragnesh.myb2b.fragments;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.rohit.pragnesh.myb2b.MainActivity;
import com.rohit.pragnesh.myb2b.R;
import com.rohit.pragnesh.myb2b.models.Users;
import com.rohit.pragnesh.myb2b.utility.AuthUtility;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class RegisterFragment extends Fragment {

    jumpToLogin mover;
    private EditText input_username, input_password, input_email;

    public RegisterFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_register, container, false);

        input_username = view.findViewById(R.id.input_username);
        input_password = view.findViewById(R.id.input_password);
        input_email = view.findViewById(R.id.input_email);
        Button btn_sign_up = view.findViewById(R.id.btn_signup);

        TextView tv_link_login = view.findViewById(R.id.link_login);
        tv_link_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mover.loginFragment();
            }
        });


        btn_sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = input_username.getText().toString().trim();
                String password = input_password.getText().toString().trim();
                String email = input_email.getText().toString().trim();

                if (!TextUtils.isEmpty(username) && !TextUtils.isEmpty(password) && !TextUtils.isEmpty(email)) {
                    register(username, password, email);
                    input_email.setText("");
                    input_password.setText("");
                    input_username.setText("");
                    mover.loginFragment();
                }
            }
        });
        return view;
    }

    public void register(String username, String password, String email) {
        Call<Users> call = MainActivity.mAPIService.registerUser(email, username, password);

        call.enqueue(new Callback<Users>() {
            @Override
            public void onResponse(@NonNull Call<Users> call, @NonNull Response<Users> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        MainActivity.prefConfig
                                .displayToast("Registration successful! \nPlease login");
                    } else MainActivity.prefConfig.displayToast("Something went Wrong");
                } else
                    AuthUtility.getInstance().getErrorMessage(response.errorBody());
            }

            @Override
            public void onFailure(@NonNull Call<Users> call, @NonNull Throwable t) {
                Log.e("NEOTag", "Unable to create User");
            }
        });

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Activity activity = (Activity) context;
        mover = (jumpToLogin) activity;
    }

    public interface jumpToLogin {
        void loginFragment();
    }
}
