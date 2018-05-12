
pipeline {
    agent any
    stages {
        stage('Init') {
            steps {
                script {
                    def javaHome = tool 'Current JDK 8'
                    def sbtHome = tool 'sbt-latest'
                    env.sbt= "${javaHome} -Xmx4G -jar ${sbtHome} -no-colors -batch"
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
