package se.kd35a.blekingskaSangbok;

import java.util.ArrayList;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class ListSongsActivity extends ListActivity implements OnItemClickListener {
	private ArrayList<Song> songs;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		DatabaseHelper dbHelper = new DatabaseHelper(this);
		//dbHelper.addDefaultSongs();
		dbHelper.populateDatabase();
		songs = dbHelper.getSongs();
		setListAdapter(new SongArrayAdapter(this, songs));

		ListView lv = getListView();
		lv.setTextFilterEnabled(true);
		lv.setOnItemClickListener(this);
	}


	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		Intent intent = new Intent(this, ViewSongActivity.class);
		intent.putExtra("song", songs.get(position).getId());
		startActivity(intent);
	}

}
