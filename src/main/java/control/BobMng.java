package control;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;

import com.github.cliftonlabs.json_simple.JsonException;
import com.github.cliftonlabs.json_simple.JsonObject;
import com.github.cliftonlabs.json_simple.Jsoner;

/**
 * Cette classe permet de definir des methodes permettant de creer un profile
 * Bob ou de verifier si un utilisateur est enregistre comme etant un Bob.
 * 
 * @author MALIFARGE Antoine
 * @author THOME Alban
 * @category control
 *
 */

public final class BobMng {
	private static String path_bob_file = "./fichierSauvegarde.json"; // Doit etre un json.
	private static boolean is_bob = false;

	public static String getPathToBobFile() {
		return path_bob_file;
	}

	public static boolean getIsBob() {
		return is_bob;
	}

	/**
	 * Cette methode se lance lorsqu'un utilisateur se connecte pour verifier si il
	 * a un profile bob. Si le fichier de sauvegarde n'existe pas, il est cree et le
	 * premier utilisateur est mis en tant que bob, c'est le premier utilisateur.
	 * 
	 * @param user_name L'identifiant de connection a Gitlab.
	 * @return Boolean true si l'utilisateur est enregistrer dans les utilisateurs
	 *         Bob, false sinon.
	 */
	public static Boolean isUserABob(String user_name) { // RENAME

		try {
			File file = new File(path_bob_file);
			// cree le fichier si il existe pas
			if (!file.exists()) {
				file.createNewFile();
				BufferedWriter writer = Files.newBufferedWriter(Paths.get(path_bob_file));
				JsonObject user = new JsonObject();
				user.put(user_name, new HashMap<>());
				Jsoner.serialize(user, writer);
				writer.close();
				System.out.println("Premiere utilisation, fichier " + path_bob_file + " cree.");
				is_bob = true;
				return true;
			}
			Reader reader = Files.newBufferedReader(Paths.get(path_bob_file));
			JsonObject users = (JsonObject) Jsoner.deserialize(reader);
			reader.close();
			if (users.containsKey(user_name)) {
				is_bob = true;
				return true;
			}
		} catch (IOException | JsonException e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * Permet a un utilisateur bob d'ajouter un nouvel utilisateur bob.
	 * 
	 * @param user_name identifiant git de l'utilisateur a ajouter.
	 */
	public static void setAsBob(String user_name) {

		if (!isUserABob(user_name)) {
			try {
				Reader reader = Files.newBufferedReader(Paths.get(path_bob_file));
				JsonObject users = (JsonObject) Jsoner.deserialize(reader);
				reader.close();
				users.put(user_name, new HashMap<>());
				BufferedWriter writer = Files.newBufferedWriter(Paths.get(path_bob_file));
				Jsoner.serialize(users, writer);
				writer.close();
				System.out.println(user_name + " a été ajouté aux profils Bob");
			} catch (IOException | JsonException e) {
				e.printStackTrace();
			}
		}
	}
}
