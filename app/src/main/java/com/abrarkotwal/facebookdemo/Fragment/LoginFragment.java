package com.abrarkotwal.facebookdemo.Fragment;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.XmlResourceParser;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.Html;
import android.text.InputType;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.abrarkotwal.facebookdemo.Other.CustomToast;
import com.abrarkotwal.facebookdemo.Activity.MainActivity;
import com.abrarkotwal.facebookdemo.R;
import com.abrarkotwal.facebookdemo.SessionManager.LoginSessionManager;
import com.abrarkotwal.facebookdemo.Other.Utils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginFragment extends Fragment implements OnClickListener {
	private View view;
	private EditText emailid, password;
	private Button loginButton;
	private TextView signUp;
	private CheckBox show_hide_password;
	private Animation shakeAnimation;
	private FragmentManager fragmentManager;
	private LoginSessionManager sessionManager;
	private LinearLayout loginLayout;
	private FirebaseAuth firebaseAuth;
	private ProgressDialog progressDialog;

	public LoginFragment() {

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_login, container, false);
		initViews();
		setListeners();
		return view;
	}

	private void initViews() {

		sessionManager = new LoginSessionManager(getActivity());

		fragmentManager = getActivity().getSupportFragmentManager();

		emailid 			=  view.findViewById(R.id.login_emailid);
		password 			=  view.findViewById(R.id.login_password);
		signUp 				=  view.findViewById(R.id.createAccount);
		loginButton 	    =  view.findViewById(R.id.loginBtn);
		show_hide_password	=  view.findViewById(R.id.show_hide_password);
		loginLayout			=  view.findViewById(R.id.login_layout);

		firebaseAuth 		= FirebaseAuth.getInstance();
		progressDialog 		= new ProgressDialog(getActivity());

		shakeAnimation 		= AnimationUtils.loadAnimation(getActivity(), R.anim.shake);
		signUp.setText(Html.fromHtml("Don’t have an account? <b>Register</b>"));

		@SuppressLint("ResourceType")
        XmlResourceParser xrp = getResources().getXml(R.drawable.text_selector);
		try {
			ColorStateList csl = ColorStateList.createFromXml(getResources(), xrp);

			show_hide_password.setTextColor(csl);
			signUp.setTextColor(csl);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void setListeners() {
		loginButton.setOnClickListener(this);
		signUp.setOnClickListener(this);

		show_hide_password.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton button, boolean isChecked) {
				if (isChecked) {
					show_hide_password.setText(R.string.hide_pwd);
					password.setInputType(InputType.TYPE_CLASS_TEXT);
					password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());// show password
				}
				else {
					show_hide_password.setText(R.string.show_pwd);
					password.setInputType(InputType.TYPE_CLASS_TEXT );
					password.setTransformationMethod(PasswordTransformationMethod.getInstance());// hide password
				}
			}
		});

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.loginBtn:
				checkValidation();
				break;

			case R.id.createAccount:
				fragmentManager
						.beginTransaction()
						.setCustomAnimations(R.anim.right_enter, R.anim.left_out)
						.replace(R.id.frameContainer, new RegisterFragment(),
								Utils.SignUp_Fragment).commit();
				break;
		}
	}


	private void checkValidation() {
		String getEmailId = emailid.getText().toString();
		String getPassword = password.getText().toString();

		if (getEmailId.equals("") || getEmailId.length() == 0
				|| getPassword.equals("") || getPassword.length() == 0) {
			loginLayout.startAnimation(shakeAnimation);
			new CustomToast().Show_Toast(getActivity(), view,"Enter both credentials.");
		}
		else if (password.length()<6){
			new CustomToast().Show_Toast(getActivity(), view,"Enter minimum 6 digit password");
		}
		else{
			checkLogin(getEmailId,getPassword);
		}
	}

	private void checkLogin(final String email, String passwrd) {
		progressDialog.setMessage("Please Wait...");
		progressDialog.show();

		firebaseAuth.signInWithEmailAndPassword(email, passwrd)
				.addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
					@Override
					public void onComplete(@NonNull Task<AuthResult> task) {
						progressDialog.dismiss();
						if (!task.isSuccessful()) {
							new CustomToast().Show_Toast(getActivity(), view,"Login Failed");
						} else {
							sessionManager.createLoginSession(email);
							Toast.makeText(getActivity(),"Successfully Login",Toast.LENGTH_LONG).show();
							Intent intent = new Intent(getActivity(), MainActivity.class);
							startActivity(intent);
							getActivity().finish();
						}
					}
				});
	}
}
