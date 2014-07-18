package com.yanas.mobileapp.mypic;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import com.yanas.mobileapp.mypic.datastore.MessageListDbData;
import com.yanas.mobileapp.mypic.datastore.MessageListDbHelper;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.app.Dialog;
import android.app.WallpaperManager;
import android.content.ClipData;
import android.content.ClipDescription;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.Display;
import android.view.DragEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

/*@TargetApi(Build.VERSION_CODES.HONEYCOMB)*/ public class MainActivity extends Activity {

    private static final int SELECT_PICTURE = 1;
    private static final int MESSAGE_SETTINGS = 2;
    public static final int BACKGROUND_SETTINGS = 3;
//    private String selectedImagePath;
    private Uri selectedImageUri = null;

    private ImageView img;
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
    private int widthDisplay;
    private int heightDisplay;

    TextView tv; // messageTV;
    
    @SuppressLint("NewApi") @TargetApi(Build.VERSION_CODES.HONEYCOMB) 
    // Added if statement to check build version
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mainActThis = this;
        
    	if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB ) 
    	{
            Display display = getWindowManager().getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            widthDisplay = size.x;
            heightDisplay = size.y;    		
    	}
    	else
    	{
            widthDisplay = this.widthDisplay;
            heightDisplay = this.heightDisplay;    		
    	}

        
        messData = retrieveSettings(); // new MessageData();
        // selectedImagePath = messData.getPic();

        selectedImageUri = Uri.parse(messData.getPic());


        // Set up the action bar.
//        final ActionBar actionBar = getActionBar();
//        setTitle("MyPic");
//        if(actionBar != null) {
//            actionBar.setIcon(new ColorDrawable(getResources().getColor(android.R.color.transparent)));
//            actionBar.setHomeButtonEnabled(false);
//        }

        
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
        // Define local to be compatible with Froyo level 8
