package model;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import control.ModulesMng;
import model.arbre.UserObject;
import vue.SwingModulePage;

/**
 * Cette classe permet de stocker la liste des modules et les projets/groupes
 * qu'ils contienent pour l'utilisateur connecte dans une HasMap avec la meme
 * structure que le fichier json. Elle utilise un support de listener se
 * trouvant dans la classe GittseModel dont elle herite.
 * <p>
 * Un tag represente le nom d'un module, c'est ce qui est assigner a un
 * UserObject pour savoir a quel module il appartient. Ce sera generalement un
 * String ou une liste de String si il y en a plusieurs. Un module represente
 * L'ensemble des UserObject qui le constitu ainsi que le nom du module. Au
 * lieux de stocker directement un UserObject, on stock une fàon unique de
 * l'identifier, son ID suivie de son Type (group ou project)
 * </p>
 * <p>
 * Exemples: 153group, 1423project
 * </p>
 * 
 * @author MALIFARGE Antoine
 * @see SwingModulePage
 * @category model
 */
public class Module extends GittseModel {
	Map<String, ArrayList<String>> modules;

	public Module() {
		modules = new HashMap<>();
		addPropertyChangeListener(new ModuleChangeListener());
	}

	/**
	 * Cette methode stock la Map des modules passe en parametre et notifie les
	 * listeners enregistrer dans pcs (PropertyChangeSupport).
	 * 
	 * @param modules La map des modules enregistrer dans le .json
	 */
	public void setModules(Map<String, ArrayList<String>> modules) {

		this.modules = modules;
		pcs.firePropertyChange(new ModuleListEvent(this, getTags(), null, "Init modules"));
	}

	public Map<String, ArrayList<String>> getModules() {
		return modules;
	}

	/**
	 * @return Liste des noms de modules de l'utilisateur.
	 */
	public ArrayList<String> getTags() {

		ArrayList<String> tags = new ArrayList<String>();
		for (String tag : modules.keySet()) {
			tags.add(tag);
		}
		return tags;
	}

	/**
	 * initialise une nouvel cle avec le nom specifie par tag
	 * 
	 * @param tag Nom du module a cree.
	 * @return Renvoi true si un module a été créé, false sinon.
	 */
	public boolean createModule(String tag) {

		if (!modules.containsKey(tag)) {
			modules.put(tag, new ArrayList<String>());
			pcs.firePropertyChange(new ModuleListEvent(this, getTags(), tag, "New module"));
			return true;
		}
		return false;
	}

	/**
	 * Suprime le module dont le nom est specifie
	 * 
	 * @param tag nom du module a suprimer
	 */
	public void delModule(String tag) {
		modules.remove(tag);
		pcs.firePropertyChange(new ModuleListEvent(this, getTags(), tag, "Module deleted"));
	}

	/**
	 * Cette methode permet d'ajouter de nouveaux UserObjects a un module.
	 * 
	 * @param user_objects Liste des UserObject a rajouter au module
	 * @param tag          Nom du module auquel rajouter les UserObject
	 */
	public void addUserObjectsToModule(ArrayList<UserObject> user_objects, String tag) {

		for (UserObject user_object : user_objects) {
			if (!user_object.isTag(tag)) {
				user_object.addTag(tag);
				if (modules.get(tag) == null) {
					// Si il n'y a pas encore de module avec le nom tag, on en cree un.
					ArrayList<String> temp = new ArrayList<String>();
					temp.add(String.valueOf(user_object.getId()) + user_object.getType());
					modules.put(tag, temp);
				} else {
					((ArrayList<String>) modules.get(tag))
							.add(String.valueOf(user_object.getId()) + user_object.getType());
				}
			}
		}
		pcs.firePropertyChange(new ModuleListEvent(this, modules.get(tag), tag, "Changes in a module"));

	}

	/**
	 * Cette methode permet de retirer des UserObjects d'un module.
	 * 
	 * @param user_objects Liste des UserObject a retirer du module
	 * @param tag          Nom du module auquel retirer les UserObject
	 */
	public void removeUserObjectsFromModule(ArrayList<UserObject> user_objects, String tag) {

		for (UserObject user_object : user_objects) {
			if (user_object.isTag(tag)) {
				user_object.deleteTag(tag);
				((ArrayList<String>) modules.get(tag))
						.remove(String.valueOf(user_object.getId()) + user_object.getType());
			}
		}
		pcs.firePropertyChange(new ModuleListEvent(this, modules.get(tag), tag, "Changes in a module"));
	}

	/**
	 * Custom PropertyChangeEvent permetant de recuperer le nom du module ayant
	 * subis des modifications.
	 */
	public class ModuleListEvent extends PropertyChangeEvent {

		private static final long serialVersionUID = 6112586277270778879L;
		private String tag;

		public ModuleListEvent(Object source, Object newValue, String tag, String eventType) {
			super(source, eventType, null, newValue);
			this.tag = tag;
		}

		public String getTag() {
			return tag;
		}
	}

	/**
	 * Custom PropertyChangeListener pour la mise a jour du json.
	 */
	public class ModuleChangeListener implements PropertyChangeListener {

		@SuppressWarnings("unchecked")
		public void propertyChange(PropertyChangeEvent evt) {
			switch (evt.getPropertyName()) {
			case "New module":
				ModulesMng.writeJson(null, ((ModuleListEvent) evt).getTag(), false);
				break;
			case "Module deleted":
				ModulesMng.writeJson(null, ((ModuleListEvent) evt).getTag(), true);
				break;
			case "Changes in a module":
				ModulesMng.writeJson((ArrayList<String>) evt.getNewValue(), ((ModuleListEvent) evt).getTag(), false);
			default:
				break;
			}
		}
	}
}
