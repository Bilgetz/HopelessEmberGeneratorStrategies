package fr.hopelessword.plugin.strategy.impl;

import java.util.Collection;

import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import org.apache.commons.lang3.StringUtils;

import fr.hopelessworld.plugin.analyzer.AnalizedEntity;
import fr.hopelessworld.plugin.analyzer.Field;
import fr.hopelessworld.plugin.strategy.AbstractUniqueFileGeneratorStrategy;
import fr.hopelessworld.plugin.utils.FieldUtils;

public class EmberRouteStrategy extends AbstractUniqueFileGeneratorStrategy {

    public CharSequence generate(Collection<AnalizedEntity> entities) {
        StringBuilder routes = new StringBuilder();

        for (AnalizedEntity entity : entities) {
            StringBuilder route = new StringBuilder();
            String lowerCaseEntity = entity.getSimpleName().toLowerCase();
            // Route for list entity
            route.append("App.").append(entity.getSimpleName()).append("sRoute = Ember.Route.extend({");
            route.append("model : function() {");
            route.append("return this.store.findAll('").append(lowerCaseEntity).append("');");
            route.append("}");
            route.append("});");

            CharSequence setupController = this.createSetupController(entity);

            // route for create entity
            route.append("App.").append(entity.getSimpleName()).append("sCreateRoute = Ember.Route.extend({");
            route.append("model : function() {");
            route.append("return this.store.createRecord('").append(lowerCaseEntity).append("');");
            route.append("},");

            route.append("renderTemplate: function(){");
            route.append("this.render('").append(lowerCaseEntity).append(".edit', {");
            route.append("controller: '").append(lowerCaseEntity).append("sCreate'");
            route.append("});");
            route.append("},");

            route.append(setupController);

            route.append("});");

            // route for edit
            route.append("App.").append(entity.getSimpleName()).append("EditRoute = Ember.Route.extend({");
            route.append(setupController);
            route.append("});");

            routes.append(route);
        }
        return routes;
    }

    /**
     * Creates the setup controller.
     *
     * @param entity
     *            the entity
     * @return the char sequence
     */
    private CharSequence createSetupController(AnalizedEntity entity) {
        StringBuilder setupController = new StringBuilder(1000);

        setupController.append("setupController:function(controller,model) {");
        setupController.append("this._super(controller,model);");
        for (Field field : entity.getFields()) {
            if (field.getAnnotation(ManyToOne.class) != null || field.getAnnotation(OneToOne.class) != null) {
                String attributType = FieldUtils.getClassName(field);
                setupController.append("controller.set('").append(StringUtils.uncapitalize(attributType)).append("sForSelect'");
                setupController.append(", this.store.find('").append(StringUtils.uncapitalize(attributType)).append("'));");
                setupController.append("controller.set('selected").append(attributType).append("', model);");
            }
        }
        setupController.append("}");
        return setupController;
    }
}
