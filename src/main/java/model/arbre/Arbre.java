package model.arbre;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import org.gitlab4j.api.GitLabApiException;
import org.gitlab4j.api.models.Group;
import org.gitlab4j.api.models.Member;
import org.gitlab4j.api.models.Project;

import control.GrpMng;
import control.ProjectsMng;
import model.TheGitLabApi;

/**
 * Classe d'objet pour la construction de l'arbre. Cette classe correspond a la
 * racine de l'arbre.
 * 
 * @see UserObject
 * @author LEROY Valentin
 * @author MALIFARGE Antoine
 * @category model.arbre
 *
 */
public class Arbre implements UserObject {

	private List<UserObject> childs = new ArrayList<UserObject>();
	private List<String> tags = null;
	private String type = "arbre";
	private URL url_icon = getClass().getResource("images/groupe.png");

	/**
	 * Constructeur de l'arbre. Cette methode recupere la liste des groupes et des
	 * projets dont on est proprietaire. Ensuite, la methode construit l'arbre en
	 * fonction de l'id du parent recupere par l'api. Dans cette methode le principe
	 * suivant a été applique : il n'est montre que les membres d'un projet car cela
	 * entrainerait une redondance avec l'onglet groupes de l'application, un groupe
	 * ne peut donc contenir que des groupes ou des projets. Les projets ne peuvent
	 * contenir que des membres.
	 * 
	 */
	public Arbre() {
		// Recuperation de la liste des groupes et des projets.
		List<Group> listeG = new ArrayList<Group>();
		List<Project> listeP = new ArrayList<Project>();
		URL group_icon = getClass().getResource("images/groupe.png");
		URL project_icon = getClass().getResource("images/projet.png");
		URL member_icon = getClass().getResource("images/membre.png");
		try {
			listeG = GrpMng.getGroups();
			listeP = ProjectsMng.getProjects();
		} catch (GitLabApiException e1) {
			JOptionPane.showMessageDialog(new JFrame(),
					"Impossible de telecharger les groupes et projets a la construction de l'arbre", "Erreur",
					JOptionPane.ERROR_MESSAGE);
		}

		// Ajout des groupes dans l'arbre
		while (listeG.size() != 0) {
			Group object = listeG.get(0);
			// Verification d'elements parent
			if (object.getParentId() == null) {
				childs.add(new GroupObject(object, group_icon));
			} else {
				UserObject parent;
				if ((parent = isInTree(object.getParentId(), "group")) != null) {
					// ajout dans l'arbre si le parent est deja present
					parent.addListUO(new GroupObject(object, group_icon));
				} else {
					// ajout a la fin de la liste si le parent n'est pas encore dans l'arbre
					listeG.add(object);
				}
			}
			listeG.remove(0);
		}

		// Ajout des projets dans l'arbre
		while (listeP.size() != 0) {
			Project object = listeP.get(0);
			// Verification d'element parent
			if (object.getNamespace().getKind().equals("user")) {
				// si le parent est l'utilisateur lui meme, ajout a la racine de l'arbre. (Le
				// projet n'appartient pas a un groupe)
				childs.add(new ProjectObject(object, project_icon));
			} else {
				UserObject parent;
				if ((parent = isInTree(object.getNamespace().getId(), "group")) != null) {
					// Si le parent est dans l'arbre, ajout du projet et de ces membres
					ProjectObject enfant = new ProjectObject(object, project_icon);
					parent.addListUO(enfant);
					try {
						List<Member> listeM = TheGitLabApi.gitLabApi_.getProjectApi().getAllMembers(object);
						for (Member membre : listeM) {
							enfant.addListUO(new MemberObject(membre, member_icon));
						}
					} catch (GitLabApiException e) {
						JOptionPane.showMessageDialog(new JFrame(),
								"Impossible de telecharger les membres du projet " + object.getName(), "Erreur",
								JOptionPane.ERROR_MESSAGE);
					}

				} else {
					listeP.add(object);
				}
			}
			listeP.remove(0);
		}
	}

	/**
	 * Getter du nom de l'arbre
	 * 
	 * @return String
	 */
	public String getLabel() {
		return ("Git'tse");
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
	@SuppressWarnings("null")
	public int getId() {
		return (Integer) null;
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
	 * (Pour le profil Bob) Methode de parcours de l'arbre. Cette methode va pour
	 * chaque element chercher si le tag reference est dans sa liste de tag, le
	 * supprimer si c'est le cas et appeler cette methode pour l'ensemble de ses
	 * enfants.
	 * 
	 * @param tag
	 * @return void
	 */
	public void searchAndDeleteTags(String tag) {
		for (UserObject object : childs) {
			object.searchAndDeleteTags(tag);
		}
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
		for (UserObject objet : childs) {
			if (objet.isInTree(id, type) != null) {
				object = objet.isInTree(id, type);
			}
		}
		return object;
	}

	// Classe ajoutees pour correspondre a l'interface

	/**
	 * Ajout d'un tag a la liste des tags de l'element de l'arbre.
	 * 
	 * @param tag_name
	 * @return void
	 */
	public void addTag(String tag_name) {
	}

	/**
	 * Verification si le nom de tag est dans la liste de l'element de l'arbre.
	 * L'element de retour est un booleen qui indique si le tag est dans la liste.
	 * 
	 * @param tag_name
	 * @return boolean
	 */
	public boolean isTag(String tag_name) {
		return false;
	}

	/**
	 * Supression d'un tag dans la liste des tags de l'element de l'arbre. Retourne
	 * un booleen en fonction de si le tag a été supprime ou non.
	 * 
	 * @param tag_name
	 * @return boolean
	 */
	public boolean deleteTag(String tag_name) {
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
}
