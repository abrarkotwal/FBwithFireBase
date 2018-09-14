package com.abrarkotwal.facebookdemo.Other;


import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.abrarkotwal.facebookdemo.R;


public class CustomToast {

	public void Show_Toast(Context context, View view, String error) {
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		View layout = inflater.inflate(R.layout.custom_toast,
				(ViewGroup) view.findViewById(R.id.toast_root));

		TextView text = (TextView) layout.findViewById(R.id.toast_error);
		text.setText(error);

		Toast toast = new Toast(context);
		toast.setGravity(Gravity.TOP | Gravity.FILL_HORIZONTAL, 0, 0);

		toast.setDuration(Toast.LENGTH_SHORT);// Set Duration
		toast.setView(layout);

		toast.show();
	}

}
