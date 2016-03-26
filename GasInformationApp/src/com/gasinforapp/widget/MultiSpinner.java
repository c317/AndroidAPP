package com.gasinforapp.widget;

import java.util.List;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnMultiChoiceClickListener;
import android.util.AttributeSet;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class MultiSpinner extends Spinner implements
		OnMultiChoiceClickListener, OnCancelListener {
	private List<String> listitems;
	private boolean[] checked;
	private MultispinnerListener multispinnerListener;

	public MultiSpinner(Context context) {
		super(context);
	}

	public MultiSpinner(Context arg0, AttributeSet arg1) {
		super(arg0, arg1);
	}

	public MultiSpinner(Context arg0, AttributeSet arg1, int arg2) {
		super(arg0, arg1, arg2);
	}

	@Override
	public void onClick(DialogInterface dialog, int ans, boolean isChecked) {
		if (isChecked)
			checked[ans] = true;
		else
			checked[ans] = false;
	}

	@Override
	public void onCancel(DialogInterface dialog) {
		if (multispinnerListener != null) {
			multispinnerListener.onItemschecked(checked);
		}
		
		String str = "已选择: ";

		for (int i = 0; i < listitems.size(); i++) {
			if (checked[i] == true) {
				str = str + "   " + listitems.get(i);
			}

		}
		
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),
				android.R.layout.simple_spinner_item, new String[] { str });
		setAdapter(adapter);

//		AlertDialog.Builder alert1 = new AlertDialog.Builder(getContext());
//
//		alert1.setTitle("提示:");
//
//		alert1.setMessage(str);
//
//		alert1.setPositiveButton("Ok", null);
//
//		alert1.show();

	}

	@Override
	public boolean performClick() {
		AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
		builder.setMultiChoiceItems(
				listitems.toArray(new CharSequence[listitems.size()]), checked,
				this);
		builder.setPositiveButton("确认",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
					}
				});
		builder.setOnCancelListener(this);
		builder.show();
		return true;
	}

	public void setItems(List<String> items, String allText,
			MultispinnerListener listener) {
		this.listitems = items;
		this.multispinnerListener = listener;

		checked = new boolean[items.size()];
		for (int i = 0; i < checked.length; i++)
			checked[i] = false;

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),
				android.R.layout.simple_spinner_item, new String[] { allText });
		setAdapter(adapter);
	}

	public interface MultispinnerListener {
		public void onItemschecked(boolean[] checked);
	}

}
