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

    boolean equals(o) {
        if (this.is(o)) return true
        if (getClass() != o.class) return false

        KFSMVizPluginExtension that = (KFSMVizPluginExtension) o

        if (fsmClassName != that.fsmClassName) return false
        if (input != that.input) return false

        return true
    }

    int hashCode() {
        int result
        result = (input != null ? input.hashCode() : 0)
        result = 31 * result + (fsmClassName != null ? fsmClassName.hashCode() : 0)
        return result
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", KFSMVizPluginExtension.class.getSimpleName() + "[", "]")
                .add("input=" + input)
                .add("fsmClassName='" + fsmClassName + "'")
                .add("outputFolder=" + outputFolder)
                .add("output='" + output + "'")
                .add("plantUmlName='" + plantUmlName + "'")
                .add("asciidocName='" + asciidocName + "'")
                .add("isGeneratePlantUml=" + isGeneratePlantUml)
                .add("isGenerateAsciidoc=" + isGenerateAsciidoc)
                .toString();
    }
}
