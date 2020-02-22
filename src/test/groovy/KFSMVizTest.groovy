package io.jumpco.open.kfsm.gradle.viz

import org.gradle.testkit.runner.GradleRunner
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder

import static org.gradle.testkit.runner.TaskOutcome.SUCCESS

class KFSMVizTest {
    @Rule
    public TemporaryFolder testProjectDir = new TemporaryFolder()
    File buildFile
    File packetReader

    @Before
    public void setup() {
        buildFile = testProjectDir.newFile('build.gradle')
        buildFile << "plugins { id 'io.jumpco.open.kfsm.viz-plugin' } "

        copy(new File('src/test/resources'), buildFile.parentFile)
    }

    @Test
    public void testKfsmVizTask() {
        buildFile << """
    kfsmViz {
        fsm('PacketReaderFSM') {
            input = file('PacketReader.kt')
            output = 'packet-reader'
            outputFolder = file('generated')            
            isGeneratePlantUml = true
            isGenerateAsciidoc = true
        }
        fsm('TurnstileFSM') {
            input = file('Turnstile.kt')
            output = 'turnstile'
            outputFolder = file('generated')
            isGeneratePlantUml = true
            isGenerateAsciidoc = true
        }            
    }
"""
        def result = GradleRunner.create()
                .withProjectDir(testProjectDir.root)
                .withArguments('--stacktrace', '-i', 'generateFsmViz')
                .withPluginClasspath()
                .withDebug(true)
                .build()

        assert result.task(":generateFsmViz").outcome == SUCCESS
        println("Temp folder=$buildFile.parent")
        copy(buildFile.parentFile, new File('output'))
    }

    def copy(File source, File target) {
        if (source.isDirectory()) {
            source.eachFile {
                copy(it, new File(target, it.name))
            }
        } else {
            if (!target.parentFile.exists()) {
                target.parentFile.mkdirs()
            }
            println("Copy $source.path -> $target.path")
            target.bytes = source.bytes
        }
    }
}
