import org.security.scanners.DefectDojoAPI

def initDefectDojo(Map config = [:]) {
    def api = new DefectDojoAPI(
        this,
        config.url ?: env.DEFECTDOJO_URL,
        config.apiKey ?: env.DEFECTDOJO_API_KEY
    )
    return api
}

def createEngagement(Map config) {
    def required = ['productId', 'name']
    required.each { param ->
        if (!config.containsKey(param)) {
            error "Параметр '${param}' обязателен для createEngagement"
        }
    }
    
    def api = initDefectDojo(config)
    def branch = config.branch ?: env.BRANCH_NAME ?: 'main'
    
    return api.createEngagement(
        config.productId,
        config.name,
        branch
    )
}

def uploadScanResults(Map config) {
    def required = ['engagementId', 'reportFile', 'scanType']
    required.each { param ->
        if (!config.containsKey(param)) {
            error "Параметр '${param}' обязателен для uploadScanResults"
        }
    }
    
    def api = initDefectDojo(config)
    return api.uploadReport(
        config.engagementId,
        config.reportFile,
        config.scanType
    )
}

def getProduct(Map config) {
    if (!config.productId) {
        error "Параметр 'productId' обязателен для getProduct"
    }
    
    def api = initDefectDojo(config)
    return api.getProduct(config.productId)
}

def createProduct(Map config) {
    def required = ['name', 'description']
    required.each { param ->
        if (!config.containsKey(param)) {
            error "Параметр '${param}' обязателен для createProduct"
        }
    }
    
    def api = initDefectDojo(config)
    return api.createProduct(
        config.name,
        config.description
    )
}

def searchFindings(Map config) {
    if (!config.engagementId) {
        error "Параметр 'engagementId' обязателен для searchFindings"
    }
    
    def api = initDefectDojo(config)
    return api.searchFindings(config.engagementId)
} 