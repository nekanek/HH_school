# run with $ python dropbox_auth.py
# access http://localhost:4333/

from flask import Flask, abort, request, session, url_for
import base64
import os
import requests
import urllib

search_for = '.pdf' # change this var to search for specific string in your dropbox folders 

app = Flask(__name__)
app.secret_key = os.urandom(18)

APP_KEY = 'wqrmyglvuplbzhk';
APP_SECRET = 'orgv7l2trmk9f4w';

@app.route('/')
def index():
    csrf_token = base64.urlsafe_b64encode(os.urandom(20))
    session['csrf_token'] = csrf_token
    headers = {
        'client_id': APP_KEY,
        'redirect_uri': url_for('callback', _external=True),
        'response_type': 'code',
        'state': csrf_token
    }
    redirect_url = 'https://www.dropbox.com/1/oauth2/authorize?%s' % urllib.urlencode(headers)
    index_html = """
    <!DOCTYPE html>
    <html>
        <body>
            <h2>Oauth2 authentication in Dropbox</h2>
            <h3>(homework task for Head Hunter developers school 2014)</h3>
            <p>Please, follow the link to authenticate access to your Dropbox account.</p>
            <a href="%s">Authentication with dropbox</a>
        </body>
    </html>
    """ % redirect_url

    return index_html

@app.route('/callback')
def callback():
    error = request.args.get('error', '')
    if error:
        return """
        Error: %s <br/>
        Please, try again: <a href="%s">Return to index page</a>
        """ % (error, url_for('index', _external=True))
    if request.args['state'] != session.pop('csrf_token'):
        abort(403)
    code = request.args['code']
    token = get_token(code)
    name = requests.get('https://api.dropbox.com/1/account/info', headers={'Authorization': 'Bearer %s' % token}).json()

    files = requests.get('https://api.dropbox.com/1/search/auto/?query=%s' % search_for, headers={'Authorization': 'Bearer %s' % token}).json()
    out="<br/><br/>"
    if len(files) == 0:
        out += "Sorry, no such files found"
    else:
        for i in files:
            out += i['path'] + '<br/>'

    index_html = """
    <!DOCTYPE html>
    <html>
        <body>
            <h2>Oauth2 authentication example in Dropbox</h2>
            <h3>(homework task for Head Hunter developers school 2014)</h3>
            <p>Authenticated as %s.
            <p>List of files and folders containing "%s":%s</p>
        </body>
    </html>
    """ % (name['display_name'], search_for, out)
    return index_html

def get_token(code):
    client_auth = (APP_KEY, APP_SECRET)
    post_data = {"grant_type": "authorization_code",
                 "code": code,
                 "redirect_uri": url_for('callback', _external=True)}
    response = requests.post("https://api.dropbox.com/1/oauth2/token",
                             auth=client_auth,
                             data=post_data).json()
    
    return response['access_token']

if __name__=='__main__':
    app.run(port=4333, debug=True)