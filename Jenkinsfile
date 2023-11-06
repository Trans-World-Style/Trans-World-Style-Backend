@Library('tws-ci-library') _
commonPipeline {
    imageName = 'prod_email_backend_spring_service'
    manifestRepo = 'Trans-World-Style/Trans-World-Style-Infra.git'
    manifestDir = 'k8s/product/backend/spring-email'
    manifestFile = 'spring-email-deploy.yaml'
    manifestBranch = 'main'
    CONFIG_MAP_NAME = 'spring-email-configmap'
    CONFIG_FILE_NAME = 'application-email-prod.properties'
    CONFIG_MAP_MOUNT_PATH = 'src/main/resources'
}
