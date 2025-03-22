import org.security.scanners.KICSScanner

def call(Map config = [:]) {
    def scanner = new KICSScanner(this, env, config)
    return scanner.run()
} 