package vue;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.table.DefaultTableModel;

import org.gitlab4j.api.models.Branch;
import org.gitlab4j.api.models.Project;

import control.BobMng;
import control.StatisticsController;
import model.arbre.ProjectObject;
import vue.TreePan.GitreeSelectionEvent;
import vue.TreePan.GitreeSelectionListener;

/**
 * Classe de creation et de gestion du panel des statistiques.
 * 
 * @see StatisticsController
 * @author LEROY Valentin
 * @author PALIX Xavier
 * @category vue
 */
public class SwingStatPage extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel stats_page;
	private TreePan display_tree;
	private JTextArea stat_textfield = new JTextArea();
	private JScrollPane scrool;
	private String text;
	private JLabel title = new JLabel();
	private JButton more_fonctionnalities_button;
	private String project_name = "";
	private long commits_max = 1;
	private Map<String, ArrayList<ArrayList<Integer>>> map = null;

	/**
	 * Constructeur. Le parametre TreePan permet d'avoir acces aux evenements de
	 * clicks sur l'arbre.
	 * 
	 * @param tree
	 */
	public SwingStatPage(TreePan tree) {
		// Creation du panel
		stats_page = new JPanel();
		stats_page.setName("Statistiques");
		stats_page.setLayout(null);
		stats_page.setBackground(new Color(0, 34, 108));

		// ajout du listener sur l'arbre
		display_tree = tree;
		display_tree.addGitreeSelectionListener(new StatPageTreeSlectionListener());

		// Ajout des proprietes au TextField
		stat_textfield.setBorder(null);
		stat_textfield.setEnabled(false);
		stat_textfield.setPreferredSize(new Dimension(725, 900));
		text = "\n\n\n\n\n\n\n\n\n\t Appuyez sur un projet de l'arbre pour afficher ses statistiques";
		stat_textfield.setText(text);

		// Instanciation d'un ScroolPane contenant le TextField
		scrool = new JScrollPane(stat_textfield);
		scrool.setBounds(20, 40, 745, 480);

		// Ajout des proprietes du titre contenu dans le TextField
		Utils.setLabel(title, 25, 0, 0, 745, 50);
		title.setForeground(Color.BLACK);
		title.setText("Infos :");

		// Si l'utilisateur est un Bob, ajout du bouton permettant d'acceder aux
		// fonctionnalites supplementaires
		if (BobMng.getIsBob()) {
			more_fonctionnalities_button = new JButton("+");
			more_fonctionnalities_button.addActionListener(this::buttonListener);
			more_fonctionnalities_button.setVisible(false);
			Utils.setButton(more_fonctionnalities_button, 15, 710, 10, 40, 20);
			more_fonctionnalities_button.setToolTipText(ToolTips.BTN_STAT);
			stats_page.add(more_fonctionnalities_button);
		}

		stat_textfield.add(title);
		stats_page.add(scrool);
	}

	/**
	 * Listener du bouton de fonctionnalites supplementaires pour Bob. Cette methode
	 * permet d'appeler la methode de creation de la Jframe comportant l'ensemble
	 * des donnees sur les commits.
	 * 
	 * @param event
	 * @return void
	 */
	private void buttonListener(ActionEvent event) {
		createStatFrame(this.project_name, this.map, this.commits_max);
	}
	public class StatPageTreeSlectionListener implements GitreeSelectionListener {
		/**
		 * Methode de gestion du TextField en fonction de la selection dans l'arbre.
		 * Affichage d'erreur si l'element selectionne est un membre ou un groupe ou si
		 * il y a plusieurs projets selectionnes en meme temps.
		 * 
		 * @param selection
		 * @return void
		 */
		@Override
		public void gitreeSelectionChanged(GitreeSelectionEvent selection) {
			if (selection.getSelectedMembers().size() != 0 || selection.getSelectedGroups().size() != 0) {
				title.setText("Erreur :");
				if (BobMng.getIsBob()) {
					project_name = "";
					map = null;
					commits_max = 1;
					more_fonctionnalities_button.setVisible(false);
				}
				text = "\n\n\n\n\n\n\n\n\n\t Il est possible d'afficher seulement des projets.";
				stat_textfield.setText(text);
				stat_textfield.add(title);
			} else {
				if (selection.getSelectedProjects().size() == 1) {
					ProjectObject project = (ProjectObject) selection.getSelectedProjects().get(0);
					displayStat(project.getProject());
				} else {
					title.setText("Erreur :");
					if (BobMng.getIsBob()) {
						project_name = "";
						map = null;
						commits_max = 1;
						more_fonctionnalities_button.setVisible(false);
					}
					text = "\n\n\n\n\t Il n'est possible d'afficher qu'un projet a la fois.";
					stat_textfield.setText(text);
					stat_textfield.add(title);
				}
			}
		}
	}

	/**
	 * Methode de creation et d'affichage du texte dans le TextField. Cette methode
	 * appelle l'ensemble des methodes de la classe StatisticsController.java afin
	 * de creer un texte contenant l'ensemble des informations a afficher pour
	 * l'utilisateur. Cette methode prend pour parametre le projet selectionne dans
	 * l'arbre.
	 * 
	 * @param project
	 * @return void
	 */
	@SuppressWarnings("unchecked")
	private void displayStat(Project project) {
		// Mise a jour du nom.
		title.setText("\n\nStatististiques du projet " + project.getName() + " : ");

		// Recuperation des infos concernant les commits
		Map<String, Object> return_tab = StatisticsController.getCommitsInfos(project);

		commits_max = 0;

		// ajout des derniers commits au texte
		if (((List<List<String>>) return_tab.get("last_commits")).size() != 0) {
			for (ArrayList<Integer> commit : ((Map<String, ArrayList<ArrayList<Integer>>>) return_tab
					.get("map_detailled_commits")).get("Global")) {
				commits_max += commit.get(2);
			}
			text = "\n\n\nNombre de Commit : " + commits_max + "\n";

			text += "Derniers Commits :\n\n";
			for (List<String> commit : (List<List<String>>) return_tab.get("last_commits")) {
				text += "\t |-> " + commit.get(0) + "\n";
				text += "\t |   " + commit.get(1) + "\n";
				text += "\t |   " + commit.get(2) + "\n";
				text += "\t |\n";
			}
			text += "\n";
			text += "Dernire action : " + ((List<List<String>>) return_tab.get("last_commits")).get(0).get(2) + "\n";
		} else {
			text = "\n\n\nNombre de Commit : " + commits_max + "\n";
			text += "\n";
		}

		// Ajout de la taille du repertoire au texte
		text += "Taille du repertoire : " + StatisticsController.getCommonStat(project).getStorageSize() + " octets\n";

		// Ajout de la liste des branches
		List<Branch> listBranch = StatisticsController.GetBranchNumber(project);
		if (listBranch != null) {
			text += "Nombre de Branches : " + listBranch.size() + "\n\n";
			for (Branch branch : listBranch) {
				text += "\t |-> " + branch.getName() + "\n";
			}
		} else {
			text += "Erreur lors de la recuperation des branches";
		}

		// Ajout de la repartition des langages au texte
		Map<String, Float> languages = StatisticsController.GetProjectLanguages(project);
		if (languages != null) {
			text += "\nRepartition des langages : \n\n";
			for (Map.Entry<String, Float> tab : languages.entrySet()) {
				text += "\t|";
				float valeur = tab.getValue();
				for (int i = 1; i <= (int) (valeur / 2); i++) {
					text += "#";
				}
				text += "(" + (int) valeur + "%) : " + tab.getKey() + "\n";
			}
		}
		stat_textfield.add(title);
		stat_textfield.setText(text);

		// Mise a jour des informations pour les fonctionnalites lies a Bob
		if (BobMng.getIsBob()) {
			project_name = project.getName();
			map = (Map<String, ArrayList<ArrayList<Integer>>>) return_tab.get("map_detailled_commits");
			more_fonctionnalities_button.setVisible(true);
		}
	}

	/**
	 * Methode de creation de la fenetre comprenant l'ensemble des statistiques des
	 * commits. Cette methode est appelee seulement si l'utilisateur est connecte en
	 * tant que Bob. L'appel de cette methode creer une nouvelle fenetre sans
	 * enlever l'ancienne ouverte ce qui permet de comparer les statistiques de
	 * plusieurs projets en meme temps. La nouvelle fenetre garde en memoire la Map
	 * et le nom du projet et le nombre maximum de commits associe afin de
	 * construire les bons tableaux dans le cas ou l'utilisateur ouvre plusieurs
	 * fenetres en meme temps.
	 * 
	 * @param project_name
	 * @param map_detailled_commits
	 * @param max_commits
	 * @return void
	 */
	public void createStatFrame(String project_name, Map<String, ArrayList<ArrayList<Integer>>> map_detailled_commits,
			long max_commits) {
		// Verification qu'il n'y ai pas d'erreur sur la creation de la Map.
		if (map != null) {
			// Creation du panel contenant le tableau des commits
			JPanel stat_panel = new JPanel();
			stat_panel.setBackground(new Color(0, 34, 108));
			stat_panel.setBounds(120, 0, 380, 300);

			// Creation de la fenetre
			JFrame fenetre_ = new JFrame(project_name);
			fenetre_.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			Utils.setJFrame(fenetre_, 800, 100, 500, 300);

			// Creation du panel (et du scroll) contenant l'ensemble des boutons
			// correspondants aux membres du projet.
			JPanel button_panel = new JPanel();
			JScrollPane scroll_button_panel = new JScrollPane(button_panel);
			scroll_button_panel.setBounds(0, 0, 120, 300);
			int position = 20;
			button_panel.setLayout(null);
			for (Map.Entry<String, ArrayList<ArrayList<Integer>>> tab : map_detailled_commits.entrySet()) {
				JButton authorButton = new JButton(tab.getKey());
				Utils.setButton(authorButton, 10, 10, position, 100, 15);
				button_panel.add(authorButton);
				authorButton.addActionListener(new ActionListener() {
					// En cas d'appui sur un membre, l'ancien tableau s'efface et est remplace par
					// le nouveau
					public void actionPerformed(ActionEvent e) {
						stat_panel.removeAll();
						createTable(stat_panel, tab.getValue(), tab.getKey(), max_commits);
					}
				});
				position += 20;
			}
			button_panel.setPreferredSize(new Dimension(100, position + 40));
			button_panel.setBackground(new Color(0, 34, 108));

			// Ajout des panels \E0 la fenetre
			fenetre_.add(scroll_button_panel);
			fenetre_.add(stat_panel);
		}
	}

	/**
	 * Methode de creation du tableau dans le panel donne (utile en cas d'ouverture
	 * de plusieurs fenetres). Le tableau est contruit pour un membre du projet
	 * donne.
	 * 
	 * @param panel
	 * @param mapAuthorProject
	 * @param authorName
	 * @param max_commits
	 * @return void
	 */
	public void createTable(JPanel panel, ArrayList<ArrayList<Integer>> mapAuthorProject, String authorName,
			long max_commits) {
		String[] columns = { "Date", "Nombre", "Rapport/CommitMax" };
		Object[][] data = new Object[mapAuthorProject.size() + 1][3];
		data[0][0] = authorName;
		int n = 1;
		int total = 0;
		for (ArrayList<Integer> donnees : mapAuthorProject) {
			data[n][0] = donnees.get(1) + "/" + donnees.get(0);
			data[n][1] = donnees.get(2);
			total += donnees.get(2);
			String text = "";
			float rapport = ((float) donnees.get(2) / (float) commits_max);
			int valeur = (int) (rapport * 10);
			for (int i = 0; i < valeur; i++) {
				text += "#";
			}
			data[n][2] = text + "( " + (int) (rapport * 100) + " %)";
			n++;
		}
		data[0][1] = total;
		JTable table = new JTable();
		DefaultTableModel model = new DefaultTableModel(data, columns);
		table.setModel(model);
		JScrollPane statPanelScrool = new JScrollPane(table);
		statPanelScrool.setBounds(0, 0, 370, 280);
		table.setPreferredSize(new Dimension(360, 900));
		table.setPreferredScrollableViewportSize(new Dimension(table.getPreferredSize().width, 200));
		table.setEnabled(false);
		panel.add(statPanelScrool);
	}

	/**
	 * Getter du panel des statistiques.
	 * 
	 * @param null
	 * @return stats_page
	 */
	public JPanel getPanel() {
		return stats_page;
	}

}
