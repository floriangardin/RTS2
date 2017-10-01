"""
1) Handle states and inputs
"""
from ..api.api import *
from .objet import *

class StateManager:

    def __iter__(self):
        for obj in self.objets:
            yield obj, obj.name, obj.state, obj.actions, obj.valid_actions

    def __init__(self, team=2):
        self.team = team
        self.objets = []
        self.has_won = False
        self.has_lost = False


    def check_victory(self, players):
        self.has_lost = players[self.team]['hasLost'] == 1
        for key in players.keys():
            if players[key]['hasLost'] == 1 and key != self.team:
                self.has_won = True
        return self.has_won, self.has_lost

    def update(self, data=None):
        if data is None:
            data = get(self.team)
        # Create state for each character
        plateau = data['plateau']
        players = data['teams']
        self.players = players
        self.check_victory(players)
        # Create objects and filter by spearman, crossbowman and barracks
        self.objets = [ObjetManager(plateau, players, idx) for idx in plateau.keys() if self.team == plateau[idx]['team'] and plateau[idx]['name'] in [SPEARMAN, CROSSBOWMAN, BARRACKS]]

