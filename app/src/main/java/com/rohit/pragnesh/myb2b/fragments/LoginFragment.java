package com.rohit.pragnesh.myb2b.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.rohit.pragnesh.myb2b.R;
import com.rohit.pragnesh.myb2b.utility.AuthUtility;

public class LoginFragment extends Fragment {
    AuthUtility authUtility = AuthUtility.getInstance();
    TextView tv_response, tv_link_sign_up;
    EditText input_username, input_password;
    Button btn_login;
    FragmentMover mover;

    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        input_username = view.findViewById(R.id.input_username);
        input_password = view.findViewById(R.id.input_password);
        btn_login = view.findViewById(R.id.btn_login);
        tv_response = view.findViewById(R.id.tv_response);
        tv_link_sign_up = view.findViewById(R.id.link_signup);

        tv_link_sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mover.activityListener("REGISTER");
            }
        });

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = input_username.getText().toString().trim();
                String password = input_password.getText().toString().trim();
                if (!TextUtils.isEmpty(username) && !TextUtils.isEmpty(password)) {
                    authUtility.login(username, password, view.getContext());
                }
                input_password.setText("");
                input_username.setText("");
            }
        });
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Activity activity = (Activity) context;
        mover = (FragmentMover) activity;
    }

    public interface FragmentMover {
        void activityListener(String choice);

    }
}
