
pipeline {
    agent any
    stages {
        stage('Init') {
            steps {
                script {
                    def sbtHome = tool 'sbt-latest'
                    env.sbt= "${sbtHome}/bin/sbt -no-colors -batch"
                }
            }
        }
        stage('Checkout') {
            steps {
                checkout scm
            }
        }
        stage('Build') {
            steps {
                sh "${sbt} clean"
                sh "${sbt} compile"
                sh "${sbt} test coverageReport"
                sh "${sbt} package"
            }
        }
        stage('Archive Artifacts') {
            steps {
                archiveArtifacts 'target/**/*.jar'
            }
        }
    }
}
