package git_tse;

import javax.swing.SwingUtilities;

import control.GittseController;
import model.GittseModel;
import vue.GittseVue;

/**
 * Point d'entree de l'application
 * 
 * Instancie le MVC Lance l'application en ouvrant la page de connexion Gitlab
 * 
 * @author THOME Alban
 * 
 * @see GittseModel
 * @see GittseContoller
 * @see GittseVue
 */
public class Gittse {

	public Gittse() {
		GittseModel model = new GittseModel();
		GittseController controler = new GittseController(model);
		GittseVue vue = new GittseVue(model);
		controler.setView(vue);
		controler.startConnexionPage();
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				new Gittse();
			}
		});
	}

}
