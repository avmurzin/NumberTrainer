/**
 * 
 */
package com.murzin.numbertrainer;

import com.murzin.numbertrainer.R;
import android.app.Activity;
import android.app.DialogFragment;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioGroup;

/**
 * @author Andrei V. Murzin
 *
 */
public class SettingsMenuDialog extends DialogFragment implements OnClickListener {
	/**
	 * The activity that creates an instance of this dialog fragment must
     * implement this interface in order to receive event callbacks.
	 * @author Andrei V. Murzin
	 *
	 */
    public interface SettingsDialogListener {
        public void onDialogClick(String pref_name);
    }
    /**
     * Pointer to parent Activity that creates an instance of this dialog fragment.
     */
    SettingsDialogListener mListener;
    
	EditText limitAdd;
	CheckBox isAdd;
	EditText limitSub;
	CheckBox isSub;
	EditText limitMult;
	CheckBox isMult;
	EditText limitDiv;
	CheckBox isDiv;
	RadioGroup showStrategy;
	
	// = (CheckBox) findViewById(R.id.checkbox_id)
	/**
	 * Override the onAttach() method to instantiate the SettingsDialogListener
	 */
	@Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mListener = (SettingsDialogListener) activity;
	}
	
	/**
	 * Inflate dialog_mainmenu.xml to Dialog view
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		getDialog().setTitle("Settings");
		
		View v = inflater.inflate(R.layout.dialog_mainmenu, null);
		
		limitAdd = (EditText) v.findViewById(R.id.limitAdd);
		isAdd = (CheckBox) v.findViewById(R.id.isAdd);
		limitSub = (EditText) v.findViewById(R.id.limitSub);
		isSub = (CheckBox) v.findViewById(R.id.isSub);;
		limitMult = (EditText) v.findViewById(R.id.limitMult);
		isMult = (CheckBox) v.findViewById(R.id.isMult);;
		limitDiv = (EditText) v.findViewById(R.id.limitDiv);
		isDiv = (CheckBox) v.findViewById(R.id.isDiv);
		showStrategy = (RadioGroup) v.findViewById(R.id.showStrategy);

		SharedPreferences settings = getActivity().getSharedPreferences(MainActivity.SETTINGS_NAME, 0);
		limitAdd.setText(settings.getString("limitAdd", "100"));
		limitSub.setText(settings.getString("limitSub", "100"));
		limitMult.setText(settings.getString("limitMult", "100"));
		limitDiv.setText(settings.getString("limitDiv", "100"));
		
		isAdd.setChecked(settings.getBoolean("isAdd", true));
		isSub.setChecked(settings.getBoolean("isSub", false));
		isMult.setChecked(settings.getBoolean("isMult", false));
		isDiv.setChecked(settings.getBoolean("isDiv", false));
		showStrategy.check(settings.getInt("showStrategy", R.id.doSpeak));
		
		v.findViewById(R.id.btSave).setOnClickListener(this);
		return v;
	}
	
	@Override
	public void onClick(View v) {

		SharedPreferences settings = getActivity().getSharedPreferences(MainActivity.SETTINGS_NAME, 0);
		SharedPreferences.Editor editor = settings.edit();
		
		editor.putString("limitAdd", limitAdd.getText().toString());
		editor.putString("limitSub", limitSub.getText().toString());
		editor.putString("limitMult", limitMult.getText().toString());
		editor.putString("limitDiv", limitDiv.getText().toString());
		
		editor.putBoolean("isAdd", isAdd.isChecked());
		editor.putBoolean("isSub", isSub.isChecked());
		editor.putBoolean("isMult", isMult.isChecked());
		editor.putBoolean("isDiv", isDiv.isChecked());
		editor.putInt("showStrategy", showStrategy.getCheckedRadioButtonId());
		
		editor.commit();
		mListener.onDialogClick(MainActivity.SETTINGS_NAME);
		dismiss();
	}

}
