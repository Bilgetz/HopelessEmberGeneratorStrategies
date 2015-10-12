package fr.hopelessword.plugin.strategy.impl;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.Predicate;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;

import fr.hopelessworld.plugin.analyzer.AnalizedEntity;
import fr.hopelessworld.plugin.analyzer.Field;
import fr.hopelessworld.plugin.predicate.IdFieldPredicate;
import fr.hopelessworld.plugin.predicate.SimpleNameFieldPredicate;
import fr.hopelessworld.plugin.strategy.GeneratorStrategy;
import fr.hopelessworld.plugin.utils.FieldUtils;

/**
 * The Class EmberTemplateStrategy.
 */
public class EmberTemplateStrategy implements GeneratorStrategy {

    /** The name simple name predicate. */
    private final Predicate<Field> nameSimpleNamePredicate = new SimpleNameFieldPredicate("name");

    /** The title simple name predicate. */
    private final Predicate<Field> titleSimpleNamePredicate = new SimpleNameFieldPredicate("title");

    /** The id field predicate. */
    private final Predicate<Field> idFieldPredicate = new IdFieldPredicate();

    /*
     * (non-Javadoc)
     * 
     * @see
     * fr.hopelessworld.plugin.strategy.GeneratorStrategy#generate(java.io.File,
     * java.util.Collection)
     */
    public void generate(File root, Collection<AnalizedEntity> entities) throws IOException {
        Validate.notNull(root, "File need to be passed");
        Validate.isTrue((!root.exists()) || root.isDirectory(), "File is not a directory");
        root.mkdirs();

        for (AnalizedEntity entity : entities) {
            this.generateSingle(root, entity);
            this.generateList(root, entity);
            this.generateEdit(root, entity);
        }

    }

    /**
     * Generate single.
     *
     * @param root
     *            the root
     * @param entity
     *            the entity
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     */
    private void generateSingle(File root, AnalizedEntity entity) throws IOException {
        String name = entity.getSimpleName().toLowerCase();
        File showSingle = new File(root, name + ".hbs");
        StringBuilder output = new StringBuilder();

        output.append("<div class='").append(name).append("-profile'>");
        output.append("{{#if deleteMode}}");
        output.append("<div class='confirm-box'>");
        output.append("<h4>confirm ?</h4>");
        output.append("<button {{action 'confirmDelete'}}> oui </button>");
        output.append("<button {{action 'cancelDelete'}}> non </button>");
        output.append("</div>");
        output.append("{{/if}}");
        //
        output.append("<button {{action 'edit'}}>Edit</button><br/>");
        output.append("<button {{action 'delete'}}>Delete</button><br/>");

        output.append("<h2>{{").append(this.getShowName("", entity)).append("}}</h2>");
        for (Field field : entity.getFields()) {
            if (field.getAnnotation(Id.class) != null) {
                output.append("{{!-- id not show --}}");
            } else if (field.getAnnotation(ManyToOne.class) != null || field.getAnnotation(OneToOne.class) != null) {
                output.append("<div><span>").append(field.getSimpleName()).append(" : </span>");
                output.append("{{#link-to '").append(field.getSimpleName()).append("' ").append(field.getSimpleName()).append("}}");
                output.append("{{").append(field.getSimpleName()).append(".").append(this.getShowName("", field.asAnalyzedEntity())).append("}}");
                output.append("{{/link-to}}");
                output.append("</div>");
            } else if (field.getAnnotation(OneToMany.class) != null || field.getAnnotation(ManyToMany.class) != null) {

            } else if (field.getAnnotation(Column.class) != null) {
                output.append("<div><span>").append(field.getSimpleName()).append(" : </span><span>{{");
                if (Date.class.getCanonicalName().equals(field.asType().toString())) {
                    output.append("formatDate ");
                }
                output.append(field.getSimpleName());
                output.append("}}</div>");
            }
        }

        // output.append("<img {{bind-attr src='avatarUrl'}} alt='Avatar de l'utilisateur' />");
        // output.append("<span>{{email}}</span>");
        // output.append("<p>{{bio}}</p>");
        // output.append("<span>Création {{formatDate creationDate}}</span>");
        // output.append("<br />");
        // output.append("Equipe :");
        // output.append("{{#link-to 'team' team}}");
        // output.append("{{team.name}}");
        // output.append("{{/link-to}}");
        //

        output.append("</div>");
        output.append("{{outlet}}");

        FileWriter fs = new FileWriter(showSingle);
        fs.append(output);
        fs.close();
    }

