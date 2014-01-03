package com.yanas.mobileapp.mypic;

import com.yanas.mobileapp.mypic.datastore.MessageListDbData;
import com.yanas.mobileapp.mypic.datastore.MessageListDbHelper;

import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Typeface;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MainActivity extends Activity {

    private static final int SELECT_PICTURE = 1;
    private static final int MESSAGE_SETTINGS = 2;
    public static final int BACKGROUND_SETTINGS = 3;
    private String selectedImagePath;
    private ImageView img;
    private Button myPicButton;
    private Uri selectedImageUri = null;
    private MainActivity mainActThis;
	MessageData messData;


    public final static String MESSAGE = "com.yanas.mobileapp.mypic.MESSAGE";
    public final static String FONT  = "com.yanas.mobileapp.mypic.FONT";
    public final static String STYLE = "com.yanas.mobileapp.mypic.STYLE";
    public final static String SIZE = "com.yanas.mobileapp.mypic.SIZE";
    public final static String COLOR = "com.yanas.mobileapp.mypic.COLOR";
    public final static String ROTATE = "com.yanas.mobileapp.mypic.ROTATE";
    public final static String BACKGROUND = "com.yanas.mobileapp.mypic.BACKGROUNDSETTING";
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mainActThis = this;
        messData = retrieveSettings(); // new MessageData();
        selectedImagePath = "";
        
        // selectedImageUri = null;
        
    	if(GlobalSettings.mainActivity) Log.d("MainActivity", "onCreate");

    	TextView tv = (TextView)findViewById(R.id.message_data);
        tv.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                Intent intent = new Intent(mainActThis, SettingsActivity.class);
                intent.putExtra(MESSAGE, messData);
                startActivityForResult(intent, MESSAGE_SETTINGS);
            }
        });
        
        setText();
        
    	img = (ImageView)findViewById(R.id.mypic);
        if(messData.getPic() != null && messData.getPic().length() > 1) {
        	img.setImageURI(Uri.parse(messData.getPic()));
            Bitmap bitmap = BitmapFactory.decodeFile(messData.getPic());
            if(bitmap != null) {
        		Matrix m = img.getImageMatrix();
        		m.postRotate(messData.getRotate());
        		bitmap = Bitmap.createBitmap(bitmap, 0, 0, 
        				  bitmap.getWidth(), bitmap.getHeight(), m, true);
        		img.setImageBitmap(bitmap );
            }
        }
        	
        img.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"Select Picture"), SELECT_PICTURE);
            }
        });

    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_PICTURE) {
                Uri selectedImageUri = data.getData();
                selectedImagePath = getPath(selectedImageUri);
                if(GlobalSettings.mainActivity) Log.d("MainActivity", "Image Path : " + selectedImagePath);
                
                // BitmapFactory.Options options = new BitmapFactory.Options();
                // options.inSampleSize = 100;
                // options.inDensity = 200;
                Bitmap bitmap = BitmapFactory.decodeFile(selectedImagePath);
                // image.setImageBitmap(BitmapFactory.decodeFile(picturePath));
                
                if(selectedImageUri != null)
                	img.setImageURI(selectedImageUri);
                
//                img.setImageBitmap(bitmap );

                if(bitmap != null) {
    				Matrix m = img.getImageMatrix();
    				m.postRotate(messData.getRotate());
    				bitmap = Bitmap.createBitmap(bitmap, 0, 0, 
    						  bitmap.getWidth(), bitmap.getHeight(), m, true);
    				img.setImageBitmap(bitmap );

//                    // img.setImageBitmap(BitmapFactory.decodeFile(selectedImagePath) );                	
            		messData.setPic(selectedImagePath);
            		saveNewSettings(messData);
                }
            }
            else if(requestCode == MESSAGE_SETTINGS) {
        		messData = (MessageData)data.getSerializableExtra(MainActivity.MESSAGE);
        		messData.setPic(selectedImagePath);
        		saveNewSettings(messData);

        		if(GlobalSettings.mainActivity) {
            		
            		Log.d("MainActivity", "onActivityResult "+ messData);
//            		Log.d("MainActivity", "onActivityResult"+
//            				data.getStringExtra(MainActivity.MESSAGE) +","+
//            				data.getStringExtra(MainActivity.FONT) +","+
//            				data.getStringExtra(MainActivity.STYLE) +","+
//            				data.getStringExtra(MainActivity.SIZE) +","+
//            				data.getStringExtra(MainActivity.COLOR) +","+
//            				data.getStringExtra(MainActivity.ROTATE) 
            	}
            } // requestCode == MESSAGE_SETTINGS
        } // resultCode == RESULT_OK
    }

    public String getPath(Uri uri) {
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
    @Override
    public void onResume() {
    	super.onResume();
        
    	setText();
        
//        if(selectedImageUri != null)
//        	img.setImageURI(selectedImageUri);

//        if(selectedImagePath != null)
//    	  img.setImageURI(Uri.parse(selectedImagePath));
        
        Bitmap bitmap = BitmapFactory.decodeFile(selectedImagePath);
        if(bitmap != null) {
    		Matrix m = img.getImageMatrix();
    		m.postRotate(messData.getRotate());
    		bitmap = Bitmap.createBitmap(bitmap, 0, 0, 
    				  bitmap.getWidth(), bitmap.getHeight(), m, true);
    		img.setImageBitmap(bitmap );
    //breaks              bitmap.recycle();

//            // img.setImageBitmap(BitmapFactory.decodeFile(selectedImagePath) );        	
        }
    }
    

    /**
     * Keep the app from restarting.  Also need to define in manifest: 
     *         <activity
     *             ...
     *             android:configChanges="orientation|screenSize" >
     */
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
    	super.onConfigurationChanged(newConfig);
    
    	if(GlobalSettings.mainActivity) Log.d("MainActivity", "onConfigurationChanged");
    }
    
    private void setText() {
    	TextView tv = (TextView)findViewById(R.id.message_data);
    	tv.setText(messData.getMessage());
        tv.setTextSize(messData.getmSize() );
        tv.setTextColor(messData.getmColor());
        tv.setTypeface(messData.stringToTypeFace(messData.getmFont() ), messData.getmStyle());
        
        View vg = (View)findViewById(R.id.main_layout) ;
        if(vg != null)
        	vg.setBackgroundColor(messData.getBackground());
        else
        	if(GlobalSettings.mainActivity) Log.e("MainActivity", "setText background not set Layout is null");
    }

    
    private void saveNewSettings(MessageData messData_in) {
		MessageListDbData messListDbData = new MessageListDbData(this);

    	messListDbData.open();

		long numRows = DatabaseUtils.longForQuery(
				messListDbData.getDatabase(), "SELECT COUNT(*) FROM "+ MessageListDbHelper.TABLE, null);
    	
    	if(numRows > 0) {
        	messListDbData.updateMessage(messData_in);
    	}
    	else {
        	messListDbData.createMessage(messData_in);    		
    	}
    	
    	messListDbData.close();
    }
    

    private MessageData retrieveSettings() {
    	MessageData messg;
    	MessageListDbData messListDbData = new MessageListDbData(this);
    	
    	messListDbData.open();
    	
    	messg = messListDbData.getLastMessageData();
    	
    	messListDbData.close();
    	
    	if(GlobalSettings.mainActivity) Log.d("MainActivity", "retrieveSettings messageData: "+ messg);

    	return messg;
    }

}
