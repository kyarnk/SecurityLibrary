// def call(
//     String reportFile = '',
//     String scanType = '',
//     int engagementId = 0,
//     String dojoUrl = '',
//     String apiKey = '',
//     String homeDir = ''
// ) {
//     // Проверка обязательных параметров
//     if (!reportFile || !scanType || !engagementId) {
//         error "Missing required parameters: reportFile, scanType, and engagementId must be provided!"
//     }

//     // Используем dojoUrl из ENV, если не передан
//     dojoUrl = dojoUrl ?: env.DEFECTDOJO_URL
//     if (!dojoUrl) {
//         error "DefectDojo URL must be provided or set via DEFECTDOJO_URL environment variable"
//     }

//     // Используем API key из ENV, если не передан
//     apiKey = apiKey ?: env.DEFECTDOJO_API_KEY
//     if (!apiKey) {
//         error "DefectDojo API Key must be provided or set via DEFECTDOJO_API_KEY environment variable"
//     }

//     // Используем homeDir из ENV, если не передан
//     homeDir = homeDir ?: env.HOME_DIR
//     if (!homeDir) {
//         error "HOME_DIR must be specified either as a parameter or in environment"
//     }

//     def reportPath = "${homeDir}/reports/${reportFile}"

//     echo "Uploading ${reportFile} to DefectDojo at ${dojoUrl}..."

//     sh """
//         curl -X POST ${dojoUrl}/api/v2/import-scan/ \
//         -H "Authorization: Token ${apiKey}" \
//         -F "file=@${reportPath}" \
//         -F "scan_type=${scanType}" \
//         -F "engagement=${engagementId}" \
//         -F "verified=true" \
//         -F "minimum_severity=Low"
//     """

//     echo "Upload completed: ${reportPath}"
// }


// def call(String reportPath, String scanType = 'Semgrep', String fileName = 'report.json') {
//     def dojoUrl     = env.DEFECTDOJO_URL
//     def apiKey      = env.DEFECTDOJO_API_KEY
//     def productName = env.DEFECTDOJO_PRODUCT_NAME
//     def engagementName = env.DEFECTDOJO_ENGAGEMENT_NAME ?: 'Automated Security Scan'

//     def authHeader = "Authorization: Token ${apiKey}"
//     def contentHeader = "Content-Type: application/json"

//     // 1. Найти или создать продукт
//     def productId = sh(script: """
//         curl -s -H '${authHeader}' '${dojoUrl}/api/v2/products/?name=${productName}' | jq '.results[0].id'
//     """, returnStdout: true).trim()

//     if (!productId || productId == "null") {
//         productId = sh(script: """
//             curl -s -X POST -H '${authHeader}' -H '${contentHeader}' '${dojoUrl}/api/v2/products/' \
//                 -d '{ "name": "${productName}" }' | jq '.id'
//         """, returnStdout: true).trim()
//     }

//     // 2. Найти или создать engagement
//     def engagementId = sh(script: """
//         curl -s -H '${authHeader}' '${dojoUrl}/api/v2/engagements/?name=${engagementName}&product=${productId}' | jq '.results[0].id'
//     """, returnStdout: true).trim()

//     if (!engagementId || engagementId == "null") {
//         def today = sh(script: "date +%Y-%m-%d", returnStdout: true).trim()
//         engagementId = sh(script: """
//             curl -s -X POST -H '${authHeader}' -H '${contentHeader}' '${dojoUrl}/api/v2/engagements/' \
//             -d '{
//                 "name": "${engagementName}",
//                 "product": ${productId},
//                 "target_start": "${today}",
//                 "target_end": "${today}",
//                 "status": "In Progress"
//             }' | jq '.id'
//         """, returnStdout: true).trim()
//     }

//     // 3. Загрузить отчёт
//     sh """
//         curl -s -X POST '${dojoUrl}/api/v2/import-scan/' \
//             -H '${authHeader}' \
//             -F "engagement=${engagementId}" \
//             -F "scan_type=${scanType}" \
//             -F "file=@${reportPath};filename=${fileName}" \
//             -F "active=true" \
//             -F "verified=true" \
//             -F "close_old_findings=true"
//     """

//     echo "Report uploaded to DefectDojo -> Product: ${productName}, Engagement: ${engagementName}"
// }


def call(
    String reportFile = '',
    String scanType = '',
    String productName = '',
    String engagementName = '',
    String homeDir = ''
) {
    if (!reportFile || !scanType || !productName || !engagementName) {
        error "Missing required parameters: reportFile, scanType, productName, or engagementName"
    }

    def dojoUrl = env.DEFECTDOJO_URL
    def apiKey  = env.DEFECTDOJO_API_KEY
    homeDir     = homeDir ?: env.HOME_DIR

    if (!dojoUrl || !apiKey || !homeDir) {
        error "DEFECTDOJO_URL, DEFECTDOJO_API_KEY and HOME_DIR must be defined in environment"
    }

    def reportPath = "${homeDir}/reports/${reportFile}"

    // 1. Find or create product
    def productId = sh(
        script: """
            curl -s -H "Authorization: Token ${apiKey}" ${dojoUrl}/api/v2/products/?name=${productName} |
            jq '.results[0].id // empty'
        """,
        returnStdout: true
    ).trim()

    if (!productId) {
        echo "Product '${productName}' not found. Creating..."
        productId = sh(
            script: """
                curl -s -X POST -H "Authorization: Token ${apiKey}" -H "Content-Type: application/json" \\
                    -d '{ "name": "${productName}", "prod_type": 1 }' \\
                    ${dojoUrl}/api/v2/products/ | jq '.id'
            """,
            returnStdout: true
        ).trim()
    }

    // 2. Find or create engagement
    def engagementId = sh(
        script: """
            curl -s -H "Authorization: Token ${apiKey}" ${dojoUrl}/api/v2/engagements/?name=${engagementName}&product=${productId} |
            jq '.results[0].id // empty'
        """,
        returnStdout: true
    ).trim()

    if (!engagementId) {
        echo "Engagement '${engagementName}' not found. Creating..."
        engagementId = sh(
            script: """
                curl -s -X POST -H "Authorization: Token ${apiKey}" -H "Content-Type: application/json" \\
                    -d '{ "name": "${engagementName}", "product": ${productId}, "status": "In Progress", "target_start": "2024-01-01", "target_end": "2025-01-01" }' \\
                    ${dojoUrl}/api/v2/engagements/ | jq '.id'
            """,
            returnStdout: true
        ).trim()
    }

    // 3. Upload scan
    echo "Uploading ${scanType} report to DefectDojo (product: ${productName}, engagement: ${engagementName})..."

    sh """
        curl -X POST ${dojoUrl}/api/v2/import-scan/ \\
            -H "Authorization: Token ${apiKey}" \\
            -F "file=@${reportPath}" \\
            -F "scan_type=${scanType}" \\
            -F "engagement=${engagementId}" \\
            -F "verified=true" \\
            -F "minimum_severity=Low"
    """

    echo "Upload completed."
}
