#!/usr/bin/env groovy
// the "!#/usr/bin... is just to to help IDEs, GitHub diffs, etc properly detect the language and do syntax highlighting for you.
// thx to https://github.com/jenkinsci/pipeline-examples/blob/master/docs/BEST_PRACTICES.md

// note that we set a default version for this library in jenkins, so we don't have to specify it here

@Library('misc')
import de.metas.jenkins.MvnConf

def build(final MvnConf mvnConf, 
          final Map scmVars, 
          final boolean forceBuild = false,
          final boolean forceSkipMobileWebui = false,
          final boolean forceSkipProcurementWebui = false) {
    
    stage('Build misc services') {
        currentBuild.description = """${currentBuild.description}<p/>
			<h2>misc services</h2>"""

        dir('procurement-webui') {
            def buildFile = load('buildfile.groovy')
            buildFile.build(mvnConf, scmVars, forceBuild, forceSkipProcurementWebui)
        }

        withMaven(jdk: 'java-17', maven: 'maven-3.6.3', mavenLocalRepo: '.repository', mavenOpts: '-Xmx1536M', options: [artifactsPublisher(disabled: true)]) {
// Comment out EDI in big_coconut_uat.
// The user doesn't care and it fails - not locally in the IDE, but on jenkins - with
// "Failed to execute goal org.apache.maven.plugins:maven-compiler-plugin:3.8.1:compile (default-compile) on project de-metas-camel-edi: Compilation failure -> [Help 1]"            
//            dir('camel/de-metas-camel-edi') {
//                def ediBuildFile = load('buildfile.groovy')
//                ediBuildFile.build(mvnConf, scmVars, forceBuild)
//            }
            dir('camel/de-metas-camel-externalsystems') {
                def externalsystemsBuildFile = load('buildfile.groovy')
                externalsystemsBuildFile.build(mvnConf, scmVars, forceBuild)
            }
            dir('admin') {
                def procurementWebuiBuildFile = load('buildfile.groovy')
                procurementWebuiBuildFile.build(mvnConf, scmVars, forceBuild)
            }
        }
    } // stage
}

return this
