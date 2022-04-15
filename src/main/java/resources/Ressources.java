package resources;

import java.awt.Image;
import java.awt.Toolkit;
/**
 * Classe qui regroupe toute les resources graphiques du projet
 * @author THOME Alban
 * @author RAHMOUNI Youssra
 */
public final class Ressources {
	// Instanciation et recuperation des images par la methode loadRessource
	public static final Image logoGittse = loadRessource("Icon.png");
	public static final Image logoTSE = loadRessource("TSELogo.png");
	public static final Image info = loadRessource("info.png");
	public static final Image image_1 = loadRessource("1.png");
	public static final Image image_2 = loadRessource("2.png");
	public static final Image image_3 = loadRessource("3.png");
	public static final Image image_4 = loadRessource("4.png");
	public static final Image image_5 = loadRessource("5.png");
	public static final Image image_6 = loadRessource("6.png");
	public static final Image image_7 = loadRessource("7.png");
	public static final Image image_8 = loadRessource("8.png");
	public static final Image image_9 = loadRessource("9.png");

	/**
	 * Mehode qui retourne l'objet image du path specifie
	 * 
	 * @param path
	 * @return objet de type Image
	 */
	private static Image loadRessource(String path) {

		try {
			return Toolkit.getDefaultToolkit().getImage(Ressources.class.getResource(path));
		} catch (Exception e1) {
			return null;
		}

	}
}
