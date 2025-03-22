package org.security.scanners

class GitleaksScanner extends BaseScanner {
    GitleaksScanner(script, env, config) {
        super(script, env, config)
    }

    def run() {
        log "Starting Gitleaks scan..."
        
        try {
            // Install Gitleaks
            script.sh """
                curl -L https://github.com/zricethezav/gitleaks/releases/download/v8.18.1/gitleaks_8.18.1_linux_x64.tar.gz -o gitleaks.tar.gz
                tar -xzf gitleaks.tar.gz
                rm gitleaks.tar.gz
                chmod +x gitleaks
                export PATH=\$PATH:\$PWD
            """
            
            // Run scan
            def results = script.sh(
                script: """
                    gitleaks detect \
                        --source=${config.scanPath ?: '.'} \
                        --report-format=json \
                        --report-path=gitleaks-results.json
                """,
                returnStdout: true
            ).trim()
            
            // Parse results
            def findings = parseResults(results)
            
            // Publish results
            publishToDefectDojo(findings)
            
            log "Gitleaks scan completed"
            return findings
        } catch (Exception e) {
            log "Error during Gitleaks scan: ${e.message}"
            throw e
        }
    }
    
    private def parseResults(String jsonResults) {
        def findings = []
        def parsedResults = script.readJSON text: jsonResults
        
        parsedResults.each { result ->
            findings << [
                title: "Secret found: ${result.rule}",
                description: "Secret found in file ${result.file} at line ${result.startLine}",
                severity: 'Critical',
                file_path: result.file,
                line: result.startLine,
                mitigation: "Remove or revoke the exposed secret",
                references: ["Commit: ${result.commit}"]
            ]
        }
        
        return findings
    }
} 