package vue;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import resources.Ressources;

/**
 * Classe pour la creation de la page d'aide
 * @author RAHMOUNI Youssra
 * @category vue
 */
public class SwingHelpPage extends JFrame implements ActionListener {

	private static final long serialVersionUID = 1L;
	ImageIcon s[];
	JLabel l;
	JButton b1, b2;
	int i, l1;
	JPanel p;

	/**
	 * Constructeur de la classe helpPage
	 */
	public SwingHelpPage() {
		setLayout(new BorderLayout());
		setSize(1000, 650);
		setVisible(true);
		JPanel p = new JPanel(new FlowLayout());
		// Bouton de navigation entre les images
		b1 = new JButton("<<");
		b2 = new JButton(">>");
		// Ajout des composants de la page
		p.add(b1);
		p.add(b2);
		add(p, BorderLayout.SOUTH);
		b1.addActionListener(this);
		b2.addActionListener(this);
		s = new ImageIcon[9];
		// Ajouts des images
		s[0] = new ImageIcon(Ressources.image_1);
		s[1] = new ImageIcon(Ressources.image_2);
		s[2] = new ImageIcon(Ressources.image_3);
		s[3] = new ImageIcon(Ressources.image_4);
		s[4] = new ImageIcon(Ressources.image_5);
		s[5] = new ImageIcon(Ressources.image_6);
		s[6] = new ImageIcon(Ressources.image_7);
		s[7] = new ImageIcon(Ressources.image_8);
		s[8] = new ImageIcon(Ressources.image_9);
		l = new JLabel("", JLabel.CENTER);
		add(l, BorderLayout.CENTER);
		l.setIcon(s[0]);

	}

	/**
	 * Methode pour la navigation entre les images
	 */
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == b1) {
			if (i == 0) {
				b1.setEnabled(false);
			} else {
				i = i - 1;
				l.setIcon(s[i]);
				b1.setEnabled(true);
			}
		}
		if (e.getSource() == b2) {
			if (i == s.length - 1) {
				b2.setEnabled(false);
			} else {
				i = i + 1;
				l.setIcon(s[i]);
				b2.setEnabled(true);
			}
		}

	}
}
