package vue;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import control.GittseControllerHomePage;
import control.ModulesMng;
import model.arbre.UserObject;
import vue.TreePan.GitreeSelectionEvent;
import vue.TreePan.GitreeSelectionListener;

/**
 * Classe de creation et de gestion du panel des Modules. Cette classe sert
 * seulement pour les profils Bob.
 * 
 * @see ModulesMng
 * @see model.Module
 * @author LEROY Valentin
 * @author MALIFARGE Antoine
 * @category vue
 */
public class SwingModulePage {

	private JPanel module_page;
	private JTextArea tag_list_text = new JTextArea();
	private JScrollPane scrool;

	private JLabel label0, label1, label2, label3, label4;
	private JTextField add_textfield;
	private JButton add_tag_button, remove_tag_button, remove_module_button, create_module_button;
	private JComboBox<String> box_choice_tag, box_choice_module;
	private TreePan tree;

	private ArrayList<UserObject> selected_list = new ArrayList<UserObject>();

	/**
	 * Constructeur de la classe. Creation du panneau et appel de toutes les
	 * methodes d'initialisation et de configuration de ces composants.
	 * 
	 * @param tree
	 * @param controller
	 */
	public SwingModulePage(TreePan tree, GittseControllerHomePage controller) {
		module_page = new JPanel();
		module_page.setName("Modules");
		module_page.setBackground(new Color(0, 34, 108));

		initComponents();
		createLayout();
		setComponentsProperties();
		this.tree = tree;
		this.tree.addGitreeSelectionListener(new ModulePageTreeSelectionListener());
		ModulesMng.addModuleListListener(new ModuleChangedTestListener());
	}

	/**
	 * Methode d'initialisation des composants du panel module.
	 * 
	 * @return void
	 */
	public void initComponents() {
		scrool = new JScrollPane(tag_list_text);
		label0 = new JLabel("Creation / Suppression de modules");
		label1 = new JLabel("Nom du module :");
		label2 = new JLabel("Gestion des membres d'un module : ");
		label3 = new JLabel("Selectionnez le module :");
		label4 = new JLabel("Selectionnez le module :");
		add_textfield = new JTextField(15);
		create_module_button = new JButton("Creer");
		add_tag_button = new JButton("Ajouter");
		remove_tag_button = new JButton("Enlever");
		remove_module_button = new JButton("Supprimer");
		box_choice_tag = new JComboBox<String>();
		box_choice_module = new JComboBox<String>();

		module_page.add(label0);
		module_page.add(label1);
		module_page.add(label2);
		module_page.add(label3);
		module_page.add(label4);
		module_page.add(add_textfield);
		module_page.add(create_module_button);
		module_page.add(add_tag_button);
		module_page.add(remove_tag_button);
		module_page.add(remove_module_button);
		module_page.add(scrool);
		module_page.add(box_choice_tag);
		module_page.add(box_choice_module);

	}

	/**
	 * Methode de mise en place des proprietes des composants de la page module.
	 * 
	 * @see Utils.java
	 * @return void
	 */
	public void setComponentsProperties() {
		create_module_button.addActionListener(this::BcreateModuleListener);
		Utils.setButton(create_module_button, 15, 250, 90, 100, 30);

		add_tag_button.addActionListener(this::BaddTagListener);
		Utils.setButton(add_tag_button, 15, 420, 200, 100, 30);

		remove_tag_button.addActionListener(this::BremoveTagListener);
		Utils.setButton(remove_tag_button, 15, 540, 200, 100, 30);

		remove_module_button.addActionListener(this::BdelModuleListener);
		Utils.setButton(remove_module_button, 15, 620, 90, 100, 30);

		Utils.setLabel(label0, 25, 20, 20, 400, 30);
		Utils.setLabel(label2, 25, 20, 150, 400, 30);
		Utils.setLabel(label1, 15, 30, 60, 120, 30);
		Utils.setLabel(label3, 15, 20, 200, 200, 30);
		Utils.setLabel(label4, 15, 400, 60, 250, 30);

		Utils.setComboBox(box_choice_module, new ArrayList<String>(), 400, 90);
		Utils.setComboBox(box_choice_tag, new ArrayList<String>(), 180, 200);

		Utils.setJTextField(add_textfield, 30, 90);

		tag_list_text.setPreferredSize(new Dimension(670, 900));
		tag_list_text.setEnabled(false);
	}

