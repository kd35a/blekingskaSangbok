package se.kd35a.blekingskaSangbok;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;

public class ListSongsActivity extends ListActivity implements OnItemClickListener {
	private ArrayList<Song> songs;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		DatabaseHelper dbHelper = new DatabaseHelper(this);
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
	
    @Override
	public boolean onCreateOptionsMenu(Menu menu) {
    	MenuInflater inflater = getMenuInflater();
    	inflater.inflate(R.menu.menu, menu);
    	return true;
    }
    
    @Override
	public boolean onOptionsItemSelected(MenuItem item) {
    	switch (item.getItemId()) {
		case R.id.add_url:
			addFromUrl();
			return true;
		}
    	return super.onOptionsItemSelected(item);
    }


	private void addFromUrl() {
		AlertDialog.Builder alert = new AlertDialog.Builder(this);
		
		alert.setTitle(getString(R.string.add_url_title));
		alert.setMessage(getString(R.string.add_url_message));
		
		// Set an EditText view to get user input
		final EditText input = new EditText(this);
		alert.setView(input);
		
		final ListSongsActivity parrent = this;
		
		alert.setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				String url = input.getText().toString();
				DatabaseHelper dbHelper = new DatabaseHelper(parrent);
				dbHelper.addToDatabaseFromUrl(url, null);
				parrent.setListAdapter(new SongArrayAdapter(parrent, dbHelper.getSongs()));
				parrent.getWindow().getDecorView().getRootView().postInvalidate();
			}
		});
		
		alert.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				dialog.cancel();
			}
		});
		
		alert.show();
	}

}
