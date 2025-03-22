import org.security.scanners.GitleaksScanner

def call(Map config = [:]) {
    def scanner = new GitleaksScanner(this, env, config)
    return scanner.run()
} 