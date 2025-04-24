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
//             curl -s -H \"Authorization: Token ${apiKey}\" \"${dojoUrl}/api/v2/products/?name=${productName}\" |
//             jq '.results[0].id // empty'
//         """,
//         returnStdout: true
//     ).trim()

//     if (!productId) {
//         echo "Product '${productName}' not found. Creating..."
//         productId = sh(
//             script: """
//                 curl -s -X POST -H \"Authorization: Token ${apiKey}\" -H \"Content-Type: application/json\" \\
//                     -d '{ "name": "${productName}", "prod_type": 1 }' \\
//                     ${dojoUrl}/api/v2/products/ | jq '.id'
//             """,
//             returnStdout: true
//         ).trim()
//     }

//     // 2. Find or create engagement
//     def engagementId = sh(
//         script: """
//             curl -s -H \"Authorization: Token ${apiKey}\" \"${dojoUrl}/api/v2/engagements/?name=${engagementName}&product=${productId}\" |
//             jq '.results[0].id // empty'
//         """,
//         returnStdout: true
//     ).trim()

//     if (!engagementId || !engagementId.isInteger()) {
//         echo "Engagement '${engagementName}' not found. Creating..."
//         engagementId = sh(
//             script: """
//                 curl -s -X POST -H \"Authorization: Token ${apiKey}\" -H \"Content-Type: application/json\" \\
//                     -d '{ "name": "${engagementName}", "product": ${productId}, "status": "In Progress", "target_start": "2024-01-01", "target_end": "2025-01-01" }' \\
//                     ${dojoUrl}/api/v2/engagements/ | jq '.id'
//             """,
//             returnStdout: true
//         ).trim()
//     }

//     if (!engagementId.isInteger()) {
//         error "Invalid engagement ID: ${engagementId}"
//     }

//     // 3. Upload scan
//     echo "Uploading ${scanType} report to DefectDojo (product: ${productName}, engagement: ${engagementName})..."

//     sh """
//         curl -X POST ${dojoUrl}/api/v2/import-scan/ \
//             -H \"Authorization: Token ${apiKey}\" \
//             -F \"file=@${reportPath}\" \
//             -F \"scan_type=${scanType}\" \
//             -F \"engagement=${engagementId}\" \
//             -F \"verified=true\" \
//             -F \"minimum_severity=Low\"
//     """

//     echo "Upload completed."
// }



// def call(Map config = [:]) {
//     def reportName = config.reportName ?: error("Missing parameter: reportName")
//     def scanType   = config.scanType ?: error("Missing parameter: scanType")
//     def productName = config.productName ?: error("Missing parameter: productName")
//     def engagementName = config.engagementName ?: "Initial Security Scan"
//     def homeDir = config.homeDir ?: "."

//     withCredentials([string(credentialsId: 'defect-dojo_api_key', variable: 'DD_API_KEY')]) {
//         sh """
//         set -e

//         mkdir -p ${homeDir}/reports/defectdojo-temp

//         # Get or create product
//         PRODUCT_ID=\$(curl -s -H "Authorization: Token \$DD_API_KEY" \
//             "${env.DEFECTDOJO_URL}/api/v2/products/?name=${productName}" | jq '.results[0].id')

//         if [ "\$PRODUCT_ID" = "null" ] || [ -z "\$PRODUCT_ID" ]; then
//             echo "Product '${productName}' not found. Creating..."
//             PRODUCT_ID=\$(curl -s -X POST -H "Authorization: Token \$DD_API_KEY" -H "Content-Type: application/json" \
//                 -d '{ "name": "${productName}", "prod_type": 1 }' \
//                 "${env.DEFECTDOJO_URL}/api/v2/products/" | jq '.id')
//         fi

//         echo "Product ID: \$PRODUCT_ID"

//         # Get or create engagement
//         ENGAGEMENT_ID=\$(curl -s -H "Authorization: Token \$DD_API_KEY" \
//             "${env.DEFECTDOJO_URL}/api/v2/engagements/?name=${engagementName}&product=\$PRODUCT_ID" | jq '.results[0].id')

