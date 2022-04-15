package control;

import java.util.List;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import org.gitlab4j.api.GitLabApiException;
import org.gitlab4j.api.models.Project;
import org.gitlab4j.api.models.ProjectFilter;

import model.TheGitLabApi;
import model.arbre.Arbre;
import model.arbre.UserObject;

/**
 * Classe pour la gestion des projets en utillisant la bibliotheque gitlab4j
 * Classe final, il y'a pas besoin de l'instancier. 

 * @author LEROY Valentin
 * @author PALIX Xavier
 * @author THOME Alban
 * 
 * @category control
 * @see GittseControllerHomePage
 */
public final class ProjectsMng {

	private static UserObject tree;

	/**
	 * 
	 * @return projectList la liste des projets dont l'utilisateur est proprietaire
	 * @throws GitLabApiException
	 */
	public static List<Project> getProjects() throws GitLabApiException {
		ProjectFilter filter = new ProjectFilter();
		// definir les filtres pour:
		// omettre les projets archives
		filter.withArchived(false);
		// recuperer les projets dont l'utilisateur est proprietaire
		filter.withOwned(true);
		// Utilisation de la methode getProjects() de gitlab4j
		// En passant le filtre en parametre
		List<Project> projectList = TheGitLabApi.gitLabApi_.getProjectApi().getProjects(filter);
		return projectList;
	}

	/**
	 * Methode de suppression d'un projet.
	 * 
	 * @param projectId
	 */
	public static void removeProject(int id) {
		try {
			TheGitLabApi.gitLabApi_.getProjectApi().deleteProject(id);
		} catch (GitLabApiException e1) {
			JOptionPane.showMessageDialog(new JFrame(), "Impossible de supprimer le projet ayant l'id : " + id,
					"Erreur", JOptionPane.ERROR_MESSAGE);
		}
	}

	// Function that archives a list of projects
	/**
	 * Fonction qui permet d'archiver un projet sur gitlab
	 * 
	 * @param id du projet a archiver
	 */
	public static void archiveProjects(int id) {
		try {
			// Utilisation de la fonction archiveProject() de gitlab4j qui est applique a
			// l'instance gitLabApi
			TheGitLabApi.gitLabApi_.getProjectApi().archiveProject(id);
		} catch (GitLabApiException e) {
			JOptionPane.showMessageDialog(new JFrame(), "Impossible d'archiver le projet ayant l'id : " + id, "Erreur",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	// Function that returns archived projects
	/**
	 * Fonction qui retourne la liste des projets archives de l'utilisateur
	 * 
	 * @return archived_projects liste des projets archives depuis gitlab
	 */
	public static List<Project> archivedProjects() {
		ProjectFilter filter = new ProjectFilter();
		// On met le filtre archived a true
		filter.withArchived(true);
		// Le filtre owned pour avoir que les projets dont on est proprietaire
		filter.withOwned(true);
		try {
			List<Project> archived_projects = TheGitLabApi.gitLabApi_.getProjectApi().getProjects(filter);
			return archived_projects;
		} catch (GitLabApiException e) {
			JOptionPane.showMessageDialog(new JFrame(), "Impossible de telecharger les projets archives", "Erreur",
					JOptionPane.ERROR_MESSAGE);
		}
		return null;
	}

	/**
	 * Fonction pour instancier l'arbre
	 */
	public static void initArbre() {
		tree = new Arbre();
	}

	/**
	 * Getter de l'arbre
	 * 
	 * @return tree
	 */
	public static UserObject getArbre() {
		return tree;
	}

}