// 	    final View.DragShadowBuilder myShadow = new MyDragShadowBuilder(tv);
        
        tv.setTag(TEXT_TAG);
        tv.setOnLongClickListener( new View.OnLongClickListener() {

        		    // Defines the one method for the interface, which is called when the View is long-clicked
        		    public boolean onLongClick(View v) {

	        		    // Create a new ClipData.
	        		    // This is done in two steps to provide clarity. The convenience method
	        		    // ClipData.newPlainText() can create a plain text ClipData in one step.
	
	        		    // Create a new ClipData.Item from the ImageView object's tag
	        		    
        		    	if(GlobalSettings.mainActivity) Log.d("MainActivity, onLongClick", 
        		    			"Build.VERSION.SDK_INT:"+Build.VERSION.SDK_INT +", Build.VERSION_CODES.HONEYCOMB:"+ Build.VERSION_CODES.HONEYCOMB);
        		    	if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB ) {
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
        		                       
        		            		// Define shadow local to make compatible with FROYO
        		            		// myShadow,  // the drag shadow builder
        		            		new View.DragShadowBuilder() {
				        		                // The drag shadow image, defined as a drawable thing
				        		                private Drawable shadow = new ColorDrawable(Color.LTGRAY);;

//								    			public View.DragShadowBuilder(View v) {
//						
//								                    // Stores the View parameter passed to myDragShadowBuilder.
//								                    super(v);
//						
//								                    // Creates a draggable image that will fill the Canvas provided by the system.
//								                    shadow = new ColorDrawable(Color.LTGRAY);
//								                }
						
								                // Defines a callback that sends the drag shadow dimensions and touch point back to the
								                // system.
								                @Override
								                public void onProvideShadowMetrics (Point size, Point touch) {
								                    // Defines local variables
								                    int width = 200;
								    				int height = 100;
						
								    				RelativeLayout rl = (RelativeLayout)mainActThis.findViewById(R.id.main_layout);
								    				
								                    if(rl != null) {
									                    // Sets the width of the shadow to half the width of the original View
								                    	width = tv.getWidth();
								                    	
									                    // Sets the height of the shadow to half the height of the original View
								                    	height = tv.getHeight();
								                    }
						
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

        		            				}, 
        		                        null,      // no need to use local data
        		                        0          // flags (not currently used, set to 0)
	
        	    		            );
        		    	}
    		            
    		            return true;

        		    }
        		} );

        setText();
        
    	if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB ) {
            vg.setOnDragListener(
            		// Define local to be able to use FROYO Level 8
            		// new myDragEventListener()
		            		new View.OnDragListener() {
		
		            			float textXoffset = 0.0f;
		            			float textYoffset = 0.0f;

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
		
		            	                    textXoffset = event.getX() - messData.getTextX(); 
		            	                    textYoffset = event.getY() - messData.getTextY(); 
		            	                    
		            	                    if(GlobalSettings.mainActivity) Log.d("myDragEventListener","onDrag, ACTION_DRAG_ENTERED."+
				            	                    " X:"+ event.getX() +", Y:"+ event.getY() +
				            	                    ", text X: "+  messData.getTextX() +", Y: "+  messData.getTextY() +
				            	                    ", offSet X: "+ textXoffset +", Y: "+ textYoffset );
		
		            	                    // Applies a green tint to the View. Return true; the return value is ignored.
		
		            	                    // ((ImageView) v).setColorFilter(Color.GREEN);
		
		            	                    // Invalidate the view to force a redraw in the new tint
		            	                    v.invalidate();
		
		            	                    return(true);
		
		            	                    // break;
		
		            	               case DragEvent.ACTION_DRAG_LOCATION:
		
		            	                    if(GlobalSettings.mainActivity) Log.d("myDragEventListener","onDrag, ACTION_DRAG_LOCATION. X:"+ 
		            	                    									event.getX() + ", Y: "+ event.getY());
//		            	                    float currxPos = event.getX();
//		            	                    float newxPos = 0.0f;
//		            	                    float curryPos = event.getY();
//		            	                    float newyPos = 0.0f;
//		            	                    
//		            	                    if(currxPos > 700)
//		            	                    	newxPos = currxPos - 700;
//		            	                    else if(currxPos > 600)
//		            	                    	newxPos = currxPos - 600;
//		            	                    else if(currxPos > 400)
//		            	                    	newxPos = currxPos - 400;
//		            	                    else if(currxPos > 200)
//		            	                    	newxPos = currxPos - 200;
//		            	                    else if(currxPos > 100)
//		            	                    	newxPos = currxPos - 100;
//		            	                    else if(currxPos > 50)
//		            	                    	newxPos = currxPos - 50;
//		            	                    else if(currxPos > 25)
//		            	                    	newxPos = currxPos - 25;
//		            	                    
//		            	                    if(curryPos > 30)
//		            	                    	newyPos = curryPos - 30;
		            	                    
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
		
		            	                        if(GlobalSettings.mainActivity) Log.d("myDragEventListener","onDrag, ACTION_DRAG_ENDED." );
		
		            	                        messData.setTextX(messData.getTextX() - textXoffset);
		            	                        messData.setTextY(messData.getTextY() - textYoffset);

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
		            	    
		            	    

            		
            		);    		
    	}
    	
        vg.addView(tv);

        img = (ImageView)findViewById(R.id.mypic);
        img.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"Select Picture"), SELECT_PICTURE);
            }
        });
        
        setPic(messData.getPic());
    } // onCreate()


    public final static String DIALOGMESSG = "DialogMessg";

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
    	
        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_PICTURE) {
                selectedImageUri = data.getData();
                // selectedImagePath = getPath(selectedImageUri);
                String logStr = "OnAct - "+ // Image Path : " + selectedImagePath +
                		        "\nselectedImageUri: "+ selectedImageUri.toString();
                if(GlobalSettings.mainActivity) {
                	Toast.makeText(this, logStr, Toast.LENGTH_LONG).show();
                	Log.d("MainActivity", logStr);
                }
                
//                for(int i=0; i < 2; i++) {
//                    Toast.makeText(this, logStr, Toast.LENGTH_LONG).show();
//                }
                
                if(selectedImageUri != null )
                {
            		messData.setPic(selectedImageUri.toString());
            		saveNewSettings(messData);
                }

                // BitmapFactory.Options options = new BitmapFactory.Options();
                // options.inSampleSize = 100;
                // options.inDensity = 200;

/*
                InputStream is = null;
				try {
					if(selectedImageUri != null)
					{
						is = getContentResolver().openInputStream(selectedImageUri);
		                if(is != null) 
		                {
	                        Toast.makeText(this, "Using InputStream", Toast.LENGTH_LONG).show();
		                	Bitmap bitmap = BitmapFactory.decodeStream(is);
		                	try {
								is.close();
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
		                	setImageWithBitmap(bitmap);
		                	
		                }						
					}
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

                if (is == null) {
                    Bitmap bitmap = setImage(selectedImagePath);
                    if(bitmap != null) {
                		messData.setPic(selectedImagePath);
                		saveNewSettings(messData);                	
                    }
                	
                } else {
            		messData.setPic(selectedImagePath);
            		saveNewSettings(messData);                	
                }
                
                
//                Bitmap bitmap = BitmapFactory.decodeFile(selectedImagePath);
//                if(bitmap != null) {
//    				Matrix m = img.getImageMatrix();
//    				m.postRotate(messData.getRotate());
//    				bitmap = Bitmap.createBitmap(bitmap, 0, 0, 
//    						  bitmap.getWidth(), bitmap.getHeight(), m, true);
//    				img.setImageBitmap(bitmap );
//
////                    // img.setImageBitmap(BitmapFactory.decodeFile(selectedImagePath) );                	
//            		messData.setPic(selectedImagePath);
//            		saveNewSettings(messData);
//                }
 * 
 */
            } // requestCode == SELECT_PICTURE
            else if(requestCode == MESSAGE_SETTINGS) {
        		messData = (MessageData)data.getSerializableExtra(MainActivity.MESSAGE);
        		messData.setPic(selectedImageUri.toString());
        		saveNewSettings(messData);
        		setText();
        		
        		if(GlobalSettings.mainActivity) {
            		
            		Log.d("MainActivity", "onActivityResult "+ messData);
            	}
            } // requestCode == MESSAGE_SETTINGS
        } // resultCode == RESULT_OK
    }

