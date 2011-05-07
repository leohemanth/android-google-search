package com.altheasoft;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.R.drawable;
import android.app.Activity;
import android.app.ProgressDialog;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class PopulationOfImages extends ListActivity {
	TextView selection;
	Bitmap bitmap;
	Button more;
	ProgressDialog pd;
	int imageDown[] = new int[1000];
	Drawable d[] = new Drawable[100];
	public List<data> dataList = new ArrayList<data>();
	String a;
	int n = 0;
	IconicAdapter i;

	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		setContentView(R.layout.list);
		i = new IconicAdapter();
		a = getIntent().getStringExtra("input");
		selection = (TextView) findViewById(R.id.selection);
		setListAdapter(i);

		try {
			populate();
		} catch (IOException e) {
			Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show();
			e.printStackTrace();
		} catch (JSONException e) {
			Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show();
			e.printStackTrace();
		}

		more = (Button) findViewById(R.id.button1);

		more.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				try {
					populate();
				} catch (IOException e) {
					e.printStackTrace();
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		});
	}

	private void populate() throws IOException, JSONException {

		selection.setText("inside pop");
		JSONObject jObject;
		URL url;
		url = new URL(
				"http://ajax.googleapis.com/ajax/services/search/images?v=1.0&q="
						+ a + "&rsz=4&start=" + n);
		selection.setText(url + " ");
		n += 4;
		BufferedReader reader = new BufferedReader(new InputStreamReader(
				url.openStream()));
		String line;
		StringBuffer output = new StringBuffer();
		while ((line = reader.readLine()) != null) {
			output.append(line);
		}
		reader.close();
		jObject = new JSONObject(new String(output));
		JSONObject respObject = jObject.getJSONObject("responseData");
		JSONArray resObjects = respObject.getJSONArray("results");

		for (int i = 0; i < resObjects.length(); i++) {
			JSONObject j = resObjects.getJSONObject(i);

			this.i.add(new data(j.getInt("tbWidth"), j.getInt("tbHeight"), j
					.getString("imageId"), j.getString("unescapedUrl"), j
					.getString("tbUrl"), j.getString("url"), j
					.getString("title"), j.getString("titleNoFormatting")));
		}
	}

	private Drawable DownloadImage(String URL) {
		Drawable d = null;
		try {
			URL urls = new URL(URL);
			InputStream is = (InputStream) urls.getContent();
			d = Drawable.createFromStream(is, "src");
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			Toast.makeText(PopulationOfImages.this, "inside image error",
					Toast.LENGTH_LONG).show();
			e1.printStackTrace();
		}
		return d;
	}

	public void onListItemClick(ListView parent, View v, int position, long id) {
	//	selection.setText(dataList.get(position).url);
		Intent browserIntent = new Intent("android.intent.action.VIEW",
				Uri.parse(dataList.get(position).visibleUrl));
		startActivity(browserIntent);

	}

	class IconicAdapter extends ArrayAdapter<data> implements Runnable {
		IconicAdapter() {
			super(PopulationOfImages.this, R.layout.pictures, dataList);
		}

		int th = 0;
		int download = 0;

		public View getView(int position, View convertView, ViewGroup parent) {
			Thread downloadThread = new Thread(this);
			LayoutInflater inflater = getLayoutInflater();
			View row = inflater.inflate(R.layout.pictures, parent, false);
			TextView label = (TextView) row.findViewById(R.id.label);
			label.setText(dataList.get(position).visibleUrl);
			ImageView icon = (ImageView) row.findViewById(R.id.icon);
			if (imageDown[position] == 0) {
				icon.setImageResource(R.drawable.tulips);
				row.setTag("dummy");
				if (th == 0) {
					downloadThread.start();
				}
				th = 1;
			} else {
				row.setTag("image");
				icon.setImageDrawable(d[position]);
				LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(dataList.get(position).width, dataList.get(position).width);
				icon.setLayoutParams(layoutParams);
			}
			return (row);
		}

		@Override
		public void run() {
			int i = download;
			if (imageDown[i] == 0) {
				d[i] = DownloadImage(dataList.get(i).url);
				imageDown[i] = 1;
			}
			download++;
			th = 0;
			handler.sendEmptyMessage(i);
		}

		private Handler handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				//update the adapter
				i.notifyDataSetChanged();
			}
		};

	}
}