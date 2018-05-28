podTemplate(label: 'label2',containers: [
    containerTemplate(
        name: 'maven',
        image: 'jorgeacetozi/maven:3.5.0-jdk-8-alpine',
        ttyEnabled: true,
        command: 'cat'
    )
]) {
    node('label2') {
        stage('Checkout from SCM') {
            git url: 'https://github.com/wwojdanowski/gsonfromjson.git',
            branch: 'master'

            stash name: 'gsonfromjson-src'
        }

        parallel(
                one: {
                    stage('Maven package') {
                        container('maven') {
                            sh 'mvn clean test'
                        }
                    }
                
                    stage('Collect unit tests results') {
                        junit 'target/surefire-reports/TEST-*.xml'  
                    }
                },
                two: {
                 podTemplate(label: 'label3',
                	containers: [
                        containerTemplate(
                            name: 'maven2',
                            image: 'jorgeacetozi/maven:3.5.0-jdk-8-alpine',
                            ttyEnabled: true,
                            command: 'cat'
                        )
                    ]
                ) {
                    node('label3') {
                        stage('Get stashed source code') {
                            unstash 'gsonfromjson-src'
                        }
                    
                        stage('Collect checkstyle results') {
                            container('maven2') {
                            sh 'mvn clean checkstyle:checkstyle'
                            }
                            step([$class: 'CheckStylePublisher',
                              canRunOnFailed: true,
                              defaultEncoding: '',
                              healthy: '100',
                              pattern: '**/target/checkstyle-result.xml',
                              unHealthy: '90',
                              useStableBuildAsReference: true
                            ]) 
                        }
                    }
                }
            }
        )
    }
}

parallel (
            one: {
                podTemplate(label: 'label2',
            	containers: [
                    containerTemplate(
                        name: 'maven',
                        image: 'jorgeacetozi/maven:3.5.0-jdk-8-alpine',
                        ttyEnabled: true,
                        command: 'cat'
                    )
                ]
                ) {
                    node('label2') {
                        stage('Checkout the Notepad application') {
                            git url: 'https://github.com/wwojdanowski/gsonfromjson.git',
                            branch: 'master'
                        }
                
                        stage('Maven package') {
                            container('maven') {
                                sh 'mvn clean test'
                            }
                        }
                    
                        stage('Collect unit tests results') {
                            junit 'target/surefire-reports/TEST-*.xml'  
                        }
                    }
                }
        },
        two: {
         podTemplate(label: 'label3',
        	containers: [
                containerTemplate(
                    name: 'maven2',
                    image: 'jorgeacetozi/maven:3.5.0-jdk-8-alpine',
                    ttyEnabled: true,
                    command: 'cat'
                )
            ]
        ) {
                node('label3') {
                    stage('Checkout the Notepad application') {
                        git url: 'https://github.com/wwojdanowski/gsonfromjson.git',
                        branch: 'master'
                    }
                
                    stage('Collect checkstyle results') {
                        container('maven2') {
                        sh 'mvn clean checkstyle:checkstyle'
                        }
                        step([$class: 'CheckStylePublisher',
                          canRunOnFailed: true,
                          defaultEncoding: '',
                          healthy: '100',
                          pattern: '**/target/checkstyle-result.xml',
                          unHealthy: '90',
                          useStableBuildAsReference: true
                        ]) 
                    }
                }
            }
        }
    )


