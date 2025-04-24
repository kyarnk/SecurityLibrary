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
//             echo "🌐 Getting product ID for: ${productName}"

//             ENCODED_PRODUCT_NAME=\$(echo "${productName}" | jq -sRr @uri)
//             PRODUCT_JSON=\$(curl -s -X GET "\${DEFECTDOJO_URL}/api/v2/products/?name=\$ENCODED_PRODUCT_NAME" \\
//                 -H "Authorization: Token \$DD_API_KEY")

//             echo "🔽 Raw product lookup result: \$PRODUCT_JSON"

//             PRODUCT_ID=\$(echo "\$PRODUCT_JSON" | jq -r '.results[0].id // empty')

//             if [ -z "\$PRODUCT_ID" ]; then
//                 echo "📦 Product not found, creating: ${productName}"
//                 PRODUCT_ID=\$(curl -s -X POST "\${DEFECTDOJO_URL}/api/v2/products/" \\
//                     -H "Authorization: Token \$DD_API_KEY" \\
//                     -H "Content-Type: application/json" \\
//                     -d '{"name": "${productName}", "description": "Created by Jenkins"}' | jq -r '.id')
//             fi

//             echo "🔍 Getting engagement ID for: ${engagementName}"

//             ENCODED_ENGAGEMENT_NAME=\$(echo "${engagementName}" | jq -sRr @uri)
//             ENGAGEMENT_JSON=\$(curl -s -X GET "\${DEFECTDOJO_URL}/api/v2/engagements/?name=\$ENCODED_ENGAGEMENT_NAME&product=\$PRODUCT_ID" \\
//                 -H "Authorization: Token \$DD_API_KEY")

//             ENGAGEMENT_ID=\$(echo "\$ENGAGEMENT_JSON" | jq -r '.results[0].id // empty')

//             if [ -z "\$ENGAGEMENT_ID" ]; then
//                 echo "🛠️ Creating engagement: ${engagementName}"
//                 ENGAGEMENT_ID=\$(curl -s -X POST "\${DEFECTDOJO_URL}/api/v2/engagements/" \\
//                     -H "Authorization: Token \$DD_API_KEY" \\
//                     -H "Content-Type: application/json" \\
//                     -d '{"name": "${engagementName}", "product": '\$PRODUCT_ID', "target_start": "2024-01-01", "target_end": "2024-12-31", "engagement_type": "CI/CD"}' | jq -r '.id')
//             fi

//             echo "📤 Uploading report to DefectDojo..."
//             curl -X POST "\${DEFECTDOJO_URL}/api/v2/import-scan/" \\
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

//     if (!fileExists(reportPath)) {
//         error "❌ Report file not found at path: ${reportPath}"
//     }

//     withCredentials([string(credentialsId: 'defect-dojo_api_key', variable: 'DD_API_KEY')]) {
//         sh """
//             set -e
//             cd "${homeDir}"

//             echo "🌐 Getting product ID for: ${productName}"
//             ENCODED_PRODUCT_NAME=\$(echo "${productName}" | jq -sRr @uri)
//             PRODUCT_JSON=\$(curl -s -X GET "\${DEFECTDOJO_URL}/api/v2/products/?name=\$ENCODED_PRODUCT_NAME" \\
//                 -H "Authorization: Token \$DD_API_KEY")

//             echo "🔽 Raw product lookup result: \$PRODUCT_JSON"
//             PRODUCT_ID=\$(echo "\$PRODUCT_JSON" | jq -r '.results[0].id // empty')

//             if [ -z "\$PRODUCT_ID" ]; then
//                 echo "📦 Product not found, creating: ${productName}"
//                 PRODUCT_ID=\$(curl -s -X POST "\${DEFECTDOJO_URL}/api/v2/products/" \\
//                     -H "Authorization: Token \$DD_API_KEY" \\
//                     -H "Content-Type: application/json" \\
//                     -d '{"name": "${productName}", "description": "Created by Jenkins"}' | jq -r '.id')
//             fi

