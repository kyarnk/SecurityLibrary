package org.security.scanners

class KICSScanner extends BaseScanner {
    KICSScanner(script, env, config) {
        super(script, env, config)
    }

    def run() {
        log "Starting KICS scan..."
        
        try {
            // Install KICS
            script.sh """
                curl -L https://github.com/Checkmarx/kics/releases/download/v1.7.0/kics_1.7.0_linux_x64.tar.gz -o kics.tar.gz
                tar -xzf kics.tar.gz
                rm kics.tar.gz
                export PATH=\$PATH:\$PWD/kics
            """
            
            def scanCommand = ""
            if (config.containerName) {
                // Scanning inside container
                scanCommand = """
                    docker exec ${config.containerName} kics scan \
                        --path="${config.scanPath ?: '.'}" \
                        --output-path="." \
                        --output-name="kics-results.json" \
                        --report-formats="json"
                """
            } else {
                // Scanning local files
                scanCommand = """
                    kics scan \
                        --path="${config.scanPath ?: '.'}" \
                        --output-path="." \
                        --output-name="kics-results.json" \
                        --report-formats="json"
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
            
            log "KICS scan completed"
            return findings
        } catch (Exception e) {
            log "Error during KICS scan: ${e.message}"
            throw e
        }
    }
    
    private def parseResults(String jsonResults) {
        def findings = []
        def parsedResults = script.readJSON text: jsonResults
        
        parsedResults.queries.each { query ->
            query.files.each { file ->
                findings << [
                    title: query.queryName,
                    description: query.description,
                    severity: mapSeverity(query.severity),
                    file_path: file.fileName,
                    line: file.line,
                    mitigation: query.descriptionId,
                    references: [query.queryId]
                ]
            }
        }
        
        return findings
    }
    
    private def mapSeverity(String severity) {
        switch (severity?.toLowerCase()) {
            case 'high': return 'Critical'
            case 'medium': return 'High'
            case 'low': return 'Medium'
            case 'info': return 'Low'
            default: return 'Info'
        }
    }
} 