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

	private String pic;
	private HashMap<Integer, String> colorIntStr;

	
	public MessageData() {
		this("You look fabulous", 18, "Sans Serif", 
				Typeface.ITALIC, Color.BLACK, 
				0,  "");
	}
	  
	  
	public MessageData(	long id_in, String message_in, int mSize_in, String mFont_in, 
						int mStyle_in, int mColor_in, 
						int rotate_in, String pic_in) {
		id = id_in;
		init(	message_in, mSize_in, mFont_in, 
				mStyle_in, mColor_in, 
				rotate_in,  pic_in);
	}
	  
	  
	public MessageData(	String message_in, int mSize_in, String mFont_in, 
						int mStyle_in, int mColor_in, 
						int rotate_in,  String pic_in) {
		id = 0;
		init(	message_in, mSize_in, mFont_in, 
				mStyle_in, mColor_in, 
				rotate_in,  pic_in);
	}
	  
	  
	public void init(	String message_in, int mSize_in, String mFont_in, 
						int mStyle_in, int mColor_in, 
						int rotate_in,  String pic_in) {
		message = message_in;
		mSize = mSize_in;
		mFont = mFont_in;
		mStyle = mStyle_in;
		mColor = mColor_in; 
		rotate = rotate_in; 
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
        if(colorStr.equals("Black") ) {
        	return Color.BLACK;
        }
        else if(colorStr.equals("Red") ) {
        	return Color.RED;
        }
        else if(colorStr.equals("Green") ) {
        	return Color.GREEN;
        }
        else if(colorStr.equals("White") ) {
        	return Color.WHITE;
        }
        else if(colorStr.equals("Blue") ) {
        	return Color.BLUE;
        }
        else if(colorStr.equals("Grey") ) {
        	return Color.GRAY;
        }
        else if(colorStr.equals("Cyan") ) {
        	return Color.CYAN;
        }
        else if(colorStr.equals("Dark Gray") ) {
        	return Color.DKGRAY;
        }
        else if(colorStr.equals("Magenta") ) {
        	return Color.MAGENTA;
        }
        else if(colorStr.equals("Transparent") ) {
        	return Color.TRANSPARENT;
        }
        else if(colorStr.equals("Yellow") ) {
        	return Color.YELLOW;
        }
        
        // Default
        if(GlobalSettings.messageData) Log.i("MessageData", "stringToColor() - using default color.");
        return Color.BLACK;
	}

	public String colorToString(int color) {
        
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
				", Rotate:"+ rotate +
				", Pic:"+ pic;

		return outStr;
	}
	
}
