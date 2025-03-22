import org.security.scanners.OWASPZAPScanner

def call(Map config = [:]) {
    def scanner = new OWASPZAPScanner(this, env, config)
    return scanner.run()
} 