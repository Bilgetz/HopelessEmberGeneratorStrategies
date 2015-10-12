package fr.hopelessworld.plugin.predicate;

import javax.persistence.Id;

import org.apache.commons.collections4.Predicate;
import fr.hopelessworld.plugin.analyzer.Field;

/**
 * The Class SimpleNameFieldPredicate.
 */
public class IdFieldPredicate implements Predicate<Field> {

	public boolean evaluate(Field object) {
		if(object == null) {
			return false;
		}
		return object.getAnnotation(Id.class) != null;
	}

}
