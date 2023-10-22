@Library('tws-ci-library') _
commonPipeline {
    imageName = 'prod_gateway_backend_spring_service'
    manifestRepo = 'Trans-World-Style/Trans-World-Style-Infra.git'
    manifestDir = 'k8s/product/backend/spring-gateway'
    manifestFile = 'spring-gateway-deploy.yaml'
    manifestBranch = 'main'
    CONFIG_MAP_NAME = 'spring-gateway-configmap'
    CONFIG_FILE_NAME = 'application-gateway-prod.properties'
    CONFIG_MAP_MOUNT_PATH = 'src/main/resources'
    SECRET_NAME = 'spring-gateway-secret'
    SECRET_FILE_NAME = 'application-gateway-prod-secret.properties'
    SECRET_MOUNT_PATH = 'src/main/resources'
}