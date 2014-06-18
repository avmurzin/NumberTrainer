package com.murzin.numbertrainer;

import java.util.LinkedList;

import com.murzin.numbertrainer.SettingsMenuDialog.SettingsDialogListener;
import android.app.Activity;
import android.app.DialogFragment;
//import android.content.DialogInterface.OnClickListener;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends Activity implements OnClickListener, SettingsDialogListener {
	public static final String SETTINGS_NAME = "Settings";
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
		//check
		//...
		//do new Task
		if (currentTask.getTaskContent().getAnswer() == Long.parseLong((String)text_answer.getText())) {
			Toast toast = Toast.makeText(getApplicationContext(), "Good!", Toast.LENGTH_SHORT);
			toast.show();
			scoreStrategy.updateScore(true);
		} else {
			Toast toast = Toast.makeText(getApplicationContext(), "Bad!", Toast.LENGTH_SHORT);
			toast.show();
			scoreStrategy.updateScore(false);
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
		isSub = settings.getBoolean("isSub", false);
		isMult = settings.getBoolean("isMult", false);
		isDiv = settings.getBoolean("isDiv", false);
		
		limitAdd = Long.parseLong(settings.getString("limitAdd", "100"));
		limitSub = Long.parseLong(settings.getString("limitSub", "100"));
		limitMult = Long.parseLong(settings.getString("limitMult", "100"));
		limitDiv = Long.parseLong(settings.getString("limitDiv", "100"));
		
		switch (settings.getInt("showStrategy", R.id.doSpeak)) {
		case R.id.doSpeak:
			showStrategy = null;
			showStrategy = new SpeakStrategy(getApplicationContext());
			break;
		case R.id.doShow:
			
			break;

		default:
			if (showStrategy != null) {
				showStrategy.release();
			}
			showStrategy = null;
			showStrategy = new SpeakStrategy(getApplicationContext());
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
}
