package io.jumpco.open.kfsm.gradle;

import java.io.File

class KFSMVizPluginExtension {
    KFSMVizPluginExtension(String fsmClassName) {
        this.fsmClassName = fsmClassName
    }
    File input = null
    File outputFolder = null
    String fsmClassName = null
    String output = null
    String plantUmlName = null
    String asciidocName = null
    Boolean isGeneratePlantUml = false
    Boolean isGenerateAsciidoc = false
}
