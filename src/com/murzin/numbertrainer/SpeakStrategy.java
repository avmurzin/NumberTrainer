package com.murzin.numbertrainer;

import java.util.LinkedList;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import android.content.Context;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

public class SpeakStrategy implements Strategy {
	private Context context;
	private Task task;
	private LinkedList<MediaPlayer> playerList = new LinkedList<MediaPlayer>();
	private playTask pt;
	private ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

	/**
	 * Thread for task operands and operation voices playing.
	 * @author murzin
	 *
	 */
	private class playTask extends AsyncTask<LinkedList<MediaPlayer>, Void, Integer> {
		@Override
		protected Integer doInBackground(LinkedList<MediaPlayer>... params) {
			try {
				lock.readLock().lock();
				for (MediaPlayer m : params[0]) {
					m.start();
					while (m.isPlaying()) {
					}
				}
			}
			finally {
				lock.readLock().unlock();
			}
			return 0;
		}
		protected void onPreExecute() {
			//Log.e("NumberTrainer", "onPreExecute");
	
		}
	     protected void onPostExecute(Integer result) {
	    	 //Log.e("NumberTrainer", "onPostExecute");
	     }
	}
	
	public SpeakStrategy(Context context) {
		this.context = context;
	}
	
	/**
	 * Generate MediaPlayer list with voices items. 
	 */
	@Override
	public void prepareTaskView(Task task) {
		//Log.e("NumberTrainer", "prepareTaskView");
		try {
			//Log.e("NumberTrainer", "1");
			lock.writeLock().lock();
			//Log.e("NumberTrainer", "2");
			release();
			//Log.e("NumberTrainer", "3");
			playerList.clear();
			//Log.e("NumberTrainer", "4");
			this.task = task;
			long operand_1 = task.getTaskContent().getOperands()[0];
			long operand_2 = task.getTaskContent().getOperands()[1];
			//Log.e("NumberTrainer", "5");
			longToVoice(operand_1);
			//Log.e("NumberTrainer", "6");
			switch (task.getTaskContent().getOperation()) {
			case Add:
				if (Math.random() > 0.5) {
					playerList.add(MediaPlayer.create(context, R.raw.plus_1));
				} else {
					playerList.add(MediaPlayer.create(context, R.raw.plus_2));
				}
				break;
			case Sub:
				if(Math.random() > 0.5) {
					playerList.add(MediaPlayer.create(context, R.raw.minus_1));
				} else {
					playerList.add(MediaPlayer.create(context, R.raw.minus_2));
				}
				break;
			case Mult:
				playerList.add(MediaPlayer.create(context, R.raw.mult));
				break;
			case Div:
				playerList.add(MediaPlayer.create(context, R.raw.div));
				break;
			default:
				break;
			}
			//Log.e("NumberTrainer", "7");
			longToVoice(operand_2);
			//Log.e("NumberTrainer", "8");
		}
		finally {
			//Log.e("NumberTrainer", "9");
			lock.writeLock().unlock();
			//Log.e("NumberTrainer", "10");
		}
	}
	/**
	 * Start voices sequence.
	 */
	@Override
	public View doTaskView() {
		//Log.e("NumberTrainer", "doTaskView");
		try {
			lock.writeLock().lock();
			long operands[] = new long[2];
			operands = task.getTaskContent().getOperands();
			pt = null;
			pt = new playTask();
			pt.execute(playerList);
		}
		finally {
			lock.writeLock().unlock();
		}
		return null;
		
	}
	
	/**
	 * Release resources.
	 */
	@Override
	public void release() {
		try {
			lock.writeLock().lock();
			for (MediaPlayer m : playerList) {
				if (m != null) {
					m.release();
					m = null;
				}
			}
		}
		finally {
			lock.writeLock().unlock();
		}

	}
	
	/**
	 * Convert number to sound. 
	 * @param operand <= 9999, otherwise throw exception.
	 */
	private void longToVoice(long operand) {
		//Log.e("NumberTrainer", "longToVoice_1");
		if (operand > 9999) {
			throw new IllegalArgumentException("Number more than 9999");
		}
		if (operand != 0) {
			int i = (int) Math.log10(operand);
			//Log.e("NumberTrainer", "longToVoice_2");
			for (int j = (int) Math.pow(10, i); j != 1; j = j/10) {
				//Log.e("NumberTrainer", "longToVoice_3");
				//Log.e("NumberTrainer", "operand = " + operand + " i = " + i);
				if (operand > 19) {
					//Log.e("NumberTrainer", "longToVoice_4");
					String wavId = "d_" + Long.toString((operand / j) * j);
					int id = context.getApplicationContext().getResources().getIdentifier(wavId, "raw",  context.getApplicationContext().getPackageName());
					//Log.e("NumberTrainer", "longToVoice_5");
					playerList.add(MediaPlayer.create(context, id));
					//Log.e("NumberTrainer", "longToVoice_6");
					operand = operand - (operand / j) * j;
				} else {
					if (operand > 9) {
						//Log.e("NumberTrainer", "longToVoice_7");
						String wavId = "d_" + Long.toString(operand);
						int id = context.getApplicationContext().getResources().getIdentifier(wavId, "raw",  context.getApplicationContext().getPackageName());
						//Log.e("NumberTrainer", "longToVoice_8");
						playerList.add(MediaPlayer.create(context, id));
						//Log.e("NumberTrainer", "longToVoice_9");
						break;
					}
				}
			}//
		} else {
			String wavId = "d_" + Long.toString(operand);
			int id = context.getApplicationContext().getResources().getIdentifier(wavId, "raw",  context.getApplicationContext().getPackageName());
			playerList.add(MediaPlayer.create(context, id));
		}
		//Log.e("NumberTrainer", "longToVoice_10");
		if ((operand < 10) && (operand > 0)) {
			String wavId = "d_" + Long.toString(operand);
			//Log.e("NumberTrainer", "longToVoice_11");
			int id = context.getApplicationContext().getResources().getIdentifier(wavId, "raw",  context.getApplicationContext().getPackageName());
			//Log.e("NumberTrainer", "longToVoice_12");
			playerList.add(MediaPlayer.create(context, id));
			//Log.e("NumberTrainer", "longToVoice_13");
		}
	}

}
