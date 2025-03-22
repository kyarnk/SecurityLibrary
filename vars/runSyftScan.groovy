import org.security.scanners.SyftScanner

def call(Map config = [:]) {
    def scanner = new SyftScanner(this, env, config)
    return scanner.run()
} 