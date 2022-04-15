package control;

import model.GittseModel;
import vue.GittseVue;

/**
 * Controlleur principal de l'application, dans le cadre de l'architecture MVC.
 * Derive de {@link AbstractController}
 * 
 * @author THOME Alban 
 * @author LEROY Valentin 
 * @see AbstractController
 * @category control
 */

public class GittseController extends AbstractController {

	public GittseController(GittseModel m) {
		super(m);
	}

	public void setView(GittseVue temp_vue) {
		this.view = temp_vue;
	}

	/**
	 * Lance la page de connexion en instanciant son controlleur
	 */
	public void startConnexionPage() {
		new GittseControllerConnexion(model, view);
	}

}