	/**
	 * Listener du bouton de creation de nouveaux modules.
	 * 
	 * @param event
	 * @return void
	 */
	private void BcreateModuleListener(ActionEvent event) {
		String new_module = add_textfield.getText();
		if (!new_module.equals("")) {
			if (!ModulesMng.createNewModule(new_module)) {
				JOptionPane.showMessageDialog(new JFrame(), "Le module " + new_module + " existe deja", "Erreur",
						JOptionPane.ERROR_MESSAGE);
			}
			;
		}
	}

	/**
	 * Listener du bouton de suppression d'un module.
	 * 
	 * @param event
	 * @return void
	 */
	private void BdelModuleListener(ActionEvent event) {
		String tag_removed = (String) box_choice_module.getSelectedItem().toString();
		if (!tag_removed.equals(null)) {
			ModulesMng.deleteExistingModule(tag_removed);
		}
		refreshTags();
	}

	/**
	 * Listener du bouton d'ajout d'un module aux projets et groupes selectionnes.
	 * Le module ajoute est celui selectionne dans la box. La methode met a jour les
	 * tags dans la textarea.
	 * 
	 * @param event
	 * @return void
	 */
	private void BaddTagListener(ActionEvent event) {
		String tagAdded = (String) box_choice_tag.getSelectedItem();
		if (!(tagAdded == null)) {
			ModulesMng.addTagToUserObjects(selected_list, tagAdded);
		}
		refreshTags();
	}

	/**
	 * Listener du bouton de suppression d'un module aux projets et groupes
	 * selectionnes. Le module ajoute est celui selectionne dans la box. La methode
	 * met a jour les tags dans la textarea.
	 * 
	 * @param event
	 * @return void
	 */
	private void BremoveTagListener(ActionEvent event) {
		String tagAdded = (String) box_choice_tag.getSelectedItem();
		if (!(tagAdded == null)) {
			ModulesMng.removeTagFromUserObjects(selected_list, tagAdded);
		}
		refreshTags();
		tree.revalidate();
		tree.repaint();
	}

	/**
	 * Methode de creation de la mise en page (layout) dans le panel.
	 * 
	 * @return void
	 */
	public void createLayout() {
		module_page.setLayout(null);
		scrool.setBounds(20, 250, 740, 270);
		tag_list_text.setBounds(20, 250, 720, 500);
	}

	/**
	 * Methode de rafraichissement de la zone de texte ou l'ensemble des elements
	 * selectionnes sont affiches avec les modules qui leur sont attaches.
	 * 
	 * @return void
	 */
	public void refreshTags() {

		String text = "";
		for (UserObject object : selected_list) {
			text += object.getLabel() + " : \n";
			text += "\t";
			for (String nom : object.getTags()) {
				text += nom + " / ";
			}
			text += "\n\n";
		}

		tag_list_text.setText(text);

	}
	
	public class ModulePageTreeSelectionListener implements GitreeSelectionListener {
		/**
	 	* Listener des elements selectionnes dans l'arbre. A chaque selection, la liste
	 	* des elements selectionnes est efface et l'ensemble des elements selectionnes
	 	* sont mis dans cette liste.
	 	* 
		* @param selection
	 	* @return void
	 	*/
		@Override
		public void gitreeSelectionChanged(GitreeSelectionEvent selection) {
			selected_list.clear();
			selected_list.addAll(selection.getSelectedGroups());
			selected_list.addAll(selection.getSelectedProjects());
			refreshTags();
		}
	}

	/**
	 * Classe d'un listener des modules ajoutes et supprimes.
	 * Cette classe implement le design pattern observer.
	 */
	public class ModuleChangedTestListener implements PropertyChangeListener {
		@SuppressWarnings("unchecked")
		@Override
		public void propertyChange(PropertyChangeEvent evt) {
			if (evt.getPropertyName() != "Changes in a module") {
				ArrayList<String> tag_list = ((ArrayList<String>) evt.getNewValue());
				Utils.setComboBox(box_choice_module, tag_list, 400, 90);
				Utils.setComboBox(box_choice_tag, tag_list, 180, 200);
			}
		}
	}

	/**
	 * Getter du panel des modules.
	 * 
	 * @return module_panel
	 */
	public JPanel getPanel() {
		return module_page;
	}
}
