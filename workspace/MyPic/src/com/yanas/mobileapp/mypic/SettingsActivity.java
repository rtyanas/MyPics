package com.yanas.mobileapp.mypic;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class SettingsActivity extends Activity {
	
	MessageData messageD;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);
		
		messageD = (MessageData)getIntent().getSerializableExtra(MainActivity.MESSAGE);
		if(messageD == null)
			messageD = new MessageData();
		
		setupSettingsActivity();

		Button button = (Button)findViewById(R.id.done);
		button.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				Intent intent = new Intent();
				messageD.setMessage(
						((EditText)findViewById(R.id.message_text)).getText().toString());
				intent.putExtra(MainActivity.MESSAGE, messageD );
//				intent.putExtra(MainActivity.MESSAGE, ((TextView)findViewById(R.id.message_text)).getText().toString() );
//				intent.putExtra(MainActivity.FONT, ((TextView)findViewById(R.id.font)).getText().toString() );
//				intent.putExtra(MainActivity.STYLE, ((TextView)findViewById(R.id.style)).getText().toString() );
//				intent.putExtra(MainActivity.SIZE, ((TextView)findViewById(R.id.size)).getText().toString() );
//				intent.putExtra(MainActivity.COLOR, ((TextView)findViewById(R.id.color)).getText().toString() );
//				intent.putExtra(MainActivity.ROTATE, ((TextView)findViewById(R.id.rotate)).getText().toString() );
				
				setResult(Activity.RESULT_OK, intent);
								
				finish();
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.settings, menu);
		return true;
	}
	
	private void setupSettingsActivity() {
		int pos=0;
		
		EditText et = (EditText)findViewById(R.id.message_text);
		et.setText(messageD.getMessage());
		
		// Font
		Spinner spinFont = (Spinner)findViewById(R.id.font);
		ArrayAdapter<CharSequence> adapterF = ArrayAdapter.createFromResource(this,
		        R.array.Fonts, android.R.layout.simple_spinner_item);
		adapterF.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinFont.setAdapter(adapterF);
		spinFont.setOnItemSelectedListener(new FontOnItemSelectedListener());
		pos = ((ArrayAdapter)spinFont.getAdapter()).getPosition(messageD.getmFont());
		spinFont.setSelection(pos);
		// Style
		Spinner spinStyle = (Spinner)findViewById(R.id.style);
		ArrayAdapter<CharSequence> adapterS = ArrayAdapter.createFromResource(this,
		        R.array.Style, android.R.layout.simple_spinner_item);
		adapterS.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinStyle.setAdapter(adapterS);
		spinStyle.setOnItemSelectedListener(new StyleOnItemSelectedListener());
		spinStyle.setSelection(messageD.getmStyle());
		// Color
		Spinner spinColor = (Spinner)findViewById(R.id.color);
		ArrayAdapter<CharSequence> adapterC = ArrayAdapter.createFromResource(this,
		        R.array.Color, android.R.layout.simple_spinner_item);
		adapterC.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinColor.setAdapter(adapterC);
		spinColor.setOnItemSelectedListener(new ColorOnItemSelectedListener());
		pos = ((ArrayAdapter)spinColor.getAdapter()).getPosition(messageD.colorToString(messageD.getmColor()));
		spinColor.setSelection(pos);
		// Size
		Spinner spinSize = (Spinner)findViewById(R.id.size);
		ArrayAdapter<CharSequence> adapterSz = ArrayAdapter.createFromResource(this,
		        R.array.Size, android.R.layout.simple_spinner_item);
		adapterSz.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinSize.setAdapter(adapterSz);
		spinSize.setOnItemSelectedListener(new SizeOnItemSelectedListener());
		pos = ((ArrayAdapter)spinSize.getAdapter()).getPosition(Integer.toString(messageD.getmSize()));
		spinSize.setSelection(pos);

		// Rotate
		Spinner spinRotate = (Spinner)findViewById(R.id.rotate);
		ArrayAdapter<CharSequence> adapterR = ArrayAdapter.createFromResource(this,
		        R.array.Rotate, android.R.layout.simple_spinner_item);
		adapterR.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinRotate.setAdapter(adapterR);
		spinRotate.setOnItemSelectedListener(new RotateOnItemSelectedListener());
		pos = ((ArrayAdapter)spinRotate.getAdapter()).getPosition(Integer.toString(messageD.getRotate()));
		spinRotate.setSelection(pos);
	}
	
	public class FontOnItemSelectedListener implements OnItemSelectedListener {

		@Override
		public void onItemSelected(AdapterView<?> parent, View view, int pos,
				long id) {
			if(GlobalSettings.settingsActivity) Log.d("SettingsActivity", "FontOnItemSelectedListener Selected Item:"+
					parent.getItemAtPosition(pos));
			messageD.setmFont((String)parent.getItemAtPosition(pos));
		}

		@Override
		public void onNothingSelected(AdapterView<?> parent) {
			if(GlobalSettings.settingsActivity) Log.d("SettingsActivity", "FontOnItemSelectedListener Nothing selected:");
		}
		
	}

	public class StyleOnItemSelectedListener implements OnItemSelectedListener {

		@Override
		public void onItemSelected(AdapterView<?> parent, View view, int pos,
				long id) {
			if(GlobalSettings.settingsActivity) Log.d("SettingsActivity", "setupSettingsActivity Selected Item:"+
					parent.getItemAtPosition(pos));
			messageD.setmStyle(pos);
		}

		@Override
		public void onNothingSelected(AdapterView<?> parent) {
			// TODO Auto-generated method stub
			
		}
		
	}

	public class ColorOnItemSelectedListener implements OnItemSelectedListener {

		@Override
		public void onItemSelected(AdapterView<?> parent, View view, int pos,
				long id) {
			if(GlobalSettings.settingsActivity) Log.d("SettingsActivity", "setupSettingsActivity Selected Item:"+
					parent.getItemAtPosition(pos));
			messageD.setmColor(messageD.stringToColor((String)parent.getItemAtPosition(pos)));
		}

		@Override
		public void onNothingSelected(AdapterView<?> parent) {
			// TODO Auto-generated method stub
			
		}
		
	}

	public class SizeOnItemSelectedListener implements OnItemSelectedListener {

		@Override
		public void onItemSelected(AdapterView<?> parent, View view, int pos,
				long id) {
			if(GlobalSettings.settingsActivity) {
				Log.d("SettingsActivity", "onItemSelected Selected Item:"+
						parent.getItemAtPosition(pos) +", messageD: "+ messageD);
			}
			messageD.setmSize(Integer.parseInt((String)parent.getItemAtPosition(pos)));
		}

		@Override
		public void onNothingSelected(AdapterView<?> parent) {
			// TODO Auto-generated method stub
			
		}
		
	}

	public class RotateOnItemSelectedListener implements OnItemSelectedListener {

		@Override
		public void onItemSelected(AdapterView<?> parent, View view, int pos,
				long id) {
			if(GlobalSettings.settingsActivity) Log.d("SettingsActivity", "onItemSelected Selected Item:"+
					parent.getItemAtPosition(pos));
			messageD.setRotate(Integer.parseInt((String)parent.getItemAtPosition(pos)));			
		}

		@Override
		public void onNothingSelected(AdapterView<?> parent) {
			// TODO Auto-generated method stub
			
		}
		
	}

}
