package vue;

import java.awt.Color;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SpringLayout;

import org.gitlab4j.api.GitLabApiException;
import org.gitlab4j.api.models.Group;
import org.gitlab4j.api.models.GroupFilter;
import org.gitlab4j.api.models.Member;

import control.DialogGrpMngControler;
import control.ListMng;
import model.TheGitLabApi;
import model.arbre.GroupObject;
import vue.TreePan.GitreeSelectionEvent;
import vue.TreePan.GitreeSelectionListener;

/**
 * Class de la page gerant les groupes
 * 
 * @author PALIX Xavier
 * @author THOME Alban
 * @author LEROY Valentin
 *
 */
public class SwingGroupPage {

	private JPanel group_page;
	private JFrame window;
	private TreePan display_tree;
	private JTextArea group_area = new JTextArea();
	private JScrollPane scrool;
	private String text;
	private JLabel title_label = new JLabel();
	private JButton btn_change = new JButton("Gerer groupe");
	private JButton btn_open_file = new JButton("Voir fichier");
	private org.gitlab4j.api.models.Group selected_group;
	private Map<String, ArrayList<ArrayList<Integer>>> map = null;

	/**
	 * constructeur initialisation des composents et placement de ceux-ci
	 * 
	 * @param vue  : vue global
	 * @param tree : l'arbre de groupe et projet
	 */
	public SwingGroupPage(GittseVue vue, TreePan tree) {
		window = vue.getFenetre();
		group_page = new JPanel();
		group_page.setName("Group List");
		group_page.setLayout(null);
		group_page.setBackground(new Color(0, 34, 108));

		display_tree = tree;
		display_tree.addGitreeSelectionListener(new GroupPageTreeSlectionListener());

		group_area.setBorder(null);
		group_area.setEnabled(false);
		group_area.setPreferredSize(new Dimension(725, 900));

		scrool = new JScrollPane(group_area);
		scrool.setBounds(20, 50, 745, 470);

		Utils.setLabel(title_label, 18, 0, 0, 745, 50);
		title_label.setForeground(Color.BLACK);
		title_label.setText("Infos :");

		btn_change.addActionListener(this::Grp_Listener);
		Utils.setButton(btn_change, 16, 20, 10, 150, 30);
		btn_change.setVisible(false);

		btn_open_file.addActionListener(this::Open_Listener);
		Utils.setButton(btn_open_file, 16, 615, 10, 150, 30);
		btn_open_file.setVisible(true);

		text = "\n\n\n\n\n\n\n\n\n\t Appuyez sur un groupe de l'arbre pour afficher ses membres";

		group_area.setText(text);
		group_area.add(title_label);
		group_page.add(btn_change);
		group_page.add(btn_open_file);
		group_page.add(scrool);
	}

	public class GroupPageTreeSlectionListener implements GitreeSelectionListener {
		/**
		 * Listener sur la selection dans l'arbre affichage d'un message en fonction de
		 * l'objet selectionne dans l'arbre
		 * 
		 * @param event
		 */

		public void gitreeSelectionChanged(GitreeSelectionEvent event) {
			if (event.getSelectedMembers().size() != 0 || event.getSelectedProjects().size() != 0) {
				title_label.setText("Erreur :");
				map = null;
				btn_change.setVisible(false);
				text = "\n\n\n\n\n\n\n\n\n\t Cliquez sur un groupe pour afficher ses membres.";
				group_area.setText(text);
				group_area.add(title_label);
			} else {
				if (event.getSelectedGroups().size() == 1) {
					GroupObject group = (GroupObject) event.getSelectedGroups().get(0);
					setSelectedgroup(group.getGroup());
					DisplayGroup(group.getGroup());
				} else {
					title_label.setText("Erreur :");
					map = null;
					btn_change.setVisible(false);
					text = "\n\n\n\n\t Il n'est possible d'afficher qu'un groupe a la fois.";
					group_area.setText(text);
					group_area.add(title_label);
				}

			}
		}
	}

