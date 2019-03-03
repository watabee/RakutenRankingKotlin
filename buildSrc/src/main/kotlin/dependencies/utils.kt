package dependencies

import org.gradle.api.Project

fun Project.getPropertyOrEnv(propertyName: String): Any? {
    return project.properties[propertyName] ?: System.getenv(propertyName)
}
