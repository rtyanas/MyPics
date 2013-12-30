package com.yanas.mobileapp.mypic.datastore;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MessageListDbHelper  extends SQLiteOpenHelper {

	  public static final String TABLE = "messages";
	  public static final String COLUMN_ID = "_id";
	  public static final String COLUMN_MESSAGE = "message";
	  public static final String COLUMN_MSIZE = "size";
	  public static final String COLUMN_MFONT = "font";
	  public static final String COLUMN_MSTYLE = "style";
	  public static final String COLUMN_MCOLOR = "color";

	  public static final String COLUMN_ROTATE = "rotate";
	  public static final String COLUMN_PIC = "pic";

	  private static final String DATABASE_NAME = "messages.db";
	  private static final int DATABASE_VERSION = 1;

	  // Database creation sql statement
	  private static final String DATABASE_CREATE = "create table "
	      + TABLE + "(" + COLUMN_ID + " integer primary key autoincrement, " + 
	      COLUMN_MESSAGE + " text not null, " +
	      COLUMN_MSIZE +  " int, " +
	      COLUMN_MFONT +  " text, " +
	      COLUMN_MSTYLE + " int, " +
	      COLUMN_MCOLOR + " int, " +
	      COLUMN_ROTATE + " int, " +
	      COLUMN_PIC + " text " +
	      ");";

	  public MessageListDbHelper(Context context) {
	    super(context, DATABASE_NAME, null, DATABASE_VERSION);
	  }

	  @Override
	  public void onCreate(SQLiteDatabase database) {
	    database.execSQL(DATABASE_CREATE);
	  }

	  @Override
	  public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	    Log.w(MessageListDbHelper.class.getName(),
	        "Upgrading database from version " + oldVersion + " to "
	            + newVersion + ", which will destroy all old data");
	    db.execSQL("DROP TABLE IF EXISTS " + TABLE);
	    onCreate(db);
	  }

	} 
