"""
Test des actions ...
"""


"""
Test each feature,
pass a plateau state with expected result of feature
"""

import unittest
import sys
sys.path.append('../')
from ia.domain.action import *
from ia.domain.state import StateManager
from ia.domain.constant import *

state = StateManager(team=1)

class TestActions(unittest.TestCase):
    def test_ActionAttackNearestFromHeadquarters(self):
        data = {
            'plateau': {
                1: {
                    'team': 1,
                    'id' :1,
                    'name': HEADQUARTERS,
                    'x': 0,
                    'y': 0
                },
                2: {
                    'team': 0,
                    'id': 2,
                    'name': BARRACKS,
                    'x': 1,
                    'y': 1
                },
                3: {
                    'team': 0,
                    'id' :3,
                    'name': BARRACKS,
                    'x': 20,
                    'y': 10
                },
                4: {
                    'team': 1,
                    'id' : 4,
                    'name': SPEARMAN,
                    'x': 30,
                    'y': 10
                }
            },
            'teams': {
                1: {
                    'food': 50,
                    'hasLost' : False
                },
                2: {
                    'food': 10,
                    'hasLost': False
                },
            }
        }
        state.update(data=data)
        action = ActionAttackNearestFromHeadquarters(BARRACKS)
        action(state[4])

        self.assertEqual(action.target, 2)

    def test_ActionAttackNearestFromHeadquarters_cando(self):
        data = {
            'plateau': {
                1: {
                    'team': 1,
                    'id' :1,
                    'name': HEADQUARTERS,
                    'x': 0,
                    'y': 0
                },
                2: {
                    'team': 0,
                    'id': 2,
                    'name': BARRACKS,
                    'x': 1,
                    'y': 1
                },
                3: {
                    'team': 0,
                    'id' :3,
                    'name': BARRACKS,
                    'x': 20,
                    'y': 10
                },
                4: {
                    'team': 1,
                    'id' : 4,
                    'name': SPEARMAN,
                    'x': 30,
                    'y': 10
                }
            },
            'teams': {
                1: {
                    'pop': 9,
                    'maxPop': 10,
                    'food' :10,
                    'hasLost' : False
                },
                2: {
                    'food': 10,
                    'hasLost': False
                },
            }
        }
        state.update(data=data)
        action = ActionAttackNearestFromHeadquarters(BARRACKS)
        self.assertTrue(action.can_do(state[4]))

    def test_ActionAttackNearestFromHeadquarters_cantdo(self):
        data = {
            'plateau': {
                1: {
                    'team': 1,
                    'id': 1,
                    'name': HEADQUARTERS,
                    'x': 0,
                    'y': 0
                },
                2: {
                    'team': 1,
                    'id': 2,
                    'name': BARRACKS,
                    'x': 1,
                    'y': 1
                },
                3: {
                    'team': 1,
                    'id': 3,
                    'name': BARRACKS,
                    'x': 20,
                    'y': 10
                },
                4: {
                    'team': 1,
                    'id': 4,
                    'name': SPEARMAN,
                    'x': 30,
                    'y': 10
                }
            },
            'teams': {
                1: {
                    'pop': 9,
                    'maxPop': 10,
                    'food': 10,
                    'hasLost': False
                },
                2: {
                    'food': 10,
                    'hasLost': False
                },
            }
        }
        state.update(data=data)
        action = ActionAttackNearestFromHeadquarters(BARRACKS)
        self.assertFalse(action.can_do(state[4]))







    def test_ActionAttackNearest(self):
        data = {
            'plateau': {
                1: {
                    'team': 1,
                    'id' :1,
                    'name': HEADQUARTERS,
                    'x': 0,
                    'y': 0
                },
                2: {
                    'team': 0,
                    'id': 2,
                    'name': BARRACKS,
                    'x': 1,
                    'y': 1
                },
                3: {
                    'team': 0,
                    'id' :3,
                    'name': BARRACKS,
                    'x': 20,
                    'y': 10
                },
                4: {
                    'team': 1,
                    'id' : 4,
                    'name': SPEARMAN,
                    'x': 30,
                    'y': 10
                }
            },
            'teams': {
                1: {
                    'food': 50,
                    'hasLost' : False
                },
                2: {
                    'food': 10,
                    'hasLost': False
                },
            }
        }
        state.update(data=data)
        action = ActionAttackNearest(BARRACKS)
        action(state[4])

        self.assertEqual(action.target, 3)

    def test_ActionAttackNearest_cando(self):
        data = {
            'plateau': {
                1: {
                    'team': 1,
                    'id' :1,
                    'name': HEADQUARTERS,
                    'x': 0,
                    'y': 0
                },
                2: {
                    'team': 0,
                    'id': 2,
                    'name': BARRACKS,
                    'x': 1,
                    'y': 1
                },
                3: {
                    'team': 0,
                    'id' :3,
                    'name': BARRACKS,
                    'x': 20,
                    'y': 10
                },
                4: {
                    'team': 1,
                    'id' : 4,
                    'name': SPEARMAN,
                    'x': 30,
                    'y': 10
                }
            },
            'teams': {
                1: {
                    'pop': 9,
                    'maxPop': 10,
                    'food' :10,
                    'hasLost' : False
                },
                2: {
                    'food': 10,
                    'hasLost': False
                },
            }
        }
        state.update(data=data)
        action = ActionAttackNearest(BARRACKS)
        self.assertTrue(action.can_do(state[4]))

    def test_ActionAttackNearest_cantdo(self):
        data = {
            'plateau': {
                1: {
                    'team': 1,
                    'id': 1,
                    'name': HEADQUARTERS,
                    'x': 0,
                    'y': 0
                },
                2: {
                    'team': 1,
                    'id': 2,
                    'name': BARRACKS,
                    'x': 1,
                    'y': 1
                },
                3: {
                    'team': 1,
                    'id': 3,
                    'name': BARRACKS,
                    'x': 20,
                    'y': 10
                },
                4: {
                    'team': 1,
                    'id': 4,
                    'name': SPEARMAN,
                    'x': 30,
                    'y': 10
                }
            },
            'teams': {
                1: {
                    'pop': 9,
                    'maxPop': 10,
                    'food': 10,
                    'hasLost': False
                },
                2: {
                    'food': 10,
                    'hasLost': False
                },
            }
        }
        state.update(data=data)
        action = ActionAttackNearest(BARRACKS)
        self.assertFalse(action.can_do(state[4]))






if __name__=='__main__':
    unittest.main()