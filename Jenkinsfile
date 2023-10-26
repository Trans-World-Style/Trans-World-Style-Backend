@Library('tws-ci-library') _
commonPipeline {
    imageName = 'prod_member_backend_spring_service'
    manifestRepo = 'Trans-World-Style/Trans-World-Style-Infra.git'
    manifestDir = 'k8s/product/backend/spring-member'
    manifestFile = 'spring-member-deploy.yaml'
    manifestBranch = 'main'
    CONFIG_MAP_NAME = 'spring-member-configmap'
    CONFIG_FILE_NAME = 'application-member-prod.properties'
    CONFIG_MAP_MOUNT_PATH = 'src/main/resources'
}
