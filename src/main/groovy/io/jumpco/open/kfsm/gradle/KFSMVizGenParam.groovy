package io.jumpco.open.kfsm.gradle

class KFSMVizGenParam {
    String className
    File input
    File outputPlantUml = null
    File outputAsciiDoc = null

    KFSMVizGenParam(String className, File input) {
        this.className = className
        this.input = input
    }

    KFSMVizGenParam(String className, File input, File outputPlantUml, File outputAsciiDoc) {
        this.className = className
        this.input = input
        this.outputPlantUml = outputPlantUml
        this.outputAsciiDoc = outputAsciiDoc
    }

    boolean equals(o) {
        if (this.is(o)) return true
        if (getClass() != o.class) return false

        KFSMVizGenParam that = (KFSMVizGenParam) o

        if (className != that.className) return false
        if (input != that.input) return false
        if (outputAsciiDoc != that.outputAsciiDoc) return false
        if (outputPlantUml != that.outputPlantUml) return false

        return true
    }

    int hashCode() {
        int result
        result = className.hashCode()
        result = 31 * result + input.hashCode()
        result = 31 * result + (outputPlantUml != null ? outputPlantUml.hashCode() : 0)
        result = 31 * result + (outputAsciiDoc != null ? outputAsciiDoc.hashCode() : 0)
        return result
    }
}
