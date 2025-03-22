package org.security.scanners

abstract class BaseScanner {
    protected def script
    protected def env
    protected def config
    
    BaseScanner(script, env, config) {
        this.script = script
        this.env = env
        this.config = config
    }
    
    abstract def run()
    
    protected def log(String message) {
        script.echo "[${this.class.simpleName}] ${message}"
    }
    
    protected def publishToDefectDojo(findings) {
        if (!findings) {
            log "No findings to publish"
            return
        }
        
        try {
            script.withCredentials([script.string(credentialsId: 'defectdojo-api-key', variable: 'DEFECTDOJO_API_KEY')]) {
                def response = script.sh(
                    script: """
                        curl -X POST "${config.defectDojoUrl}/api/v2/findings/" \
                            -H "Authorization: Token ${DEFECTDOJO_API_KEY}" \
                            -H "Content-Type: application/json" \
                            -d '${script.groovy.json.JsonOutput.toJson(findings)}'
                    """,
                    returnStdout: true
                ).trim()
                
                log "Successfully published ${findings.size()} findings to DefectDojo"
            }
        } catch (Exception e) {
            log "Failed to publish findings to DefectDojo: ${e.message}"
            throw e
        }
    }
} 