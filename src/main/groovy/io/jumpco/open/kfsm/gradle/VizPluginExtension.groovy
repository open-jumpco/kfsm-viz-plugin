package io.jumpco.open.kfsm.gradle

import org.gradle.api.Action
import org.gradle.util.ConfigureUtil

class VizPluginExtension {

    List<KFSMVizGenParam> fsms = []

    def List<KFSMVizPluginExtension> getFsms()  {
        return fsms
    }

    def fsm(String className, Closure closure) {
        def fsm = new KFSMVizPluginExtension(className)
        fsms += fsm
        ConfigureUtil.configure(closure, fsm)
        return fsm
    }

    public void fsm(String className, Action<KFSMVizPluginExtension> action) {
        def fsm = new KFSMVizPluginExtension(className)
        fsms += fsm
        action.execute(fsm)
    }
}
