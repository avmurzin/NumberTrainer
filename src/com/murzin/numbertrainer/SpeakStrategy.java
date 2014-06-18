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
			lock.readLock().lock();
			for (MediaPlayer m : params[0]) {
				m.start();
				while (m.isPlaying()) {
				}
			}
	    	 lock.readLock().unlock();
			return 0;
		}
		protected void onPreExecute() {
			Log.e("NumberTrainer", "onPreExecute");
	
		}
	     protected void onPostExecute(Integer result) {
	    	 Log.e("NumberTrainer", "onPostExecute");
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
		Log.e("NumberTrainer", "prepareTaskView");
		lock.writeLock().lock();
		release();
		playerList.clear();
		this.task = task;
		long operand_1 = task.getTaskContent().getOperands()[0];
		long operand_2 = task.getTaskContent().getOperands()[1];
		

//		if (operand_1 < 20) {
//			String wavId = "d_" + Long.toString(operand_1);
//			int id = context.getApplicationContext().getResources().getIdentifier(wavId, "raw",  context.getApplicationContext().getPackageName());
//			playerList.add(MediaPlayer.create(context, id));
//		}
		
		longToVoice(operand_1);
		
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
		default:
			break;
		}
		
//		if (operand_2 < 20) {
//			String wavId = "d_" + Long.toString(operand_2);
//			int id = context.getApplicationContext().getResources().getIdentifier(wavId, "raw",  context.getApplicationContext().getPackageName());
//			playerList.add(MediaPlayer.create(context, id));
//		}
		longToVoice(operand_2);
		lock.writeLock().unlock();
	}
	/**
	 * Start voices sequence.
	 */
	@Override
	public View doTaskView() {
		Log.e("NumberTrainer", "doTaskView");
		lock.writeLock().lock();
		long operands[] = new long[2];
		operands = task.getTaskContent().getOperands();
		String message = "task: " + operands[0] + task.getTaskContent().getOperation().toString() + operands[1];
		Toast toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
		toast.show();
//		try {
////			sema.acquire();
////			lock.lock();
//		} catch (Exception e) {
//			// TODO: handle exception
//		}
//		if (pt != null) {
//			while (pt.getStatus() == AsyncTask.Status.RUNNING) {
//			}
//		}
		pt = null;
		pt = new playTask();
		pt.execute(playerList);
		lock.writeLock().unlock();
		return null;
		
	}
	
	/**
	 * Release resources.
	 */
	@Override
	public void release() {
		lock.writeLock().lock();
		for (MediaPlayer m : playerList) {
			if (m != null) {
				m.release();
				m = null;
			}
		}
		lock.writeLock().unlock();

	}
	
	/**
	 * Convert number to sound. 
	 * @param operand <= 9999, otherwise throw exception.
	 */
	private void longToVoice(long operand) {
		if (operand > 9999) {
			throw new IllegalArgumentException("Number more than 9999");
		}
		int i = (int) Math.log10(operand);
//		if (i == 0) {
//			String wavId = "d_" + Long.toString(operand);
//			int id = context.getApplicationContext().getResources().getIdentifier(wavId, "raw",  context.getApplicationContext().getPackageName());
//			playerList.add(MediaPlayer.create(context, id));
//		}
		for (int j = (int) Math.pow(10, i); j != 1; j = j/10) {
			if (operand > 19) {
				String wavId = "d_" + Long.toString((operand / j) * j);
				int id = context.getApplicationContext().getResources().getIdentifier(wavId, "raw",  context.getApplicationContext().getPackageName());
				playerList.add(MediaPlayer.create(context, id));
				operand = operand - (operand / j) * j;
			} else {
				if (operand > 9) {
					String wavId = "d_" + Long.toString(operand);
					int id = context.getApplicationContext().getResources().getIdentifier(wavId, "raw",  context.getApplicationContext().getPackageName());
					playerList.add(MediaPlayer.create(context, id));
					break;
				}
			}
		}
		if ((operand < 10) && (operand > 0)) {
			String wavId = "d_" + Long.toString(operand);
			int id = context.getApplicationContext().getResources().getIdentifier(wavId, "raw",  context.getApplicationContext().getPackageName());
			playerList.add(MediaPlayer.create(context, id));
		}
	}

}
