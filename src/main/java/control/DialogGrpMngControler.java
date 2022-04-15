package control;

import java.util.List;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import org.gitlab4j.api.GitLabApiException;
import org.gitlab4j.api.models.AccessLevel;
import org.gitlab4j.api.models.Group;
import org.gitlab4j.api.models.Member;
import org.gitlab4j.api.models.User;

import model.TheGitLabApi;
import vue.DialogGrpMngVue;
import vue.SwingGroupPage;

/**
 * Classe controller gerant la boite de dialog de gestion des groupes
 * 
 * @author PALIX Xavier
 * @author THOME Alban
 */

public class DialogGrpMngControler {

	/**
	 * vue : la fenetre d'interaction avec l'utilisateur Mygroup : le groupe
	 * selectionne pour la gestion
	 */

	private DialogGrpMngVue vue;
	private Group my_group;

	
	/**
	 * constructor
	 * 
	 * @param parent : la fenetre parent, ici SwingGroupPage fenetre
	 * @param group  : le groupe selectionne dans l'arbre pour la gestion du groupe
	 */
	public DialogGrpMngControler(java.awt.Frame parent, Group group, SwingGroupPage retour) {
		my_group = group;
		vue = new DialogGrpMngVue(parent, this, retour);
	}
	
	/**
	 * Getter du groupe
	 * 
	 * @return my_group
	 */
	public Group getMygroup() {
		return my_group;
	}

	/**
	 * Setter du groupe
	 * 
	 * @param mygroup
	 */
	public void setMygroup(Group mygroup) {
		my_group = mygroup;
	}

	/**
	 * verification de l'existence d'un user entre dans le textfield pour
	 * ajout/suppression du groupe affichage du status de l'existence de
	 * l'utilisateur (voir GrpMngDialogVue.setUserAlreadyExists(int i))
	 * 
	 * @param username : nom d'utilisateur (entre dans le textfield)
	 */

	public void checkIfUserExists(String username) {
		if (username.isEmpty()) {
			this.vue.setUserAlreadyExists(0);
		} else if (!username.matches("[a-z0-9A-Z._\\-]+")) {
			this.vue.setUserAlreadyExists(4);
		} else {
			try {
				this.vue.setUserAlreadyExists(TheGitLabApi.gitLabApi_.getUserApi().getUser(username) == null ? 2 : 1);

			} catch (GitLabApiException e) {
				JOptionPane.showMessageDialog(new JFrame(), "API indisponible", "Erreur",
						JOptionPane.ERROR_MESSAGE);
				System.err.println("API indisponible");
				return;
			}
		}
	}

	/**
	 * Le format du string doit etre nom.prenom, nom.prenom pour que le parsage
	 * fonctionne normalement Test de l'existence et de l'appartenance au groupe du
	 * username entre, ajout si possible en tant que DEVELOPER
	 * 
	 * @param UsersString : string suite de username d'utilisateur (entre dans le
	 *                    textfield)
	 */

	public void addMember(String users_string) {
		try {
			String delims = "[, ]+";
			String[] users = users_string.split(delims);
			List<Member> members = TheGitLabApi.gitLabApi_.getGroupApi().getAllMembers(my_group);
			for (String user : users) {
				boolean already_in = false;
				if (!user.isEmpty()) {
					User current_user = TheGitLabApi.gitLabApi_.getUserApi().getUser(user);
					for (int i = 0; i < members.size(); i++)
						if (current_user.getUsername().toString()
								.contentEquals(members.get(i).getUsername().toString())) {
							already_in = true;
							vue.getjLabel3().setText("Cet utilisateur est déjà présent");
						}
					if (!already_in) {
						TheGitLabApi.gitLabApi_.getGroupApi().addMember(my_group.getId(), current_user.getId(),
								AccessLevel.DEVELOPER);
						vue.getjLabel3().setText("Ajout reussi");

					}
				}
			}
			
			ListMng.saveGrouplist(); // mise a jour de la sauvegarde locale des groupes
		} catch (GitLabApiException e) {
			JOptionPane.showMessageDialog(new JFrame(), "API indisponible", "Erreur",
					JOptionPane.ERROR_MESSAGE);
			System.err.println("API indisponible");
		} catch (Exception e) {
			vue.getjLabel3().setText("\t format : nom1.prenom1,nom2.prenom2");
		}

	}

	/**
	 * Le format du string doit etre nom.prenom, nom.prenom pour que le parsage
	 * fonctionne normalement Test de l'existence et de l'appartenance au groupe du
	 * username entre, supression si possible en tant que DEVELOPER
	 * 
	 * @param UsersString : string suite de username d'utilisateur (entre dans le
	 *                    textfield)
	 */

	public void removeMember(String users_string) {
		try {
			String delims = "[, ]+";
			String[] users = users_string.split(delims);
			List<Member> members = TheGitLabApi.gitLabApi_.getGroupApi().getAllMembers(my_group);
			for (String user : users) {
				boolean already_in = false;
				User current_user = TheGitLabApi.gitLabApi_.getUserApi().getUser(user);
				for (int i = 0; i < members.size(); i++)
					if (current_user.getUsername().toString().contentEquals(members.get(i).getUsername().toString())) {
						already_in = true;
					}
				if (already_in) {
					TheGitLabApi.gitLabApi_.getGroupApi().removeMember(my_group.getId(), current_user.getId());
					vue.getjLabel3().setText("Suppression reussie");
				}
				else {
					vue.getjLabel3().setText("Cet utilisateur n'est pas présent dans le groupe");
				}
			}
			ListMng.saveGrouplist(); // mise a jour de la sauvegarde locale des goupes
		} catch (GitLabApiException e) {
			JOptionPane.showMessageDialog(new JFrame(), "API indisponible", "Erreur",
					JOptionPane.ERROR_MESSAGE);
			System.err.println("API indisponible");
		}

	}

}
