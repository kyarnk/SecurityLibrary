# import requests
# import sys

# file_name = sys.argv[1]
# scan_type = ''

# if file_name == 'gitleaks.json':
#     scan_type = 'Gitleaks Scan'
# elif file_name == 'semgrep.json':
#     scan_type = 'Semgrep JSON Report'


# headers = {
#         'Authorization': 'Token b6042dd7fe6395dc49140f24f99a5a9c65f1e9de'
#         'username': 'kyarnkey'
#         'password': ''
#         }

# url = 'http://51.250.92.214:8080/api/key-v2/import-scan/'

# data = {
#         'active': True,
#         'verified': True,
#         'scan_type': scan_type,
#         'minimum_serevity': 'Low',
#         'engagement': 5
#         }

# files = {
#         'file': open(file_name, 'rb')
#         }

# response = requests.post(url, headers=headers, data=data, files=files)

# if response.status_code == 201:
#     print('Scan results imported successfully')
# else:
#     print(f'Failed to import scan results: {response.content}')




# #How to run >
# # pip3 install requests
# # python3 upload_reports.py gitleaks.json
# # python3 upload_reports.py semgrep.json

import argparse
import requests

parser = argparse.ArgumentParser()
parser.add_argument('--token', required=True)
parser.add_argument('--host', required=True)
parser.add_argument('--file', required=True)
parser.add_argument('--engagement', required=True)
parser.add_argument('--scan_type', required=True)
parser.add_argument('--scan_date', required=True)
args = parser.parse_args()

headers = {
    'Authorization': f'Token {args.token}'
}
files = {
    'file': open(args.file, 'rb')
}
data = {
    'engagement': args.engagement,
    'scan_type': args.scan_type,
    'scan_date': args.scan_date,
    'active': 'true',
    'verified': 'true',
    'close_old_findings': 'true'
}

response = requests.post(f"{args.host}/api/v2/import-scan/", headers=headers, files=files, data=data)

print(response.status_code)
print(response.text)
