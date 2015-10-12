package fr.hopelessword.plugin.strategy.impl;

import java.util.Collection;

import fr.hopelessworld.plugin.analyzer.AnalizedEntity;
import fr.hopelessworld.plugin.strategy.AbstractUniqueFileGeneratorStrategy;

public class EmberControllerStrategy extends AbstractUniqueFileGeneratorStrategy {

    public CharSequence generate(Collection<AnalizedEntity> entities) {
        StringBuilder controllers = new StringBuilder();

        for (AnalizedEntity entity : entities) {
            StringBuilder controller = new StringBuilder();
            String lowerCaseEntity = entity.getSimpleName().toLowerCase();
            controller.append("App.").append(entity.getSimpleName()).append("Controller = Ember.ObjectController.extend({");
            controller.append("deleteMode: false,");
            controller.append("actions: {");
            controller.append("edit: function(){");
            controller.append("this.transitionToRoute('").append(lowerCaseEntity).append(".edit');");
            controller.append("},");
            controller.append("delete: function(){");
            controller.append("this.toggleProperty('deleteMode');");
            controller.append("},");
            controller.append("cancelDelete: function(){");
            controller.append("this.set('deleteMode', false);");
            controller.append("},");
            controller.append("confirmDelete: function(){");
            controller.append("var entity = this.get('model');");
            controller.append("entity.deleteRecord();");
            controller.append("entity.save();");
            controller.append("this.transitionToRoute('").append(lowerCaseEntity).append("s").append("');");
            controller.append("this.set('deleteMode', false);");
            controller.append("}");
            controller.append("}");
            controller.append("});");

            controller.append("App.").append(entity.getSimpleName()).append("EditController = Ember.ObjectController.extend({");
            controller.append("actions: {");

            controller.append("save: function(){var entity = this.get('model');entity.save();");
            controller.append("this.transitionToRoute('").append(lowerCaseEntity).append("', entity);");
            controller.append("}");

            controller.append("}");

            controller.append("});");
            controllers.append(controller);

        }
        return controllers;
    }

}
