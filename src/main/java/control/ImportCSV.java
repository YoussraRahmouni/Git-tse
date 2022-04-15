package control;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileFilter;

import org.gitlab4j.api.GitLabApiException;
import org.gitlab4j.api.models.GroupParams;

import model.TheGitLabApi;
import resources.Ressources;
import vue.DialogCSVImportFeedback;
import vue.SwingHomePage;
import vue.TreePan;

/**
 * Classe statique pour la selection et l'import d'un fichier CSV La tache est
 * effectuee dans un thread separe car la procadure peut-etre longue, selon le
 * nombre d'actions demandees, la connexion de l'utilisateur et le temps de
 * reponse du serveur
 * 
 * Le format attendu du fichier CSV est
 * 
 * nom-du-groupe,nom-du-projet,nom-de-l-utilisateur
 * 
 * Pour creer seulement un projet sans affecter d'utilisateur, il suffit de
 * laisser ce champ vide dans le csv
 * 
 * 
 * Au long de l'import, un rapport est constitue afin d'informer l'utilisateur
 * de la reussite ou non des operations. Les differentes erreurs plausibles
 * retournees par l'api y sont consignees
 * 
 * 
 * Il est possible d'interrompre la procedure en fermant la popup, apres
 * confirmation. L'import va terminer proprement sa ligne puis s'interrompre
 * 
 * 
 * @author THOME Alban 
 * @author LEROY Valentin 
 * 
 * @see DialogCSVImportFeedback
 * @see SwingHomePage
 * 
 * @category control
 */

public final class ImportCSV {

	private static boolean stop_import;
	private static boolean import_done;

	private static String report;
	private static String report_line;

	private static DialogCSVImportFeedback dialog_CSV_report;

	public static void stopImport() {
		ImportCSV.stop_import = true;
		import_done = true;
	}

	public static boolean isImportTermine() {
		return import_done;
	}

