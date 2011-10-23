package se.kd35a.blekingskaSangbok;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {
	private static final int DATABASE_VERSION = 1;
	private static final String DATABASE_NAME = "BlekingskaSangBok";
	private static final String TABLE_NAME = "songs";
	static final String ID = "_id";
	static final String TITLE = "title";
	static final String MELODY = "melody";
	static final String CREDITS = "credits";
	static final String TEXT = "text";
	private static final String tag = "DatabaseHelper";

	public DatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		/**
		 * Song-title Melody Credits Song-text
		 */
		db.execSQL("CREATE TABLE " + TABLE_NAME + " (" + ID
				+ " INTEGER PRIMARY KEY AUTOINCREMENT," + TITLE + " TEXT,"
				+ MELODY + " TEXT," + CREDITS + " TEXT," + TEXT + " TEXT);");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.w(tag, "Upgrading database, which will destroy" + "all old data");
		db.execSQL("DROP TABLE IF EXISTS constants");
		onCreate(db);
	}

	/** Method for parsing JSON **/
	private void getJSONSongs(String json) {
		boolean DEBUG = false; 
		
		try {
			String x = "";
			JSONArray entries = new JSONArray(json);
			x = "JSON parsed.\nThere are [" + entries.length() + "]\n\n";
			for (int i = 0; i < entries.length(); i++) {
				JSONObject post = entries.getJSONObject(i);
				if(DEBUG) {
					x += "------------\n";
					x += "Title: " + post.getString("title") + "\n";
					x += "Melody: " + post.getString("melody") + "\n";
					x += "Credits: " + post.getString("credits") + "\n";
					x += "Lyric: " + post.getString("lyric") + "\n\n";					
				}else {
					addToDB(post.getString("title"), post.getString("melody"), post.getString("credits"),  post.getString("lyric"));
				}
			}
			Log.w("Found JSON: ", x);

		} catch (Exception je) {
			Log.w("JSON", "ERROR: " + je.getMessage());
		}
	}
	/** Fills database with content from JSON lyric file. 
	 * Empties database first.**/
	public void populateDatabase(){
		SQLiteDatabase db = getWritableDatabase();
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
		onCreate(db);
		getJSONSongs(retrieve_url("http://kryptoanarki.se/temp/lyric.json"));
	}
	/**
	 * Returns content of URL and returns it as String
	 * @param url
	 * @return
	 */
	private String retrieve_url(String url) {

		HttpGet getRequest = new HttpGet(url);

		try {
			DefaultHttpClient client = new DefaultHttpClient();   
			HttpResponse getResponse = client.execute(getRequest);
			final int statusCode = getResponse.getStatusLine().getStatusCode();

			if (statusCode != HttpStatus.SC_OK) {
				Log.w(getClass().getSimpleName(), "Error " + statusCode
						+ " for URL " + url);
				return null;
			}

			HttpEntity getResponseEntity = getResponse.getEntity();
			
			if (getResponseEntity != null) {
				return EntityUtils.toString(getResponseEntity,  HTTP.UTF_8);
			}

		} catch (IOException e) {
			getRequest.abort();
			Log.w(getClass().getSimpleName(), "Error for URL " + url, e);
		}

		return null;

	}
	/** Adds song to Database 
	 * Note that new databse object is reacreated each time. Room for optimization **/
	private void addToDB(String title, String melody, String credits, String text) {
		SQLiteDatabase db = getWritableDatabase();
		ContentValues cv = new ContentValues();
		cv.put(TITLE, title);
		cv.put(MELODY, melody);
		cv.put(CREDITS, credits);
		cv.put(TEXT, text);
		db.insert(TABLE_NAME, TITLE, cv);
	}
	public void addDefaultSongs() {
		
		SQLiteDatabase db = getWritableDatabase();
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
		onCreate(db);

		ContentValues cv = new ContentValues();

		String title = "Hvila vid denna källa";
		String melody = "";
		String credits = "Carl Michael Bellman, 1790";
		String text = "Hvila vid denna källa,\n"
				+ "Vår lilla Frukost vi framställa;\n"
				+ "Rödt Vin med Pimpinella\n"
				+ "Och en nyss skuten Beccasin.\n"
				+ "Klang hvad Buteljer, Ulla!\n"
				+ "I våra Korgar öfverstfulla,\n" + "Tömda i gräset rulla,\n"
				+ "Och känn hvad ångan dunstar fin,\n" + "Ditt middags Vin\n"
				+ "Sku vi ur krusen hälla,\n" + "Med glättig min.\n"
				+ "Hvila vid denna källa,\n"
				+ "Hör våra Valdthorns klang Cousine.\n"
				+ "Valdthornens klang Cousine.";
		cv.put(TITLE, title);
		cv.put(MELODY, melody);
		cv.put(CREDITS, credits);
		cv.put(TEXT, text);
		db.insert(TABLE_NAME, TITLE, cv);

		title = "Så lunka vi så småningom";
		melody = "";
		credits = "Carl Michael Bellman, 1791";
		text = "Så lunka vi så småningom\n"
				+ "från Bacchi buller och tumult,\n"
				+ "när döden ropar: \"Granne, kom,\n"
				+ "ditt timglas är nu fullt!\"\n"
				+ "Du gubbe, fäll din krycka ner -\n"
				+ "och du, du yngling, lyd min lag:\n"
				+ "den skönsta nymf som åt dig ler,\n" + "inunder armen tag!\n"
				+ "Tycker du att graven är för djup,\n"
				+ "nå, välan så tag dig då en sup,\n"
				+ "tag dig sen dito en, dito två, dito tre,\n"
				+ "så dör du nöjdare!";
		cv.put(TITLE, title);
		cv.put(MELODY, melody);
		cv.put(CREDITS, credits);
		cv.put(TEXT, text);
		db.insert(TABLE_NAME, TITLE, cv);
	}

	public ArrayList<Song> getSongs() {
		SQLiteDatabase db = getReadableDatabase();
		Cursor c = db.query(TABLE_NAME, null, null, null, null, null, TITLE);

		ArrayList<Song> songs = new ArrayList<Song>();

		if (c.getCount() < 1) {
			c.close();
			db.close();
			return songs;
		}
		c.moveToFirst();
		do {
			songs.add(new Song(c.getString(c.getColumnIndex(TITLE)), c
					.getString(c.getColumnIndex(MELODY)), c.getString(c
					.getColumnIndex(CREDITS)), c.getString(c
					.getColumnIndex(TEXT)), c.getLong(c.getColumnIndex(ID))));
		} while (c.moveToNext());

		c.close();
		db.close();
		return songs;
	}

	public Song getSong(long id) {
		SQLiteDatabase db = getReadableDatabase();
		Cursor c = db.query(TABLE_NAME, null, ID + "==" + id, null, null, null,
				TITLE);

		if (c.getCount() != 1) {
			throw new IllegalArgumentException("id " + id
					+ "is not a valid id, does not exist in database.");
		}

		c.moveToFirst();
		Song song = new Song(c.getString(c.getColumnIndex(TITLE)),
				c.getString(c.getColumnIndex(MELODY)), c.getString(c
						.getColumnIndex(CREDITS)), c.getString(c
						.getColumnIndex(TEXT)), id);

		c.close();
		db.close();
		return song;
	}

}
