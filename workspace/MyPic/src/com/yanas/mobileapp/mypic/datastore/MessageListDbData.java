package com.yanas.mobileapp.mypic.datastore;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import com.yanas.mobileapp.mypic.MessageData;
import com.yanas.mobileapp.mypic.GlobalSettings;
import com.yanas.mobileapp.mypic.MessageData;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class MessageListDbData {
	
	Context context;

	  // Database fields
	  private SQLiteDatabase database;
	  private MessageListDbHelper dbHelper;
	  private String[] allColumns = {
			  MessageListDbHelper.COLUMN_ID,
			  MessageListDbHelper.COLUMN_MESSAGE,
			  MessageListDbHelper.COLUMN_MSIZE,
			  MessageListDbHelper.COLUMN_MFONT,
			  MessageListDbHelper.COLUMN_MSTYLE,
			  MessageListDbHelper.COLUMN_MCOLOR,
			  MessageListDbHelper.COLUMN_ROTATE,
			  MessageListDbHelper.COLUMN_PIC, 
			  MessageListDbHelper.COLUMN_BG,
			  MessageListDbHelper.COLUMN_TEXT_X,
			  MessageListDbHelper.COLUMN_TEXT_Y
	  };

	  public MessageListDbData(Context context_in) {
		  context = context_in;
	    dbHelper = new MessageListDbHelper(context);
	  }

	  public void open() throws SQLException {
	    database = dbHelper.getWritableDatabase();
	    Log.d("BuildingDbData", "database: open? "+ database.isOpen() + database.toString());
	  }

	  public void close() {
	    dbHelper.close();
	  }

	  public long createMessage(MessageData messageD) {
	    ContentValues values = new ContentValues();

	    values.put(MessageListDbHelper.COLUMN_MESSAGE, messageD.getMessage());
	    values.put(MessageListDbHelper.COLUMN_MSIZE, messageD.getmSize());
	    values.put(MessageListDbHelper.COLUMN_MFONT, messageD.getmFont());
	    values.put(MessageListDbHelper.COLUMN_MSTYLE, messageD.getmStyle());
	    values.put(MessageListDbHelper.COLUMN_MCOLOR, messageD.getmColor());
	    values.put(MessageListDbHelper.COLUMN_BG, messageD.getBackground());
	    values.put(MessageListDbHelper.COLUMN_TEXT_X, messageD.getTextX());
	    values.put(MessageListDbHelper.COLUMN_TEXT_Y, messageD.getTextY());
	    values.put(MessageListDbHelper.COLUMN_ROTATE, messageD.getRotate());
	    values.put(MessageListDbHelper.COLUMN_PIC, messageD.getPic());
	    long insertId = database.insert(MessageListDbHelper.TABLE, null,
	        values);
//	    if(insertId != -1) {
//		    Cursor cursor = database.query(MessageListDbHelper.TABLE,
//		        allColumns, MessageListDbHelper.COLUMN_ID + " = " + insertId, null,
//		        null, null, null);
//		    cursor.moveToFirst();
//		    newBuilding = cursorToBuilding(cursor);
//		    cursor.close();
//	    }
	    return insertId;
	  }

	  public int updateMessage(MessageData messageD) {
		    ContentValues values = new ContentValues();

		    values.put(MessageListDbHelper.COLUMN_MESSAGE, messageD.getMessage());
		    values.put(MessageListDbHelper.COLUMN_MSIZE, messageD.getmSize());
		    values.put(MessageListDbHelper.COLUMN_MFONT, messageD.getmFont());
		    values.put(MessageListDbHelper.COLUMN_MSTYLE, messageD.getmStyle());
		    values.put(MessageListDbHelper.COLUMN_MCOLOR, messageD.getmColor());
		    values.put(MessageListDbHelper.COLUMN_BG, messageD.getBackground());
		    values.put(MessageListDbHelper.COLUMN_TEXT_X, messageD.getTextX());
		    values.put(MessageListDbHelper.COLUMN_TEXT_Y, messageD.getTextY());
		    values.put(MessageListDbHelper.COLUMN_ROTATE, messageD.getRotate());
		    values.put(MessageListDbHelper.COLUMN_PIC, messageD.getPic());
		    
		    int insertId = database.update(MessageListDbHelper.TABLE, values, 
		    		MessageListDbHelper.COLUMN_ID +"="+ messageD.getId(), null);
		    
		    if(GlobalSettings.dataStore) Log.d("MessageListDbData", "MessageDB ID: "+ messageD.getId() +" insert Return: "+ insertId);
		    
		    return insertId;
	  }

	  public void deleteStation(MessageData messageD_in) {
	    long id = messageD_in.getId();
	    
	    if(GlobalSettings.dataStore) Log.i("MessageListDbData", "delete Station with id: " + id);
	    
	    database.delete(MessageListDbHelper.TABLE, MessageListDbHelper.COLUMN_ID
	        + " = " + id, null);
	  }
	  
	  public int initDB(Vector<MessageData> messages) {
			int numStationsRet=0;
//			Cursor cursor = database.rawQuery("", null);
			long numRows = DatabaseUtils.longForQuery(
					database, "SELECT COUNT(*) FROM "+ MessageListDbHelper.TABLE, null);
			if(numRows <= 0) {
				for(MessageData mess : messages) {
					this.createMessage(mess);
					numStationsRet++;
				}
			}
			else {
				Log.d("MessageListDbData", 
						"records not added to database, existing records: "+ numRows);
			}
			
			return numStationsRet;
	  }


	  public List<MessageData> getMessageData() {
	    List<MessageData> comments = new ArrayList<MessageData>();

	    Cursor cursor = database.query(MessageListDbHelper.TABLE,
	        allColumns, null, null, null, null, null);

	    cursor.moveToFirst();
	    while (!cursor.isAfterLast()) {
	    	MessageData comment = cursorToMessage(cursor);
	      comments.add(comment);
	      cursor.moveToNext();
	    }
	    // make sure to close the cursor
	    cursor.close();
	    return comments;
	  }

	  
	  public MessageData getLastMessageData() {
		    List<MessageData> comments = new ArrayList<MessageData>();

		    Cursor cursor = database.query(MessageListDbHelper.TABLE,
		        allColumns, null, null, null, null, null);

		    cursor.moveToFirst();
		    while (!cursor.isAfterLast()) {
		    	MessageData comment = cursorToMessage(cursor);
		      comments.add(comment);
		      cursor.moveToNext();
		    }
		    // make sure to close the cursor
		    cursor.close();
		    MessageData messData_Ret;
		    if( comments.size() > 0)
		    	messData_Ret = comments.get(comments.size() - 1);
		    else
		    	messData_Ret = new MessageData(); 
		    return messData_Ret;
	  }

		  
	  public MessageData getMessage(long id_in) {
		    MessageData mess = new MessageData();
		    MessageData messReturn = new MessageData();

		    Cursor cursor = database.query(MessageListDbHelper.TABLE,
		        allColumns, null, null, null, null, null);

		    cursor.moveToFirst();
		    while ( ! cursor.isAfterLast()) {
		    	mess = cursorToMessage(cursor);
		    	if(mess.getId() == id_in)
		    		break;
		    	cursor.moveToNext();
		    }
		    if( ! cursor.isAfterLast())
		    	messReturn = mess;
		    else
		    	messReturn = new MessageData();
		    
		    // make sure to close the cursor
		    cursor.close();

		    return messReturn;
	  }

	  private MessageData cursorToMessage(Cursor cursor) {
		MessageData md= new MessageData(Long.parseLong(cursor.getString(0)),
				 cursor.getString(1), Integer.parseInt(cursor.getString(2)), 
				 cursor.getString(3), Integer.parseInt(cursor.getString(4)), 
				 Integer.parseInt(cursor.getString(5)), 
				 Integer.parseInt(cursor.getString(6)), 
				 cursor.getString(7), 
				 Integer.parseInt(cursor.getString(8)),
				 Integer.parseInt(cursor.getString(9)),
				 Integer.parseInt(cursor.getString(10))
				);
		
		return md;
	  }

	public SQLiteDatabase getDatabase() {
		return database;
	}


	  
}
