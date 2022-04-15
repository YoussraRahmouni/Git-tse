package model.arbre;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.gitlab4j.api.models.Group;

/**
 * Classe d'objet groupe servant a la creation de l'arbre.
 * 
 * @author LEROY Valentin
 * @author MALIFARGE Antoine
 * @category model.arbre
 * @see UserObject
 */
public class GroupObject implements UserObject {
	private Group group;
	private int id;
	private List<UserObject> childs = new ArrayList<UserObject>();
	private List<String> tags = new ArrayList<String>();
	private String type = "group";
	private URL url_icon;

	/**
	 * Constructeur de la classe GroupObject.
	 * 
	 * @param group
	 * @param icon
	 */
	public GroupObject(Group group, URL icon) {
		this.group = group;
		this.id = group.getId();
		this.url_icon = icon;
	}

	/**
	 * Getter de l'objet group de l'arbre
	 * @return Group
	 */
	public Group getGroup() {
		return group;
	}

	/**
	 * Getter du type d'objet de l'element de l'arbre.
	 * 
	 * @return String
	 */
	public String getType() {
		return type;
	}

	/**
	 * Getter de l'id de l'element de l'arbre.
	 * 
	 * @return int
	 */
	public int getId() {
		return id;
	}

	/**
	 * Ajout d'un tag a la liste des tags de l'element de l'arbre.
	 * 
	 * @param tag_name
	 * @return void
	 */
	public void addTag(String tag_name) {
		tags.add(tag_name);
	}

	/**
	 * Verification si le nom de tag est dans la liste de l'element de l'arbre.
	 * L'element de retour est un booleen qui indique si le tag est dans la liste.
	 * 
	 * @param tag_name
	 * @return boolean
	 */
	public boolean isTag(String tag_name) {
		if (tags.contains(tag_name)) {
			return true;
		}
		return false;
	}

	/**
	 * Getter retournant la liste de l'ensemble des tags associes a l'element de
	 * l'arbre.
	 * 
	 * @return List<String>
	 */
	public List<String> getTags() {
		return tags;
	}

	/**
	 * Supression d'un tag dans la liste des tags de l'element de l'arbre. Retourne
	 * un booleen en fonction de si le tag a été supprime ou non.
	 * 
	 * @param tag_name
	 * @return boolean
	 */
	public boolean deleteTag(String tag_name) {
		if (tags.contains(tag_name)) {
			tags.remove(tag_name);
			return true;
		}
		return false;
	}

	/**
	 * (Pour le profil Bob) Methode de parcours de l'arbre. Cette methode va pour
	 * chaque element chercher si le tag reference est dans sa liste de tag, le
	 * supprimer si c'est le cas et appeler cette methode pour l'ensemble de ses
	 * enfants.
	 * 
	 * @param tag
	 * @return void
	 */
	public void searchAndDeleteTags(String tag) {
		deleteTag(tag);
		for (UserObject objet : childs) {
			objet.searchAndDeleteTags(tag);
		}
	}

	/**
	 * Getter du nom du groupe
	 * 
	 * @return String
	 */
	public String getLabel() {
		return group.getName();
	}

	/**
	 * Getter de la liste des enfants de l'element de l'arbre
	 * 
	 * @return List<UserObject>
	 */
	public List<UserObject> getListUO() {
		return childs;
	}

	/**
	 * Methode d'ajout d'un enfant a un element de l'arbre.
	 * 
	 * @param member
	 * @return void
	 */
	public void addListUO(UserObject member) {
		childs.add(member);
	}

	/**
	 * Methode de recherche d'un element dans l'arbre pour un id et un type
	 * d'element donne. Si aucune correspondance n'a été trouve, la methode renvoi
	 * null. L'appel se fait a chaque niveau pour l'ensemble des enfants des
	 * elements a ce niveau.
	 * 
	 * @param id
	 * @param type
	 * @return UserObject
	 */
	public UserObject isInTree(int id, String type) {
		UserObject object = null;
		if (getId() == id && getType().equals(type)) {
			object = this;
		} else {
			for (UserObject objet : childs) {
				if (objet.isInTree(id, type) != null) {
					object = objet.isInTree(id, type);
				}
			}
		}
		return object;
	}

	/**
	 * Getter de l'url de l'icon correspondant a cet element de l'arbre.
	 * 
	 * @return URL
	 */
	@Override
	public URL getIconUrl() {
		return url_icon;
	}
}
