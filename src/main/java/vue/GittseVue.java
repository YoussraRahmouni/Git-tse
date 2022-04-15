package vue;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import control.GittseControllerConnexion;
import model.GittseModel;
import model.TheGitLabApi;
import resources.Ressources;

/**
 * Vue principale de l'application, dans le cadre de l'architecture MVC.
 * 
 * Elle met en place la fenetre JFrame principale de l'application L'ensemble de
 * l'application est d'une taille graphique fixe. Ce choix a été retenu pour sa
 * simplicite de mise en oeuvre et de la reduction de tests d'integration a
 * realiser
 * 
 * Le LookAndFeel utilise est celui du systeme d'exploitation sur lequel
 * l'application est executee
 * 
 * @author THOME Alban 
 * @author LEROY Valentin 
 * @category vue
 */

public class GittseVue {

	private GittseModel model;
	private JFrame frame;

	public GittseVue(GittseModel model) {
		this.model = model;
		frame = new JFrame("GIT'TSE");
		frame.setIconImage(Ressources.logoGittse);
		Utils.setJFrame(frame, 200, 100, 1000, 600);

		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				try {
					TheGitLabApi.gitLabApi_.close(); // Ferme proprement la connexion gitlab avant de fermer
				} catch (Exception e1) {
				}
				System.exit(0);

			}
		});
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE); // Execute d'abord le contenu du windowListener
																	// ci-dessus

		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
				| UnsupportedLookAndFeelException e) {
		}
	}

	/**
	 * Instancie la page de connexion Gitlab
	 * 
	 * @param controller
	 */
	public void startConnectionPageVue(GittseControllerConnexion controller) {
		new SwingConnectionPage(this, model, controller);
	}

	public JFrame getFenetre() {
		return frame;
	}

}
