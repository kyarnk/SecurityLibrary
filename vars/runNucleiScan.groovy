import org.security.scanners.NucleiScanner

def call(Map config = [:]) {
    def scanner = new NucleiScanner(this, env, config)
    return scanner.run()
} 