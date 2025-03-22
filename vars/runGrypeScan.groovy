import org.security.scanners.GrypeScanner

def call(Map config = [:]) {
    def scanner = new GrypeScanner(this, env, config)
    return scanner.run()
} 