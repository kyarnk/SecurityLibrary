package org.security.scanners

class SemgrepScanner extends BaseScanner {
    SemgrepScanner(script, env, config) {
        super(script, env, config)
    }

    def run() {
        log "Starting Semgrep scan..."
        
        try {
            def scanCommand = ""
            if (config.containerName) {
                // Scanning inside container
                scanCommand = """
                    docker exec ${config.containerName} semgrep scan --json \
                        --config=auto \
                        --output=results.json \
                        ${config.scanPath ?: '.'}
                """
            } else {
                // Scanning local files
                scanCommand = """
                    semgrep scan --json \
                        --config=auto \
                        --output=results.json \
                        ${config.scanPath ?: '.'}
                """
            }
            
            // Run scan
            def results = script.sh(
                script: scanCommand,
                returnStdout: true
            ).trim()
            
            // Parse results
            def findings = parseResults(results)
            
            // Publish results
            publishToDefectDojo(findings)
            
            log "Semgrep scan completed"
            return findings
        } catch (Exception e) {
            log "Error during Semgrep scan: ${e.message}"
            throw e
        }
    }
    
    private def parseResults(String jsonResults) {
        def findings = []
        def parsedResults = script.readJSON text: jsonResults
        
        parsedResults.results.each { result ->
            findings << [
                title: result.check_id,
                description: result.extra.message,
                severity: mapSeverity(result.extra.severity),
                file_path: result.path,
                line: result.start.line,
                mitigation: result.extra.fix,
                references: result.extra.references
            ]
        }
        
        return findings
    }
    
    private def mapSeverity(String severity) {
        switch (severity?.toLowerCase()) {
            case 'error': return 'Critical'
            case 'warning': return 'High'
            case 'info': return 'Medium'
            default: return 'Low'
        }
    }
} 