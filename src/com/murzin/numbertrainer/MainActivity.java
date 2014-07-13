package com.murzin.numbertrainer;

import java.util.LinkedList;

import com.murzin.numbertrainer.SettingsMenuDialog.SettingsDialogListener;
import android.app.Activity;
import android.app.DialogFragment;
//import android.content.DialogInterface.OnClickListener;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends Activity implements OnClickListener, SettingsDialogListener {
	public static final String SETTINGS_NAME = "Settings";
	public static final long MAX_ADD = 199;
	public static final long MAX_SUB = 199;
	public static final long MAX_MUL = 14;
	public static final long MAX_DIV = 14;
	private boolean isAdd = false;
	private boolean isSub = false;
	private boolean isMult = false;
	private boolean isDiv = false;
	private Button bt1, bt2, bt3, bt4, bt5, bt6, bt7, bt8, bt9, bt0, enter, bs, repeat;
	private TextView text_answer;
	private Strategy showStrategy;
	private StrategyScore scoreStrategy;
	private Task currentTask;
	private LinearLayout linLayout;
	
	private long limitAdd, limitSub, limitMult, limitDiv;
	
	LinkedList<Task> taskList = new LinkedList<Task>();
	
	SaveToDB saveToDB;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		linLayout = (LinearLayout) findViewById(R.id.score);
		
		bt1 = (Button) findViewById(R.id.bt1);
		bt2 = (Button) findViewById(R.id.bt2);
		bt3 = (Button) findViewById(R.id.bt3);
		bt4 = (Button) findViewById(R.id.bt4);
		bt5 = (Button) findViewById(R.id.bt5);
		bt6 = (Button) findViewById(R.id.bt6);
		bt7 = (Button) findViewById(R.id.bt7);
		bt8 = (Button) findViewById(R.id.bt8);
		bt9 = (Button) findViewById(R.id.bt9);
		bt0 = (Button) findViewById(R.id.bt0);
		bs = (Button) findViewById(R.id.bs);
		enter = (Button) findViewById(R.id.enter);
		repeat = (Button) findViewById(R.id.repeat);
		text_answer = (TextView) findViewById(R.id.text_answer);
		bt1.setOnClickListener(this);
		bt2.setOnClickListener(this);
		bt3.setOnClickListener(this);
		bt4.setOnClickListener(this);
		bt5.setOnClickListener(this);
		bt6.setOnClickListener(this);
		bt7.setOnClickListener(this);
		bt8.setOnClickListener(this);
		bt9.setOnClickListener(this);
		bt0.setOnClickListener(this);
		bs.setOnClickListener(this);
		enter.setOnClickListener(this);
		repeat.setOnClickListener(this);
		
		restoreSettings();
		doTask();
	}
	
	private void checkAnswer(){
		LayoutInflater inflater = getLayoutInflater();
		View layout = inflater.inflate(R.layout.toast_layout, 
				(ViewGroup) findViewById(R.id.toast_layout_root));
		TextView text = (TextView) layout.findViewById(R.id.toast_text);
		ImageView image = (ImageView) layout.findViewById(R.id.toast_image);
		
		Toast toast = new Toast(getApplicationContext());
		
		toast.setDuration(Toast.LENGTH_LONG);
		//
		String badId = "b_" + Long.toString(Math.round(Math.random() * 27));
		int id_bad = getApplicationContext().getResources().getIdentifier(badId, "raw", getApplicationContext().getPackageName());
		//
		String goodId = "g_" + Long.toString(Math.round(Math.random() * 20));
		int id_good = getApplicationContext().getResources().getIdentifier(goodId, "raw", getApplicationContext().getPackageName());
		
	
		if (currentTask.getTaskContent().getAnswer() == Long.parseLong((String)text_answer.getText())) {
			text.setText("Отлично!");
			image.setImageResource(id_good);

			toast.setView(layout);
			toast.show();
			scoreStrategy.updateScore(true);
			saveToDB.store(currentTask, true);
		} else {
			text.setText("Ошибка!");
			image.setImageResource(id_bad);
//			Toast toast = Toast.makeText(getApplicationContext(), "Ошибка!", Toast.LENGTH_SHORT);
			toast.setView(layout);
			toast.show();
			scoreStrategy.updateScore(false);
			saveToDB.store(currentTask, false);
		}
		doTask();
	}
	private void doTask() {
		currentTask = null;
		currentTask = taskList.get((int) (Math.random() * taskList.size()));
		currentTask.refresh();
		showStrategy.prepareTaskView(currentTask);
		showStrategy.doTaskView();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			getSettings();
			return true;
		}
		if (id == R.id.action_exit) {
			saveAndExit();
			return true;
		}
		if (id == R.id.score_clear) {
			scoreStrategy.clear();
			return true;
		}
		if (id == R.id.score_export) {
			saveToDB.export();
			return true;
		}
		
		return super.onOptionsItemSelected(item);
	}
	
	private void saveAndExit() {
		finish();
	}
	
	private void getSettings() {
		DialogFragment settings = new SettingsMenuDialog();
		settings.show(getFragmentManager(), "settings");
	}

	@Override
	public void onDialogClick(String pref_name) {
		SharedPreferences settings = getSharedPreferences(pref_name, 0);
		isAdd = settings.getBoolean("isAdd", true);
		isSub = settings.getBoolean("isSub", true);
		isMult = settings.getBoolean("isMult", true);
		isDiv = settings.getBoolean("isDiv", true);
		
		try {
			limitAdd = Long.parseLong(settings.getString("limitAdd", "100"));
			if (limitAdd > MAX_ADD) {
				limitAdd = MAX_ADD;
			}
		} catch (NumberFormatException e) {
			limitAdd = 100;
		}
		
		try {
			limitSub = Long.parseLong(settings.getString("limitSub", "100"));
			if (limitSub > MAX_SUB) {
				limitSub = MAX_SUB;
			}
		} catch (NumberFormatException e) {
			limitSub = 100;
		}
		
		try {
			limitMult = Long.parseLong(settings.getString("limitMult", "10"));
			if (limitMult > MAX_MUL) {
				limitMult = MAX_MUL;
			}
		} catch (NumberFormatException e) {
			limitMult = 10;
		}
		
		try {
			limitDiv = Long.parseLong(settings.getString("limitDiv", "10"));
			if (limitDiv > MAX_DIV) {
				limitDiv = MAX_DIV;
			}
		} catch (NumberFormatException e) {
			limitDiv = 10;
		}
		
	
		switch (settings.getInt("showStrategy", R.id.doSpeak)) {
		case R.id.doSpeak:
			if (showStrategy != null) {
				showStrategy.release();
			}
			showStrategy = null;
			showStrategy = new SpeakStrategy(getApplicationContext());
			break;
		case R.id.doShow:
			if (showStrategy != null) {
				showStrategy.release();
			}
			showStrategy = null;
			showStrategy = new ViewStrategy(getApplicationContext());
			break;

		default:
			break;
		}
		
		if (scoreStrategy != null) {
			linLayout.removeAllViews();
			scoreStrategy.release();
		}
		scoreStrategy = null;
		scoreStrategy = new NetScoreStrategy(this);
		linLayout.addView(scoreStrategy.getScoreView());
		scoreStrategy.updateScore(true);
				
		taskList.clear();

		if (isAdd) {
			taskList.add(new TaskAdd(limitAdd));
		}
		if (isSub) {
			taskList.add(new TaskSub(limitSub));
		}
		if (isMult) {
			taskList.add(new TaskMult(limitMult));
		}
		if (isDiv) {
			taskList.add(new TaskDiv(limitDiv));
		}
		
		if (taskList.size() == 0) {
			enter.setClickable(false);
		} else {
			enter.setClickable(true);
		}
		doTask();
	}
	
	private void restoreSettings() {
		onDialogClick(SETTINGS_NAME);
	}

	   @Override
	   public void onClick (View v) {
		   
		   String string = "";
		   
		   switch (v.getId()) {
			case R.id.bt1: 
				text_answer.setText(text_answer.getText() + "1");
				    break;
			case R.id.bt2:
				text_answer.setText(text_answer.getText() + "2");
					break;
			case R.id.bt3:
				text_answer.setText(text_answer.getText() + "3");
					break;		
			case R.id.bt4:
				text_answer.setText(text_answer.getText() + "4");
					break;	
			case R.id.bt5:
				text_answer.setText(text_answer.getText() + "5");
					break;	
			case R.id.bt6:
				text_answer.setText(text_answer.getText() + "6");
					break;	
			case R.id.bt7:
				text_answer.setText(text_answer.getText() + "7");
					break;	
			case R.id.bt8:
				text_answer.setText(text_answer.getText() + "8");
					break;	
			case R.id.bt9:
				text_answer.setText(text_answer.getText() + "9");
					break;	
			case R.id.bt0:
				text_answer.setText(text_answer.getText() + "0");
				break;	
			case R.id.bs: {
				string = text_answer.getText().toString();
				if (string.length() == 1) {
					string = "";
				}
				if (string.length() > 1) {
					string = string.substring(0, string.length() - 1);
				}
				text_answer.setText(string);}
				break;
			case R.id.enter:{
				if (!text_answer.getText().toString().equals("")) {
					checkAnswer();
					text_answer.setText("");
				}
				}			
				break;
			case R.id.repeat: {
				showStrategy.doTaskView();
			}
			break;
	       }
		   
		   }
	   
	   @Override
	protected void onResume() {
		   super.onResume();
		   if (saveToDB != null) {
			   saveToDB = null;
		   }
		   saveToDB = new SaveToDB(getApplicationContext(), this);
	}
	   
	   @Override
	protected void onPause() {
		   super.onPause();
		   if (saveToDB != null) {
			   saveToDB = null;
		   }
	}
}
