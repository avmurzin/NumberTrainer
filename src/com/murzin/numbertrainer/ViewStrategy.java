package com.murzin.numbertrainer;

import java.util.LinkedList;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import android.content.Context;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

public class ViewStrategy implements Strategy {
	private Context context;
	private Task task;

	/**
	 * Thread for task operands and operation voices playing.
	 * @author murzin
	 *
	 */
	
	public ViewStrategy(Context context) {
		this.context = context;
	}
	
	/**
	 * Generate MediaPlayer list with voices items. 
	 */
	@Override
	public void prepareTaskView(Task task) {
		this.task = task;
	}
	/**
	 * Start voices sequence.
	 */
	@Override
	public View doTaskView() {
		long operands[] = new long[2];
		operands = task.getTaskContent().getOperands();
		String message = "   " + operands[0] + " " + task.getTaskContent().getOperation().getString() + " "  + operands[1];
		Toast toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
		toast.show();
		return null;
	}
	
	/**
	 * Release resources.
	 */
	@Override
	public void release() {

	}
	
}
