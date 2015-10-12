# README #

### Informations ###

This is strategie for generate EmberJs component for [Hopeless Entity Analyzer](https://github.com/Bilgetz/HopelessEntityAnalyzer)

### How to use ###

``buildscript {
 ...
    dependencies {
        classpath("fr.hopelessworld.plugin:EntityAnalyzerPlugin:1.0")
        classpath("fr.hopelessworld.plugin:EntityAnalyzerPlugin:1.0")
        classpath("fr.hopelessworld.plugin:EmberGeneratorStrategy:1.0")
   }
}

apply plugin: 'fr.hopelessworld.plugin.entity-analyzer'

analyzeEntity {
     entityDirectory = file("/src/main/java/fr/hopelessworld/entity")
    strategies {
        model {
            strategyClass = "fr.hopelessword.plugin.strategy.impl.EmberModelStrategy"
            outputFiles = file("${buildDir}/generated/js/model.js")
        }
        template {
            strategyClass = "fr.hopelessword.plugin.strategy.impl.EmberTemplateStrategy"
            outputFiles = file("${buildDir}/generated/templates")
        }
        controller {
            strategyClass = "fr.hopelessword.plugin.strategy.impl.EmberControllerStrategy"
            outputFiles = file("${buildDir}/generated/js/controllers.js")
        }
        route {
            strategyClass = "fr.hopelessword.plugin.strategy.impl.EmberRouteStrategy"
            outputFiles = file("${buildDir}/generated/js/routes.js")
        }
        routemap {
            strategyClass = "fr.hopelessword.plugin.strategy.impl.EmberRouteMapStrategy"
            outputFiles = file("${buildDir}/generated/js/routesmap.js")
        }
             
    }
}
``