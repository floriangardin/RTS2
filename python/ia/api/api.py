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

def get(team=2):
    """
    Get the state of plateau
    :return:
    """
    port = str(8000+team)
    res = json.loads(requests.get("http://localhost:"+port+"/get").text)
    res['plateau'] = {int(idx) : val for idx,val in res['plateau'].items()}
    res['teams'] = {int(idx): val for idx, val in res['teams'].items()}
    return res
def post(data, team=2):
    """
    Send action to game
    :param data:
    :return:
    """
    port = str(8000 + team)
    print("posting :", json.dumps([data], cls=MyEncoder) )
    return requests.post("http://localhost:"+port+"/post", data=json.dumps([data], cls=MyEncoder))
