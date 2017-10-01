
from .constant import *
"""
4) Actions for output in game, part of the policy
"""

class Action:
    def __init__(self, verb=None, adverb=None, target=None, produce=None):
        self.verb = verb
        self.adverb = adverb
        self.target = target
        self.produce = produce
        self.name = self.get_name()

    def __call__(self, objet):
        pass

    def can_do(self, objet):
        return True

    def get_name(self):
        """
        Very important : name of the action to be accounted in Q
        :return:
        """
        result = ''
        if self.verb is not None:
            result += str(self.verb)
        if self.adverb is not None:
            result += str(self.adverb)
        if self.target is not None:
            result += str(self.target)
        if self.produce is not None:
            result += str(self.produce)
        return result

    def to_dict(self):
        result = {}
        if self.subject is not None:
            result['subject'] = self.subject
        if self.verb is not None:
            result['verb'] = self.verb
        if self.target is not None:
            result['target'] = self.target
        if self.produce is not None:
            result['produce'] = self.produce
        return result

class ActionAttackNearest(Action):
    def __call__(self, objet):
        pass

class NoAction(Action):
    def __init__(self):
        self.final_target = -1
        self.final_verb = STOP

    def can_do(self, objet):
        return True
    def __call__(self, objet):
        return

class ActionProduce(Action):
    def __init__(self, to_produce):
        super().__init__(verb=PRODUCE, target=to_produce)

    def can_do(self, objet):
        return  objet.teams[objet.team]['food'] > 50 and ( objet.teams[objet.team]['maxPop']- objet.teams[objet.team]['pop']) > 0

    def __call__(self, objet):
        self.subject = objet.id
        self.produce = self.target
        self.target = None
        pass

def find_one(plateau, objet, team):
    return find_all(plateau, objet, team)[0]
def find_all(objet, name, team):
    return [val for key, val in objet.plateau.items() if val['team'] == team and val['name'] == name]
def find_all_not_team(objet, name, team):
    return [val for key, val in objet.plateau.items() if val['team'] != team and val['name'] == name]

def find_all_enemy(objet, name, team):
    return [val for key, val in objet.plateau.items() if val['team'] != team and val['team'] != 0 and val['name'] == name]

class ActionAttackNearestFromHeadquarters(Action):
    def __init__(self, target):
        super().__init__(verb=ATTACK, adverb=NEARESTFROMHEADQUARTERS, target=target)

    def can_do(self, objet):
        return (((objet.teams[objet.team]['maxPop']-objet.teams[objet.team]['pop']) > 0) \
               or (self.target in [MINE, SPEARMAN, CROSSBOWMAN, INQUISITOR]) ) and len(find_all_not_team(objet, self.target, objet.team))>0

    def __call__(self, objet):
        # Find nearest neutral or enemy from HQ
        hq = find_one(objet, HEADQUARTERS, objet.team)
        x = hq['x']
        y = hq['y']
        # On recherche les target ennemies ou neutres
        filtered = find_all_not_team(objet, self.target, objet.team)
        filtered = [(i['id'], (i['x']-x)**2 + (i['y']-y)**2) for i in filtered]
        filtered = sorted(filtered, key=lambda x: x[1])
        self.subject = objet.idx
        self.target = filtered[0][0]

class ActionAttackNearest(Action):
    def __init__(self, target):
        super().__init__(verb=ATTACK, adverb=NEAREST, target=target)

    def can_do(self, objet):
        return (((objet.teams[objet.team]['maxPop']-objet.teams[objet.team]['pop']) > 0 ) \
               or self.target in [MINE, SPEARMAN, CROSSBOWMAN, INQUISITOR])\
                  and len(find_all_not_team(objet, self.target, objet.team)) > 0

    def __call__(self, objet):
        # Find nearest neutral or enemy from HQ
        x = objet.objet['x']
        y = objet.objet['y']
        # On recherche les target ennemies ou neutres
        filtered = find_all_not_team(objet, self.target, objet.team)
        filtered = [(i['id'], (i['x']-x)**2 + (i['y']-y)**2) for i in filtered]
        filtered = sorted(filtered, key=lambda x: x[1])
        self.subject = objet.id
        self.target = filtered[0][0]

class ActionAttackNearestEnemy(Action):
    def __init__(self, target):
        super().__init__(verb=ATTACK, adverb=NEARESTENEMY, target=target)

    def can_do(self, objet):
        return len(find_all_enemy(objet, self.target, objet.team)) > 0

    def __call__(self, objet):
        # Find nearest neutral or enemy from HQ
        x = objet.objet['x']
        y = objet.objet['y']
        # On recherche les target ennemies ou neutres
        filtered = find_all_enemy(objet, self.target, objet.team)
        filtered = [(i['id'], (i['x'] - x) ** 2 + (i['y'] - y) ** 2) for i in filtered]
        filtered = sorted(filtered, key=lambda x: x[1])
        self.subject = objet.id
        self.target = filtered[0][0]
