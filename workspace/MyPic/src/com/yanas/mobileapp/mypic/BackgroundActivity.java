package com.yanas.mobileapp.mypic;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class BackgroundActivity extends Activity {
	
	int rows = 5;
	int cols = 5;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_background);
		
	    LinearLayout mainLayout = new LinearLayout(this);
	    mainLayout.setOrientation(LinearLayout.VERTICAL);

		if(GlobalSettings.BackgroundActivity) Log.d("BackgroundActivity", "onCreate ");
	    TextView backgroundText = new TextView(this);
	    backgroundText.setText("Select Background" );
	    
	    LinearLayout buildingNameLay = new LinearLayout(this);
	    buildingNameLay.setBackgroundColor(Color.CYAN);
	    buildingNameLay.setHorizontalGravity(LinearLayout.TEXT_ALIGNMENT_CENTER);
	    buildingNameLay.addView(backgroundText);

	    mainLayout.addView(buildingNameLay, new ViewGroup.LayoutParams (
        		ViewGroup.LayoutParams.MATCH_PARENT, 
        		ViewGroup.LayoutParams.WRAP_CONTENT));
	    	    
	    BackgroundLayoutGenerator backgroundLO = new BackgroundLayoutGenerator(rows,cols,0,this);
	    backgroundLO.generateTableLayout();
	    initBackgroundButtonTable(backgroundLO);
	    
	    LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,  //.FILL_PARENT,
	            LayoutParams.WRAP_CONTENT);
		mainLayout.addView(backgroundLO.getTableLO(), params);

		addContentView(mainLayout, new ViewGroup.LayoutParams (
        		ViewGroup.LayoutParams.WRAP_CONTENT, 
        		ViewGroup.LayoutParams.WRAP_CONTENT));


	}

	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.background, menu);
		return true;
	}

	int colors[] = {Color.BLACK,
					Color.RED,
					Color.GREEN,
					Color.WHITE,
					Color.BLUE,
					Color.GRAY,
					Color.CYAN,
					Color.DKGRAY,
					Color.MAGENTA,
					Color.TRANSPARENT,
					Color.YELLOW
	};
    public void initBackgroundButtonTable(BackgroundLayoutGenerator backgroundLO) {

    	int tableChildCnt = backgroundLO.getTableLO().getChildCount();
    	View rowChild ;
    	View cellChild ;
    	int cellCount = 0;
    	int numColors = colors.length;
    	int colorCount = 0;
    	
    	for(int i = 0; i < tableChildCnt; i++) {
    		rowChild = backgroundLO.getTableLO().getChildAt(i);
    		if(rowChild instanceof TableRow) {
    			cellCount = ((TableRow) rowChild).getChildCount();
    			for(int j = 0; j < cellCount; j++) {
    				cellChild = ((TableRow) rowChild).getChildAt(j);
    				if(cellChild instanceof Button) {
    					if(colorCount < numColors) {
	    					cellChild.setOnClickListener(new ButtonClickListener(colors[colorCount]));
	    					cellChild.setBackgroundColor(colors[colorCount]);
	    					colorCount++;
    					}
    				}
    			}
    			
    		} // if(rowChild instanceof TableRow)
    	} // for - tableChildCnt
	
    }


    public class ButtonClickListener implements OnClickListener {
    	int color;
    	public ButtonClickListener(int color_in)  {
    		color = color_in;
    	}
    	
    	@Override
    	public void onClick(View v) {
			Intent intent = new Intent();
			intent.putExtra(MainActivity.BACKGROUND, color );
			setResult(Activity.RESULT_OK, intent);
			finish();				
    		
    	}
    }



}