//    public String getPath(Uri uri) {
//        
//    	Cursor cursor = null;
//
//        try {
//        String[] projection = { MediaStore.Images.Media.DATA };
//        String scheme = uri.getScheme();
//        String pathLoc = uri.getPath();
//        
//        // cursor = managedQuery(uri, projection, null, null, null);
//        cursor = getContentResolver().query(uri, projection, null, null, null);
//        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
//        cursor.moveToFirst();
//        return cursor.getString(column_index);
//    	} finally {
//    		if(cursor != null)
//    			cursor.close();
//    	}
//    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	String helpInfo;
    	// Handle item selection    	
        switch (item.getItemId()) {
        case R.id.wallpaper_item:
            WallpaperManager myWallpaperManager
            = WallpaperManager.getInstance(getApplicationContext());

            Bitmap bitmap;
        	View v1 = img.getRootView();
        	v1.setDrawingCacheEnabled(true);
        	bitmap = Bitmap.createBitmap(v1.getDrawingCache());
        	v1.setDrawingCacheEnabled(false);
            try {
				myWallpaperManager.setBitmap(bitmap);
			} catch (IOException e1) {
		    	Toast.makeText(this, "Error, wallpaper not set", Toast.LENGTH_LONG);
				
				if(GlobalSettings.mainActivity) Log.d("mainActivity onOptionsItemSelected", e1.getMessage());
			}

            return true;
        case R.id.load_new_pic:
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent,"Select Picture"), SELECT_PICTURE);
            return true;
        case R.id.setting_menu_item:
       		startSettingsActivity();
            return true;
        case R.id.tips_menu_item:
			helpInfo = "Change picture by selecting image.  \nMove text by using long click.";
			Toast.makeText(this, helpInfo, Toast.LENGTH_LONG).show();
            return true;
        case R.id.version_menu_item:
			PackageInfo pInfo;
			String version = "";
			try {
				pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
				version = pInfo.versionName;
				
			} catch (NameNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        	
			helpInfo = version + ", Android release "+ android.os.Build.VERSION.RELEASE;
			Toast.makeText(this, helpInfo, Toast.LENGTH_LONG).show();
            return true;
        default:
//            	Toast d = Toast.makeText(this, "Default menu", Toast.LENGTH_LONG);
//            	d.show();
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

    	if(GlobalSettings.mainActivity) 
    		Toast.makeText(this, "selectedImageUri: "+ selectedImageUri, Toast.LENGTH_LONG).show();

			if(/* selectedImageUri != null &&  */  !  "".equals(selectedImageUri.toString()) ) {
				if(this.getContentResolver() != null)
				{
					new ImageLoad().execute(selectedImageUri);
					// loadUriImageExec(selectedImageUri);
				}
				else 
				{
			    	if(GlobalSettings.mainActivity) 
			    		Toast.makeText(this, "getContentResolver() is null!", Toast.LENGTH_LONG).show();					
				}
			}

    }
    
    
    /**
     * Load image using AsyncTask
     * @param uri_in
     */
