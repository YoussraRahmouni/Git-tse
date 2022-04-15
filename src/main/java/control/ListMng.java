package control;

import java.awt.Desktop;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import org.gitlab4j.api.GitLabApiException;
import org.gitlab4j.api.models.Group;
import org.gitlab4j.api.models.GroupFilter;
import org.gitlab4j.api.models.Member;

import model.TheGitLabApi;
import vue.SwingGroupPage;

/**
 * Classe pour la gestion des groupes
 * 
 * @author PALIX Xavier
 * 
 * @category control
 * @see SwingGroupPage
 */
public class ListMng {

	private final static String group_save_path = "groups_export.txt";

	/**
	 * Enregistre dans un fichier les listes de membre de chaque groupe que
	 * l'utilisateur possede >overwrite en cas de changement plutot que de
	 * reparcourir toutes la liste et tester chaque ligne
	 */
	public static void saveGrouplist() {
		try {
			File fichier = new File(group_save_path);
			if (!fichier.exists()) {
				fichier.createNewFile();
			}
			if (fichier.canWrite()) {
				BufferedWriter writer = new BufferedWriter(new FileWriter(fichier, false)); // Le false sert a overwrite
																							// dans le fichier plutot
																							// que append
				GroupFilter filter = new GroupFilter();
				filter.withOwned(true);
				List<Group> group_list = TheGitLabApi.gitLabApi_.getGroupApi().getGroups(filter);
				String data = "";
				for (int i = 0; i < group_list.size(); i++) {
					List<Member> group_members = TheGitLabApi.gitLabApi_.getGroupApi().getAllMembers(group_list.get(i));
					for (int j = 0; j < group_members.size(); j++) {
						data = data + group_list.get(i).getFullName().toString() + ", "
								+ group_members.get(j).getUsername().toString() + "\n";
					}
				}
				writer.write(data);
				writer.close();
			}
		} catch (FileNotFoundException e) {
			System.err.println("Impossible d'enregistrer l'export des groupes");
			e.printStackTrace(); // pour debug
		} catch (IOException e) {
			System.err.println("Impossible d'enregistrer l'export des groupes");
			e.printStackTrace(); // pour debug
		} catch (GitLabApiException e) {
			System.err.println("Impossible d'enregistrer l'export des groupes");
			e.printStackTrace(); // pour debug
			System.err.println("API indisponible");
		}
	}

	public static void openGrouplist() {
		File file = new File(group_save_path);
		if (!file.exists() && file.length() < 0) {
			System.err.println("Impossible d'ouvrir le fichier " + group_save_path);
			JOptionPane.showMessageDialog(new JFrame(), "Impossible d'ouvrir le fichier " + group_save_path,
					"Erreur", JOptionPane.ERROR_MESSAGE);
		} else {
			Desktop desktop = null;
			if (Desktop.isDesktopSupported()) {
				desktop = Desktop.getDesktop();
			}
			try {
				desktop.edit(file);
			} catch (IOException ex) {
				System.err.println("Impossible d'ouvrir le fichier " + group_save_path);
			}
		}
	}
}
