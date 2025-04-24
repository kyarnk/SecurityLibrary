// def call(Map config = [:]) {
//     def reportName     = config.reportName
//     def scanType       = config.scanType
//     def productName    = config.productName
//     def engagementName = config.engagementName
//     def homeDir        = config.homeDir ?: env.WORKSPACE

//     def reportPath = "${homeDir}/reports/${reportName}"
//     if (!fileExists(reportPath)) {
//         reportPath = "${homeDir}/${reportName}"
//     }

//     if (!fileExists(reportPath)) {
//         error "❌ Report file not found at path: ${reportPath}"
//     }

//     withCredentials([string(credentialsId: 'defect-dojo_api_key', variable: 'DD_API_KEY')]) {
//         sh """
//             set -e

//             cd "${homeDir}"

//             PRODUCT_ID=\$(curl -s -X GET "${env.DEFECTDOJO_URL}/api/v2/products/?name=${productName}" \\
//                 -H "Authorization: Token \$DD_API_KEY" | jq -r '.results[0].id')

//             if [ "\$PRODUCT_ID" = "null" ] || [ -z "\$PRODUCT_ID" ]; then
//                 echo "Creating product: ${productName}"
//                 PRODUCT_ID=\$(curl -s -X POST "${env.DEFECTDOJO_URL}/api/v2/products/" \\
//                     -H "Authorization: Token \$DD_API_KEY" \\
//                     -H "Content-Type: application/json" \\
//                     -d '{"name": "${productName}", "description": "Created by Jenkins"}' | jq -r '.id')
//             fi

//             ENGAGEMENT_ID=\$(curl -s -X GET "${env.DEFECTDOJO_URL}/api/v2/engagements/?name=${engagementName}&product=\$PRODUCT_ID" \\
//                 -H "Authorization: Token \$DD_API_KEY" | jq -r '.results[0].id')

//             if [ "\$ENGAGEMENT_ID" = "null" ] || [ -z "\$ENGAGEMENT_ID" ]; then
//                 echo "Creating engagement: ${engagementName}"
//                 ENGAGEMENT_ID=\$(curl -s -X POST "${env.DEFECTDOJO_URL}/api/v2/engagements/" \\
//                     -H "Authorization: Token \$DD_API_KEY" \\
//                     -H "Content-Type: application/json" \\
//                     -d '{"name": "${engagementName}", "product": \$PRODUCT_ID, "target_start": "2024-01-01", "target_end": "2024-12-31", "engagement_type": "CI/CD"}' | jq -r '.id')
//             fi

//             curl -X POST "${env.DEFECTDOJO_URL}/api/v2/import-scan/" \\
//                 -H "Authorization: Token \$DD_API_KEY" \\
//                 -F "file=@${reportPath}" \\
//                 -F "engagement=\$ENGAGEMENT_ID" \\
//                 -F "scan_type=${scanType}" \\
//                 -F "verified=true" \\
//                 -F "active=true" \\
//                 -F "auto_create_context=true"
//         """
//     }
// }


// def call(Map config = [:]) {
//     def reportName     = config.reportName
//     def scanType       = config.scanType
//     def productName    = config.productName
//     def engagementName = config.engagementName
//     def homeDir        = config.homeDir ?: env.WORKSPACE

//     def reportPath = "${homeDir}/reports/${reportName}"
//     if (!fileExists(reportPath)) {
//         reportPath = "${homeDir}/${reportName}"
//     }

//     echo "🔍 Looking for report at: ${reportPath}"
//     if (!fileExists(reportPath)) {
//         error "❌ Report file not found at path: ${reportPath}"
//     }

//     withCredentials([string(credentialsId: 'defect-dojo_api_key', variable: 'DD_API_KEY')]) {
//         sh """
//             set -e
//             echo "📁 Changing directory to: ${homeDir}"
//             cd "${homeDir}"

//             echo "🌐 Getting product ID for: ${productName}"
//             PRODUCT_JSON=\$(curl -s -X GET "${env.DEFECTDOJO_URL}/api/v2/products/?name=${productName}" \\
//                 -H "Authorization: Token \$DD_API_KEY")
//             echo "🧾 Product JSON: \$PRODUCT_JSON"
//             PRODUCT_ID=\$(echo "\$PRODUCT_JSON" | jq -r '.results[0].id')

//             if [ "\$PRODUCT_ID" = "null" ] || [ -z "\$PRODUCT_ID" ]; then
//                 echo "📦 Product not found. Creating product: ${productName}"
//                 CREATE_PRODUCT_RESPONSE=\$(curl -s -X POST "${env.DEFECTDOJO_URL}/api/v2/products/" \\
//                     -H "Authorization: Token \$DD_API_KEY" \\
//                     -H "Content-Type: application/json" \\
//                     -d '{"name": "${productName}", "description": "Created by Jenkins"}')
//                 echo "📦 Product creation response: \$CREATE_PRODUCT_RESPONSE"
//                 PRODUCT_ID=\$(echo "\$CREATE_PRODUCT_RESPONSE" | jq -r '.id')
//             fi

