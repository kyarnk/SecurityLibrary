package org.security.scanners

class NucleiScanner extends BaseScanner {
    NucleiScanner(script, env, config) {
        super(script, env, config)
    }

    def run() {
        log "Starting Nuclei scan..."
        
        try {
            // Install Nuclei
            script.sh """
                go install -v github.com/projectdiscovery/nuclei/v2/cmd/nuclei@latest
                export PATH=\$PATH:\$HOME/go/bin
            """
            
            // Run scan
            def results = script.sh(
                script: """
                    nuclei -u ${config.targetUrl} \
                        -json \
                        -o nuclei-results.json
                """,
                returnStdout: true
            ).trim()
            
            // Parse results
            def findings = parseResults(results)
            
            // Publish results
            publishToDefectDojo(findings)
            
            log "Nuclei scan completed"
            return findings
        } catch (Exception e) {
            log "Error during Nuclei scan: ${e.message}"
            throw e
        }
    }
    
    private def parseResults(String jsonResults) {
        def findings = []
        def parsedResults = script.readJSON text: jsonResults
        
        parsedResults.each { result ->
            findings << [
                title: result.info.name,
                description: result.info.description,
                severity: mapSeverity(result.info.severity),
                file_path: result.matched,
                mitigation: result.info.remediation,
                references: result.info.reference
            ]
        }
        
        return findings
    }
    
    private def mapSeverity(String severity) {
        switch (severity?.toLowerCase()) {
            case 'critical': return 'Critical'
            case 'high': return 'High'
            case 'medium': return 'Medium'
            case 'low': return 'Low'
            default: return 'Info'
        }
    }
} 