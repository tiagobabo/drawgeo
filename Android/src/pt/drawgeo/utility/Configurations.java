package pt.drawgeo.utility;

import android.graphics.Bitmap;

public class Configurations {

	final public static float AVALIABLE_RADIUS = 100;
	final public static int SEARCH_RADIUS = 1000;
	final public static double MINIMUM_RADIUS = 30.0;
	final public static String AUTHORITY = "drawgeo.herokuapp.com";
	final public static String SCHEME = "http";
	final public static String FORMAT = "json";
					
	// MÉTODOS
	final public static String GETBYCOORDINATES = "radius/getByCoordinates";
	final public static String CHECKUSER = "/play/getUserByEmail";
	final public static String ADDCHALLENGE = "/play/addNewDraw";
	final public static String GETDRAW ="draws";
	final public static String GETNEWWORDS ="/play/getNewWords";
	final public static String GUESS ="/play/guess";
	final public static String REPLACEDRAW ="/play/replace";
	
	// DADOS DO UTILIZADOR
	public static String email;
	public static int id;
	public static int keys;
	public static int num_done;
	public static int num_success;
	public static int piggies;
	public static String avatarURL;
	public static String name;
	public static Bitmap avatarImage;
	public static String drawidreplay;
	public static int num_created;
	public static int ranking;

	public static String current_password = null;
	public static String current_description = null;
	public static int wordID = -1;
	public static Word current_word;
	public static double latitudenow = -1.0;
	public static double longitudenow = -1.0;
	
	// MUSICA
	public static final int MENU_MUSIC = 0;
	public static final int GAME_MUSIC = 1;
	public static final int END_MUSIC = 2;
	public static int CURRENT_MUSIC = -1;
	public static boolean PAUSED = false;
	
	
	public static String getRanking(int ranking){
		if(ranking < 2){
			return "Wannabe";
		}else if(ranking < 20){
			return "Baby";
		}else if(ranking < 40){
			return "Toddler";
		}else if(ranking < 70){
			return "Young";
		}else if(ranking < 100){
			return "Teen";
		}else if(ranking < 150){
			return "Grown Up";
		}else if(ranking < 200){
			return "Veteran";
		}else if(ranking < 300){
			return "Master";
		}else if(ranking < 500){
			return "Galactic";
		}else if(ranking < 1000){
			return "God";
		}else{
			return "Picasso";
		}
	}
	

}