//         if [ "\$ENGAGEMENT_ID" = "null" ] || [ -z "\$ENGAGEMENT_ID" ]; then
//             echo "Engagement '${engagementName}' not found. Creating..."
//             ENGAGEMENT_ID=\$(curl -s -X POST -H "Authorization: Token \$DD_API_KEY" -H "Content-Type: application/json" \
//                 -d '{ "name": "${engagementName}", "product": '\$PRODUCT_ID', "status": "In Progress", "target_start": "2024-01-01", "target_end": "2025-01-01" }' \
//                 "${env.DEFECTDOJO_URL}/api/v2/engagements/" | jq '.id')
//         fi

//         echo "Engagement ID: \$ENGAGEMENT_ID"

//         # Upload report
//         curl -X POST "${env.DEFECTDOJO_URL}/api/v2/import-scan/" \
//             -H "Authorization: Token \$DD_API_KEY" \
//             -F "file=@${homeDir}/reports/${reportName}" \
//             -F "engagement=\$ENGAGEMENT_ID" \
//             -F "scan_type=${scanType}" \
//             -F "verified=true" \
//             -F "active=true" \
//             -F "auto_create_context=true"
//         """
//     }
// }


def call(Map config = [:]) {
    def reportName     = config.reportName
    def scanType       = config.scanType
    def productName    = config.productName
    def engagementName = config.engagementName
    def homeDir        = config.homeDir ?: env.WORKSPACE

    def reportPath = "${homeDir}/reports/${reportName}"
    if (!fileExists(reportPath)) {
        reportPath = "${homeDir}/${reportName}"
    }

    if (!fileExists(reportPath)) {
        error "❌ Report file not found at path: ${reportPath}"
    }

    withCredentials([string(credentialsId: 'defect-dojo_api_key', variable: 'DD_API_KEY')]) {
        sh """
            set -e

            cd "${homeDir}"

            PRODUCT_ID=\$(curl -s -X GET "${env.DEFECTDOJO_URL}/api/v2/products/?name=${productName}" \\
                -H "Authorization: Token \$DD_API_KEY" | jq -r '.results[0].id')

            if [ "\$PRODUCT_ID" = "null" ] || [ -z "\$PRODUCT_ID" ]; then
                echo "Creating product: ${productName}"
                PRODUCT_ID=\$(curl -s -X POST "${env.DEFECTDOJO_URL}/api/v2/products/" \\
                    -H "Authorization: Token \$DD_API_KEY" \\
                    -H "Content-Type: application/json" \\
                    -d '{"name": "${productName}", "description": "Created by Jenkins"}' | jq -r '.id')
            fi

            ENGAGEMENT_ID=\$(curl -s -X GET "${env.DEFECTDOJO_URL}/api/v2/engagements/?name=${engagementName}&product=\$PRODUCT_ID" \\
                -H "Authorization: Token \$DD_API_KEY" | jq -r '.results[0].id')

            if [ "\$ENGAGEMENT_ID" = "null" ] || [ -z "\$ENGAGEMENT_ID" ]; then
                echo "Creating engagement: ${engagementName}"
                ENGAGEMENT_ID=\$(curl -s -X POST "${env.DEFECTDOJO_URL}/api/v2/engagements/" \\
                    -H "Authorization: Token \$DD_API_KEY" \\
                    -H "Content-Type: application/json" \\
                    -d '{"name": "${engagementName}", "product": \$PRODUCT_ID, "target_start": "2024-01-01", "target_end": "2024-12-31", "engagement_type": "CI/CD"}' | jq -r '.id')
            fi

            curl -X POST "${env.DEFECTDOJO_URL}/api/v2/import-scan/" \\
                -H "Authorization: Token \$DD_API_KEY" \\
                -F "file=@${reportPath}" \\
                -F "engagement=\$ENGAGEMENT_ID" \\
                -F "scan_type=${scanType}" \\
                -F "verified=true" \\
                -F "active=true" \\
                -F "auto_create_context=true"
        """
    }
}
