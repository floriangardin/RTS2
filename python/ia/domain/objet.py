
"""
2) Represent Objets, their states and associated actions
"""
from .constant import *
from .action import *
from .feature import *

class Objet:
    def init(self, plateau, players, idx):
        self.plateau = plateau
        self.teams = players
        self.idx = idx
        self.id = idx
        self.objet = self.plateau[idx]
        self.team = self.objet['team']
        self.state_name = []
        self.states_ = self.compute_states()
        self.actions_ = self.compute_actions()

    def compute_states(self):
        pass
    def compute_actions(self):
        pass

    @property
    def state(self):
        return str.join('', [i() for i in self.states_])

    @property
    def actions(self):
        return self.actions_

    @property
    def valid_actions(self):
        return [action for action in self.actions_ if action.can_do(self)]

    @property
    def name(self):
        return self.objet['name']

class Spearman(Objet):

    def compute_states(self):
        return [
            IsIdle(self),
            FoodBetween(self, 0, 50),
            FoodBetween(self, 50, 100),
            FoodBetween(self, 100, 200),
            FoodBetween(self, 200, 200000),
            NbMyObjetsBetween(self, MILL, 0, 1),
            NbMyObjetsBetween(self, BARRACKS, 0, 1),
            NbMyObjetsBetween(self, MINE, 0, 1),
            NbMyObjetsBetween(self, TOWER, 0, 1)
        ]
    def compute_actions(self):
        return [
            ActionAttackNearestFromHeadquarters(BARRACKS),
            ActionAttackNearestFromHeadquarters(MILL),
            ActionAttackNearestFromHeadquarters(MINE),
            ActionAttackNearestEnemy(TOWER),
            ActionAttackNearestEnemy(HEADQUARTERS),
            ActionAttackNearestEnemy(BARRACKS),
            ActionAttackNearestEnemy(MINE),
            ActionAttackNearestEnemy(MILL)
        ]

class Crossbowman(Objet):
    def compute_states(self):
        return [
            IsIdle(self),
            FoodBetween(self, 0, 50),
            FoodBetween(self, 50, 100),
            FoodBetween(self, 100, 200),
            FoodBetween(self, 200, 200000),
            NbMyObjetsBetween(self, MILL, 0, 1),
            NbMyObjetsBetween(self, BARRACKS, 0, 1),
            NbMyObjetsBetween(self, MINE, 0, 1),
            NbMyObjetsBetween(self, TOWER, 0, 1)
        ]

    def compute_actions(self):
        return [
            ActionAttackNearestEnemy(SPEARMAN),
            ActionAttackNearestEnemy(CROSSBOWMAN)
        ]

class Barracks(Objet):
    def compute_states(self):
        return [
            IsIdle(self),
            FoodBetween(self, 0, 50),
            FoodBetween(self, 50, 100),
            FoodBetween(self, 100, 200),
            FoodBetween(self, 200, 200000),
            NbMyObjetsBetween(self, SPEARMAN, 0, 1),
            NbMyObjetsBetween(self, CROSSBOWMAN, 0, 1),
            NbMyObjetsBetween(self, SPEARMAN, 1, 2),
            NbMyObjetsBetween(self, CROSSBOWMAN, 1, 2),
            NbMyObjetsBetween(self, TOWER, 0, 1)
        ]
    def compute_actions(self):
        return [
            ActionProduce(SPEARMAN),
            ActionProduce(CROSSBOWMAN)
        ]
class DefaultObjet(Objet):
    def compute_states(self):
        return []
