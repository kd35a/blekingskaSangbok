package se.kd35a.blekingskaSangbok;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
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
	private Context context;
	
	public DatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		this.context = context;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		/**
		 * Song-title Melody Credits Song-text
		 */
		db.execSQL("CREATE TABLE " + TABLE_NAME + " (" + ID
				+ " INTEGER PRIMARY KEY AUTOINCREMENT," + TITLE + " TEXT,"
				+ MELODY + " TEXT," + CREDITS + " TEXT," + TEXT + " TEXT);");
		addToDatabaseFromFile(db);
	}
	
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.w(tag, "Upgrading database, which will destroy all old data");
		db.execSQL("DROP TABLE IF EXISTS constants");
		onCreate(db);
	}
	
	/**
	 * Method for parsing JSON. Adds results do database.
	 * @param json the JSON-content to be parsed
	 * @param db SQLiteDatabase to be used (can be set to <code>null</code>,
	 * the system takes care of that case)
	 */
	private void getJSONSongs(String json, SQLiteDatabase db) {
		try {
			JSONArray entries = new JSONArray(json);
			for (int i = 0; i < entries.length(); i++) {
				JSONObject post = entries.getJSONObject(i);
				if (db != null) {
					addToDB(db, post.getString("title"), post.getString("melody"),
							post.getString("credits"),  post.getString("lyric"));
				} else {
					addToDB( post.getString("title"), post.getString("melody"),
						post.getString("credits"), post.getString("lyric"));
				}
			}
		} catch (JSONException e) {
			Log.e(tag + ".JSON", "ERROR: " + e.getMessage());
		}
	}


	
	/**
	 * Returns content of file as JSON-coded String
	 * @return String JSON-data
	 */
	private String retrieveFromFile() {
		try {
			InputStream is =  this.context.getResources().openRawResource(R.raw.lyric);
			byte[] buffer = new byte[is.available()];
			while (is.read(buffer) != -1);
			return new String(buffer);
		} catch (Exception e) {
			Log.e(tag, e.getMessage());
		}
		return null;
	}
	
	/**
	 * Returns content of URL and returns it as String
	 * @param url the url to the content
	 * @return the content found on the url
	 */
	private String retrieveFromURL(String url) {
		HttpGet getRequest = new HttpGet(url);
		
		try {
			DefaultHttpClient client = new DefaultHttpClient();
			HttpResponse getResponse = client.execute(getRequest);
			final int statusCode = getResponse.getStatusLine().getStatusCode();
			
			if (statusCode != HttpStatus.SC_OK) {
				Log.w(tag, "Error: " + statusCode
						+ ", for URL: " + url);
				return null;
			}
			
			HttpEntity getResponseEntity = getResponse.getEntity();
			
			if (getResponseEntity != null) {
				return EntityUtils.toString(getResponseEntity,  HTTP.UTF_8);
			}
		} catch (IOException e) {
			getRequest.abort();
			Log.w(tag, "Error for URL: " + url, e);
		}
		return null;
	}
	
	/**
	 * Parses the json-file linked with the <code>url</code>, and adds the songs
	 * in the file to the database.
	 * @param url the url to the json-file to be parsed.
	 * @param db SQLiteDatabase that is used.
	 */
	public void addToDatabaseFromUrl(String url, SQLiteDatabase db) {
		getJSONSongs(retrieveFromURL(url), db);
	}
	
	/**
	 * Populates database with songs found in /res/raw/lyric.json.
	 * @param db SQLiteDatabase that is used.
	 */
	public void addToDatabaseFromFile(SQLiteDatabase db) {
		getJSONSongs(retrieveFromFile(), db);
	}
	
	/**
	 * Adds a new song in the DB.
	 * @param db the database to use
	 * @param title
	 * @param melody
	 * @param credits
	 * @param text
	 */
	private void addToDB(SQLiteDatabase db, String title, String melody, String credits, String text) {
		ContentValues cv = new ContentValues();
		cv.put(TITLE, title);
		cv.put(MELODY, melody);
		cv.put(CREDITS, credits);
		cv.put(TEXT, text);
		db.insert(TABLE_NAME, TITLE, cv);
	}
	
	/**
	 * Adds a new song in the DB.
	 * @param title
	 * @param melody
	 * @param credits
	 * @param text
	 */
	private void addToDB(String title, String melody, String credits, String text) {
		SQLiteDatabase db = getWritableDatabase();
		addToDB(db, title, melody, credits, text);
		db.close();
	}
	
	/**
	 * @deprecated replaced by {@link #addToDatabaseFromFile(SQLiteDatabase)}
	 */
	public void addDefaultSongs() {
		SQLiteDatabase db = getWritableDatabase();
		
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
	
	/**
	 * Gives a list of all songs in the database. The songs are
	 * @return the list of songs
	 */
	public ArrayList<Song> getSongs() {
		SQLiteDatabase db = getReadableDatabase();
		Cursor c = db.query(TABLE_NAME, null, null, null, null, null, TITLE);

		ArrayList<Song> songs = new ArrayList<Song>();

		if (c.getCount() > 0) {
			c.moveToFirst();
			do {
				songs.add(new Song(c.getString(c.getColumnIndex(TITLE)), c
						.getString(c.getColumnIndex(MELODY)), c.getString(c
						.getColumnIndex(CREDITS)), c.getString(c
						.getColumnIndex(TEXT)), c.getLong(c.getColumnIndex(ID))));
			} while (c.moveToNext());
		}
		
		c.close();
		db.close();
		return songs;
	}
	
	/**
	 * Gives the unique song identified by its <code>id</code>.
	 * @param id the unique <code>id</code> for the song
	 * @return the song specified
	 * @throws IllegalArgumentException If the <code>id</code> given does not
	 * exist in the database.
	 */
	public Song getSong(long id) throws IllegalArgumentException {
		SQLiteDatabase db = getReadableDatabase();
		Cursor c = db.query(TABLE_NAME, null, ID + "==" + id, null, null, null,
				TITLE);
		
		/*
		 * Because the table is created with "PRIMARY KEY" for the id-field
		 * it should be impossible to get more than one answer from the database.
		 */
		if (c.getCount() != 1) {
			throw new IllegalArgumentException("id " + id
					+ "is not a valid id, does not exist in database.");
		}
		
		c.moveToFirst();
		Song song = new Song(c.getString(c.getColumnIndex(TITLE)),
				c.getString(c.getColumnIndex(MELODY)),
				c.getString(c.getColumnIndex(CREDITS)),
				c.getString(c.getColumnIndex(TEXT)), id);
		
		c.close();
		db.close();
		return song;
	}

}
