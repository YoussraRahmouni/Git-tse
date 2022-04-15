package vue;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.EventListener;
import java.util.EventObject;
import java.util.HashMap;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.SpringLayout;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeCellRenderer;
import javax.swing.tree.TreePath;

import control.GittseControllerHomePage;
import model.arbre.UserObject;

/**
 * Cette classe permet l'affichage de l'arbre et gere ses interactions. Elle
 * herite de JScrollPane pour avoir directement une barre de scrolling integre.
 * 
 * @author MALIFARGE Antoine
 * @category vue
 *
 */
public class TreePan extends JScrollPane {

	private static final long serialVersionUID = -5236430169134061869L;
	private static JTree gitree;
	private static GittseControllerHomePage controller;
	private SpringLayout tree_layout;
	private ArrayList<GitreeSelectionListener> gitree_selection_listeners = new ArrayList<GitreeSelectionListener>();
	private static ArrayList<String> selected_tags = new ArrayList<String>();
	private GitreeSelectionEvent selection;

	/**
	 * @param controller permet d'avoir acces au controller ayant la methode
	 *                   getArbre()
	 */
	public TreePan(GittseControllerHomePage controller) {

		super();
		TreePan.controller = controller;
		initTree();
		this.setViewportView(gitree);
		createLayout();
	}