	public static void doImport() {
		stop_import = false;
		import_done = false;

		report = "";
		report_line = "";

		Thread worker = new Thread() { // Execution de la tache d'import possiblement longue dans un thread distinct
			public void run() {

				JFileChooser dialog_file_chooser = new JFileChooser(new File("."));
				dialog_file_chooser.setFileFilter(new FileFilter() { // Filtre seulement les fichiers avec l'extention
																		// .csv

					@Override
					public String getDescription() {
						return "Comma Separated Values file";
					}

					@Override
					public boolean accept(File f) {
						return f.getName().endsWith(".csv") || f.isDirectory();
					}
				});

				File csv_file;
				JFrame dialog_frame_base = new JFrame();

				dialog_frame_base.setIconImage(Ressources.logoGittse);

				if (dialog_file_chooser.showOpenDialog(dialog_frame_base) == JFileChooser.APPROVE_OPTION) {
					csv_file = dialog_file_chooser.getSelectedFile();

					BufferedReader reader_with_buffer = null;
					String csv_file_line;

					try {
						reader_with_buffer = new BufferedReader(new FileReader(csv_file));
						reader_with_buffer.readLine();

						dialog_CSV_report = new DialogCSVImportFeedback(new JFrame());
						newLine("Lecture du fichier '" + csv_file.getName() + "'\n");

						List<String> skipped_groups = new ArrayList<String>();

						while ((csv_file_line = reader_with_buffer.readLine()) != null && !stop_import) {

							if (!csv_file_line.isEmpty()) {
								String[] column = csv_file_line.split(",|;");
								try {
									if (column[0] != "" && column[1] != "" && column[2] != "") {

										newLine(column[0] + "  " + column[1] + "  " + column[2] + " > ");
										if (skipped_groups.contains(column[1])) {
											addToCurrentLine("Ignore");
											continue;
										}

										/// Selection du groupe
										Optional<org.gitlab4j.api.models.Group> optGroup = TheGitLabApi.gitLabApi_
												.getGroupApi().getGroupsStream(column[1]).findAny();
										if (!optGroup.isPresent()) {

											int choice = JOptionPane.showConfirmDialog(new JFrame(),
													"Voulez vous creer le groupe " + column[1] + " ?",
													"Le groupe " + column[1] + " n'existe pas",
													JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

											if (choice == JOptionPane.YES_OPTION) {
												try {
													optGroup = Optional.of(TheGitLabApi.gitLabApi_.getGroupApi()
															.createGroup(new GroupParams().withName(column[1])
																	.withPath(column[1])));
													addToCurrentLine(" Le groupe " + column[1] + " a été créé");
												} catch (GitLabApiException e) {
													addToCurrentLine(" Le groupe " + column[1]
															+ " n'a pas été créé (nom non valide)");
													continue;
												}
											} else if (choice == JOptionPane.NO_OPTION) {
												addToCurrentLine(" Le groupe " + column[1] + " ne sera pas cree");
												skipped_groups.add(column[1]);
												continue;
											}
										}

										org.gitlab4j.api.models.Group group = optGroup.get();

										/// Selection du projet
										org.gitlab4j.api.models.Project project = null;
										try {
											project = TheGitLabApi.gitLabApi_.getProjectApi().getProject(column[1],
													column[2]);
										} catch (GitLabApiException e) {
											if (e.getHttpStatus() == 404) {
												try {
													project = TheGitLabApi.gitLabApi_.getProjectApi()
															.createProject(group.getId(), column[2]);
													addToCurrentLine(" Le projet " + column[2] + " a été créé");
												} catch (GitLabApiException e1) {
													addToCurrentLine(
															"ERREUR : Vous n'avez pas les droits sur le groupe "
																	+ column[1]);
													continue;
												}
											} else {
												System.err.println("TheGitLabApi.gitLabApi_Exception");
												e.printStackTrace();
											}
										}

										/// Selection de l'utilisateur
										org.gitlab4j.api.models.User user = TheGitLabApi.gitLabApi_.getUserApi()
												.getUser(column[0]);
										if (user == null) {
											addToCurrentLine("ERREUR : L'utilisateur " + column[0] + " n'existe pas");
										} else {
											try {
												TheGitLabApi.gitLabApi_.getProjectApi().addMember(project.getId(),
														user.getId(), 30);
												addToCurrentLine(" " + column[0] + " a été ajouté à " + column[2]);
											} catch (GitLabApiException e) {
												addToCurrentLine(
														"ERREUR : " + column[0] + " est déjà membre de ce projet");
											}
										}

									}
								} catch (ArrayIndexOutOfBoundsException exc) {
									newLine("ERREUR : La ligne '" + csv_file_line + "' est incorrecte");
								} catch (GitLabApiException e1) {
									System.err.println("TheGitLabApi.gitLabApi_Exception");
									e1.printStackTrace();
								}
							}
						}
						reader_with_buffer.close();
						ListMng.saveGrouplist();

						if (import_done) {
							newLine("\nImport stoppé");
						} else {
							newLine("\nImport terminé");
							import_done = true;
						}
						newLine("");

					} catch (FileNotFoundException exc) {
						System.out.println("Erreur d'ouverture du fichier csv");
						JOptionPane.showMessageDialog(new JFrame(), "Erreur d'ouverture du fichier csv", "Import CSV",
								JOptionPane.ERROR_MESSAGE);

					} catch (IOException e) {
						System.err.println("Erreur parcours fichier csv");
						e.printStackTrace();
					}
				}
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						TreePan.reloadTree();
					}
				});

			}
		};
		worker.start();
	}

	private static void newLine(String contenu) {
		if (!report_line.isEmpty()) {
			report += "\n" + report_line;
			System.out.println("    " + report_line);
		}
		report_line = contenu;

		dialog_CSV_report.setRapportCSV(report);
	}

	private static void addToCurrentLine(String contenu) {
		report_line += contenu + "  ";
	}
}