//    private void loadUriImageExec(Uri uri_in) {
//		try {
//        InputStream is = null;
//		is = getContentResolver().openInputStream(uri_in);
//        if(is != null) {
//        	Bitmap bitmap = BitmapFactory.decodeStream(is);
//        	try {
//				is.close();
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//        	setImageWithBitmap(bitmap);	
//        }									
//		} catch (FileNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			Toast.makeText(this, "File not found: "+ selectedImageUri.toString(), Toast.LENGTH_SHORT).show();
//		}	
//    }
    
    
    public class ImageLoad extends AsyncTask<Uri, Integer, Bitmap > {

		@Override
		protected Bitmap doInBackground(Uri... uri_in) {
        	Bitmap bitmap = null;
			try {
		        InputStream is = null;
				is = getContentResolver().openInputStream(uri_in[0]);
		        if(is != null) {
		        	bitmap = BitmapFactory.decodeStream(is);
		        	try {
						is.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
		        }									
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					Toast.makeText(MainActivity.this, "File not found: "+ selectedImageUri.toString(), Toast.LENGTH_SHORT).show();
				}	
			return bitmap;
		}
    	
		@Override
		protected void onPostExecute(Bitmap resultBitmap) {
        	setImageWithBitmap(resultBitmap);	
			
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
    
 	@TargetApi(Build.VERSION_CODES.HONEYCOMB) 
 	private void setText() {
    	// TextView tv = (TextView)findViewById(R.id.message_data);
    	String textData = messData.getMessage();
    	
    	if(textData.equals(MessageData.DEFAULT_MESSAGE)) {
    		textData = getString(R.string.fabulous);
    	}

    	tv.setText(textData);
        tv.setTextSize(messData.getmSize() );
        tv.setTextColor(messData.getmColor());
        tv.setTypeface(MessageData.stringToTypeFace(messData.getmFont() ), messData.getmStyle());

    	if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB ) {
    		if( messData.getTextX()  > widthDisplay || messData.getTextX() < 0 ) {
    			tv.setX(MessageData.textPosXDefault);
       			messData.setTextX(MessageData.textPosXDefault);
    		}
    		else
    			tv.setX(messData.getTextX());
    		
       		if( messData.getTextY()  > heightDisplay || messData.getTextY() < 0 ) {
       			tv.setY(MessageData.textPosYDefault);       			
       			messData.setTextY(MessageData.textPosYDefault);
       		}
       		else
       			tv.setY(messData.getTextY());
    	}
        
        View vg = (View)findViewById(R.id.main_layout) ;
        if(vg != null)
        	vg.setBackgroundColor(messData.getBackground());
        else
        	if(GlobalSettings.mainActivity) Log.e("MainActivity", "setText background not set Layout is null");
    }

    private void setPic(String picPath_in) {
        if(picPath_in != null) {
        	if( picPath_in.length() > 1) {
            	// img.setImageURI(Uri.parse(messData.getPic()));
        		setImage(picPath_in);
        	}
        	else { // messData.getPic().length() <= 1
        		img.setImageResource(R.drawable.default_image);
        	}
        }
    }

    private Bitmap setImage(String picPath_in) {
        Bitmap bitmap = BitmapFactory.decodeFile(picPath_in);
        
        if(bitmap != null) {
    		Matrix m = img.getImageMatrix();
    		m.postRotate(messData.getRotate());
    		bitmap = Bitmap.createBitmap(bitmap, 0, 0, 
    				  bitmap.getWidth(), bitmap.getHeight(), m, true);
    		img.setImageBitmap(bitmap );
        }
        else {
        	if(GlobalSettings.mainActivity) {
            	Toast t = Toast.makeText(this, "Error showing pic, bitmap is null.", Toast.LENGTH_LONG);
            	t.show();
        	}
        }
        
        return bitmap;
    }
    
    private void setImageWithBitmap(Bitmap bitmap_in) {
        
        if(bitmap_in != null) {
    		Matrix m = img.getImageMatrix();
    		m.postRotate(messData.getRotate());
    		bitmap_in = Bitmap.createBitmap(bitmap_in, 0, 0, 
    				  bitmap_in.getWidth(), bitmap_in.getHeight(), m, true);
    		img.setImageBitmap(bitmap_in );
        }
        else {
        	if(GlobalSettings.mainActivity) 
            	Toast.makeText(this, "Error showing pic, bitmap is null.", Toast.LENGTH_LONG).show();
        }
    }
    
    private void startSettingsActivity() {
        Intent intent = new Intent(this, SettingsActivity.class);
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
        	if(messListDbData.updateMessage(messData_in) == 0 && GlobalSettings.mainActivity)
        		Log.e("MainActivity", "saveNewSettings - Not saved");
        	else
        		Log.d("MainActivity", "saveNewSettings - Saved OK");

    	}
    	else {
        	if(messListDbData.createMessage(messData_in) == 0 && GlobalSettings.mainActivity)
        		Log.e("MainActivity", "saveNewSettings - Not saved");
        	else
        		Log.d("MainActivity", "saveNewSettings - Saved OK");
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

    /**
     * Move the text somewhere on the screen.
     * @author RT
     *
     */
    
    
/***
 ** Make compatible with Froyo Level, move to local to use if statments

    // @SuppressLint("NewApi")
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
    
    
    // @SuppressLint("NewApi")
	private static class MyDragShadowBuilder extends View.DragShadowBuilder {

        // The drag shadow image, defined as a drawable thing
        private static Drawable shadow;

            // Defines the constructor for myDragShadowBuilder
            // @SuppressLint("NewApi")
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
    
**/    
}
