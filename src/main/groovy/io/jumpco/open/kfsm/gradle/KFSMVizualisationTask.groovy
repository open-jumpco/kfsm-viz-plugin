package io.jumpco.open.kfsm.gradle

import io.jumpco.open.kfsm.viz.Parser
import io.jumpco.open.kfsm.viz.Visualization
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.InputFiles
import org.gradle.api.tasks.OutputFiles
import org.gradle.api.tasks.TaskAction
import java.io.File


class KFSMVizualisationTask extends DefaultTask {
    @OutputFiles
    Set<File> outputFiles = new HashSet<>()
    @InputFiles
    Set<File> inputFiles = new HashSet<>()

    List<KFSMVizGenParam> fsmParams = []

    @TaskAction
    def generateVisualization() {
        def kfsmViz =
            project.extensions.findByType(VizPluginExtension)
        kfsmViz.getFsms().forEach {
            def taskParam = new KFSMVizGenParam(
                    it.fsmClassName ?: error("fsmClassName required"),
                    it.input ?: error("input required")
                )

            if (it.isGenerateAsciidoc || it.asciidocName != null) {
                def fileName = it.asciidocName ?: it.output ?: "${it.fsmClassName}.adoc"
                taskParam.outputAsciiDoc = new File(
                    it.outputFolder ?: project.file("${project.buildDir}/generated"),
                    fileName.contains(".") ? fileName : fileName + ".adoc"
                )
            }
            if (it.isGeneratePlantUml || it.plantUmlName != null) {
                def fileName = it.plantUmlName ?: it.output ?: "${it.fsmClassName}.plantuml"
                taskParam.outputPlantUml = new File(
                    it.outputFolder ?: project.file("${project.buildDir}/generated"),
                    fileName.contains(".") ? fileName : fileName + ".plantuml"
                )
            }
            fsmParams.add(taskParam)
            inputFiles += taskParam.input
            if (taskParam.outputAsciiDoc != null) {
                outputFiles += taskParam.outputAsciiDoc
            }
            if (taskParam.outputPlantUml != null) {
                outputFiles += taskParam.outputPlantUml
            }
        }
        fsmParams.forEach {
            project.logger.lifecycle("${this.name}:reading ${it.className} from ${it.input}")
            assert it.input.exists()
            def fsmContent = Parser.parseStateMachine(it.className, it.input)
            if (it.outputPlantUml != null) {
                project.logger.lifecycle("${this.name}:creating ${it.outputPlantUml}")
                if(!it.outputPlantUml.parentFile.exists()) {
                    it.outputPlantUml.parentFile.mkdirs()
                }
                it.outputPlantUml.text = Visualization.plantUml(fsmContent)
            }
            if (it.outputAsciiDoc != null) {
                project.logger.lifecycle("${this.name}:creating ${it.outputAsciiDoc}")
                if(!it.outputAsciiDoc.parentFile.exists()) {
                    it.outputAsciiDoc.parentFile.mkdirs()
                }
                it.outputAsciiDoc.text = Visualization.asciiDoc(fsmContent)
            }
        }
    }
}