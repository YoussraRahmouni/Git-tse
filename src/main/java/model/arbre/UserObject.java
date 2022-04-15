package model.arbre;

import java.net.URL;
import java.util.List;

/**
 * Cette interface permet de generaliser tout les objets placer dans les noeuds
 * de l'arbre. Que ce soit la racine, les groupes, les projets, ou les membres.
 * Elle rend la manipulation des donnes beaucoup plus simple tout au long de
 * l'application.
 * 
 * @author MALIFARGE Antoine
 * @author LEROY Valentin
 * @category model.arbre
 */

public interface UserObject {

	public void addTag(String tag);

	public boolean isTag(String tag);

	public boolean deleteTag(String tag);

	public List<String> getTags();

	public void searchAndDeleteTags(String tag);

	public String getLabel();

	public String getType();

	public int getId();

	public UserObject isInTree(int id, String type);

	public List<UserObject> getListUO();

	public URL getIconUrl();
	
	public void addListUO(UserObject enfant);

}
