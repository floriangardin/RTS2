import requests
import json
import numpy

class MyEncoder(json.JSONEncoder):
    def default(self, obj):
        if isinstance(obj, numpy.integer):
            return int(obj)
        elif isinstance(obj, numpy.floating):
            return float(obj)
        elif isinstance(obj, numpy.ndarray):
            return obj.tolist()
        else:
            return super(MyEncoder, self).default(obj)

def get():
    """
    Get the state of plateau
    :return:
    """
    res = json.loads(requests.get("http://localhost:8000/get").text)
    res['plateau'] = {int(idx) : val for idx,val in res['plateau'].items()}
    res['teams'] = {int(idx): val for idx, val in res['teams'].items()}
    return res
def post(data):
    """
    Send action to game
    :param data:
    :return:
    """
    print("posting :", json.dumps([data], cls=MyEncoder) )
    return requests.post("http://localhost:8000/post", data=json.dumps([data], cls=MyEncoder))
