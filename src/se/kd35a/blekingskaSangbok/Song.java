package se.kd35a.blekingskaSangbok;

import java.io.Serializable;

public class Song implements Serializable {
	private static final long serialVersionUID = 298977941791737741L;
	private String title;
	private String melody;
	private String credits;
	private String text;
	private long id;
	
	public Song(String title, String melody, String creadits, String text) {
		this.title = title;
		this.melody = melody;
		this.credits = creadits;
		this.text = text;
		this.id = -1;
	}
	
	public Song(String title, String melody, String creadits, String text, long id) {
		this.title = title;
		this.melody = melody;
		this.credits = creadits;
		this.text = text;
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public String getMelody() {
		return melody;
	}

	public String getCredits() {
		return credits;
	}

	public String getText() {
		return text;
	}

	public long getId() {
		return id;
	}
	
}
