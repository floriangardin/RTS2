
"""
3) Features used in States , they are boolean features only
"""
from .utils import *

class Feature:
    def __call__(self):
        return self.state

    def __add__(self, other):
        if isinstance(other, Feature):
            return self()+other
        else:
            return self()+other()
    def __radd__(self, other):
        return self.__add__(other)
    def __repr__(self):
        return "{}, {}".format(self.__class__.__name__, self.state)

class FoodBetween(Feature):
    def __init__(self, objet, low, up):
        self.state = '1' if low <= objet.teams[objet.team]['food'] < up else '0'
class PopBetween(Feature):
    def __init__(self, objet, low, up):
        self.state = '1' if low <= objet.teams[objet.team]['pop'] < up else '0'

class MaxPopBetween(Feature):
    def __init__(self, objet, low, up):
        self.state = '1' if low <= objet.teams[objet.team]['maxPop'] < up else '0'
class PopRemainingBetween(Feature):
    def __init__(self, objet, low, up):
        self.state = '1' if low <= objet.teams[objet.team]['maxPop']-objet.teams[objet.team]['pop'] < up else '0'

class NbMyObjetsBetween(Feature):
    def __init__(self, objet, to_count, low, up):
        quantity = len([val for key, val in objet.plateau.items() if val['name'] == to_count and val['team'] == objet.team])
        self.state = '1' if low <= quantity < up else '0'

class NbObjetsBetween(Feature):
    def __init__(self, objet, to_count, team, low, up):
        quantity = len([val for key, val in objet.plateau if val['name'] == to_count and val['team'] == team])
        self.state = '1' if low <= quantity < up else '0'

class EnemyInRangeAtLeast(Feature):
    def __init__(self, objet, to_count, low):
        me = objet.objet
        enemies = [val for key, val in objet.plateau if val['name'] == to_count and val['team'] != objet['team'] and val['team'] != 0]
        # Filter in range
        enemies = [e for e in enemies if distance(e, me) < e['range']]
        self.state = '1' if len(enemies)> low else '0'


class IsIdle(Feature):
    def __init__(self, objet):

        has_target = objet.objet['target'] > -1
        if has_target:
            if objet.objet['target'] in objet.plateau:
                if objet.plateau[objet.objet['target']]['team'] != objet.team:
                    self.state = '0'
                    return
        self.state = '1'