package vue;

import java.awt.Color;
import java.awt.event.ActionEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

import control.BobMng;
import control.DialogProjectCreatorControler;
import control.GittseControllerHomePage;
import control.ImportCSV;
import control.ModulesMng;
import control.ProjectsMng;
import model.GittseModel;
import model.arbre.UserObject;
import resources.Ressources;
import vue.TreePan.GitreeSelectionEvent;
import vue.TreePan.GitreeSelectionListener;

/**
 * Classe principale de la vue gerant toutes les autres pages une fois
 * l'utilisateur connecte.
 * 
 * @author LEROY Valentin
 * @author MALIFARGE Antoine
 *
 */
public class SwingHomePage {

	private GittseControllerHomePage controller;

	private JFrame window;
	private JTabbedPane menu;
	private JPanel home_panel;
	private SwingStatPage stat_page;
	private SwingArchivePage archive_page;
	private SwingModulePage module_page;
	private SwingGroupPage group_page;

	private JSplitPane split_panel;
	private TreePan display_tree;
	private JScrollPane display_module_selection;
	private JPanel tree_menu;
	private GitreeSelectionEvent tree_selection;
	private JLabel L1, L2;
	private JTextField TF1;

	private JButton button_csv, button_add, button_supr, button_archive, button_addbob, button_infos;

	/**
	 * Constructeur de la classe. Inititalise l'ensemble des champs de la classe.
	 * 
	 * @param vue
	 * @param model
	 * @param controller_hp
	 */
	public SwingHomePage(GittseVue vue, GittseModel model, GittseControllerHomePage controller_hp) {
		window = vue.getFenetre();
		controller = controller_hp;
		menu = new JTabbedPane();
		display_tree = new TreePan(controller);
		display_tree.addGitreeSelectionListener(new HomePageTreeSelectionListener());
		createComponents(vue);
		setProperties();
		createLayout();

		split_panel.setBackground(new Color(0, 34, 108));
		split_panel.setDividerLocation(200);
		menu.setLayout(null);
		menu.setVisible(true);
		window.setContentPane(split_panel);
		SwingUtilities.updateComponentTreeUI(window);
	}

	/**
	 * Methode de creation des differentes pages ainsi que l'ensemble de l'arbre.
	 * 
	 * @param vue
	 * @return void
	 */
	public void createComponents(GittseVue vue) {
		createHP();
		createSP();
		createAP();
		createGP(vue);
		if (BobMng.getIsBob()) {
			ModulesMng.initModule();
			display_module_selection = new JScrollPane();
			display_module_selection.setViewportView(new ModuleSelector(display_tree));
			createMP();
			menu.add(module_page.getPanel().getName(), module_page.getPanel());
			tree_menu = new JPanel();
			tree_menu.setLayout(null);
			display_tree.setBounds(0, 0, 400, 450);
			display_module_selection.setBounds(0, 450, 400, 110);
			tree_menu.add(display_tree);
			tree_menu.add(display_module_selection);
			ModulesMng.assignerTags(ProjectsMng.getArbre());
			split_panel = new JSplitPane(SwingConstants.VERTICAL, tree_menu, menu);

		} else {
			split_panel = new JSplitPane(SwingConstants.VERTICAL, display_tree, menu);
		}

	}

	/**
	 * Methode de creation de la page principale. Cette methode initialise
	 * l'ensemble des composants de cette page.
	 * 
	 * @return void
	 */
	public void createHP() {
		home_panel = new JPanel();
		home_panel.setName("HomePage");
		button_csv = new JButton("Import CSV");
		button_csv.setToolTipText(ToolTips.BTN_CSV);
		button_add = new JButton("Ajouter");
		button_add.setToolTipText(ToolTips.BTN_ADD);
		button_supr = new JButton("Supprimer");
		button_supr.setToolTipText(ToolTips.BTN_SUPR);
		button_archive = new JButton("Archiver Projet(s)");
		button_archive.setToolTipText(ToolTips.BTN_ARCHIVE);
		ImageIcon icon = new ImageIcon(Ressources.info); // scale it the smooth way transform it back
		button_infos = new JButton(icon);
		L1 = new JLabel("Bienvenue dans GIT'TSE");
		L2 = new JLabel();
		home_panel.add(button_csv);
		home_panel.add(button_add);
		home_panel.add(button_supr);
		home_panel.add(button_archive);
		home_panel.add(L1);
		home_panel.add(L2);
		home_panel.add(button_infos);
		if (BobMng.getIsBob()) {
			TF1 = new JTextField(30);
			button_addbob = new JButton("ajouter");
			home_panel.add(TF1);
			home_panel.add(button_addbob);

		}

	}

