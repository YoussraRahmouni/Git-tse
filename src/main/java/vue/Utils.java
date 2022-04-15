package vue;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

import resources.Ressources;

/**
 * Classe non instanciable d'outils servant a la creation des elements en
 * frontend. Cette classe permet d'assurer une harmonie pour l'ensemble des
 * elements visuels du projet. Cette classe evite egalement les redondances dans
 * le code.
 * 
 * @author LEROY Valentin
 * @category vue
 */
public final class Utils {

	/**
	 * Methode permettant de definir l'ensemble des caracteristiques d'un bouton
	 * donne. L'element size defini la taille du contenu a l'interieur du bouton. x,
	 * y, width et height definissent la position du bouton. Le layout doit donc
	 * etre defini a "null" pour utiliser cette methode completement.
	 * 
	 * @param button
	 * @param size
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 * @return void
	 */
	public static void setButton(JButton button, int size, int x, int y, int width, int height) {
		button.setBackground(new Color(255, 255, 255));
		button.setForeground(new Color(0, 34, 108));
		button.setOpaque(true);
		button.setContentAreaFilled(true); // On met false pour empcher le composant de peindre l'intrieur du JButton.
		button.setBorderPainted(false); // De mme, on ne veut pas afficher les bordures.
		button.setFocusPainted(true); // On n'affiche pas l'effet de focus.
		button.setPreferredSize(new Dimension(width, height));
		button.setFont(new Font("Calibri", Font.BOLD, size));
		button.setBounds(x, y, width, height);
	}

	/**
	 * Methode permettant de definir l'ensemble des caracteristiques pour un label
	 * donne. La taille est a definir dans les parametres. x, y, width et height
	 * definissent la position du label. Le layout doit donc etre defini a "null"
	 * pour utiliser cette methode completement. La couleur par defaut est blanc
	 * (utile pour cette application) mais peut etre reecrite apres l'appel de cette
	 * methode.
	 * 
	 * @param text
	 * @param taille
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 * @return void
	 */
	public static void setLabel(JLabel text, int taille, int x, int y, int width, int height) {
		text.setForeground(Color.WHITE);
		text.setFont(new Font("Calibri", Font.BOLD, taille));
		text.setBounds(x, y, width, height);
	}

	/**
	 * Methode permettant de definir l'ensemble des caracteristiques pour un
	 * comboBox donne. On defini la liste des elements a ajouter a la comboBox. x,
	 * y, width et height definissent la position de la box. Le layout doit donc
	 * etre defini a "null" pour utiliser cette methode completement. La couleur par
	 * defaut est blanc (utile pour cette application) mais peut etre reecrite apres
	 * l'appel de cette methode.
	 * 
	 * @param CB
	 * @param elements
	 * @param x
	 * @param y
	 * @return void
	 */
	public static void setComboBox(JComboBox<String> CB, ArrayList<String> elements, int x, int y) {
		CB.removeAllItems();
		for (String element : elements) {
			CB.addItem(element);
		}
		CB.setFont(new Font("Calibri", Font.BOLD, 15));
		CB.setEditable(false);
		CB.getEditor().getEditorComponent().setBackground(Color.WHITE);
		CB.setBounds(x, y, 200, 30);
	}

	/**
	 * Methode permettant de definir l'ensemble des caracteristiques pour un
	 * textField donne. x, y definissent la position du texte. Le layout doit etre
	 * defini a "null" pour utiliser cette methode completement. La couleur par
	 * defaut est blanc (utile pour cette application) mais peut etre reecrite apres
	 * l'appel de cette methode.
	 * 
	 * @param text
	 * @param x
	 * @param y
	 * @return void
	 */
	public static void setJTextField(JTextField text, int x, int y) {
		text.setFont(new Font("Calibri", Font.BOLD, 15));
		text.setBounds(x, y, 200, 30);
		text.setText("");
	}

	/**
	 * Methode permettant de definir l'ensemble des caracteristiques pour une
	 * fenetre donne. x, y, width et height definissent la position et la taille de
	 * la fenetre. Le layout doit donc etre defini a "null" pour utiliser cette
	 * methode completement.
	 * 
	 * @param fenetre
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 * @return void
	 */
	public static void setJFrame(JFrame fenetre, int x, int y, int width, int height) {
		fenetre.setIconImage(Ressources.logoGittse);
		fenetre.setBounds(x, y, width, height);
		fenetre.setLayout(null);
		fenetre.setResizable(false);
		fenetre.setVisible(true);
	}
}
