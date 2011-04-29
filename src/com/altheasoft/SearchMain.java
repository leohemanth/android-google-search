package com.altheasoft;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class SearchMain extends Activity {
	EditText input;
	OnClickListener getImageBtnOnClick = new OnClickListener() {
		public void onClick(View view) {
			// Toast.makeText(SearchMain.this,
			// input.getText(),Toast.LENGTH_LONG).show();
			Intent population = new Intent(SearchMain.this,
					com.altheasoft.PopulationOfImages.class);
			String a = input.getText().toString();
			population.putExtra("input", a);
			startActivity(population);

		}
	};

	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		setContentView(R.layout.main);
		input = ((EditText) findViewById(R.id.imageUrl));
		Button getImageButton = (Button) findViewById(R.id.getImageButton);
		getImageButton.setOnClickListener(getImageBtnOnClick);

	}
}