	/**
	 * Appel de la classe d'instanciation du panel statistiques.
	 * 
	 * @return void
	 */
	public void createSP() {
		stat_page = new SwingStatPage(display_tree);
	}

	/**
	 * Appel de la classe d'instanciation du panel projets archives.
	 * 
	 * @return void
	 */
	public void createAP() {
		archive_page = new SwingArchivePage();
	}

	/**
	 * Appel de la classe d'instanciation du panel modules.
	 * 
	 * @return void
	 */
	public void createMP() {
		module_page = new SwingModulePage(display_tree, controller);
	}

	/**
	 * Appel de la classe d'instanciation du panel groupes.
	 * 
	 * @return void
	 */
	public void createGP(GittseVue vue) {
		group_page = new SwingGroupPage(vue, display_tree);
	}

	/**
	 * Methode pour definir le layout
	 * 
	 * @return void
	 */
	public void createLayout() {
		home_panel.setLayout(null);
	}

	/**
	 * Methode definissant les proprietes des elements de la page principale
	 * 
	 * @return void
	 */
	public void setProperties() {
		String tabButtonCss = "margin:0;width:90px;height:7px;border-radius:8px;background:#ffffff;text-align:center;border-color:#fffff;border-width:3px";
		menu.addTab(generateHtml("Page Principale", tabButtonCss), null, home_panel, ToolTips.MENU_HOME);
		menu.addTab(generateHtml("Projets Archives", tabButtonCss), null, archive_page.getPanel(),
				ToolTips.MENU_ARCHIVE);
		menu.addTab(generateHtml("Statistiques", tabButtonCss), null, stat_page.getPanel(), ToolTips.MENU_STAT);
		menu.addTab(generateHtml("Groupes", tabButtonCss), null, group_page.getPanel(), ToolTips.MENU_GRP);

		if (BobMng.getIsBob()) {
			menu.addTab(generateHtml("Modules", tabButtonCss), null, module_page.getPanel(), ToolTips.MENU_MODULE);
			Utils.setJTextField(TF1, 40, 250);
		}

		int x = 500;
		menu.setBorder(new EmptyBorder(0, -5, -10, -10));
		home_panel.setBackground(new Color(0, 34, 108));

		button_add.addActionListener(this::addUserObjectListener);
		Utils.setButton(button_add, 20, x, 100, 200, 60);

		button_supr.addActionListener(this::suprUserObjectListener);
		Utils.setButton(button_supr, 20, x, 200, 200, 60);

		button_archive.addActionListener(this::archiveListener);
		Utils.setButton(button_archive, 20, x, 300, 200, 60);

		button_csv.addActionListener(this::csvListener);
		Utils.setButton(button_csv, 20, x, 400, 200, 60);

		button_infos.addActionListener(this::infosListener);
		Utils.setButton(button_infos, 10, 700, 10, 30, 30);
		button_infos.setToolTipText(ToolTips.BTN_INFOS);

		if (BobMng.getIsBob()) {
			String text = "<html><H2>En tant que super utilisateur, vous pouvez en ajouter d'autres.<br>Renseignez son identifiant (nom.prenom) :</H2></html>";
			L2.setText(text);

			button_addbob.addActionListener(this::addBobListener);
			Utils.setButton(button_addbob, 12, 200, 300, 100, 30);
		} else {
			String text = "<html><H2>En tant qu'utilisateur standard, vous êtes limités dans l'utilisation de l'application.<br>Veuillez contacter un super utilisateur pour plus d'options.</H2></html>";
			L2.setText(text);
		}

		Utils.setLabel(L1, 30, 40, 20, 400, 40);
		Utils.setLabel(L2, 30, 40, 70, 250, 250);

	}

	/**
	 * Methode de mise en forme du menu.
	 * 
	 * @param tab_button_label
	 * @param style
	 * @return return_text
	 */
	public static String generateHtml(String tab_button_label, String style) {
		String return_text = "<html><body style = '" + style + "'>" + tab_button_label + "</body></html>";
		return return_text;
	}

	/// Actions listeners

