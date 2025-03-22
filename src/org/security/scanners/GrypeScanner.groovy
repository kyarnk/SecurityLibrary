package org.security.scanners

class GrypeScanner extends BaseScanner {
    GrypeScanner(script, env, config) {
        super(script, env, config)
    }

    def run() {
        log "Starting Grype scan..."
        
        try {
            // Install Grype
            script.sh """
                curl -sSfL https://raw.githubusercontent.com/anchore/grype/main/install.sh | sh -s -- -b /usr/local/bin
            """
            
            // Run scan
            def results = script.sh(
                script: """
                    grype ${config.imageName} -o json > grype-results.json
                """,
                returnStdout: true
            ).trim()
            
            // Parse results
            def findings = parseResults(results)
            
            // Publish results
            publishToDefectDojo(findings)
            
            log "Grype scan completed"
            return findings
        } catch (Exception e) {
            log "Error during Grype scan: ${e.message}"
            throw e
        }
    }
    
    private def parseResults(String jsonResults) {
        def findings = []
        def parsedResults = script.readJSON text: jsonResults
        
        parsedResults.matches.each { match ->
            findings << [
                title: "Vulnerability: ${match.vulnerability.id}",
                description: """
                    Package: ${match.artifact.name}
                    Version: ${match.artifact.version}
                    Type: ${match.artifact.type}
                    Vulnerability: ${match.vulnerability.description}
                """.stripIndent(),
                severity: mapSeverity(match.vulnerability.severity),
                file_path: match.artifact.locations?.collect { it.path }?.join(', ') ?: 'N/A',
                mitigation: match.vulnerability.fix?.versions?.join(', ') ?: 'Update to latest version',
                references: match.vulnerability.urls
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