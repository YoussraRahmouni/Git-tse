package vue;

import javax.swing.tree.DefaultMutableTreeNode;
import model.arbre.UserObject;

/**
 * Cette classe herite de  DefaultMutableTreeNode pour creer des noeuds
 * personalise pour l'arbre afin de le construire recursivement.
 * 
 * @author MALIFARGE Antoine
 * @category vue
 */

public class GittseTreeNode extends DefaultMutableTreeNode {
	
	private static final long serialVersionUID = -4644864036429363787L;
	/**
	 * Sert pour la racine de l'arbre et les groupes. Ils peuvent avoire en
	 * descendent des groupes ou des projets.
	 * 
	 * @param user_object si il est de type arbre, sert de racine a l'arbre. Sinon
	 *                   user_object est un UserObject de type GroupObject.
	 */
	public GittseTreeNode(UserObject user_object) {
		
		this.setUserObject(user_object);
		for (UserObject u_oject : user_object.getListUO()) {
			switch (u_oject.getType()) {
			case "group":
				this.add(new GittseTreeNode(u_oject));
				break;
			case "project":
				this.add(new ProjectTreeNode(u_oject));
				break;
			default:
				break;
			}
		}
	}

	public static class ProjectTreeNode extends DefaultMutableTreeNode {

		private static final long serialVersionUID = -5103882512386439588L;
		/**
		 * Sert pour les noeuds projet. Ne peut avoir en descendent que des membres.
		 * 
		 * @param project UserObject de type ProjectObject
		 */
		public ProjectTreeNode(UserObject project) {
			
			this.setUserObject(project);
			for (UserObject member : project.getListUO()) {
				this.add(new MembersTreeLeaf(member));
			}
		}
	}

	public static class MembersTreeLeaf extends DefaultMutableTreeNode {

		private static final long serialVersionUID = -8718162883464022165L;
		/**
		 * Sert pour les noueds membres. Feuille de l'arbre.
		 * 
		 * @param member UserObject de type MemberObject
		 */
		public MembersTreeLeaf(UserObject member) {
			
			this.setUserObject(member);

		}

		@Override
		public boolean isLeaf() {
			return true;
		}

	}

}