//             echo "🔍 Getting engagement ID for: ${engagementName}"
//             ENCODED_ENGAGEMENT_NAME=\$(echo "${engagementName}" | jq -sRr @uri)
//             ENGAGEMENT_JSON=\$(curl -s -X GET "\${DEFECTDOJO_URL}/api/v2/engagements/?name=\$ENCODED_ENGAGEMENT_NAME&product=\$PRODUCT_ID" \\
//                 -H "Authorization: Token \$DD_API_KEY")

//             ENGAGEMENT_ID=\$(echo "\$ENGAGEMENT_JSON" | jq -r '.results[0].id // empty')

//             if [ -z "\$ENGAGEMENT_ID" ]; then
//                 echo "🛠️ Creating engagement: ${engagementName}"
//                 ENGAGEMENT_ID=\$(curl -s -X POST "\${DEFECTDOJO_URL}/api/v2/engagements/" \\
//                     -H "Authorization: Token \$DD_API_KEY" \\
//                     -H "Content-Type: application/json" \\
//                     -d '{"name": "${engagementName}", "product": '\$PRODUCT_ID', "target_start": "2024-01-01", "target_end": "2024-12-31", "engagement_type": "CI/CD"}' | jq -r '.id')
//             fi

//             echo "📤 Uploading report to DefectDojo..."
//             curl -X POST "\${DEFECTDOJO_URL}/api/v2/import-scan/" \\
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
    def defectDojoUrl  = env.DEFECTDOJO_URL

    if (!reportName || !scanType || !productName || !engagementName) {
        error "❌ Missing required parameters: reportName, scanType, productName, or engagementName"
    }

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

            echo "🌐 Getting or creating product: ${productName}"
            PRODUCT_JSON=\$(curl -s -G "${defectDojoUrl}/api/v2/products/" \\
                --data-urlencode "name=${productName}" \\
                -H "Authorization: Token \$DD_API_KEY")

            echo "🔽 Product lookup result: \$PRODUCT_JSON"
            PRODUCT_ID=\$(echo "\$PRODUCT_JSON" | jq -r '.results[0].id // empty')

            if [ -z "\$PRODUCT_ID" ]; then
                echo "📦 Product not found. Creating new one..."
                PRODUCT_ID=\$(curl -s -X POST "${defectDojoUrl}/api/v2/products/" \\
                    -H "Authorization: Token \$DD_API_KEY" \\
                    -H "Content-Type: application/json" \\
                    -d '{"name": "${productName}", "description": "Created by Jenkins"}' | jq -r '.id')
            fi

            echo "🔍 Getting or creating engagement: ${engagementName}"
            ENGAGEMENT_JSON=\$(curl -s -G "${defectDojoUrl}/api/v2/engagements/" \\
                --data-urlencode "name=${engagementName}" \\
                --data-urlencode "product=\$PRODUCT_ID" \\
                -H "Authorization: Token \$DD_API_KEY")

            ENGAGEMENT_ID=\$(echo "\$ENGAGEMENT_JSON" | jq -r '.results[0].id // empty')

            if [ -z "\$ENGAGEMENT_ID" ]; then
                echo "🛠️ Creating new engagement..."
                ENGAGEMENT_ID=\$(curl -s -X POST "${defectDojoUrl}/api/v2/engagements/" \\
                    -H "Authorization: Token \$DD_API_KEY" \\
                    -H "Content-Type: application/json" \\
                    -d '{
                        "name": "${engagementName}",
                        "product": '\$PRODUCT_ID',
                        "target_start": "2024-01-01",
                        "target_end": "2024-12-31",
                        "engagement_type": "CI/CD"
                    }' | jq -r '.id')
            fi

            echo "📤 Uploading report to DefectDojo..."
            curl -s -X POST "${defectDojoUrl}/api/v2/import-scan/" \\
                -H "Authorization: Token \$DD_API_KEY" \\
                -F "file=@${reportPath}" \\
                -F "engagement=\$ENGAGEMENT_ID" \\
                -F "scan_type=${scanType}" \\
                -F "verified=true" \\
                -F "active=true" \\
                -F "auto_create_context=true"

            echo "✅ Report successfully uploaded to DefectDojo."
        """
    }
}