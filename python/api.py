from api import get, post


def think(data):
    return [{
            "subject": 77,
            "action": "attack",
            "target": 69
      }]

plateau = get()
plan = think(plateau)
post(plan)
