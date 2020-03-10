package io.jumpco.open.kfsm.gradle


import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.invocation.Gradle

class VizPlugin implements Plugin<Project> {

    public final static KFSM_VIZ = "kfsmViz"
    public final static FSM_CONFIG = "fsm"

    void apply(Project project) {
        def kfsmViz = project.extensions.create(KFSM_VIZ, VizPluginExtension)
        project.tasks.register('generateFsmViz', KFSMVizualisationTask) { task ->
            group = 'documentation'
            doFirst {
                task.configureTask(project.extensions.findByType(VizPluginExtension))
            }
        }
    }

}
