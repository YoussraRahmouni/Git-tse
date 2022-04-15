

package model;

import org.gitlab4j.api.GitLabApi;
/**
 * Implementation du design pattern singleton pour creer l'instance de gitlab 
 * @author LEROY Valentin
 * @author RAHMOUNI Youssra
 * @category model
 * @see GittseControllerConnexion
 */
public final class TheGitLabApi {

	private static TheGitLabApi thegitLabApi_;
	public static GitLabApi gitLabApi_;

	/**
	 * Un constructeur prive de la classe, l'instance n'est cree qu'au sein de la
	 * classe
	 * 
	 * @param value instance de GitlabApi
	 */
	private TheGitLabApi(GitLabApi value) {
		gitLabApi_ = value;
	}

	/**
	 * Creation de l'instance du GitlabApi si elle existe pas
	 * 
	 * @param value
	 * @return thegitLabApi_ l'instance unique de GitlabApi
	 */
	public static TheGitLabApi getInstance(GitLabApi value) {
		if (thegitLabApi_ == null) {
			thegitLabApi_ = new TheGitLabApi(value);
		}
		return thegitLabApi_;
	}
}
