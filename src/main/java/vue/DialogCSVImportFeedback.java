package vue;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import control.ImportCSV;
import resources.Ressources;

/**
 * Fenetre affichant le deroulement de l'import CSV
 * 
 * Au cours de l'import, cette fenetre affiche le rapport d'execution de ce
 * dernier
 * 
 * Si l'on ferme cette fenetre a l'aide de la croix de la fenetre, une
 * confirmation apparait. Dans ce cas l'execution du rapport CSV est interrompue
 * a la ligne suivante
 * 
 * @author THOME Alban
 * 
 * @see ImportCSV
 * @category vue
 */
public class DialogCSVImportFeedback extends javax.swing.JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Cree une nouvelle fenetre de rapport d'import CSV
	 */
	public DialogCSVImportFeedback(java.awt.Frame parent) {
		super(parent, false);
		setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				if (!ImportCSV.isImportTermine()) {
					int result = JOptionPane.showConfirmDialog(new JFrame(),
							"Etes-vous sur de vouloir interrompre l'import CSV ?", "Import CSV en cours d'execution",
							JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

					if (result == JOptionPane.YES_OPTION) {
						ImportCSV.stopImport();
					}
				} else {
					dispose();
				}
			}
		});
		initComponents();
		this.setLocationRelativeTo(parent);
		this.setIconImage(Ressources.logoGittse);
		this.setVisible(true);
	}

	private void initComponents() {

		jScrollPane = new javax.swing.JScrollPane();
		jLabelRapportCSV = new javax.swing.JLabel();
		setTitle("Rapport import CSV");

		jLabelRapportCSV.setText(" ");
		jLabelRapportCSV.setVerticalAlignment(javax.swing.SwingConstants.TOP);
		jLabelRapportCSV.setBorder(javax.swing.BorderFactory.createEmptyBorder(8, 15, 8, 15));
		jScrollPane.setViewportView(jLabelRapportCSV);

		javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
		getContentPane().setLayout(layout);
		layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addComponent(jScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 700, Short.MAX_VALUE));
		layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addComponent(jScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 450, Short.MAX_VALUE));

		pack();
	}

	/**
	 * Affiche dans la fenetre le rapport d'execution de l'import CSV
	 * 
	 * @param rapport
	 */
	public void setRapportCSV(final String rapport) {

		SwingUtilities.invokeLater(new Runnable() { // pour affichage depuis le thread import CSV
			public void run() {
				jLabelRapportCSV
						.setText("<html>" + rapport.replaceAll("\n", "<br>").replaceAll("é", "&eacute;") + "</html>");
			}
		});

	}

	private javax.swing.JLabel jLabelRapportCSV;
	private javax.swing.JScrollPane jScrollPane;
}
