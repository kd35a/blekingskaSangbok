package se.kd35a.blekingskaSangbok;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class ViewSongActivity extends Activity {
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.song);
		
		TextView title = (TextView)findViewById(R.id.song_title);
		TextView info = (TextView)findViewById(R.id.song_info);
		TextView text = (TextView)findViewById(R.id.song_text);
		
		long songId = getIntent().getExtras().getLong("song");
		DatabaseHelper dbHelper = new DatabaseHelper(this);
		Song s = dbHelper.getSong(songId);
		title.setText(s.getTitle());
		
		StringBuilder sb = new StringBuilder();
		if (!s.getCredits().trim().equals(""))
			sb.append(s.getCredits());
		if (!s.getCredits().trim().equals("") &&
				!s.getMelody().trim().equals(""))
			sb.append(", ");
		if (!s.getMelody().trim().equals(""))
			sb.append(s.getMelody());
		info.setText(sb.toString());
		
		text.setText(s.getText());
		findViewById(R.id.song_info).getRootView().postInvalidate();
	}
	
}
