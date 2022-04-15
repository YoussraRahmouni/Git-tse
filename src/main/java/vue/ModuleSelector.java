package vue;

import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;

import javax.swing.JCheckBox;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

import control.ModulesMng;

/**
 * Cette classe permet de gerer le filtrage des modules dans l'arbre.
 * 
 * @author MALIFARGE Antoine
 * @see model.Module
 * @category vue
 */
public class ModuleSelector extends JList<JCheckBox> {

	private static final long serialVersionUID = -4878990974022471570L;
	protected static Border noFocusBorder = new EmptyBorder(1, 1, 1, 1);
	private TreePan displayed_tree;

	/**
	 * @param displayed_tree Permet d'avoir acces aux methodes de TreePan pour le
	 *                       mettre a jour et lui passer la liste des tags a
	 *                       afficher.
	 */
	public ModuleSelector(TreePan displayed_tree) {

		this.displayed_tree = displayed_tree;
		ModuleChangedListener module_changed_listener = new ModuleChangedListener();
		ModulesMng.addModuleListListener(module_changed_listener);
		setCellRenderer(new CellRenderer());
		addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent mouseevent) {
				// Cette solution permet ne permet pas d'interagire avec les checkbox
				// contenus dans la liste. On doit les selectionner manuelement.
				// Le fait d'utiliser des checkbox est un moyen d'avoir un composant ayant deja
				// tout les outils pour montrer visuelement qu'il est selectionner.
				// On pourrai tres bien utiliser autre chose.
				int index = locationToIndex(mouseevent.getPoint());
				if (index != -1) {
					JCheckBox checkbox = (JCheckBox) getModel().getElementAt(index);
					// La ligne suivante permet de basculer l'etat de la checkbox entre selectionne
					// ou non.
					checkbox.setSelected(!checkbox.isSelected());
					repaint();
					refreshTree();
				}
			}
		});
		setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	}

	public void refreshTree() {
		displayed_tree.setTags(selected());
		displayed_tree.revalidate();
		displayed_tree.repaint();
	}

	/**
	 * Cette methode permet d'initialiser ou de mettre a jour la liste des tags
	 * afficher dans dans le filtre.
	 * 
	 * @param tag_list Liste des tags a afficher
	 */
	public void setModules(ArrayList<String> tag_list) {

		JCheckBox[] module_list = new JCheckBox[tag_list.size()];
		int n = 0;
		for (String tag : tag_list) {
			module_list[n] = new JCheckBox(tag);
			n++;
		}
		setListData(module_list);
		repaint();
		refreshTree();
	}

	public class ModuleChangedListener implements PropertyChangeListener {
		/**
		 * Listener notifier chaque fois que la liste des modules change.
		 */
		@SuppressWarnings("unchecked")
		@Override
		public void propertyChange(PropertyChangeEvent evt) {
			if (evt.getPropertyName().equals("Changes in a module")) {
				refreshTree();
			} else {
				setModules((ArrayList<String>) evt.getNewValue());
			}
		}
	}

	/**
	 * @return tags Liste des tag selectionne.
	 */
	public ArrayList<String> selected() {

		ArrayList<String> tags = new ArrayList<String>();
		for (int i = 0; i < getModel().getSize(); i++) {
			JCheckBox checkbox = getModel().getElementAt(i);
			if (checkbox.isSelected()) {
				tags.add(checkbox.getText());
			}
		}
		return tags;
	}

	public class CellRenderer implements ListCellRenderer<Object> {
		public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected,
				boolean cellHasFocus) {
			JCheckBox checkbox = (JCheckBox) value;
			checkbox.setOpaque(false);
			checkbox.setEnabled(isEnabled());
			checkbox.setFocusPainted(false);
			checkbox.setBorder(isSelected ? UIManager.getBorder("List.focusCellHighlightBorder") : noFocusBorder);
			return checkbox;
		}
	}
}