	/**
	 * affichage du groupe selectionne et ses membres si l'utilisateur est Owner
	 * 
	 * @param group : group selectionne dans l'arbre
	 */
	private void DisplayGroup(Group group) {
		title_label.setText("\n\n " + group.getFullName() + " : ");

		text = "\n\n\n\n Membres : \n\n";
		try {
			boolean contains = false;
			GroupFilter filter = new GroupFilter();
			filter.withOwned(true);
			List<Group> Grouplist = TheGitLabApi.gitLabApi_.getGroupApi().getGroups(filter);
			for (int c = 0; c < Grouplist.size(); c++) {
				if (Grouplist.get(c).getId().equals(group.getId())) {
					contains = true;
				}
			}
			if (contains) {
				List<Member> Groupmembers = TheGitLabApi.gitLabApi_.getGroupApi().getAllMembers(group);
				for (int j = 0; j < Groupmembers.size(); j++) {
					text += " - " + Groupmembers.get(j).getUsername().toString() + "\n";
				}
			} else {
				text += "vous ne possedez pas ce groupe";
			}
		} catch (GitLabApiException e) {
			e.printStackTrace();
		}
		group_area.add(title_label);
		group_area.setText(text);
		btn_change.setVisible(true);
	}

	/**
	 * creation de l'onglet groupe
	 * 
	 * @param groupName
	 */
	public void createGroupFrame(String group_name) {
		if (map != null) {
			JPanel StatPanel = new JPanel();

			StatPanel.setBackground(new Color(0, 34, 108));
			StatPanel.setBounds(120, 0, 380, 300);

			JFrame window_bis = new JFrame(group_name);
			window_bis.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			window_bis.setBounds(800, 100, 500, 300);
			window_bis.setLayout(null);
			window_bis.setResizable(false);
			window_bis.setVisible(true);
			JPanel btn_panel = new JPanel();
			JScrollPane btn_panel_scrool = new JScrollPane(btn_panel);
			btn_panel_scrool.setBounds(0, 0, 120, 300);
			int position = 20;
			SpringLayout layout = new SpringLayout();
			btn_panel.setLayout(layout);
			btn_panel.setPreferredSize(new Dimension(100, position + 40));
			btn_panel.setBackground(new Color(0, 34, 108));

			window_bis.add(btn_panel_scrool);
			window_bis.add(StatPanel);
		}
	}

	public void refresh() {
		DisplayGroup(selected_group);
	}

	/**
	 * Construction de la boite de dialogue de gestion de groupe sur le
	 * actionlistener de button_
	 * 
	 * @param event
	 */
	private void Grp_Listener(ActionEvent event) {
		new DialogGrpMngControler(window, selected_group, this);
	}

	private void Open_Listener(ActionEvent event) {
		ListMng.openGrouplist();
	}

	/**
	 * getter
	 * 
	 * @return groupPage_ : onglet groupe
	 */

	public JPanel getPanel() {
		return group_page;
	}

	/**
	 * getter
	 * 
	 * @return Selectedgroup : groupe selectionne dans l'arbre
	 */
	public org.gitlab4j.api.models.Group getSelectedgroup() {
		return selected_group;
	}

	/**
	 * setter
	 * 
	 * @param selectedgroup : groupe selectionne dans l'arbre
	 */
	public void setSelectedgroup(org.gitlab4j.api.models.Group selectedgroup) {
		selected_group = selectedgroup;
	}

	public static void launch(String filename) {
		File file = new File(filename);
		if (!file.exists() && file.length() < 0) {
			System.out.println("Le fichier specifie n'existe pas");
			System.exit(0);
		}
		Desktop desktop = null;
		if (Desktop.isDesktopSupported()) {
			desktop = Desktop.getDesktop();
		}
		try {
			desktop.edit(file);
		} catch (IOException ex) {
		}
	}

}
