package com.murzin.numbertrainer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.os.Environment;

public class SaveToDB {
	
	public class ExportDialog extends DialogFragment {
		private String message;
		public ExportDialog(String message) {
			this.message = message;
		}
	    @Override
	    public Dialog onCreateDialog(Bundle savedInstanceState) {

	        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
	        builder.setMessage(getString(R.string.export_dialog) + System.getProperty("line.separator") + message)
	               .setPositiveButton(R.string.export_dialog_ok, new DialogInterface.OnClickListener() {
	                   public void onClick(DialogInterface dialog, int id) {

	                   }
	               });

	        return builder.create();
	    }
	}
	
	
	private final static int VERSION = 1;
	private final static String DB_NAME = "scoreDB";
	private final static String TABLE_NAME = "score";
	private final static String CREATESQL = 
			"create table " + 
			TABLE_NAME + " (" +
			" id integer primary key autoincrement, " + 
			" operand_1 integer, " + 
			" operand_2 integer, " + 
			" operation text, " + 
			" answer integer, " +
			" time integer" + 
			");";
	 private DBHelper dbHelper;
	 private Context context;
	 private Activity activity;

	public SaveToDB(Context context, Activity activity) {
		this.context = context;
		this.activity = activity;
		dbHelper = new DBHelper(context);
	}
	
	public void store(Task task, boolean answer) {
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		ContentValues cv = new ContentValues();
		cv.put("operand_1", task.getTaskContent().getOperands()[0]);
		cv.put("operand_2", task.getTaskContent().getOperands()[1]);
		cv.put("operation", task.getTaskContent().getOperation().toString());
		if (answer) {
			cv.put("answer", 1);
		} else {
			cv.put("answer", 0);
		}
		cv.put("time", System.currentTimeMillis() / 1000);
		
		db.insert(TABLE_NAME, null, cv);
		dbHelper.close();
	}
	
	public void export() {
		if(!isExternalStorageWritable()) {
			// TODO: alert for no external storage
			return;
		}
	    File path = Environment.getExternalStoragePublicDirectory(
	            Environment.DIRECTORY_DOWNLOADS);
	    File file = new File(path, Long.toString(System.currentTimeMillis() / 1000) + ".txt");
	    FileOutputStream outputStream;
	    String outString;
		
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		String[] columns = {"id", "operand_1", "operand_2", "operation", "answer", "time"};
		Cursor cursor = db.query(false, TABLE_NAME, columns, null, null, null, null, "id", null);
		cursor.moveToFirst();
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z", Locale.getDefault());
		
		try {
			path.mkdirs();
			outputStream = new FileOutputStream(file);
			while (!cursor.isAfterLast()) {
				outString = cursor.getInt(0) + "," 
						+ cursor.getInt(1) + "," 
						+ cursor.getInt(2) + "," 
						+ cursor.getString(3) + ","
						+ cursor.getInt(4) + ","
						+ cursor.getInt(5) + ","
						+ sdf.format(new Date(cursor.getInt(5)*1000L))
						+ System.getProperty("line.separator");
				outputStream.write(outString.getBytes());
				cursor.moveToNext();
			}
			outputStream.close();
		} catch (IOException e) {
			// TODO: handle exception
		}
		dbHelper.close();
		DialogFragment dialog = new ExportDialog(file.getPath());
		dialog.show(activity.getFragmentManager(), "export_dialog");
	}
	
	class DBHelper extends SQLiteOpenHelper {

		public DBHelper(Context context) {
			super(context, DB_NAME, null, VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL(CREATESQL);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

		}
	}
	
	/* Checks if external storage is available for read and write */
	public boolean isExternalStorageWritable() {
	    String state = Environment.getExternalStorageState();
	    if (Environment.MEDIA_MOUNTED.equals(state)) {
	        return true;
	    }
	    return false;
	}
}
