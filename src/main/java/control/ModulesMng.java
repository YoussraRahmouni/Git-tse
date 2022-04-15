package control;

import java.beans.PropertyChangeListener;
import java.io.BufferedWriter;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.github.cliftonlabs.json_simple.JsonObject;
import com.github.cliftonlabs.json_simple.Jsoner;

import model.Module;
import model.arbre.UserObject;
import vue.SwingModulePage;

/**
 * Cette classe permet de gererla recuperation des modules dans le json, ainsi
 * que leurs ecriture. Il fait aussi le lient entre la vue et la classe Module
 * du model. On utilise la librairie json_simple pour lire et ecrire des donnes
 * dans le json.
 * 
 * @author MALIFARGE Antoine
 * @category control
 * @see SwingModulePage
 */
public final class ModulesMng {
	private static Module model_module;
	private static String current_user;

	private static String chemin_fichier_bobs = "./fichierSauvegarde.json";

	public static void initModule() {
		// Instanciation de la classe Module.
		model_module = new Module();
	}

	public static void setUserName(String user_name) {
		current_user = user_name;
	}

	/**
	 * Cette methode permet de recuperer les informations sur les modules enregistre
	 * pour l'utilisateur en cours et de les stocker dans la classe Module.
	 */
	@SuppressWarnings("unchecked")
	public static void readJson() {

		try {
			Map<String, ArrayList<String>> modules = new HashMap<>();
			Reader reader = Files.newBufferedReader(Paths.get(chemin_fichier_bobs));
			JsonObject users = (JsonObject) Jsoner.deserialize(reader);
			modules = (Map<String, ArrayList<String>>) users.get(current_user);
			reader.close();
			model_module.setModules(modules);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Cette methode permet d'ecrire de nouvelles donnes dans le json ou de retirer
	 * des donnes existantes. cas de figures :
	 * <ul>
	 * <li>user_objects est null et delet est true -> on suprimer le modules dont le
	 * nom est specifie par tag</li>
	 * <li>user_objects est null et delet est false -> on ajoute le module dont le
	 * nom est specifie par tag</li>
	 * <li>user_objects est non null et delet est false -> associe au module dont le
	 * nom est specifie par tag la liste user_objects</li>
	 * </ul>
	 * 
	 * @param user_objects Nouvelle liste des userObjects sous la forme "IDtype" a
	 *                     associer au tag specifie.
	 * @param tag          Nom du module sur lequel faire une modification.
	 * @param delet        specifie l'action a effectuer.
	 */
	@SuppressWarnings("unchecked")
	public static void writeJson(ArrayList<String> user_objects, String tag, boolean delet) {

		try {
			Reader reader = Files.newBufferedReader(Paths.get(chemin_fichier_bobs));
			JsonObject users = (JsonObject) Jsoner.deserialize(reader);
			Map<String, ArrayList<String>> user = (Map<String, ArrayList<String>>) users.get(current_user);
			reader.close();
			if (delet) {
				user.remove(tag);
			} else {
				user.put(tag, user_objects);
			}
			users.put(current_user, user);
			BufferedWriter writer = Files.newBufferedWriter(Paths.get(chemin_fichier_bobs));
			Jsoner.serialize(users, writer);
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Permet d'assigner a chaque UserObject dans l'arbre les tags qui lui
	 * correspond selon les donnes du json. Cette methode est appele une fois apres
	 * la connexion lorsque l'utilisateur connecte est un bob. Elle recupere les
	 * modules enregistrer dans la classe Module par la methode readJson, et fait
	 * appel a la methode assignerTagsIter qui va assigner les tags de façon
	 * recursive.
	 * 
	 * @param arbre UserObject de type arbre.
	 */
	public static void assignerTags(UserObject arbre) {
		readJson();
		Map<String, ArrayList<String>> userModules = model_module.getModules();
		for (UserObject userObject : arbre.getListUO()) {
			assignTagsIter(userObject, userModules);
		}
	}

	/**
	 * Methode recursive assignant les tags a un noeud avant de s'appeler elle meme
	 * pour chaque descendents.
	 * 
	 * @param node         UserObject auquel assigner les tags.
	 * @param user_modules HashMap representant tout les modules et les UserObjects
	 *                     assigne de l'utilisateur en cours.
	 */
	private static void assignTagsIter(UserObject node, Map<String, ArrayList<String>> user_modules) {
		// ici tag represente toujours le nom du module, et module represente la liste
		// de UserObject contenus dans le module sous la forme "IDtype".
		user_modules.forEach((tag, module) -> {
			if (!(module == null)) {
				if (module.contains(String.valueOf(node.getId()) + node.getType())) {
					node.addTag(tag);
				}
			}
		});
		if (!(node.getListUO() == null)) {
			for (UserObject user_object : node.getListUO()) {
				// les membres n'ont pas de tags
				if (!user_object.getType().equals("member")) {
					assignTagsIter(user_object, user_modules);
				}
			}
		}
	}

	/**
	 * Appel la methode {@link model.Module#createModule(String)} de la classe
	 * Module.
	 * 
	 * @param tag Nom du module a creer.
	 * @return true si le module a été créér, false sinon.
	 */
	public static boolean createNewModule(String tag) {
		return model_module.createModule(tag);
	}

	/**
	 * Appel la methode {@link model.Module#delModule(String)} de la classe Module
	 * et la methode {@link model.arbre.Arbre#searchAndDeleteTags(String)}
	 * searchAndDeleteTags de la classe Arbre.
	 * 
	 * @param tag Nom du module a suprimer.
	 */
	public static void deleteExistingModule(String tag) {
		model_module.delModule(tag);
		ProjectsMng.getArbre().searchAndDeleteTags(tag);
	}

	/**
	 * Appel la methode
	 * {@link model.Module#addUserObjectsToModule(ArrayList, String)} de la classe
	 * Module.
	 * 
	 * @param user_objects Liste des UserObjects a ajouter au module.
	 * @param tag          Nom du module auquel ajouter les UserObjects.
	 */
	public static void addTagToUserObjects(ArrayList<UserObject> user_objects, String tag) {
		model_module.addUserObjectsToModule(user_objects, tag);
	}

	/**
	 * Appel la methode
	 * {@link model.Module#removeUserObjectsFromModule(ArrayList, String)} de la
	 * classe Module.
	 * 
	 * @param user_objects Liste des UserObjects a retirer du module.
	 * @param tag          Nom du module auquel duquel retirer les UserObjects.
	 */
	public static void removeTagFromUserObjects(ArrayList<UserObject> user_objects, String tag) {
		model_module.removeUserObjectsFromModule(user_objects, tag);
	}

	/**
	 * Appel la methode addPropertyChangeListener de la classe Module.
	 * 
	 * @param listener
	 */
	public static void addModuleListListener(PropertyChangeListener listener) {
		model_module.addPropertyChangeListener(listener);
	}
}
