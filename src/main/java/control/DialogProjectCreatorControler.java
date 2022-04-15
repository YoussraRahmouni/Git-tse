package control;

import java.util.Optional;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import org.gitlab4j.api.GitLabApiException;
import org.gitlab4j.api.models.Group;
import org.gitlab4j.api.models.GroupParams;

import model.TheGitLabApi;
import vue.DialogProjectCreatorVue;
import vue.TreePan;

/**
 * Classe gerant la boite de dialogue de la fonctionnalite ajout de
 * projets/groupes
 * 
 * @author THOME Alban
 *
 */
public class DialogProjectCreatorControler {
	private DialogProjectCreatorVue vue;

	/**
	 * constructeur
	 * 
	 * @param parent : fenetre parent (ici SwingHomePage fenetre)
	 */
	public DialogProjectCreatorControler(java.awt.Frame parent) {
		vue = new DialogProjectCreatorVue(parent, this);
	}

	/**
	 * Verification de l'existence d'un groupe entre dans le textfield pour
	 * attribuer un projet ou créer le groupe affichage du status de l'existence de
	 * l'utilisateur (voir ProjectCreatorDialogVue.setGroupAlreadyExists(int i))
	 * 
	 * @param Groupname : nom d'utilisateur (entre dans le textfield)
	 */
	public void checkIfGroupExists(String group_name) {
		if (group_name.isEmpty()) {
			this.vue.setGroupAlreadyExists(0);
		} else if (!group_name.matches("[a-z0-9A-Z._\\-]+")) {
			this.vue.setGroupAlreadyExists(4);
		} else {
			try {
				this.vue.setGroupAlreadyExists(
						TheGitLabApi.gitLabApi_.getGroupApi().getGroupsStream(group_name).findAny().isPresent() ? 1
								: 2);

			} catch (GitLabApiException e) {
				System.err.println("API indisponible");
				return;
			}
		}
	}

	public void checkIfProjectExists(final String nomGroupe, final String nomProjet) {
		Thread worker_ = new Thread() { // Execution de la possiblement longue tâche dans un thread distinct
			public void run() {
				if (nomGroupe.isEmpty()) {
					vue.setProjectAlreadyExists(0);
				} else if (!nomGroupe.matches("[a-z0-9A-Z._\\-]+")) {
					vue.setProjectAlreadyExists(4);
				} else {
					try {
						int res = 1;
						try {
							TheGitLabApi.gitLabApi_.getProjectApi().getProject(nomGroupe, nomProjet);
						} catch (GitLabApiException e) {
							if (e.getHttpStatus() == 404) {
								res = 2;
							} else {
								throw e;
							}
						}
						vue.setProjectAlreadyExists(res);

					} catch (GitLabApiException e) {
						System.err.println("API indisponible");
					}
				}
			}
		};

		worker_.start();
	}

	/**
	 * si le groupe dont le nom du groupe a été entre n'existe pas, il est cree,
	 * puis on lui donne le projet cree (si il y en a un) sinon on ajoute au groupe
	 * existant le projet cree
	 * 
	 * @param group_name   : String nom du group
	 * @param project_name : String nom du projet
	 */
	public void createProject(String group_name, String project_name) {
		Optional<Group> group_opt;
		try {
			group_opt = TheGitLabApi.gitLabApi_.getGroupApi().getGroupsStream(group_name).findAny();
			if (!group_opt.isPresent()) {
				group_opt = Optional.of(TheGitLabApi.gitLabApi_.getGroupApi()
						.createGroup(new GroupParams().withName(group_name).withPath(group_name)));
				System.out.println("Le groupe " + group_name + " a été créé");
			}

			Group groupe = group_opt.get();
			try {
				TheGitLabApi.gitLabApi_.getProjectApi().getProject(group_name, project_name);
			} catch (GitLabApiException e) {
				if (e.getHttpStatus() == 404) {
					String msg = "";
					if (group_name.isEmpty()) {
						TheGitLabApi.gitLabApi_.getProjectApi().createProject(project_name);
						msg = "Le projet " + project_name + " a été créé";
						System.out.println("Le projet " + project_name + " a été créé");
						JOptionPane.showMessageDialog(new JFrame(), msg, "Création projet", JOptionPane.PLAIN_MESSAGE);
					} else if (project_name.isEmpty()) {
						try {
							TheGitLabApi.gitLabApi_.getGroupApi()
									.createGroup(new GroupParams().withName(group_name).withPath(group_name));
						} catch (GitLabApiException e1) {
							if (!e1.getMessage().equals("Failed to save group {:path=>[\"has already been taken\"]}")) {
								throw e1;
							}
						}
						msg = "Le Groupe " + group_name + " a été créé";
						JOptionPane.showMessageDialog(new JFrame(), msg, "Création Groupe", JOptionPane.PLAIN_MESSAGE);
					} else {
						TheGitLabApi.gitLabApi_.getProjectApi().createProject(groupe.getId(), project_name);
						msg = "Le projet " + group_name + "/" + project_name + " a été créé";
						System.out.println("Le projet " + project_name + " a été créé");
						JOptionPane.showMessageDialog(new JFrame(), msg, "Création projet", JOptionPane.PLAIN_MESSAGE);
					}

				}
			}
		} catch (GitLabApiException e1) {
			JOptionPane.showMessageDialog(new JFrame(),
					"Le projet '" + group_name + "/" + project_name + "' n'a pas été créé", "Rapport creation",
					JOptionPane.ERROR_MESSAGE);
		}
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				TreePan.reloadTree();
			}
		});

	}
}
