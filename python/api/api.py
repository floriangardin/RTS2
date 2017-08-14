import requests
import json

def get():
    """
    Get the state of plateau
    :return:
    """
    return json.loads(requests.get("http://localhost:8000/get").text)

def post(data):
    """
    Send action to game
    :param data:
    :return:
    """
    return requests.post("http://localhost:8000/post", data=json.dumps(data))
