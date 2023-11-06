@Library('tws-ci-library') _
commonPipeline {
    imageName = 'prod_video_backend_spring_service'
    manifestRepo = 'Trans-World-Style/Trans-World-Style-Infra.git'
    manifestDir = 'k8s/product/backend/spring-video'
    manifestFile = 'spring-video-deploy.yaml'
    manifestBranch = 'main'
    CONFIG_MAP_NAME = 'spring-video-configmap'
    CONFIG_FILE_NAME = 'application-video-prod.properties'
    CONFIG_MAP_MOUNT_PATH = 'src/main/resources'
}