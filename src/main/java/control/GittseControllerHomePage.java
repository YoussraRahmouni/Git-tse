package control;

import model.GittseModel;
import model.arbre.GroupObject;
import model.arbre.ProjectObject;
import model.arbre.UserObject;

/**
 * Classe controller gerant la page d'accueil, implemente Abstract Controller
 * @author THOME Alban
 * @author MALIFARGE Antoine
 * @author LEROY Valentin
 * @category control
 * @see AbstractController
 */
public class GittseControllerHomePage extends AbstractController {
	
	/**
	 * constructeur
	 * @param m : Le model global de l'application Gittse
	 */

	public GittseControllerHomePage(GittseModel m) {
		super(m);

	}
	
	/**
	 * 
	 * @return l'arbre initialise, les projets avec pour "enfants" leurs membres
	 */

	public UserObject getArbre() {
		ProjectsMng.initArbre();
		return ProjectsMng.getArbre();
	}
	
	/**
	 * fonctions qui supprime l'element dans l'arbre si c'est un groupe ou projet
	 * @param uObject : un element de l'arbre
	 */

	public void suprUserObject(UserObject u_object) {
		if (u_object.getType() == "project") {
			ProjectsMng.removeProject(((ProjectObject) u_object).getId());
		}
		if (u_object.getType() == "group") {
			GrpMng.removeGroup(((GroupObject) u_object).getId());
		}
	}

}
