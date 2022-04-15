/**
 * 
 * @author Rahmouni Youssra
 * @category Control
 */
package control;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.swing.SwingUtilities;

import org.gitlab4j.api.Constants.TokenType;
import org.gitlab4j.api.GitLabApi;
import org.gitlab4j.api.GitLabApi.ApiVersion;
import org.json.JSONObject;

import model.GittseModel;
import model.TheGitLabApi;
import vue.GittseVue;
import vue.SwingConnectionPage;

/**
 * Classe controller gerant la page de connexion, implemente Abstract Controller
 * 
 * @author THOME Alban
 * @author LEROY Valentin
 * @see AbstractController
 * @category control
 */
public class GittseControllerConnexion extends AbstractController {

	private SwingConnectionPage connection_view;

	/**
	 * 
	 * @param m l'instance modele a passe en parametre
	 */
	public GittseControllerConnexion(GittseModel model, GittseVue vue) {
		// herite constructeur de la classe AbstractController
		super(model);
		vue.startConnectionPageVue(this);
	}

	/**
	 * Setter de la vue associee au controlleur de la connexion
	 * 
	 * @param vue
	 */
	public void setView(SwingConnectionPage vue) {
		connection_view = vue;
	}

	/**
	 * Creation de l'instance de GitlabApi a l'aide de l'access token retourne par
	 * generateAccessToken
	 * 
	 * @param username nom de l'utilisateur
	 * @param password mot de passe de l'utilisateur
	 */
	public void tryConnection(String username, String password) {

		Thread worker = new Thread() { // Execution de la tache d'import possiblement longue dans un thread distinct
			public void run() {
				System.out.print("Connexion en cours ...");
				try {
					// creation du singleton GitLabApi visible dans tout le projet.
					TheGitLabApi.getInstance(new GitLabApi(ApiVersion.V4, "https://code.telecomste.fr",
							TokenType.OAUTH2_ACCESS, generateAccessToken(username, password), (String) null, null));

					System.out.println(" Connexion reussie");
					if (BobMng.isUserABob(username)) {

						System.out.println(username + " a le profil Bob");
					} else {
						System.out.println(username + " a le profil Alice");
					}

					// Lancement de l'application si l'authentification est reussie
					// Set le nom de l'utilsateur courant pour recupere ses modules
					ModulesMng.setUserName(username);
					startHomePage(username);

				} catch (IOException e) {
					System.err.println("\nConnection failed - Check your credentials");
					connection_view.connectionStatus(false);
				}
			}
		};
		worker.start();
	}

	private void startHomePage(String username) {
		SwingUtilities.invokeLater(new Runnable() { // pour affichage depuis le thread import CSV
			public void run() {
				startGittseControllerHP(username);
				connection_view.connectionStatus(true);
				// la gestion du fichier contenant la liste des groupes de l'utilisateur
				ListMng.saveGrouplist();
			}
		});
	}

	/**
	 * Generer un access token pour l'utilisateur en utilisant le gitlab api
	 * 
	 * @param username
	 * @param password
	 * @return access_token
	 * @throws IOException
	 */
	public static String generateAccessToken(String username, String password) throws IOException {
		String access_token = "";
		// Url de l'instance de gitlab de telecom
		URL url = new URL("https://code.telecomste.fr/oauth/token");
		// Creation de l'instance de connection pour faire une requete
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		// on utilise cette connection pour avoir un output qui est l'access token
		// Donc il faut mettre le flag de output a true
		connection.setDoOutput(true);
		// specifier le contenu retourne
		connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
		connection.setRequestProperty("Accept", "application/json");
		// specifier la methode de la requete
		connection.setRequestMethod("POST");
		//
		String jsonInputString = "{ \"grant_type\"	:\"password\",	\"username\" :\" " + username
				+ "\",	\"password\"	:\"" + password + "\"}";
		// Ecrire dans la requete
		try (OutputStream os = connection.getOutputStream()) {
			byte[] input = jsonInputString.getBytes("UTF-8");
			os.write(input, 0, input.length);
		}
		connection.setConnectTimeout(10000);
		// recuperer la reponse de la requete
		try (BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"))) {
			StringBuilder response = new StringBuilder();
			String responseLine = null;
			while ((responseLine = br.readLine()) != null) {
				response.append(responseLine.trim());
			}
			// l'api retourne la reponse sous format json
			JSONObject obj = new JSONObject(response.toString());
			// recuperer l'access token depuis la reponse de la requete
			access_token = obj.getString("access_token");
			// fermer le buffer reader
			br.close();
		}
		return access_token;
	}

	/**
	 * Methode d'intanciation de la page principale.
	 * 
	 * @param username
	 */
	public void startGittseControllerHP(String username) {
		// Creation du controlleur de homePage
		GittseControllerHomePage controller = new GittseControllerHomePage(model);
		// Associer le controlleur a la vue
		connection_view.startHomePageVue(controller);
	}
}
