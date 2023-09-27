@Library('tws-ci-library') _
pipeline {
    agent {
        kubernetes {
//             cloud "kubernetes-docker-job"
            yaml '''
                apiVersion: v1
                kind: Pod
                metadata:
                  labels:
                    role: kaniko
                spec:
                  containers:
                  - name: kaniko
                    image: gcr.io/kaniko-project/executor:debug
                    command:
                    - /busybox/cat
                    imagePullPolicy: Always
                    tty: true
                    volumeMounts:
                    - name: jenkins-docker-cfg
                      mountPath: /kaniko/.docker
                  volumes:
                  - name: jenkins-docker-cfg
                    projected:
                      sources:
                      - secret:
                          name: dockerhub-secret
                          items:
                            - key: config.json
                              path: config.json
                '''
        }
    }
    environment {
        DOCKERHUB_USERNAME = 'dodo133' // Docker Hub의 사용자 이름을 여기에 넣으세요.
        IMAGE_NAME = 'prod_video_backend_spring_service' // 원하는 이미지 이름을 여기에 넣으세요.
    }
    stages {
        stage('extract docker tag') {
            steps {
                script {
                    env.DOCKER_TAG = extractDockerTag()
                }
            }
        }
        stage('Build and Push') {
            steps {
                container('kaniko') {
                    script {
                        sh "ls /kaniko/.docker"

                        sh """
                        echo '${DOCKERHUB_USERNAME}/${IMAGE_NAME}:${env.DOCKER_TAG}'
                        """
                        buildAndPush(DOCKERHUB_USERNAME, IMAGE_NAME, env.DOCKER_TAG)
                    }
                }
            }
        }
    }
}
