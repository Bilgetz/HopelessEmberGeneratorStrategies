package fr.hopelessword.plugin.strategy.impl;

import java.util.Collection;
import java.util.Date;

import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.apache.commons.lang3.StringUtils;

import fr.hopelessworld.plugin.analyzer.AnalizedEntity;
import fr.hopelessworld.plugin.analyzer.Field;
import fr.hopelessworld.plugin.strategy.AbstractUniqueFileGeneratorStrategy;
import fr.hopelessworld.plugin.utils.FieldUtils;

public class EmberModelStrategy extends AbstractUniqueFileGeneratorStrategy {

    public CharSequence generate(Collection<AnalizedEntity> entities) {
        StringBuilder models = new StringBuilder();

        for (AnalizedEntity entity : entities) {
            StringBuilder model = new StringBuilder();
            model.append("App.").append(entity.getSimpleName()).append(" = DS.Model.extend({");
            boolean first = true;
            for (Field field : entity.getFields()) {

                if (!first) {
                    model.append(",");
                }

                if (field.getAnnotation(Id.class) != null) {
                    model.append("/* Id are ignored */");
                } else if (field.getAnnotation(ManyToOne.class) != null) {
                    model.append(this.generateModelForManyToOne(field));
                    first = false;
                } else if (field.getAnnotation(OneToMany.class) != null) {
                    model.append(this.generateModelForOneToMany(field));
                    first = false;
                } else if (field.getAnnotation(Column.class) != null) {
                    model.append(this.generateModelForColum(field));
                    first = false;
                } else {
                    model.append("/* ").append(field.getSimpleName()).append(" ignored */");
                }
            }

            model.append("});");
            models.append(model);
        }
        return models;
    }

    private StringBuffer generateModelForColum(Field element) {
        StringBuffer attribute = new StringBuffer();
        attribute.append(element.getSimpleName()).append(':');
        TypeMirror typeMirror = element.asType();

        if (String.class.getCanonicalName().equals(typeMirror.toString())) {
            attribute.append("DS.attr('string')");
        } else if (Date.class.getCanonicalName().equals(typeMirror.toString())) {
            attribute.append("DS.attr('date')");
        } else if (element.asType().getKind() == TypeKind.LONG || Long.class.getCanonicalName().equals(typeMirror.toString())
                || element.asType().getKind() == TypeKind.INT || Integer.class.getCanonicalName().equals(typeMirror.toString())) {
            attribute.append("DS.attr('number')");
        } else if (element.asType().getKind() == TypeKind.BOOLEAN || Boolean.class.getCanonicalName().equals(typeMirror.toString())) {
            attribute.append("DS.attr('boolean')");
        } else {
            attribute.append("/*element ").append(element.getSimpleName()).append("ignored, class unknow*/");
        }

        return attribute;
    }

    private StringBuffer generateModelForManyToOne(Field element) {
        StringBuffer attribute = new StringBuffer();

        ManyToOne anot = element.getAnnotation(ManyToOne.class);
        // TypeMirror typeMirror = element.asType();
        // String fullType = typeMirror.toString();
        // String attributType = fullType.substring(fullType.lastIndexOf('.') +
        // 1);

        String attributType = FieldUtils.getClassName(element);

        attribute.append(element.getSimpleName()).append(':');
        attribute.append("DS.belongsTo('");
        attribute.append(StringUtils.uncapitalize(attributType));
        attribute.append("' ,{async: true})");

        return attribute;
    }

    private StringBuffer generateModelForOneToMany(Field element) {
        StringBuffer attribute = new StringBuffer();

        TypeMirror typeMirror = element.asType();

        String typeString = typeMirror.toString();
        String fullType = typeString.substring(typeString.indexOf('<') + 1, typeString.indexOf('>'));
        String attributType = fullType.substring(fullType.lastIndexOf('.') + 1);

        OneToMany anot = element.getAnnotation(OneToMany.class);

        attribute.append(element.getSimpleName()).append(':');
        attribute.append("DS.hasMany('");
        attribute.append(StringUtils.uncapitalize(attributType));
        attribute.append("', { inverse :'");
        attribute.append(anot.mappedBy());
        attribute.append("', async: true})");

        // TypeMirror typeMirror = it.asType();
        // // typeMirror.getKind().isPrimitive()
        // DeclaredType declaredTypeMiror = (DeclaredType) typeMirror;
        // Collection<String> typeArguments = new ArrayList<String>();
        // List<? extends TypeMirror> generics = ((DeclaredType)
        // typeMirror).getTypeArguments();
        // type.setTypeParameters(typeArguments);
        // if (CollectionUtils.isNotEmpty(generics)) {
        // String typeString = typeMirror.toString();
        // String fullType = typeString.substring(0, typeString.indexOf('<'));
        //
        // type.setMainClass(fullType);
        // for (TypeMirror genericsTypeMiror : ((DeclaredType)
        // typeMirror).getTypeArguments()) {
        // typeArguments.add(genericsTypeMiror.toString());
        // }
        // } else {
        // type.setMainClass(declaredTypeMiror.toString());
        // }

        return attribute;
    }

}
