import requests
import sys

file_name = sys.argv[1]
scan_type = ''

if file_name == 'gitleaks.json':
    scan_type = 'Gitleaks Scan'
elif file_name == 'semgrep.json':
    scan_type = 'Semgrep JSON Report'


headers = {
        'Authorization': 'Token b6042dd7fe6395dc49140f24f99a5a9c65f1e9de'
        }

url = 'http://defectdojo.ru/api/v2/import-scan/'

data = {
        'active': True,
        'verified': True,
        'scan_type': scan_type,
        'minimum_serevity': 'Low',
        'engagement': 5
        }

files = {
        'file': open(file_name, 'rb')
        }

response = requests.post(url, headers=headers, data=data, files=files)

if response.status_code == 201:
    print('Scan results imported successfully')
else:
    print(f'Failed to import scan results: {response.content}')




#How to run >
# pip3 install requests
# python3 upload_reports.py gitleaks.json
# python3 upload_reports.py semgrep.json
