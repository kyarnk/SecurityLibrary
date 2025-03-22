package org.security.scanners

class SyftScanner extends BaseScanner {
    SyftScanner(script, env, config) {
        super(script, env, config)
    }

    def run() {
        log "Starting Syft scan..."
        
        try {
            // Install Syft
            script.sh """
                curl -sSfL https://raw.githubusercontent.com/anchore/syft/main/install.sh | sh -s -- -b /usr/local/bin
            """
            
            // Run scan
            def results = script.sh(
                script: """
                    syft ${config.imageName} -o json > syft-results.json
                """,
                returnStdout: true
            ).trim()
            
            // Parse results
            def findings = parseResults(results)
            
            // Publish results
            publishToDefectDojo(findings)
            
            log "Syft scan completed"
            return findings
        } catch (Exception e) {
            log "Error during Syft scan: ${e.message}"
            throw e
        }
    }
    
    private def parseResults(String jsonResults) {
        def findings = []
        def parsedResults = script.readJSON text: jsonResults
        
        parsedResults.artifacts.each { artifact ->
            findings << [
                title: "Package: ${artifact.name}",
                description: "Version: ${artifact.version}\nType: ${artifact.type}",
                severity: 'Info',
                file_path: artifact.locations?.collect { it.path }?.join(', ') ?: 'N/A',
                mitigation: "Review package version and update if necessary",
                references: ["PURL: ${artifact.purl}"]
            ]
        }
        
        return findings
    }
} 