package org.security.scanners

class OWASPZAPScanner extends BaseScanner {
    OWASPZAPScanner(script, env, config) {
        super(script, env, config)
    }

    def run() {
        log "Starting OWASP ZAP scan..."
        
        try {
            // Install ZAP
            script.sh """
                wget https://github.com/zaproxy/zaproxy/releases/download/v2.14.0/ZAP_2.14.0_Linux.tar.gz
                tar -xf ZAP_2.14.0_Linux.tar.gz
                rm ZAP_2.14.0_Linux.tar.gz
                mv ZAP_2.14.0 zap
                export PATH=\$PATH:\$PWD/zap
            """
            
            // Run scan
            def results = script.sh(
                script: """
                    zap-cli quick-scan \
                        --self-contained \
                        --spider \
                        --ajax-spider \
                        --format json \
                        --output zap-results.json \
                        ${config.targetUrl}
                """,
                returnStdout: true
            ).trim()
            
            // Parse results
            def findings = parseResults(results)
            
            // Publish results
            publishToDefectDojo(findings)
            
            log "OWASP ZAP scan completed"
            return findings
        } catch (Exception e) {
            log "Error during OWASP ZAP scan: ${e.message}"
            throw e
        }
    }
    
    private def parseResults(String jsonResults) {
        def findings = []
        def parsedResults = script.readJSON text: jsonResults
        
        parsedResults.site.each { site ->
            site.alerts.each { alert ->
                findings << [
                    title: alert.name,
                    description: alert.description,
                    severity: mapSeverity(alert.riskcode),
                    file_path: alert.url,
                    mitigation: alert.solution,
                    references: [alert.reference]
                ]
            }
        }
        
        return findings
    }
    
    private def mapSeverity(int riskCode) {
        switch (riskCode) {
            case 3: return 'Critical'
            case 2: return 'High'
            case 1: return 'Medium'
            case 0: return 'Low'
            default: return 'Info'
        }
    }
} 