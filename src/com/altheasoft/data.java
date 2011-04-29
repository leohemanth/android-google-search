package com.altheasoft;

import android.util.Log;

public class data {
	int width;
	int height;
	String imageId;
	String unescapedUrl;
	String url;
	String visibleUrl;
	String title;
	String titleNoFormatting;

	data(int w, int h, String imageId, String unescapedUrl, String url,String visibleUrl,
			String title, String titleNoFormatting) {
		this.width = w;
		this.height = h;
		this.imageId = imageId;
		this.unescapedUrl = unescapedUrl;
		this.url=url;
		this.visibleUrl = visibleUrl;
		this.title = title;
		this.titleNoFormatting = titleNoFormatting;
		Log.w("data",w + h + imageId + unescapedUrl+url+visibleUrl+title+titleNoFormatting);
	}
}
