package com.abrarkotwal.facebookdemo.Fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.abrarkotwal.facebookdemo.Other.CustomToast;
import com.abrarkotwal.facebookdemo.Activity.MainActivity;
import com.abrarkotwal.facebookdemo.R;
import com.abrarkotwal.facebookdemo.SessionManager.LoginSessionManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterFragment extends Fragment implements OnClickListener {
	private View view;
	private EditText etemailId,etpassword;
	private Button signUpButton;
	private Animation shakeAnimation;
	private FirebaseAuth firebaseAuth;
	private ProgressDialog progressDialog;
	private LinearLayout registerLayout;
    private LoginSessionManager sessionManager;

	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_register, container, false);
		initViews();
		setListeners();
		return view;
	}

	private void initViews() {
        sessionManager  =  new LoginSessionManager(getActivity());
        progressDialog 	=  new ProgressDialog(getActivity());
        shakeAnimation 	=  AnimationUtils.loadAnimation(getActivity(), R.anim.shake);
        firebaseAuth 	=  FirebaseAuth.getInstance();

		etemailId		=  view.findViewById(R.id.email);
		etpassword 		=  view.findViewById(R.id.password);
		signUpButton 	=  view.findViewById(R.id.signUpBtn);
		registerLayout 	=  view.findViewById(R.id.registerLayout);
	}

	// Set Listeners
	private void setListeners() {
		signUpButton.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.signUpBtn:
				checkValidation();
			break;
		}

	}

	private void checkValidation() {
		final String email = etemailId.getText().toString();
		final String password = etpassword.getText().toString();

		if (email.equals("") || email.length() == 0
				|| password.equals("") || password.length() == 0) {
			registerLayout.startAnimation(shakeAnimation);
			new CustomToast().Show_Toast(getActivity(), view,"Enter all credentials");
		}
		else if (password.length()<6){
			new CustomToast().Show_Toast(getActivity(), view,"Enter minimum 6 digit password");
		}
		else {
			registerUser(email,password);
		}
	}

	private void registerUser(final String email, final String password) {
	    progressDialog.setMessage("Registering Please Wait...");
		progressDialog.show();

		firebaseAuth.createUserWithEmailAndPassword(email, password)
				.addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
					@Override
					public void onComplete(@NonNull Task<AuthResult> task) {
                        progressDialog.dismiss();
						if(task.isSuccessful()){
                            sessionManager.createLoginSession(email);
							Toast.makeText(getActivity(),"Successfully registered",Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(getActivity(), MainActivity.class);
                            startActivity(intent);
                            getActivity().finish();
						}else{
							//display some message here
							new CustomToast().Show_Toast(getActivity(), view,"Registration Error");
						}
					}
				});
	}
}
