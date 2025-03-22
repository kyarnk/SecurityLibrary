import org.security.scanners.SemgrepScanner

def call(Map config = [:]) {
    def scanner = new SemgrepScanner(this, env, config)
    return scanner.run()
} 
 