package control;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.gitlab4j.api.GitLabApiException;
import org.gitlab4j.api.models.Branch;
import org.gitlab4j.api.models.Commit;
import org.gitlab4j.api.models.Project;
import org.gitlab4j.api.models.ProjectStatistics;

import model.TheGitLabApi;

/**
 * Classe non instanciable recuperant l'ensemble des statistiques pour un projet
 * donnee. L'ensemble des methodes sont separees afin de rendre le code plus
 * modulable pour des versions futures.
 * 
 * @author LEROY Valentin
 * @author PALIX Xavier
 * @category control
 * @see SwingStatPage
 */
public final class StatisticsController {

	/**
	 * Methode statique recuperant l'ensemble des donnees fournies par gitLab4j.
	 * 
	 * @param project
	 * @return ProjectStatistics
	 */
	public static ProjectStatistics getCommonStat(Project project) {
		try {
			return TheGitLabApi.gitLabApi_.getProjectApi().getProject(project.getId(), true).getStatistics();
		} catch (GitLabApiException e) {
			return null;
		}
	}

	/**
	 * Methode retournant l'ensemble des branches d'un projet. Retourne null en cas
	 * d'erreur(s).
	 * 
	 * @param project
	 * @return List<Branch>
	 */
	public static List<Branch> GetBranchNumber(Project project) {
		try {
			List<Branch> Listbranches = TheGitLabApi.gitLabApi_.getRepositoryApi().getBranches(project);
			return Listbranches;
		} catch (GitLabApiException e) {
			return null;
		}
	}

	/**
	 * Methode permetant la recuperation de l'ensemble des commits d'un projet ainsi
	 * que leur traitement. Le parametre de retour est une Map de 2 objets : la
	 * liste des 3 derniers commits et une Map qui pour chaque membre du projet
	 * enregistre une liste de date s'etendant du premier commit au dernier. Pour
	 * chaque date, la methode calcul le nombre de commits effectues par les membres
	 * du projet. Cette derniere map comporte aussi une clee "Global" correspondant
	 * au nombre de commits total effectue par l'ensemble des membres du projet.
	 * Retourne null en cas d'erreur(s).
	 * 
	 * @param project
	 * @return Map<String, Object>
	 */
	@SuppressWarnings("deprecation")
	public static Map<String, Object> getCommitsInfos(Project project) {

		List<Commit> list_commits = new ArrayList<Commit>();

		List<List<String>> last_commits = new ArrayList<List<String>>();
		Map<String, ArrayList<ArrayList<Integer>>> map_detailled_commits = new HashMap<String, ArrayList<ArrayList<Integer>>>();
		try {
			list_commits = TheGitLabApi.gitLabApi_.getCommitsApi().getCommits(project);

			// Creation de la liste des derniers commits
			int n = list_commits.size();
			for (int i = 1; i < 4; i++) {
				if (n - i >= 0) {
					List<String> commit_ = new ArrayList<String>();
					commit_.add("Titre : " + list_commits.get(n - i).getTitle());
					commit_.add("Auteur : " + list_commits.get(n - i).getAuthorName());
					commit_.add("Date : " + list_commits.get(n - i).getAuthoredDate());
					last_commits.add(commit_);
				}
			}

			// Creation de la liste detaillees des commits par membre
			if (list_commits.size() != 0) {

				// Date de debut du projet
				int year_ = list_commits.get(0).getCommittedDate().getYear() + 1900;
				int month_ = list_commits.get(0).getCommittedDate().getMonth() + 1;

				// Initialisation du membre GLOBAL regroupant l'ensemble des membres
				map_detailled_commits.put("Global", new ArrayList<ArrayList<Integer>>());
				ArrayList<Integer> list = new ArrayList<Integer>();
				list.add(year_);
				list.add(month_);
				list.add((int) 0);
				map_detailled_commits.get("Global").add(list);

				// Initialisation de l'ensemble des membres du projet
				for (Commit commit : list_commits) {
					if (!map_detailled_commits.containsKey(commit.getAuthorName())) {
						map_detailled_commits.put(commit.getAuthorName(), new ArrayList<ArrayList<Integer>>());
						list = new ArrayList<Integer>();
						list.add(year_);
						list.add(month_);
						list.add((int) 0);
						map_detailled_commits.get(commit.getAuthorName()).add(list);
					}
				}

				// Assignation des commits aux membres
				for (Commit commit : list_commits) {
					int yearCommit = commit.getCommittedDate().getYear() + 1900;
					int monthCommit = commit.getCommittedDate().getMonth() + 1;
					String autor = commit.getAuthorName();

					// Ajout du commit si la date correspond
					boolean ajout_ = false;
					while (ajout_ == false) {
						int taille = map_detailled_commits.get(autor).size();
						// Ajout d'un commit supplementaire si l'auteur a la date qui correspond au
						// commit dans son tableau
						if (yearCommit == map_detailled_commits.get(autor).get(taille - 1).get(0)
								&& monthCommit == map_detailled_commits.get(autor).get(taille - 1).get(1)) {
							// Ajout pour l'auteur
							int nombre = map_detailled_commits.get(autor).get(taille - 1).get(2);
							map_detailled_commits.get(autor).get(taille - 1).set(2, nombre + 1);
							// Ajout pour le Global
							int nombretotal = map_detailled_commits.get("Global").get(taille - 1).get(2);
							map_detailled_commits.get("Global").get(taille - 1).set(2, nombretotal + 1);
							// Changement de l'etat d'ajout_
							ajout_ = true;
						} else {
							// Gestion des annees
							if (month_ < 12) {
								month_ += 1;
							} else {
								year_ += 1;
								month_ = 1;
							}
							// Ajout a l'ensemble des membres la nouvelle date
							for (Map.Entry<String, ArrayList<ArrayList<Integer>>> tab : map_detailled_commits
									.entrySet()) {
								ArrayList<Integer> listAutor = new ArrayList<Integer>();
								listAutor.add(year_);
								listAutor.add(month_);
								listAutor.add((int) 0);
								tab.getValue().add(listAutor);
							}
						}
					}
				}
			}
			// creation de la map transmise a la vue
			Map<String, Object> returnTab = new HashMap<String, Object>();
			returnTab.put("last_commits", last_commits);
			returnTab.put("map_detailled_commits", map_detailled_commits);
			return returnTab;
		} catch (GitLabApiException e) {
			return null;
		}

	}

	/**
	 * Methode retournant une Map liant un langage de programmation a un pourcentage
	 * d'utilisation dans le projet renseigne. Retourne null en cas d'erreur(s).
	 * 
	 * @param project
	 * @return Map<String, Float>
	 */
	public static Map<String, Float> GetProjectLanguages(Project project) {
		try {
			Map<String, Float> pl = TheGitLabApi.gitLabApi_.getProjectApi().getProjectLanguages(project);
			return pl;
		} catch (GitLabApiException e) {
			return null;
		}
	}

}
