/*
 * Copyright (c) 2020-2021. Open JumpCO
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and limitations under the License.
 */
package io.jumpco.open.kfsm.gradle

import io.jumpco.open.kfsm.viz.Parser
import io.jumpco.open.kfsm.viz.Visualization
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputFiles
import org.gradle.api.tasks.OutputFiles
import org.gradle.api.tasks.TaskAction

class KFSMVizualisationTask extends DefaultTask {
    @OutputFiles
    Set<File> outputFiles = new HashSet<>()
    @InputFiles
    Set<File> inputFiles = new HashSet<>()

    @Input
    List<KFSMVizGenParam> fsmParams = []

    @TaskAction
    def generateVisualization() {
        if (fsmParams.isEmpty()) {
            project.logger.debug("Configuring in action")
            def kfsmViz = project.extensions.findByType(VizPluginExtension)
            configureTask(kfsmViz)
        }
        fsmParams.forEach {
            project.logger.lifecycle("${this.name}:reading ${it.className} from ${it.input}")
            assert it.input.exists()
            def fsmContent = Parser.parseStateMachine(it.className, it.input)
            if (it.outputPlantUml != null) {
                project.logger.lifecycle("${this.name}:creating ${it.outputPlantUml}")
                if (!it.outputPlantUml.parentFile.exists()) {
                    it.outputPlantUml.parentFile.mkdirs()
                }
                it.outputPlantUml.text = Visualization.plantUml(fsmContent)
            }
            if (it.outputAsciiDoc != null) {
                project.logger.lifecycle("${this.name}:creating ${it.outputAsciiDoc}")
                if (!it.outputAsciiDoc.parentFile.exists()) {
                    it.outputAsciiDoc.parentFile.mkdirs()
                }
                it.outputAsciiDoc.text = Visualization.asciiDoc(fsmContent)
            }
        }
    }

    def configureTask(VizPluginExtension kfsmViz) {
        kfsmViz.getFsms().forEach {
            project.logger.debug("${this.name}:$it")
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
    }
}
