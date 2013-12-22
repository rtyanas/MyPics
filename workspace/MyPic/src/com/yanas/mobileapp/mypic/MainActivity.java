package com.yanas.mobileapp.mypic;

import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Typeface;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends Activity {

    private static final int SELECT_PICTURE = 1;
    private static final int MESSAGE_SETTINGS = 2;
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
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mainActThis = this;
        messData = new MessageData();
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
    // breaks              bitmap.recycle();

//                    // img.setImageBitmap(BitmapFactory.decodeFile(selectedImagePath) );                	
                }
            }
            else if(requestCode == MESSAGE_SETTINGS) {
        		messData = (MessageData)data.getSerializableExtra(MainActivity.MESSAGE);
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
            }
        }
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
        
        if(selectedImageUri != null)
        	img.setImageURI(selectedImageUri);

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

    }
}
