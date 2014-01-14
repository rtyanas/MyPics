package com.yanas.mobileapp.mypic;

import java.io.Serializable;
import java.util.HashMap;

import android.graphics.Color;
import android.graphics.Typeface;
import android.util.Log;

public class MessageData implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private long id;
	private String message ;
	private int mSize ;
	private String mFont;
	private int mStyle;
	private int mColor;
	private int rotate;
	private int background;
	private float textX, textY;

	private String pic;
	private HashMap<Integer, String> colorIntStr;
	private HashMap<String, Integer> colorStrInt;

	
	public MessageData() {
		this("You look fabulous", 18, "Sans Serif", 
				Typeface.ITALIC, Color.BLACK, 
				0,  "", Color.WHITE, 0.0f, 0.0f);
	}
	  
	  
	public MessageData(	long id_in, String message_in, int mSize_in, String mFont_in, 
						int mStyle_in, int mColor_in, 
						int rotate_in, String pic_in, int bg_in, float textX, float textY) {
		id = id_in;
		init(	message_in, mSize_in, mFont_in, 
				mStyle_in, mColor_in, 
				rotate_in,  pic_in, bg_in, textX, textY);
	}
	  
	  
	public MessageData(	String message_in, int mSize_in, String mFont_in, 
						int mStyle_in, int mColor_in, 
						int rotate_in,  String pic_in, int bg_in, float textX, float textY) {
		id = 0;
		init(	message_in, mSize_in, mFont_in, 
				mStyle_in, mColor_in, 
				rotate_in,  pic_in, bg_in, textX, textY);
	}
	  
	  
	public void init(	String message_in, int mSize_in, String mFont_in, 
						int mStyle_in, int mColor_in, 
						int rotate_in,  String pic_in, int bg_in, float textX_in, float textY_in) {
		message = message_in;
		mSize = mSize_in;
		mFont = mFont_in;
		mStyle = mStyle_in;
		mColor = mColor_in; 
		rotate = rotate_in; 
		background = bg_in; 
		textX = textX_in;
		textY = textY_in;
		
		pic = pic_in;
		
		colorIntStr = new HashMap<Integer, String>();
		colorIntStr.put(Color.BLACK, "Black"); 
		colorIntStr.put(Color.RED, "Red");
        colorIntStr.put(Color.GREEN , "Green");
        colorIntStr.put(Color.WHITE, "White");
        colorIntStr.put(Color.BLUE, "Blue");
        colorIntStr.put(Color.GRAY, "Grey");
        colorIntStr.put(Color.CYAN, "Cyan");
        colorIntStr.put(Color.DKGRAY, "Dark Gray");
        colorIntStr.put(Color.MAGENTA, "Magenta");
        colorIntStr.put(Color.TRANSPARENT, "Transparent");
        colorIntStr.put(Color.YELLOW, "Yellow");

		colorStrInt = new HashMap<String, Integer>();
		colorStrInt.put("Black", Color.BLACK); 
		colorStrInt.put("Red", Color.RED);
        colorStrInt.put("Green", Color.GREEN);
        colorStrInt.put("White", Color.WHITE);
        colorStrInt.put("Blue", Color.BLUE);
        colorStrInt.put("Grey", Color.GRAY);
        colorStrInt.put("Cyan", Color.CYAN);
        colorStrInt.put("Dark Gray", Color.DKGRAY);
        colorStrInt.put("Magenta", Color.MAGENTA);
        colorStrInt.put("Transparent", Color.TRANSPARENT);
        colorStrInt.put("Yellow", Color.YELLOW);
        
	}


	public long getId() {
		return id;
	}


	public void setId(long id) {
		this.id = id;
	}


	public String getMessage() {
		return message;
	}


	public void setMessage(String message) {
		this.message = message;
	}


	public int getmSize() {
		return mSize;
	}


	public void setmSize(int mSize) {
		this.mSize = mSize;
	}


	public String getmFont() {
		return mFont;
	}


	public void setmFont(String mFont) {
		this.mFont = mFont;
	}


	public int getmStyle() {
		return mStyle;
	}


	public void setmStyle(int mStyle) {
		this.mStyle = mStyle;
	}


	public int getmColor() {
		return mColor;
	}


	public void setmColor(int mColor) {
		this.mColor = mColor;
	}


	public int getRotate() {
		return rotate;
	}


	public void setRotate(int rotate) {
		this.rotate = rotate;
	}


	public String getPic() {
		return pic;
	}


	public void setPic(String pic) {
		this.pic = pic;
	}

	
	public int getBackground() {
		return background;
	}


	public void setBackground(int background) {
		this.background = background;
	}


	public float getTextX() {
		return textX;
	}


	public void setTextX(float textX) {
		this.textX = textX;
	}


	public float getTextY() {
		return textY;
	}


	public void setTextY(float textY) {
		this.textY = textY;
	}


	static public Typeface stringToTypeFace(String typeFaceStr) {
		Typeface typeFaceRet = Typeface.DEFAULT;
		
		if(typeFaceStr.equals("Normal")) {
			typeFaceRet = Typeface.DEFAULT;
		}
		else if(typeFaceStr.equals("Normal Bold")) {
			typeFaceRet = Typeface.DEFAULT_BOLD;
		}
		else if(typeFaceStr.equals("Monospace")) {
			typeFaceRet = Typeface.MONOSPACE;
		}
		else if(typeFaceStr.equals("Sans Serif")) {
			typeFaceRet = Typeface.SANS_SERIF;
		}
		else if(typeFaceStr.equals("Serif")) {
			typeFaceRet = Typeface.SERIF;
		}
		
		return typeFaceRet;
	}

	public int stringToColor(String colorStr) {

		if(colorStrInt != null) {
			return colorStrInt.get(colorStr);
		}
		// Default
        if(GlobalSettings.messageData) Log.i("MessageData", "stringToColor() - using default color.");
        return Color.BLACK;

	}

	public String colorToString(int color) {
		if(colorIntStr != null) {
			return colorIntStr.get(color);
		}
        
        // Default
        if(GlobalSettings.messageData) Log.i("MessageData", "stringToColor() - using default color.");
        return "Black";
	}

	public String toString() {
		String outStr;
		
		outStr = "\""+ message +"\"" +
				", Size:"+ mSize +
				", Font:"+ mFont +
				", Style:"+ mStyle +
				", Color:"+ mColor +
				", X:"+ textX + ", Y:"+textY +
				", Rotate:"+ rotate +
				", Pic:"+ pic;

		return outStr;
	}
	
}
