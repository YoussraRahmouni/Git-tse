package control;

import java.util.List;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import org.gitlab4j.api.GitLabApiException;
import org.gitlab4j.api.models.Group;
import org.gitlab4j.api.models.GroupFilter;

import model.TheGitLabApi;

/**
 * Classe controler gerant les groupes gitlab de l'utilisateur
 * 
 * @author PALIX Xavier
 * @author LEROY Valentin
 * @category control
 * @see GittseControllerHomePage
 */

public class GrpMng {

	/**
	 * Filtre des groupes selon le role Owner de l'utilisateur
	 * 
	 * @return La liste des groupes gitlab dont l'utilisateur est owner
	 * @throws GitLabApiException
	 */

	public static List<Group> getGroups() throws GitLabApiException {
		GroupFilter filter = new GroupFilter();
		filter.withOwned(true);
		List<Group> group_list = TheGitLabApi.gitLabApi_.getGroupApi().getGroups(filter);
		return group_list;
	}

	/**
	 * verification de l'existence du groupe et du role de l'utilisateur dans
	 * celui-ci
	 * 
	 * @param id : ID du groupe gitlab
	 */

	public static void removeGroup(int id) {
		try {
			TheGitLabApi.gitLabApi_.getGroupApi().deleteGroup(id);
		} catch (GitLabApiException e) {
			JOptionPane.showMessageDialog(new JFrame(), "Impossible de supprimer le projet ayant l'id : " + id,
					"Erreur", JOptionPane.ERROR_MESSAGE);
		}
	}
}
