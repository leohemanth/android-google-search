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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class PopulationOfImages extends ListActivity {

	/** Called when the activity is first created. */
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

		// Thread thread = new Thread(this);
		// thread.start();
		try {
			populate();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show();
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show();
			e.printStackTrace();
		}

		more = (Button) findViewById(R.id.button1);

		more.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				try {
					populate();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
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
		// Toast.makeText(PopulationOfImages.this, url+" ",
		// Toast.LENGTH_LONG).show();
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

			this.i.add(new data(j.getInt("width"), j.getInt("height"), j
					.getString("imageId"), j.getString("unescapedUrl"), j
					.getString("tbUrl"), j.getString("url"), j
					.getString("title"), j.getString("titleNoFormatting")));
		}
		// pd.dismiss();
		// Toast.makeText(PopulationOfImages.this, resObjects.length() +
		// " "+dataList.size(),Toast.LENGTH_LONG).show();

	}

	private void populate1() {
		try {
			JSONObject jObject;
			URL url;
			url = new URL(
					"http://ajax.googleapis.com/ajax/services/search/images?v=1.0&q="
							+ a + "&rsz=4&start=" + n);
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
			// Toast.makeText(PopulationOfImages.this, output,
			// Toast.LENGTH_LONG).show();

			// Toast.makeText(PopulationOfImages.this, resObjects.length() +
			// " "+dataList.size(),Toast.LENGTH_LONG).show();
			for (int i = 0; i < resObjects.length(); i++) {
				JSONObject j = resObjects.getJSONObject(i);

				this.i.add(new data(j.getInt("width"), j.getInt("height"), j
						.getString("imageId"), j.getString("unescapedUrl"), j
						.getString("tbUrl"), j.getString("url"), j
						.getString("title"), j.getString("titleNoFormatting")));
			}

		} catch (Exception e) { // TODO Auto-generated catch block
			// Toast.makeText(PopulationOfImages.this, e.toString(), 50).show();
			// t.setText(e.toString());
			e.printStackTrace();
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
		selection.setText(dataList.get(position).url);
		Intent browserIntent = new Intent("android.intent.action.VIEW", Uri.parse(dataList.get(position).visibleUrl));
		startActivity(browserIntent);

	}

	class IconicAdapter extends ArrayAdapter implements Runnable {
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
//				Toast.makeText(PopulationOfImages.this,
//						position + " inside if", Toast.LENGTH_SHORT).show();
				icon.setImageResource(R.drawable.tulips);
				if (th == 0) {
					downloadThread.start();
				}
				th = 1;
			} else {
//				Toast.makeText(PopulationOfImages.this,
//						"setting image" + position, Toast.LENGTH_SHORT).show();
				icon.setImageDrawable(d[position]);
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
//				Toast.makeText(PopulationOfImages.this,
//						msg.toString() + " handler", Toast.LENGTH_SHORT).show();
				i.notifyDataSetChanged();
				((TextView) findViewById(R.id.selection))
						.setText("inside handler");
			}
		};

	}
}