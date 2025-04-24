// def call(
//     String reportFile = '',
//     String scanType = '',
//     String productName = '',
//     String engagementName = '',
//     String homeDir = ''
// ) {
//     if (!reportFile || !scanType || !productName || !engagementName) {
//         error "Missing required parameters: reportFile, scanType, productName, or engagementName"
//     }

//     def dojoUrl = env.DEFECTDOJO_URL
//     def apiKey  = env.DEFECTDOJO_API_KEY
//     homeDir     = homeDir ?: env.HOME_DIR

//     if (!dojoUrl || !apiKey || !homeDir) {
//         error "DEFECTDOJO_URL, DEFECTDOJO_API_KEY and HOME_DIR must be defined in environment"
//     }

//     def reportPath = "${homeDir}/reports/${reportFile}"

//     // 1. Find or create product
//     def productId = sh(
//         script: """
//             curl -s -H "Authorization: Token ${apiKey}" ${dojoUrl}/api/v2/products/?name=${productName} |
//             jq '.results[0].id // empty'
//         """,
//         returnStdout: true
//     ).trim()

//     if (!productId) {
//         echo "Product '${productName}' not found. Creating..."
//         productId = sh(
//             script: """
//                 curl -s -X POST -H "Authorization: Token ${apiKey}" -H "Content-Type: application/json" \\
//                     -d '{ "name": "${productName}", "prod_type": 1 }' \\
//                     ${dojoUrl}/api/v2/products/ | jq '.id'
//             """,
//             returnStdout: true
//         ).trim()
//     }

//     // 2. Find or create engagement
//     def engagementId = sh(
//         script: """
//             curl -s -H "Authorization: Token ${apiKey}" ${dojoUrl}/api/v2/engagements/?name=${engagementName}&product=${productId} |
//             jq '.results[0].id // empty'
//         """,
//         returnStdout: true
//     ).trim()

//     if (!engagementId) {
//         echo "Engagement '${engagementName}' not found. Creating..."
//         engagementId = sh(
//             script: """
//                 curl -s -X POST -H "Authorization: Token ${apiKey}" -H "Content-Type: application/json" \\
//                     -d '{ "name": "${engagementName}", "product": ${productId}, "status": "In Progress", "target_start": "2024-01-01", "target_end": "2025-01-01" }' \\
//                     ${dojoUrl}/api/v2/engagements/ | jq '.id'
//             """,
//             returnStdout: true
//         ).trim()
//     }

//     // 3. Upload scan
//     echo "Uploading ${scanType} report to DefectDojo (product: ${productName}, engagement: ${engagementName})..."

//     sh """
//         curl -X POST ${dojoUrl}/api/v2/import-scan/ \\
//             -H "Authorization: Token ${apiKey}" \\
//             -F "file=@${reportPath}" \\
//             -F "scan_type=${scanType}" \\
//             -F "engagement=${engagementId}" \\
//             -F "verified=true" \\
//             -F "minimum_severity=Low"
//     """

//     echo "Upload completed."
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
            curl -s -H \"Authorization: Token ${apiKey}\" \"${dojoUrl}/api/v2/products/?name=${productName}\" |
            jq '.results[0].id // empty'
        """,
        returnStdout: true
    ).trim()

    if (!productId) {
        echo "Product '${productName}' not found. Creating..."
        productId = sh(
            script: """
                curl -s -X POST -H \"Authorization: Token ${apiKey}\" -H \"Content-Type: application/json\" \\
                    -d '{ "name": "${productName}", "prod_type": 1 }' \\
                    ${dojoUrl}/api/v2/products/ | jq '.id'
            """,
            returnStdout: true
        ).trim()
    }

    // 2. Find or create engagement
    def engagementId = sh(
        script: """
            curl -s -H \"Authorization: Token ${apiKey}\" \"${dojoUrl}/api/v2/engagements/?name=${engagementName}&product=${productId}\" |
            jq '.results[0].id // empty'
        """,
        returnStdout: true
    ).trim()

    if (!engagementId || !engagementId.isInteger()) {
        echo "Engagement '${engagementName}' not found. Creating..."
        engagementId = sh(
            script: """
                curl -s -X POST -H \"Authorization: Token ${apiKey}\" -H \"Content-Type: application/json\" \\
                    -d '{ "name": "${engagementName}", "product": ${productId}, "status": "In Progress", "target_start": "2024-01-01", "target_end": "2025-01-01" }' \\
                    ${dojoUrl}/api/v2/engagements/ | jq '.id'
            """,
            returnStdout: true
        ).trim()
    }

    if (!engagementId.isInteger()) {
        error "Invalid engagement ID: ${engagementId}"
    }

    // 3. Upload scan
    echo "Uploading ${scanType} report to DefectDojo (product: ${productName}, engagement: ${engagementName})..."

    sh """
        curl -X POST ${dojoUrl}/api/v2/import-scan/ \
            -H \"Authorization: Token ${apiKey}\" \
            -F \"file=@${reportPath}\" \
            -F \"scan_type=${scanType}\" \
            -F \"engagement=${engagementId}\" \
            -F \"verified=true\" \
            -F \"minimum_severity=Low\"
    """

    echo "Upload completed."
}