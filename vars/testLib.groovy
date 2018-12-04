  def call(body) {

        def deployScript = libraryResource 'deploy.sh'
        def config = [:]
        body.resolveStrategy = Closure.DELEGATE_FIRST
        body.delegate = config
        body()

        node {
            // Clean workspace before doing anything
            deleteDir()
            label config.agent
            try {
                stage ('Clone') {
                    checkout scm
                }
                stage ('Build') {
                    sh "ls; echo 'building ${config.projectName} ...'"
                }
                stage ('Tests') {
                    parallel 'static': {
                        sh "echo 'shell scripts to run static tests...'"
                    },
                    'unit': {
                        sh "echo 'shell scripts to run unit tests...'"
                    },
                    'integration': {
                        sh "echo 'shell scripts to run integration tests...'"
                    }
                }
                stage ('Deploy') {
                    sh deployScript
                }
            } catch (err) {
                currentBuild.result = 'FAILED'
                throw err
            }
        }
    }
