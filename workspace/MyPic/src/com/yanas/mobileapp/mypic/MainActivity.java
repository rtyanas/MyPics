package com.yanas.mobileapp.mypic;

import com.yanas.mobileapp.mypic.datastore.MessageListDbData;
import com.yanas.mobileapp.mypic.datastore.MessageListDbHelper;

import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ClipData;
import android.content.ClipDescription;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.DragEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

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
	private static final String TEXT_TAG = "text_message";

    public final static String MESSAGE = "com.yanas.mobileapp.mypic.MESSAGE";
    public final static String FONT  = "com.yanas.mobileapp.mypic.FONT";
    public final static String STYLE = "com.yanas.mobileapp.mypic.STYLE";
    public final static String SIZE = "com.yanas.mobileapp.mypic.SIZE";
    public final static String COLOR = "com.yanas.mobileapp.mypic.COLOR";
    public final static String ROTATE = "com.yanas.mobileapp.mypic.ROTATE";
    public final static String BACKGROUND = "com.yanas.mobileapp.mypic.BACKGROUNDSETTING";
    
    TextView tv; // messageTV;
    
    @SuppressLint("NewApi")
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mainActThis = this;
        messData = retrieveSettings(); // new MessageData();
        selectedImagePath = messData.getPic();
        // messageTV = new TextView(this);
        
        // selectedImageUri = null;
        
    	if(GlobalSettings.mainActivity) Log.d("MainActivity", "onCreate");

    	// TextView tv = (TextView)findViewById(R.id.message_data);
    	tv = new TextView(this);
    	RelativeLayout vg = (RelativeLayout)findViewById(R.id.main_layout) ;

        tv.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
            	startSettingsActivity();
            }
        });
        
	    // Instantiates the drag shadow builder.
	    final View.DragShadowBuilder myShadow = new MyDragShadowBuilder(tv);
        
        tv.setTag(TEXT_TAG);
        tv.setOnLongClickListener( new View.OnLongClickListener() {

        		    // Defines the one method for the interface, which is called when the View is long-clicked
        		    public boolean onLongClick(View v) {

	        		    // Create a new ClipData.
	        		    // This is done in two steps to provide clarity. The convenience method
	        		    // ClipData.newPlainText() can create a plain text ClipData in one step.
	
	        		    // Create a new ClipData.Item from the ImageView object's tag
	        		    ClipData.Item item = new ClipData.Item((CharSequence) v.getTag());
	
	        		    // Create a new ClipData using the tag as a label, the plain text MIME type, and
	        		    // the already-created item. This will create a new ClipDescription object within the
	        		    // ClipData, and set its MIME type entry to "text/plain"
	        		    // ClipData dragData = new ClipData(v.getTag(), new String[] { ClipDescription.MIMETYPE_TEXT_PLAIN},item);
	        		    ClipData dragData = new ClipData((CharSequence) v.getTag(), new String[] {ClipDescription.MIMETYPE_TEXT_PLAIN}, item);
	        		    // Instantiates the drag shadow builder.
	        		    // View.DragShadowBuilder myShadow = new MyDragShadowBuilder(tv);
	
	        		    // Starts the drag	
    		            v.startDrag(dragData,  // the data to be dragged
    		                        myShadow,  // the drag shadow builder
    		                        null,      // no need to use local data
    		                        0          // flags (not currently used, set to 0)
    		            );
    		            
    		            return true;

        		    }
        		} );

        setText();
        
        vg.setOnDragListener(new myDragEventListener() );
        vg.addView(tv);

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
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_settings_menu_item:
            	startSettingsActivity();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    
    @Override
    public void onResume() {
    	super.onResume();
        
    	setText();
        
//        if(selectedImageUri != null)
//        	img.setImageURI(selectedImageUri);

//        if(selectedImagePath != null)
//    	  img.setImageURI(Uri.parse(selectedImagePath));
        
        Bitmap bitmap = BitmapFactory.decodeFile(messData.getPic());
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
    
    @SuppressLint("NewApi")
	private void setText() {
    	// TextView tv = (TextView)findViewById(R.id.message_data);
    	String textData = messData.getMessage();
    	
    	if(textData.equals(MessageData.DEFAULT_MESSAGE)) {
    		textData = getString(R.string.fabulous);
    	}

    	tv.setText(textData);
        tv.setTextSize(messData.getmSize() );
        tv.setTextColor(messData.getmColor());
        tv.setTypeface(messData.stringToTypeFace(messData.getmFont() ), messData.getmStyle());

        tv.setX(messData.getTextX());
        tv.setY(messData.getTextY());
        
        View vg = (View)findViewById(R.id.main_layout) ;
        if(vg != null)
        	vg.setBackgroundColor(messData.getBackground());
        else
        	if(GlobalSettings.mainActivity) Log.e("MainActivity", "setText background not set Layout is null");
    }
    
    private void startSettingsActivity() {
        Intent intent = new Intent(mainActThis, SettingsActivity.class);
        intent.putExtra(MESSAGE, messData);
        startActivityForResult(intent, MESSAGE_SETTINGS);

    }

    
    private void saveNewSettings(MessageData messData_in) {
		MessageListDbData messListDbData = new MessageListDbData(this);

		if(GlobalSettings.mainActivity) Log.d("MainActivity", "saveNewSettings "+ messData_in);
    	
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

    
    @SuppressLint("NewApi")
	protected class myDragEventListener implements View.OnDragListener {

        // This is the method that the system calls when it dispatches a drag event to the
        // listener.
        public boolean onDrag(View v, DragEvent event) {

            // Defines a variable to store the action type for the incoming event
            final int action = event.getAction();

            // if(GlobalSettings.mainActivity) Log.d("myDragEventListener","onDrag, Start.");

            // Handles each of the expected events
            switch(action) {

                case DragEvent.ACTION_DRAG_STARTED:

                    if(GlobalSettings.mainActivity) Log.d("myDragEventListener","onDrag, ACTION_DRAG_STARTED.");

                    // Determines if this View can accept the dragged data
                    if (event.getClipDescription().hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN)) {

                        if(GlobalSettings.mainActivity) Log.d("myDragEventListener","onDrag, ACTION_DRAG_STARTED.");

                        // As an example of what your application might do,
                        // applies a blue color tint to the View to indicate that it can accept
                        // data.
                        // ((ImageView) v).setColorFilter(Color.BLUE);

                        // Invalidate the view to force a redraw in the new tint
                        v.invalidate();

                        // returns true to indicate that the View can accept the dragged data.
                        return(true);

                        } else {

                        // Returns false. During the current drag and drop operation, this View will
                        // not receive events again until ACTION_DRAG_ENDED is sent.
                        return(false);

                        }
                    // break;

                case DragEvent.ACTION_DRAG_ENTERED: 

                    if(GlobalSettings.mainActivity) Log.d("myDragEventListener","onDrag, ACTION_DRAG_ENTERED.");

                    // Applies a green tint to the View. Return true; the return value is ignored.

                    // ((ImageView) v).setColorFilter(Color.GREEN);

                    // Invalidate the view to force a redraw in the new tint
                    v.invalidate();

                    return(true);

                    // break;

                    case DragEvent.ACTION_DRAG_LOCATION:

                    if(GlobalSettings.mainActivity) Log.d("myDragEventListener","onDrag, ACTION_DRAG_LOCATION. X:"+ 
                    									event.getX() + ", Y: "+ event.getY() );

                    messData.setTextX(event.getX());
                    messData.setTextY(event.getY());

                    // Ignore the event
                        return(true);

                    // break;

                    case DragEvent.ACTION_DRAG_EXITED:

                        if(GlobalSettings.mainActivity) Log.d("myDragEventListener","onDrag, ACTION_DRAG_EXITED.");

                        // Re-sets the color tint to blue. Returns true; the return value is ignored.
                        // ((ImageView) v).setColorFilter(Color.BLUE);

                        // Invalidate the view to force a redraw in the new tint
                        v.invalidate();

                        return(true);

                    // break;

                    case DragEvent.ACTION_DROP:

                        if(GlobalSettings.mainActivity) Log.d("myDragEventListener","onDrag, ACTION_DROP.");

                        // Gets the item containing the dragged data
                        ClipData.Item item = event.getClipData().getItemAt(0);

                        // Gets the text data from the item.
                        String dragData = (String) item.getText();

                        // Displays a message containing the dragged data.
                        Toast.makeText(mainActThis, "Dragged data is " + dragData, Toast.LENGTH_LONG);

                        // Turns off any color tints
                        // ((ImageView) v).clearColorFilter();

                        // Invalidates the view to force a redraw
                        v.invalidate();

                        // Returns true. DragEvent.getResult() will return true.
                        return(true);

                    // break;

                    case DragEvent.ACTION_DRAG_ENDED:

                        if(GlobalSettings.mainActivity) Log.d("myDragEventListener","onDrag, ACTION_DRAG_ENDED.");

                        setText();
                		saveNewSettings(messData);
                        
                        // Turns off any color tinting
                        // ((ImageView) v).clearColorFilter();

                        // Invalidates the view to force a redraw
                        v.invalidate();

                        // Does a getResult(), and displays what happened.
                        if (event.getResult()) {
                            Toast.makeText(mainActThis, "The drop was handled.", Toast.LENGTH_LONG);

                        } else {
                            Toast.makeText(mainActThis, "The drop didn't work.", Toast.LENGTH_LONG);

                        };

                        if(GlobalSettings.mainActivity) Log.d("myDragEventListener","onDrag, Unknown action type received by OnDragListener.");

                        // returns true; the value is ignored.
                        return(true);

                    // break;

                    // An unknown action type was received.
                    default:
                        Log.e("DragDrop Example","Unknown action type received by OnDragListener.");

                    break;
            };
            
            return true;
        }
    } // class myDragEventListener
    
    
    @SuppressLint("NewApi")
	private static class MyDragShadowBuilder extends View.DragShadowBuilder {

        // The drag shadow image, defined as a drawable thing
        private static Drawable shadow;

            // Defines the constructor for myDragShadowBuilder
            @SuppressLint("NewApi")
			public MyDragShadowBuilder(View v) {

                // Stores the View parameter passed to myDragShadowBuilder.
                super(v);

                // Creates a draggable image that will fill the Canvas provided by the system.
                shadow = new ColorDrawable(Color.LTGRAY);
            }

            // Defines a callback that sends the drag shadow dimensions and touch point back to the
            // system.
            @Override
            public void onProvideShadowMetrics (Point size, Point touch) {
                // Defines local variables
                int width;
				int height;

                // Sets the width of the shadow to half the width of the original View
                width = getView().getWidth() * 2; // / 2;

                // Sets the height of the shadow to half the height of the original View
                height = getView().getHeight() * 2; //  / 2;

                // The drag shadow is a ColorDrawable. This sets its dimensions to be the same as the
                // Canvas that the system will provide. As a result, the drag shadow will fill the
                // Canvas.
                shadow.setBounds(0, 0, width, height);

                // Sets the size parameter's width and height values. These get back to the system
                // through the size parameter.
                size.set(width, height);

                // Sets the touch point's position to be in the middle of the drag shadow
                touch.set(width / 2, height / 2);
            }

            // Defines a callback that draws the drag shadow in a Canvas that the system constructs
            // from the dimensions passed in onProvideShadowMetrics().
            @Override
            public void onDrawShadow(Canvas canvas) {

                // Draws the ColorDrawable in the Canvas passed in from the system.
                shadow.draw(canvas);
            }
        }
    
    
}
