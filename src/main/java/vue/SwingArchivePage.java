package vue;

import java.awt.Color;
import java.awt.Dimension;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import org.gitlab4j.api.models.Project;

import control.ProjectsMng;

/**
 * Classe pour la creation du panel qui contient la liste des projets archives
 * @author RAHMOUNI Youssra
 * @category vue
 * @see ProjectsMng
 */
public class SwingArchivePage extends JPanel {

	private static final long serialVersionUID = 1L;
	private JPanel archived_panel;

	public SwingArchivePage() {
		archived_panel = new JPanel();
		archived_panel.setBackground(new Color(0, 34, 108));
		// Methode pour le remplissage de la table
		populateTable();
	}

	public void populateTable() {
		// Recupere la liste des projets archives
		List<Project> projects = ProjectsMng.archivedProjects();
		// Definition des noms colonnes
		String[] columns = { "Project Name", "Default Branch" };
		// Remplissage de l'objet data pour stocker le nom du projet et sa branche
		Object[][] data = new Object[projects.size()][2];
		int n = 0;
		for (Project project : projects) {
			data[n][0] = project.getName();
			data[n][1] = project.getDefaultBranch();
			n++;
		}
		// Instanciqtion de la table
		JTable archivedTable = new JTable();
		// Instanciation du model de la table
		DefaultTableModel archivedModel = new DefaultTableModel(data, columns);
		archivedTable.setEnabled(false);
		// Placer la table dans un JScrollPane
		JScrollPane scrollPane = new JScrollPane(archivedTable);
		archivedTable.setPreferredSize(new Dimension(600, 600));
		scrollPane.setSize(new Dimension(600, 600));
		archivedTable.setPreferredScrollableViewportSize(new Dimension(archivedTable.getPreferredSize().width, 480));
		scrollPane.setBounds(80, 70, 350, 400);
		archived_panel.add(scrollPane);
		archivedTable.setModel(archivedModel);
	}

	/**
	 * 
	 * @return archived_panel objet JPanel contenant la table des projets archives
	 */
	public JPanel getPanel() {
		return archived_panel;
	}

}
