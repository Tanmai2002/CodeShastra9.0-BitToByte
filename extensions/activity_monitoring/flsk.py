from flask import Flask, request
import json
import time
from flask_cors import CORS
from better_profanity import profanity    #import better_profanity module
import requests
from bs4 import BeautifulSoup

# def check(url):
#     # url = "https://www.google.com"
#     try:
#         response = requests.get(url)
#         html = response.text
#         soup = BeautifulSoup(html, "html.parser")
#         text = soup.get_text()
#         # text = "Hello,how are you bullshit?"  
#         print(profanity.censor(text))             
#         if(profanity.contains_profanity(text)):  
#             return True
#         else:
#             return False
#     except Exception as e:
#         print(e)
#         return True
    
    
app = Flask(__name__)
CORS(app)

@app.route('/log_url', methods=['POST'])
def log_url():
    # Get the URL and current timestamp from the request data
    data = request.get_json()
    url = data['url']
    timestamp = time.ctime()
    # safe=check(url)
    # print(safe)
    print(url,timestamp)
    # Log the URL and timestamp in a JSON file
    f=open('history.json', 'a')
    log_entry = {'url': url, 'timestamp': timestamp}
    json.dump(log_entry, f)
    f.write('\n')
    f.close()

    # Return a response to the extension
    return 'OK'

if __name__ == '__main__':
    app.run()
