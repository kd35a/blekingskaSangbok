package se.kd35a.blekingskaSangbok;

import java.util.LinkedList;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class ListActivity extends Activity {
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.list);
		
		DatabaseHelper dbHelper = new DatabaseHelper(this);
		dbHelper.addDefaultSongs();
		LinkedList<Song> songs = dbHelper.getSongs();
		
		TextView tv = (TextView)findViewById(R.id.listText);
		StringBuilder sb = new StringBuilder();
		int i = 0;
		for (Song s : songs) {
			sb.append(i).append(": ").append(s.getTitle()).append("\n");
			i++;
		}
		tv.setText(sb.toString());
	}

}
