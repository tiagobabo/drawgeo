package pt.drawgeo.utility;

import android.graphics.Bitmap;

public class Configurations {

	final public static float AVALIABLE_RADIUS = 100;
	final public static int SEARCH_RADIUS = 1000;
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
	public static double latitude = 41.3;
	public static double longitude = -8.6366445;
	public static String drawidreplay;
	public static int num_created;
	public static int wordID = -1;
	public static Word current_word;
	
	// MUSICA
	public static final int MENU_MUSIC = 0;
	public static final int GAME_MUSIC = 1;
	public static final int END_MUSIC = 2;
	public static int CURRENT_MUSIC = -1;
	public static boolean PAUSED = false;
	
	
}