	/**
	 * Listener du bouton d'ajout des bob.
	 * 
	 * @param event
	 */
	private void addBobListener(ActionEvent event) {
		if (!TF1.getText().equals("")) {
			BobMng.setAsBob(TF1.getText());
			JOptionPane.showConfirmDialog(new JFrame(),
					TF1.getText() + " a été ajouté a la liste des supers utilisateurs", "INFO",
					JOptionPane.PLAIN_MESSAGE);
			TF1.setText("");
			System.out.println("Ajout de " + TF1.getText() + " a la liste des Bob");
		} else {
			JOptionPane.showMessageDialog(new JFrame(), "Veuillez rentrer nom.prenom", "Erreur",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	/**
	 * Listener du bouton de projets et groupes d'ajout via csv.
	 * 
	 * @param event
	 */
	private void csvListener(ActionEvent event) {
		System.out.println("Fonction d'import de projets/groupes via csv");
		ImportCSV.doImport();
		TreePan.reloadTree();
		SwingUtilities.updateComponentTreeUI(window);
	}

	public class HomePageTreeSelectionListener implements GitreeSelectionListener {
		/**
		 * Listener des elements selectionnes dans l'arbre.
		 * 
		 * @param event
		 */
		@Override
		public void gitreeSelectionChanged(GitreeSelectionEvent event) {
			tree_selection = event;
		}
	}

	/**
	 * Listener du bouton d'ajout de projet dans les archives.
	 * 
	 * @param event
	 */
	private void archiveListener(ActionEvent event) {
		System.out.println("Fonction d'archivage de projets");
		try {
			if (tree_selection.getSelectedProjects().size() != 0) {
				String text = "Voulez-vous vraiment archiver :\n";
				for (UserObject object : tree_selection.getSelectedProjects()) {
					text += object.getLabel() + "\n";
				}
				int confirm = JOptionPane.showConfirmDialog(null, text);
				if (confirm == 0) {
					for (UserObject project : tree_selection.getSelectedProjects()) {
						ProjectsMng.archiveProjects(project.getId());
					}
					archive_page.getPanel().removeAll();
					archive_page.populateTable();
				}
				TreePan.reloadTree();
				SwingUtilities.updateComponentTreeUI(window);
			} else {
				JOptionPane.showMessageDialog(null, "Veuillez selectionner au moins 1 projet");
			}

		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Aucun projet n'a été selectionne");
			TreePan.reloadTree();
			SwingUtilities.updateComponentTreeUI(window);
		}
	}

	/**
	 * Listener du bouton d'ajout de projets ou groupes manuellement.
	 * 
	 * @param event
	 */
	private void addUserObjectListener(ActionEvent event) {
		System.out.println("Fonction d'ajout de projets/groupes");
		new DialogProjectCreatorControler(window);
		TreePan.reloadTree();
		SwingUtilities.updateComponentTreeUI(window);
	}

	/**
	 * Listener du bouton de suppression de projets/groupes.
	 * 
	 * @param event
	 */
	private void suprUserObjectListener(ActionEvent event) {
		System.out.println("Fonction de suppression de projets/groupes");
		if (tree_selection != null) {
			if (tree_selection.getSelectedProjects().size() != 0) {
				String text = "(Projets) Voulez-vous vraiment supprimer :\n";
				for (UserObject object : tree_selection.getSelectedProjects()) {
					text += object.getLabel() + "\n";
				}
				int input = JOptionPane.showConfirmDialog(null, text);
				if (input == 0) {
					for (UserObject object : tree_selection.getSelectedProjects()) {
						controller.suprUserObject(object);
					}
				}
			}
			if (tree_selection.getSelectedGroups().size() != 0) {
				String textbis = "(Groupes) Voulez-vous vraiment supprimer :\n";
				for (UserObject object : tree_selection.getSelectedGroups()) {
					textbis += object.getLabel() + "\n";
				}
				int input2 = JOptionPane.showConfirmDialog(null, textbis);
				if (input2 == 0) {
					for (UserObject object : tree_selection.getSelectedGroups()) {
						controller.suprUserObject(object);
					}
				}
			}
			TreePan.reloadTree();
			SwingUtilities.updateComponentTreeUI(window);
		} else {
			JOptionPane.showMessageDialog(new JFrame(), "Veuillez selectionner des elements de l'arbre", "Erreur",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	private void infosListener(ActionEvent event) {
		System.out.println("Ouverture de la page d'aide");
		SwingHelpPage fenetre = new SwingHelpPage();
		fenetre.setVisible(true);

	}

}
