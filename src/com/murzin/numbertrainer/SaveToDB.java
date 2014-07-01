package com.murzin.numbertrainer;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SaveToDB {
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

	public SaveToDB(Context context) {
		this.context = context;
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
}
