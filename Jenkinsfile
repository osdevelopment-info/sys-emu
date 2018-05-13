
pipeline {
    agent any
    stages {
        stage('Init') {
            steps {
                script {
                    def javaHome = tool 'Current JDK 8'
                    def sbtHome = tool 'sbt-latest'
                    env.sbt= "${javaHome}/bin/java -Xmx4G -Dsbt.log.noformat=true -jar ${sbtHome}"
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
            }
        }
        stage('Test') {
            steps {
                sh "${sbt} test coverageReport"
            }
        }
        stage('Publish Reports') {
            steps {
                junit 'target/test-reports/*.xml'
                step([$class: 'ScoveragePublisher',
                    reportDir: 'target/scala-2.11/scoverage-report',
                    reportFile: 'scoverage.xml'])
            }
        }
        stage('Create Artifacts') {
            steps {
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
