package model.arbre;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.gitlab4j.api.models.Member;

/**
 * Classe d'objet Membre servant a la creation de l'arbre.
 * @author LEROY Valentin
 * @author MALIFARGE Antoine
 * @category model.arbre
 * @see UserObject
 */
public class MemberObject implements UserObject {
	private Member member;
	private String type_ = "member";
	private List<UserObject> childs = new ArrayList<UserObject>();
	private List<String> tags_ = new ArrayList<String>();
	private int id_;
	private URL iconUrl;

	/**
	 * Constructeur de la classe MemberObject
	 * @param member
	 * @param icon
	 */
	public MemberObject(Member member, URL icon) {
		this.member = member;
		this.id_ = member.getId();
		this.iconUrl = icon;
	}

	/**
	 * Getter de la liste des enfants de l'element de l'arbre. Dans le cas d'un
	 * membre cette liste est toujours vide.
	 * 
	 * @return List<UserObject>
	 */
	public List<UserObject> getListUO() {
		return childs;
	}

	/**
	 * Methode d'ajout d'un enfant a un element de l'arbre. Dans le cas d'un membre,
	 * aucun enfant ne peut lui etre ajoute.
	 * 
	 * @param member
	 * @return void
	 */
	public void addListUO(UserObject member) {
	}

	/**
	 * Getter de l'id de l'element de l'arbre.
	 * 
	 * @return int
	 */
	public int getId() {
		return id_;
	}

	/**
	 * Getter de l'objet member de l'arbre
	 * @return Member
	 */
	public Member getMember() {
		return member;
	}

	/**
	 * Getter du nom du membre.
	 * 
	 * @return String
	 */
	public String getLabel() {
		return member.getName();
	}

	/**
	 * Getter du type d'objet de l'element de l'arbre.
	 * 
	 * @return String
	 */
	public String getType() {
		return type_;
	}

	/**
	 * Verification si le nom de tag est dans la liste de l'element de l'arbre.
	 * L'element de retour est un booleen qui indique si le tag est dans la liste.
	 * 
	 * @param tag_name
	 * @return boolean
	 */
	public boolean isTag(String tag_name) {
		if (tags_.contains(tag_name)) {
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
		return tags_;
	}

	/**
	 * Supression d'un tag dans la liste des tags de l'element de l'arbre. Retourne
	 * un booleen en fonction de si le tag a été supprime ou non. Dans le ca sd'un
	 * membre, aucun tag ne lui est ajoute dans l'element retourne sera toujours
	 * null.
	 * 
	 * @param tag_name
	 * @return boolean
	 */
	public boolean deleteTag(String tag_name) {
		return false;
	}

	/**
	 * (Pour le profil Bob) Methode de parcours de l'arbre. Cette methode va pour
	 * chaque element chercher si le tag reference est dans sa liste de tag, le
	 * supprimer si c'est le cas et appeler cette methode pour l'ensemble de ses
	 * enfants. Dans le cas d'un membre, ce dernier n'a pas d'elements enfants,
	 * cette methode ne fera pas d'appel.
	 * 
	 * @param tag
	 * @return void
	 */
	public void searchAndDeleteTags(String tag) {
	}

	/**
	 * Methode de recherche d'un element dans l'arbre pour un id et un type
	 * d'element donne. Si aucune correspondance n'a été trouve, la methode renvoi
	 * null.
	 * 
	 * @param id
	 * @param type
	 * @return UserObject
	 */
	public UserObject isInTree(int id, String type) {
		UserObject object = null;
		if (getId() == id && getType().equals(type)) {
			object = this;
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
		return iconUrl;
	}

	/**
	 * Ajout d'un tag a la liste des tags de l'element de l'arbre.
	 * 
	 * @param tag_name
	 * @return void
	 */
	public void addTag(String tag_name) {
	}

}
