package se.kd35a.blekingskaSangbok;

import java.util.ArrayList;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class SongArrayAdapter extends ArrayAdapter<Song> {
	private ArrayList<Song> songs;
	private Activity context;

	public SongArrayAdapter(Activity context, ArrayList<Song> objects) {
		super(context, R.layout.list, objects);
		this.songs = objects;
		this.context = context;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = context.getLayoutInflater();
		View rowView = inflater.inflate(R.layout.list, null, true);
		
		TextView title = (TextView)rowView.findViewById(R.id.list_title);
		title.setText(songs.get(position).getTitle());
		
		TextView info = (TextView)rowView.findViewById(R.id.list_info);
		StringBuilder sb = new StringBuilder();
		if (!songs.get(position).getCredits().trim().equals(""))
			sb.append(songs.get(position).getCredits().trim());
		if (!songs.get(position).getCredits().trim().equals("") &&
				!songs.get(position).getMelody().trim().equals(""))
			sb.append(", ");
		if (!songs.get(position).getMelody().trim().equals(""))
			sb.append(songs.get(position).getMelody().trim());
		info.setText(sb.toString());
		
		return rowView;
	}

}
