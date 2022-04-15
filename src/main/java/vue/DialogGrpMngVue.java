package vue;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import control.DialogGrpMngControler;
import resources.Ressources;

/**
 * Classe affichage et interaction avec utilisateur pour la boite de dialogue de gestion des groupes
 * 
 * @author PALIX Xavier
 * @author THOME Alban
 * @category vue
 */
public class DialogGrpMngVue extends javax.swing.JDialog {

	private static final long serialVersionUID = 1L;
	private javax.swing.JTextField member_name_tf;
	private javax.swing.JButton btn_suppr, btn_add;
	private javax.swing.JLabel title_label, member_exists_label, tf_label, status_label;
	private javax.swing.JLayeredPane panel_layered;
	private javax.swing.JToolBar tool_bar;

	private SwingGroupPage return_page; // pour recharger la page

	DialogGrpMngControler controler;

	/**
	 * constructeur initialisation des composents et listener pour afficher ou non
	 * le statut d'existence de l'utilisateur entre
	 * 
	 * @param parent    : fenetre parent (ici SwingGroupPage fenetre)
	 * @param controler
	 */

	public DialogGrpMngVue(java.awt.Frame parent, DialogGrpMngControler controler, SwingGroupPage retour) {
		super(parent, false);
		this.controler = controler;
		initComponents();

		member_name_tf.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void insertUpdate(DocumentEvent e) {
				controler.checkIfUserExists(member_name_tf.getText());
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				controler.checkIfUserExists(member_name_tf.getText());
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				controler.checkIfUserExists(member_name_tf.getText());
			}
		});

		this.setLocationRelativeTo(parent);
		this.setIconImage(Ressources.logoGittse);
		this.setVisible(true);
		this.return_page = retour;
	}

	/**
	 * initialisation des composents et mise en place de ceux-ci
	 */

	private void initComponents() {

		tool_bar = new javax.swing.JToolBar();
		panel_layered = new javax.swing.JLayeredPane();
		title_label = new javax.swing.JLabel();
		tf_label = new javax.swing.JLabel();
		status_label = new javax.swing.JLabel();
		status_label.setText(" ");
		member_name_tf = new javax.swing.JTextField();
		btn_add = new javax.swing.JButton();
		btn_suppr = new javax.swing.JButton();
		member_exists_label = new javax.swing.JLabel();

		tool_bar.setRollover(true);

		setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
		setTitle("Gestion de groupe");
		setResizable(false);
		setSize(400, 200);
		setType(java.awt.Window.Type.POPUP);

		title_label.setFont(new java.awt.Font("Tahoma", 0, 18));
		title_label.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
		title_label.setText("Gerer le groupe '" + controler.getMygroup().getFullName().toString() + "'");

		tf_label.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
		tf_label.setText("Utilisateur a ajouter/enlever");

		status_label.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

		btn_add.setFont(new java.awt.Font("Tahoma", 1, 11));
		btn_add.setText("Ajouter");
		btn_add.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				btnAjouterActionPerformed(evt);
			}
		});

		btn_suppr.setText("Supprimer");
		btn_suppr.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				btnSupprActionPerformed(evt);
			}
		});

		member_exists_label.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
		panel_layered.setLayer(title_label, javax.swing.JLayeredPane.DEFAULT_LAYER);
		panel_layered.setLayer(tf_label, javax.swing.JLayeredPane.DEFAULT_LAYER);
		panel_layered.setLayer(member_name_tf, javax.swing.JLayeredPane.DEFAULT_LAYER);
		panel_layered.setLayer(status_label, javax.swing.JLayeredPane.DEFAULT_LAYER);
		panel_layered.setLayer(btn_add, javax.swing.JLayeredPane.DEFAULT_LAYER);
		panel_layered.setLayer(btn_suppr, javax.swing.JLayeredPane.DEFAULT_LAYER);
		panel_layered.setLayer(member_exists_label, javax.swing.JLayeredPane.DEFAULT_LAYER);

		javax.swing.GroupLayout jLayeredPane1Layout = new javax.swing.GroupLayout(panel_layered);
		jLayeredPane1Layout.setHorizontalGroup(jLayeredPane1Layout.createParallelGroup(Alignment.LEADING)
				.addGroup(jLayeredPane1Layout.createSequentialGroup().addGap(80)
						.addComponent(status_label, GroupLayout.DEFAULT_SIZE, 128, Short.MAX_VALUE).addGap(86))
				.addGroup(jLayeredPane1Layout.createSequentialGroup().addGroup(jLayeredPane1Layout
						.createParallelGroup(Alignment.LEADING)
						.addGroup(jLayeredPane1Layout.createSequentialGroup().addContainerGap().addComponent(tf_label))
						.addGroup(jLayeredPane1Layout.createSequentialGroup().addGap(24).addComponent(btn_suppr,
								GroupLayout.PREFERRED_SIZE, 57, Short.MAX_VALUE)))
						.addPreferredGap(ComponentPlacement.RELATED)
						.addGroup(jLayeredPane1Layout.createParallelGroup(Alignment.LEADING)
								.addGroup(jLayeredPane1Layout.createParallelGroup(Alignment.LEADING, false)
										.addComponent(member_name_tf, GroupLayout.DEFAULT_SIZE, 140, Short.MAX_VALUE)
										.addComponent(member_exists_label, GroupLayout.DEFAULT_SIZE,
												GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
								.addGroup(jLayeredPane1Layout.createSequentialGroup().addGap(14)
										.addComponent(btn_add, GroupLayout.PREFERRED_SIZE, 47, Short.MAX_VALUE)))
						.addContainerGap())
				.addComponent(title_label, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 294, Short.MAX_VALUE));
		jLayeredPane1Layout.setVerticalGroup(jLayeredPane1Layout.createParallelGroup(Alignment.LEADING)
				.addGroup(jLayeredPane1Layout.createSequentialGroup().addGap(21).addComponent(title_label).addGap(25)
						.addGroup(jLayeredPane1Layout.createParallelGroup(Alignment.BASELINE).addComponent(tf_label)
								.addComponent(member_name_tf, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
										GroupLayout.PREFERRED_SIZE))
						.addGap(5)
						.addComponent(member_exists_label, GroupLayout.PREFERRED_SIZE, 13, GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(ComponentPlacement.UNRELATED)
						.addComponent(status_label, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addGap(24).addGroup(jLayeredPane1Layout.createParallelGroup(Alignment.BASELINE)
								.addComponent(btn_add).addComponent(btn_suppr))
						.addGap(24)));
		panel_layered.setLayout(jLayeredPane1Layout);

		javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
		getContentPane().setLayout(layout);
		layout.setHorizontalGroup(
				layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(panel_layered));
		layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(layout.createSequentialGroup()
						.addComponent(panel_layered, javax.swing.GroupLayout.PREFERRED_SIZE,
								javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
						.addGap(0, 0, Short.MAX_VALUE)));

		pack();
	}

	/**
	 * getter
	 * 
	 * @return jLabel3
	 */
	public javax.swing.JLabel getjLabel3() {
		return status_label;
	}

	/**
	 * setter
	 * 
	 * @param jlabel3
	 */
	public void setjLabel3(javax.swing.JLabel jlabel3) {
		this.status_label = jlabel3;
	}

	/**
	 * actionperformed avec actionlistener du bouton ajouter
	 * 
	 * @param evt
	 */
	private void btnAjouterActionPerformed(java.awt.event.ActionEvent evt) {
		controler.addMember(member_name_tf.getText());
		return_page.refresh();
	}

	/**
	 * actionperformed avec actionlistener du bouton supprimer
	 * 
	 * @param evt
	 */
	private void btnSupprActionPerformed(java.awt.event.ActionEvent evt) {
		controler.removeMember(member_name_tf.getText());
		return_page.refresh();
	}

	/**
	 * label affichant un message d'existence ou non en fonction du nombre exists,
	 * donne par la fonction checkIfUserAlreadyExists dans GrpMngDialogControler
	 * 
	 * @param exists
	 */
	public void setUserAlreadyExists(int exists) {
		if (exists == 0) {
			member_exists_label.setText("");
		} else if (exists == 1) {
			member_exists_label.setText("Cet utilisateur existe");
		} else {
			member_exists_label.setText("Cet utilisateur n'existe pas");
		}
	}

}