//             echo "🔍 Getting engagement ID for: ${engagementName} in product \$PRODUCT_ID"
//             ENGAGEMENT_JSON=\$(curl -s -X GET "${env.DEFECTDOJO_URL}/api/v2/engagements/?name=${engagementName}&product=\$PRODUCT_ID" \\
//                 -H "Authorization: Token \$DD_API_KEY")
//             echo "📃 Engagement JSON: \$ENGAGEMENT_JSON"
//             ENGAGEMENT_ID=\$(echo "\$ENGAGEMENT_JSON" | jq -r '.results[0].id')

//             if [ "\$ENGAGEMENT_ID" = "null" ] || [ -z "\$ENGAGEMENT_ID" ]; then
//                 echo "📁 Engagement not found. Creating engagement: ${engagementName}"
//                 CREATE_ENGAGEMENT_RESPONSE=\$(curl -s -X POST "${env.DEFECTDOJO_URL}/api/v2/engagements/" \\
//                     -H "Authorization: Token \$DD_API_KEY" \\
//                     -H "Content-Type: application/json" \\
//                     -d '{"name": "${engagementName}", "product": \$PRODUCT_ID, "target_start": "2024-01-01", "target_end": "2024-12-31", "engagement_type": "CI/CD"}')
//                 echo "📁 Engagement creation response: \$CREATE_ENGAGEMENT_RESPONSE"
//                 ENGAGEMENT_ID=\$(echo "\$CREATE_ENGAGEMENT_RESPONSE" | jq -r '.id')
//             fi

//             echo "📤 Uploading scan report to engagement \$ENGAGEMENT_ID"
//             curl -v -X POST "${env.DEFECTDOJO_URL}/api/v2/import-scan/" \\
//                 -H "Authorization: Token \$DD_API_KEY" \\
//                 -F "file=@${reportPath}" \\
//                 -F "engagement=\$ENGAGEMENT_ID" \\
//                 -F "scan_type=${scanType}" \\
//                 -F "verified=true" \\
//                 -F "active=true" \\
//                 -F "auto_create_context=true"
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
            echo "🌐 Getting product ID for: ${productName}"

            ENCODED_PRODUCT_NAME=\$(echo "${productName}" | jq -sRr @uri)
            PRODUCT_JSON=\$(curl -s -X GET "\${DEFECTDOJO_URL}/api/v2/products/?name=\$ENCODED_PRODUCT_NAME" \\
                -H "Authorization: Token \$DD_API_KEY")

            echo "🔽 Raw product lookup result: \$PRODUCT_JSON"

            PRODUCT_ID=\$(echo "\$PRODUCT_JSON" | jq -r '.results[0].id // empty')

            if [ -z "\$PRODUCT_ID" ]; then
                echo "📦 Product not found, creating: ${productName}"
                PRODUCT_ID=\$(curl -s -X POST "\${DEFECTDOJO_URL}/api/v2/products/" \\
                    -H "Authorization: Token \$DD_API_KEY" \\
                    -H "Content-Type: application/json" \\
                    -d '{"name": "${productName}", "description": "Created by Jenkins"}' | jq -r '.id')
            fi

            echo "🔍 Getting engagement ID for: ${engagementName}"

            ENCODED_ENGAGEMENT_NAME=\$(echo "${engagementName}" | jq -sRr @uri)
            ENGAGEMENT_JSON=\$(curl -s -X GET "\${DEFECTDOJO_URL}/api/v2/engagements/?name=\$ENCODED_ENGAGEMENT_NAME&product=\$PRODUCT_ID" \\
                -H "Authorization: Token \$DD_API_KEY")

            ENGAGEMENT_ID=\$(echo "\$ENGAGEMENT_JSON" | jq -r '.results[0].id // empty')

            if [ -z "\$ENGAGEMENT_ID" ]; then
                echo "🛠️ Creating engagement: ${engagementName}"
                ENGAGEMENT_ID=\$(curl -s -X POST "\${DEFECTDOJO_URL}/api/v2/engagements/" \\
                    -H "Authorization: Token \$DD_API_KEY" \\
                    -H "Content-Type: application/json" \\
                    -d '{"name": "${engagementName}", "product": '\$PRODUCT_ID', "target_start": "2024-01-01", "target_end": "2024-12-31", "engagement_type": "CI/CD"}' | jq -r '.id')
            fi

            echo "📤 Uploading report to DefectDojo..."
            curl -X POST "\${DEFECTDOJO_URL}/api/v2/import-scan/" \\
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
