#!groovy

pipeline {
    agent any

    stages {
        stage("staging test") {
            when {
                expression { params.APPIUM_SERVER == 'http://appium.staging.testobject.org/wd/hub' }
            }
            steps {
                lock (resource: params.TESTOBJECT_DEVICE_ID) {
                    sh "./gradlew clean test"
                }
            }
        }
        stage("test") {
            when {
                 expression { params.APPIUM_SERVER != 'http://appium.staging.testobject.org/wd/hub' }
            }
            steps {
                sh "./gradlew clean test"
            }
        }
    }

    post {
        always {
            junit "**/test-results/TEST-*.xml"
        }
        failure {
            script {
                if (params.APPIUM_SERVER == 'http://appium.testobject.com/wd/hub') {
                    slackSend channel: "#${env.SLACK_CHANNEL}", color: "bad", message: "`${env.JOB_BASE_NAME}` failed (<${BUILD_URL}|open>)", teamDomain: "${env.SLACK_SUBDOMAIN}", token: "${env.SLACK_TOKEN}"
                }
            }
        }
    }
}
