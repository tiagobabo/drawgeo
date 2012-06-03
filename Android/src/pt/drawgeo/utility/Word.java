package pt.drawgeo.utility;

import org.json.JSONException;
import org.json.JSONObject;

public class Word {

	private int id;
	private String word;
	private int difficulty;
	
	
	public Word(){
		
	}
	public Word(int id,String word, int dif){
		
		this.id = id;
		this.word = word;
		this.difficulty = dif;
		
	}
	public Word(JSONObject word) throws JSONException{
		
		this.word = word.getString("word");
		this.id = word.getInt("id");
		this.difficulty = word.getInt("difficulty");
		
		
	}
	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}
	/**
	 * @return the word
	 */
	public String getWord() {
		return word;
	}
	/**
	 * @param word the word to set
	 */
	public void setWord(String word) {
		this.word = word;
	}
	/**
	 * @return the difficulty
	 */
	public int getDifficulty() {
		return difficulty;
	}
	/**
	 * @param difficulty the difficulty to set
	 */
	public void setDifficulty(int difficulty) {
		this.difficulty = difficulty;
	}
	
	
}
