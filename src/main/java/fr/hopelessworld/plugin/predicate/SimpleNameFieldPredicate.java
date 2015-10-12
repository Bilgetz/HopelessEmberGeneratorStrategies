package fr.hopelessworld.plugin.predicate;

import org.apache.commons.collections4.Predicate;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;

import fr.hopelessworld.plugin.analyzer.Field;

/**
 * The Class SimpleNameFieldPredicate.
 */
public class SimpleNameFieldPredicate implements Predicate<Field> {

    /** The simple name. */
    private final String simpleName;

    /**
     * Instantiates a new simple name field predicate.
     *
     * @param simpleName
     *            the simple name
     */
    public SimpleNameFieldPredicate(String simpleName) {
        super();
        Validate.isTrue(StringUtils.isNotBlank(simpleName), "can't be null or blank");
        this.simpleName = simpleName;
    }

    public boolean evaluate(Field object) {
        if (object == null) {
            return false;
        }
        return StringUtils.equals(simpleName, object.getSimpleName());
    }

}
