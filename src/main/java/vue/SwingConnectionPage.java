package vue;

import java.awt.Color;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import control.GittseControllerConnexion;
import control.GittseControllerHomePage;
import model.GittseModel;
import resources.Ressources;

/**
 * Classe vue de la page de connexion
 * 
 * @author RAHMOUNI Youssra
 * @author LEROY Valentin
 * @author THOME Alban
 * @category vue
 * @see GittseControllerConnexion
 */
public class SwingConnectionPage { // ActionListener,

	private GittseModel model;
	private GittseControllerConnexion controller;
	private GittseVue view;

	private JFrame window;
	private JPanel connection_panel, deco_panel;

	// elements du panneau
	private JLabel label_0, label_1, label_2, label_s, image_label;
	private JTextField tf_1;
	private JPasswordField pf_1;
	private JButton b_1;
	private JCheckBox cb_1;
	private Image my_picture;

	/**
	 * Constructeur de la page de connexion
	 * 
	 * @param vue
	 * @param model
	 * @param controller
	 */
	public SwingConnectionPage(GittseVue vue_, GittseModel model_, GittseControllerConnexion controller_) {
		view = vue_;
		window = vue_.getFenetre();
		model = model_;
		controller = controller_;
		controller.setView(this);
		connection_panel = new JPanel();
		connection_panel.setName("Connection");
		createComponents();
		setProperties();
		createLayout();
		connection_panel.setVisible(true);
		window.setContentPane(connection_panel);
		tf_1.requestFocus();
		automaticFill(tf_1, pf_1);
		SwingUtilities.updateComponentTreeUI(window);
	}

	/**
	 * Methode pour la creation des composants de la page de connexion
	 */
	public void createComponents() {
		label_0 = new JLabel("Connexion GitLab");
		label_1 = new JLabel("Entrez votre nom.prenom :");
		tf_1 = new JTextField(15);
		label_2 = new JLabel("Entrez votre mot de passe :");
		pf_1 = new JPasswordField(15);
		b_1 = new JButton("Confirmer");
		cb_1 = new JCheckBox("Mot de Passe visible");
		label_s = new JLabel("");
		deco_panel = new JPanel();
		my_picture = Ressources.logoTSE;
		my_picture = my_picture.getScaledInstance(400, 500, Image.SCALE_SMOOTH);
		image_label = new JLabel(new ImageIcon(my_picture));
		// ajout des elements instancies au panel
		connection_panel.add(tf_1);
		connection_panel.add(pf_1);
		connection_panel.add(label_0);
		connection_panel.add(label_1);
		connection_panel.add(label_2);
		connection_panel.add(b_1);
		connection_panel.add(cb_1);
		connection_panel.add(label_s);
		connection_panel.add(deco_panel);
		deco_panel.add(image_label);
	}

	/**
	 * Creation du layout
	 */
	public void createLayout() {
		connection_panel.setLayout(null);
		deco_panel.setLayout(null);
	}

	/**
	 * Definition des proprietes des elements du layout
	 */
	public void setProperties() {
		connection_panel.setBackground(new Color(230, 30, 0));
		deco_panel.setBackground(new Color(0, 34, 108));
		deco_panel.setBounds(0, 0, 600, 600);
		// Action listener pour le checkbox pour la visibilite ou non du mot de passe
		cb_1.addActionListener(this::CB1Listener);
		cb_1.setBackground(new Color(230, 30, 0));
		cb_1.setBounds(650, 350, 200, 30);
		cb_1.setForeground(Color.WHITE);
		// Ajout des keyListener pour les textfields
		tf_1.addKeyListener(keyListener);
		pf_1.addKeyListener(keyListener);

		b_1.addActionListener(this::B1Listener);
		Utils.setButton(b_1, 15, 800, 450, 100, 30);

		Utils.setLabel(label_0, 25, 650, 80, 250, 30);
		Utils.setLabel(label_1, 15, 650, 160, 200, 30);
		Utils.setLabel(label_2, 15, 650, 250, 200, 30);
		Utils.setLabel(label_s, 15, 650, 30, 300, 30);
		Utils.setLabel(image_label, 40, 40, 15, 550, 550);

		Utils.setJTextField(tf_1, 650, 190);
		Utils.setJTextField(pf_1, 650, 280);
	}

	/**
	 * Checkbox listener
	 * 
	 * @param event
	 */
	private void CB1Listener(ActionEvent event) {
		if (cb_1.isSelected()) {
			pf_1.setEchoChar((char) 0);
		} else {
			pf_1.setEchoChar('*');
		}
	}

	@SuppressWarnings("deprecation")
	private void B1Listener(ActionEvent event) {
		connectionStatus(true);
		controller.tryConnection(tf_1.getText(), pf_1.getText());
		pf_1.setText("");
		pf_1.requestFocus();
	}

	// ajout du listener de la touche entrer pour faciliter l'acces a l'application
	KeyListener keyListener = new KeyListener() {
		public void keyPressed(KeyEvent keyEvent) {
			if (keyEvent.getKeyCode() == KeyEvent.VK_ENTER) {
				B1Listener(null);
			}
		}

		public void keyReleased(KeyEvent keyEvent) {

		}

		public void keyTyped(KeyEvent keyEvent) {

		}
	};

	public void setConnectionPanel_(JPanel connectionPanel_) {
		connection_panel = connectionPanel_;
	}

	public JTextField getTf() {
		return tf_1;
	}

	public void setTf(JTextField tF1_) {
		tf_1 = tF1_;
	}

	public JPasswordField getPf() {
		return pf_1;
	}

	public void setPf(JPasswordField pF1_) {
		pf_1 = pF1_;
	}

	public void startHomePageVue(GittseControllerHomePage controller) {
		new SwingHomePage(view, model, controller);
	}

	/**
	 * Affichage de la page qui change selon le statut de la connexion
	 * 
	 * @param bool
	 */
	public void connectionStatus(final Boolean bool) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				if (bool == true) {
					label_s.setText("Connexion en cours... Veuillez patienter");
					label_s.setForeground(Color.WHITE);
				} else {
					label_s.setText("La connexion a echouée, veuillez réessayer");
					label_s.setForeground(Color.YELLOW);
				}
			}
		});

		SwingUtilities.updateComponentTreeUI(window);
	}

	/**
	 * Pre-remplissage des champs nom d'utilisateur et mot de passe Cette methode
	 * est une methode d'aide au developpement et laisse volontairement a
	 * l'utilisateur si il souhaite l'utiliser
	 * 
	 * @param text_field
	 * @param password_field
	 */
	private void automaticFill(JTextField text_field, JPasswordField password_field) {
		try {
			// Lecture des champs depuis le fichier id.txt
			BufferedReader lecteurAvecBuffer = new BufferedReader(new FileReader("../id.txt"));
			text_field.setText(lecteurAvecBuffer.readLine());
			password_field.setText(lecteurAvecBuffer.readLine());
			lecteurAvecBuffer.close();
		} catch (IOException e) {
		}
	}
}
