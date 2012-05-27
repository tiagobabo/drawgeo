package pt.drawgeo.utility;

public class Configurations {

	final public static float AVALIABLE_RADIUS = 100;
	final public static int SEARCH_RADIUS = 1000;
	final public static String AUTHORITY = "drawgeo.herokuapp.com";
	final public static String SCHEME = "http";
	final public static String FORMAT = "json";
					
	// MÉTODOS
	final public static String GETBYCOORDINATES = "radius/getByCoordinates";
	final public static String CHECKUSER = "/play/getUserByEmail";
	
	// DADOS DO UTILIZADOR
	public static String email;
	public static int id;
	public static int keys;
	public static int num_done;
	public static int num_success;
	public static int piggies;
	public static String avatarURL;
}
