@Library('original-library') _
 
pipeline {
    agent any
    environment {
        GRUPO = 'corporativo'
    }
    stages {
        stage ("Checkout"){
            steps {
                originalCheckout()
            }
        }
        stage ("Build") {
            steps {
                originalMavenBuild()
            }
        }
        stage ("Analysis") {
            steps { 
                originalMavenSonarqube()
            }
        }
        stage ("Quality Gate") {
            steps { 
                originalMavenQualityGates()
            }
        }
        stage ("Publish") {
            steps {
                originalMavenPublish()
            }
        }
        stage ("Deploy") {
            steps {
                originalDeployBatch()
            }
        }
    }
}
