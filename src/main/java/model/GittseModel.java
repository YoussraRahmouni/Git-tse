package model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/**
 * Modele principal de l'application, dans le cadre de l'architecture MVC.
 * 
 * Classe modele dont derive les autres modeles de l'applicaiton
 * 
 * @author THOME Alban
 * @author MALIFARGE Antoine
 * @see Module
 * @category model
 */
public class GittseModel {
	/**
	 * Desing Pattern observer
	 */
	protected final PropertyChangeSupport pcs = new PropertyChangeSupport(this);

	public void addPropertyChangeListener(PropertyChangeListener listener) {
		this.pcs.addPropertyChangeListener(listener);
	}
}
