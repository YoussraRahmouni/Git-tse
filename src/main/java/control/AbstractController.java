package control;

import model.GittseModel;
import vue.GittseVue;

/**
 * Classe abstraite dont derive les autres controleurs. Stocke l'acces au modele
 * 
 * @author THOME Alban
 * @see GittseModel
 * @category control
 */
public class AbstractController {
	protected GittseModel model;
	protected GittseVue view = null;

	public AbstractController(GittseModel m) {
		model = m;
	}
}
