package org.security.scanners

class DefectDojoAPI implements Serializable {
    def script
    String apiUrl
    String apiKey
    
    DefectDojoAPI(script, apiUrl, apiKey) {
        this.script = script
        this.apiUrl = apiUrl.endsWith('/') ? apiUrl : apiUrl + '/'
        this.apiKey = apiKey
    }
    
    def createEngagement(productId, name, branch) {
        def today = new Date().format('yyyy-MM-dd')
        def payload = [
            name: name,
            product: productId,
            target_start: today,
            target_end: today,
            branch_tag: branch,
            active: true,
            status: "In Progress"
        ]
        
        return makeRequest('POST', 'engagements/', payload)
    }
    
    def uploadReport(engagementId, reportFile, reportType) {
        def payload = [
            engagement: engagementId,
            scan_type: reportType,
            file: reportFile,
            active: true,
            verified: false
        ]
        
        return makeRequest('POST', 'import-scan/', payload)
    }
    
    private def makeRequest(method, endpoint, payload) {
        def response
        
        script.withCredentials([script.string(credentialsId: 'defectdojo-api-key', variable: 'API_KEY')]) {
            def curl = """
                curl -X ${method} "${apiUrl}${endpoint}" \\
                -H "Authorization: Token ${apiKey}" \\
                -H "Content-Type: application/json" \\
                -d '${groovy.json.JsonOutput.toJson(payload)}' \\
                --fail
            """
            
            try {
                response = script.sh(script: curl, returnStdout: true).trim()
                return script.readJSON(text: response)
            } catch (Exception e) {
                script.error "Ошибка при запросе к DefectDojo API: ${e.message}"
                if (response) {
                    script.error "Ответ API: ${response}"
                }
                throw e
            }
        }
    }
    
    def getProduct(productId) {
        return makeRequest('GET', "products/${productId}/", [:])
    }
    
    def createProduct(name, description) {
        def payload = [
            name: name,
            description: description,
            prod_type: 1  // Web Service
        ]
        
        return makeRequest('POST', 'products/', payload)
    }
    
    def searchFindings(engagementId) {
        return makeRequest('GET', "findings/?engagement=${engagementId}", [:])
    }
} 
