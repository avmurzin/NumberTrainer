package com.murzin.numbertrainer;

import android.app.Activity;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

public class NetScoreStrategy implements StrategyScore {
	public static final String SCORE_NAME = "Score";
	private Activity activity;
	private View scoreView;
	private TextView up_count, down_count;
	private int up_score = -1, down_score = -1;
	private SharedPreferences scorePref;
	private SharedPreferences.Editor editor;
//	private Animation operandOut1, operandOut2, operandIn1, operandIn2;
	
	NetScoreStrategy(Activity activity) {
		this.activity = activity;
//		operandOut1 = AnimationUtils.makeOutAnimation(activity, false); //to left
//		operandOut2 = AnimationUtils.makeOutAnimation(activity, true); //to right
//		operandIn1 = AnimationUtils.makeInAnimation(activity, true); //to right
//		operandIn2 = AnimationUtils.makeInAnimation(activity, false);
	}

	@Override
	public View getScoreView() {
		LayoutInflater ltInflater =  activity.getLayoutInflater();
        scoreView = ltInflater.inflate(R.layout.score, null, false);
        scorePref = activity.getSharedPreferences(SCORE_NAME, 0);
        editor = scorePref.edit();
        return scoreView;
	}

	@Override
	public void updateScore(boolean answer) {
        up_count = (TextView) activity.findViewById(R.id.up_count);
        down_count = (TextView) activity.findViewById(R.id.down_count);
        
        if ((up_score == -1) && (down_score == -1)) {
        	up_score = scorePref.getInt("up_score", 0);
        	
//        	up_count.startAnimation(operandOut1);
        	up_count.setText(Integer.toString(up_score));
//        	up_count.startAnimation(operandIn1);
        	
        	down_score = scorePref.getInt("down_score", 0);
        	
//        	down_count.startAnimation(operandOut2);
        	down_count.setText(Integer.toString(down_score));
//        	down_count.startAnimation(operandIn2);
        } else {
        	if (answer) {
//        		up_count.startAnimation(operandOut1);
        		up_count.setText(Integer.toString(++ up_score));
//        		up_count.startAnimation(operandIn1);
        		editor.putInt("up_score", up_score);
        	} else {
//        		down_count.startAnimation(operandOut2);
        		down_count.setText(Integer.toString(++ down_score));
//        		down_count.startAnimation(operandIn2);
        		editor.putInt("down_score", down_score);
        	}
        	editor.commit();
        }

	}

	@Override
	public void release() {
		// TODO Auto-generated method stub

	}

	@Override
	public void clear() {
		up_score = 0;
		down_score = 0;
		editor.putInt("up_score", up_score);
		editor.putInt("down_score", down_score);
		editor.commit();
		up_count.setText(Integer.toString(up_score));
		down_count.setText(Integer.toString(down_score));
	}

}