    /**
     * Generate list.
     *
     * @param root
     *            the root
     * @param entity
     *            the entity
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     */
    private void generateList(File root, AnalizedEntity entity) throws IOException {
        String entityName = entity.getSimpleName();
        String lowerCaseEntityName = entity.getSimpleName().toLowerCase();
        String uncapitalizedName = StringUtils.uncapitalize(entityName);

        File showList = new File(root, lowerCaseEntityName + "s.hbs");
        StringBuilder output = new StringBuilder();

        output.append("<span>").append(entityName).append("s:</span>");
        // <span>Utilisateurs: {{usersCount}}</span>
        output.append("<ul class=\"").append(lowerCaseEntityName).append("-listing\">");
        // <ul class="users-listing">
        output.append("{{#each ").append(uncapitalizedName).append(" in controller}}");
        // {{#each user in controller}}
        output.append("<li>");
        // <li>
        output.append("{{#link-to \"").append(uncapitalizedName).append("\" ").append(uncapitalizedName).append(" }}");
        // {{#link-to "user" user}}
        output.append("{{").append(uncapitalizedName).append(".").append(this.getShowName("", entity)).append("}}");
        // {{user.name}}
        output.append("{{/link-to}}");
        // {{/link-to}}
        output.append("</li>");
        // </li>
        output.append("{{else}}");
        // {{else}}
        output.append("No ").append(entityName).append(" to show");
        // <li>pas d'utilisateurs… :-(</li>
        output.append("{{/each}}");
        // {{/each}}
        output.append("</ul>");
        // </ul>
        output.append("{{#link-to \"").append(lowerCaseEntityName).append("s.create\" class=\"create-btn\"}} add a ").append(entityName);
        // {{#link-to "users.create" class="create-btn"}} Ajouter un utilisateur
        output.append("{{/link-to}}");
        // {{/link-to}}
        output.append("{{outlet}}");
        // {{outlet}}

        FileWriter fs = new FileWriter(showList);
        fs.append(output);
        fs.close();
    }

    /**
     * Generate edit.
     *
     * @param root
     *            the root
     * @param entity
     *            the entity
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     */
    private void generateEdit(File root, AnalizedEntity entity) throws IOException {
        String entityName = entity.getSimpleName();
        String lowerCaseEntityName = entity.getSimpleName().toLowerCase();

        File showEdit = new File(root, lowerCaseEntityName + "/edit.hbs");
        this.mkdirs(showEdit);
        StringBuilder output = new StringBuilder();

        output.append("<div class=\"").append(lowerCaseEntityName).append("-edit\">");

        for (Field field : entity.getFields()) {
            if (field.getAnnotation(Id.class) != null) {
                output.append("{{!-- Id are ignored --}}");
            } else if (field.getAnnotation(ManyToOne.class) != null || field.getAnnotation(OneToOne.class) != null) {
                output.append("<label>").append(field.getSimpleName()).append("</label>");

                String attributType = FieldUtils.getClassName(field);

                output.append("{{view \"select\" ").append("optionValuePath=\"content.id\" ").append("optionLabelPath=\"content.")
                        .append(this.getShowName("", field.asAnalyzedEntity())).append("\" contentBinding=\"").append(StringUtils.uncapitalize(attributType))
                        .append("sForSelect\" ").append("selection=\"selected").append(attributType).append("\" prompt=\"Please select a ")
                        .append(attributType).append("\" }}");
                output.append("<br />");
            } else if (field.getAnnotation(OneToMany.class) != null) {
                output.append("{{!-- ").append(field.getSimpleName()).append(" ignored  --}}");
            } else if (field.getAnnotation(Column.class) != null) {
                output.append("<label>").append(field.getSimpleName()).append("</label>");
                output.append("{{input value=").append(field.getSimpleName()).append("}}");
                output.append("<br />");
            } else {
                output.append("{{!-- ").append(field.getSimpleName()).append(" ignored  --}}");
            }
        }

        output.append("<button {{action \"save\"}}> ok </button>");
        // <button {{action "save"}}> ok </button>
        output.append("</div>");
        // </div>
        FileWriter fs = new FileWriter(showEdit);
        fs.append(output);
        fs.close();
    }

    /**
     * Gets the show name.
     *
     * @param name
     *            the name
     * @param entity
     *            the entity
     * @return the show name
     */
    private String getShowName(String name, AnalizedEntity entity) {

        Field fieldName = CollectionUtils.find(entity.getFields(), this.nameSimpleNamePredicate);
        if (fieldName == null) {
            fieldName = CollectionUtils.find(entity.getFields(), this.titleSimpleNamePredicate);
        }
        Field idField = CollectionUtils.find(entity.getFields(), this.idFieldPredicate);
        if (fieldName != null) {
            return StringUtils.join(name, fieldName.getSimpleName());
        } else if (idField != null) {
            return StringUtils.join(name, idField.getSimpleName());
        } else {
            String finalName = null;
            for (Field field : entity.getFields()) {
                if (field.getAnnotation(Column.class) != null && field.getAnnotation(OneToMany.class) != null && field.getAnnotation(OneToOne.class) != null
                        && field.getAnnotation(ManyToOne.class) != null && field.getAnnotation(ManyToMany.class) != null
                        && field.getAnnotation(JoinColumn.class) != null) {
                    finalName = StringUtils.join(finalName, name, field.getSimpleName(), " ");
                }
            }
            return finalName;
        }

    }

    /**
     * Mkdirs.
     *
     * @param file
     *            the file
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     */
    private void mkdirs(File file) throws IOException {
        if (!file.exists()) {
            file.getParentFile().mkdirs();
            file.createNewFile();
        }
    }

}
