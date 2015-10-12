package fr.hopelessword.plugin.strategy.impl;

import java.util.Collection;

import fr.hopelessworld.plugin.analyzer.AnalizedEntity;
import fr.hopelessworld.plugin.strategy.AbstractUniqueFileGeneratorStrategy;

public class EmberRouteMapStrategy extends AbstractUniqueFileGeneratorStrategy {

    public CharSequence generate(Collection<AnalizedEntity> entities) {
        StringBuilder routes = new StringBuilder();

        routes.append("App.Router.map(function() {");
        // App.Router.map(
        // function() {
        for (AnalizedEntity entity : entities) {
            StringBuilder route = new StringBuilder();
            String lowerCaseEntity = entity.getSimpleName().toLowerCase();

            route.append("this.resource('").append(lowerCaseEntity).append("s',function() {");
            // this.resource('users',function() {
            route.append("this.resource(");
            // this.resource(
            route.append("'").append(lowerCaseEntity).append("',");
            // 'user',
            route.append(" {path : '/:id'},");
            // {path : '/:id'},
            route.append("function() {");
            // function() {
            route.append("this.route('edit');");
            // this.route('edit');
            route.append("}");
            // }
            route.append(");");
            // );
            route.append("this.route('create');");
            // this.route('create');
            route.append(" });");
            // }); //this.resource('users',function() {

            routes.append(route);
        }
        routes.append("});");
        // });//App.Router.map(//function() {
        return routes;
    }

}