	/**
	 * Cette methode permet d'initialiser l'arbre. le UserObject arbre recuperer
	 * grace a controller_.getArbre contient toute la structure de l'arbre, et est
	 * donc utilise par GittseTreeNode pour le construire selon la procedure
	 * recursive implemente.
	 */
	public void initTree() {

		gitree = null;
		UserObject arbre = controller.getArbre();
		GittseTreeNode gitroot = new GittseTreeNode(arbre);
		gitree = new JTree(gitroot);
		gitree.setModel(new DefaultTreeModel(gitroot));
		gitree.setCellRenderer((TreeCellRenderer) new GittseCellRenderer());
		gitree.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent mouseevent) {
				fireGitreeSelectionChanged(gitree.getSelectionPaths());
			}
		});
	}

	/**
	 * Methode utilise pour lorsque la structure de l'arbre doit etre changer.
	 */
	public static void reloadTree() {

		UserObject arbre = controller.getArbre();
		GittseTreeNode gitroot = new GittseTreeNode(arbre);
		gitree.setModel(new DefaultTreeModel(gitroot));
	};

	/**
	 * CelleRenderer custom permetant le bon fonctionnement du filtrage et
	 * l'affichage correcte des noeuds.
	 */
	public static class GittseCellRenderer implements TreeCellRenderer {
		private JLabel node_label = new JLabel();
		private Boolean is_enabled;

		@Override
		public Component getTreeCellRendererComponent(JTree tree, Object node, boolean selected, boolean expanded,
				boolean leaf, int row, boolean hasFocus) {

			UserObject data = (UserObject) ((DefaultMutableTreeNode) node).getUserObject();
			is_enabled = true;
			// Ici on gere le filtrage. Si des tags sont selectionne dans le ModuleSelector,
			// alors on verifie si le UserObject contenus dans le noeud possede ce tag,
			// et on l'active que dans ce cas.
			if (!selected_tags.isEmpty()) {
				is_enabled = false;
				if (!(data.getTags() == null)) {
					for (String tag : data.getTags()) {
						if (selected_tags.contains(tag)) {
							is_enabled = true;
						}
					}
				}
			}
			node_label.setEnabled(is_enabled);
			// Si le noeud n'est pas actif ou si c'est la racine de l'arbre, on le
			// deselectionne.
			if (!is_enabled || data.getType().equals("arbre")) {
				selected = false;
			}
			// Ces deux lignes permettent de colorer le fond en bleu si un noeud est
			// selectionne
			node_label.setBackground(new Color(0, 0, 255, 50));
			node_label.setOpaque(selected);
			// Grace a ses UserObject custom, on peu mettre devant le noeud une image qu'on
			// a stocker dedans.
			node_label.setIcon(new ImageIcon(data.getIconUrl()));
			node_label.setText(data.getLabel());
			return node_label;
		}
	}

	/**
	 * Methode appele dans ModuleSelector pour mettre a jour les tags a affiche.
	 * 
	 * @param tags Liste des tags selectionne dans ModuleSelector
	 */
	public void setTags(ArrayList<String> tags) {

		selected_tags = tags;
	}

	public JTree getGitree() {
		return gitree;
	}

	public void createLayout() {
		tree_layout = new SpringLayout();
		tree_layout.putConstraint(SpringLayout.WEST, gitree, 30, SpringLayout.WEST, upperRight);
		tree_layout.putConstraint(SpringLayout.NORTH, gitree, 30, SpringLayout.NORTH, upperRight);
	}

	// Le reste des classes et methodes permettent l'implementation d'un observer.
	// Les listeners sont notifie a chaque nouvelle selection
	// dans l'arbre.

	public static interface GitreeSelectionListener extends EventListener {
		public void gitreeSelectionChanged(GitreeSelectionEvent selection);
	}

	/**
	 * Cette classe permet de creer un objet contenant toutes les informations sur
	 * la selection et sera envoyer a toutes les listeners lorsqu'une nouvelle
	 * selection est faite. Elle permet notament de faire le trie en amont des
	 * differents types de UserObject.
	 *
	 */
	public static class GitreeSelectionEvent extends EventObject {

		private static final long serialVersionUID = 1241300189460419211L;
		private static HashMap<String, ArrayList<UserObject>> selection = new HashMap<String, ArrayList<UserObject>>();

		/**
		 * @param source       Objet sur lequel l'evenement s'est produit. necessaire
		 *                     pour pour construire un Event.
		 * @param user_objects Liste de userObjects correspondant a la selection actuel
		 *                     dans l'arbre.
		 */
		public GitreeSelectionEvent(Object source, ArrayList<UserObject> user_objects) {

			super(source);
			selection.put("group", new ArrayList<UserObject>());
			selection.put("project", new ArrayList<UserObject>());
			selection.put("member", new ArrayList<UserObject>());
			if (!(user_objects == null)) {
				for (UserObject user_object : user_objects) {
					// On fait attention de ne pas envoyer la racine de l'arbre.
					if (!(user_object.getType() == "arbre")) {
						selection.get(user_object.getType()).add(user_object);
					}
				}
			}
		}

		// Methodes utilises par les listeners pour recuperer les listes voulus.
		public ArrayList<UserObject> getSelectedGroups() {
			return selection.get("group");
		}

		public ArrayList<UserObject> getSelectedProjects() {
			return selection.get("project");
		}

		public ArrayList<UserObject> getSelectedMembers() {
			return selection.get("member");
		}

		public HashMap<String, ArrayList<UserObject>> getAll() {
			return selection;
		}
	}

	public void addGitreeSelectionListener(GitreeSelectionListener listener) {
		this.gitree_selection_listeners.add(listener);
	}

	public void removeGitreeSelectionListener(GitreeSelectionListener listener) {
		this.gitree_selection_listeners.remove(listener);
	}

	/**
	 * Cette methode permet l'envoie du GitreeSelectionEvent aux listeners.
	 * 
	 * @param paths liste des chemins de l'arbre selectionne.
	 */
	private void fireGitreeSelectionChanged(TreePath[] paths) {

		// Liste des UserObjects selectionne.
		ArrayList<UserObject> user_objects = new ArrayList<UserObject>();
		if (!(paths == null)) {
			for (TreePath path : paths) {
				Object[] data = path.getPath();
				// La ligne suivante permet de recuperer le dernier noeud du chemin, donc celui
				// sur lequel l'utilisateur a clicker
				DefaultMutableTreeNode node = (DefaultMutableTreeNode) data[data.length - 1];
				UserObject user_object = (UserObject) node.getUserObject();
				Boolean is_enabled = true;
				// Si il y a un filtre, on verifie que le UserObject selectionne a bien l'un des
				// tags selectionne. Ce bout de code est redondant avec
				// celui dans le GittseCellRenderer, mais puisqu'on ne controlle
				// pas totalement l'appel de cette classe, on a une securite ici.
				if (!selected_tags.isEmpty()) {
					is_enabled = false;
					if (!(user_object.getTags() == null)) {
						for (String tag : user_object.getTags()) {
							if (selected_tags.contains(tag)) {
								is_enabled = true;
							}
						}
					}
				}
				if (is_enabled) {
					user_objects.add(user_object);
				}
			}
		}
		// Creation du nouvel evenement contenant la selection et envoie a tout les
		// listeners enregistre dans gitreeSelectionListeners.
		selection = new GitreeSelectionEvent(this, user_objects);
		for (GitreeSelectionListener listener : this.gitree_selection_listeners) {
			listener.gitreeSelectionChanged(selection);
		}
	}
}
