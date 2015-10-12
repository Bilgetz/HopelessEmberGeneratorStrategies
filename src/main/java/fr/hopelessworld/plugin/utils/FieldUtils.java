package fr.hopelessworld.plugin.utils;

import javax.lang.model.type.TypeMirror;

import fr.hopelessworld.plugin.analyzer.Field;

/**
 * The Class FieldUtils.
 */
public final class FieldUtils {

    /**
     * Instantiates a new field utils.
     */
    private FieldUtils() {
    }

    /**
     * Gets the class name.
     *
     * @param field
     *            the field
     * @return the class name
     */
    public static String getClassName(Field field) {
        TypeMirror typeMirror = field.asType();
        String fullType = typeMirror.toString();
        return fullType.substring(fullType.lastIndexOf('.') + 1);
    }

}
