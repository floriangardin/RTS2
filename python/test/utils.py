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

from ia.domain.state import StateManager
from ia.domain.constant import *
from ia.domain.utils import *

state = StateManager(team=1)

class TestUtils(unittest.TestCase):
    def test_distance(self):
        data = {
            'plateau': {
                1  : {
                    'team' :1,
                    'x': 0,
                    'y': 2,
                    'name' : SPEARMAN
                },
                2: {
                    'team': 1,
                    'x': 0,
                    'y': 4,
                    'name': SPEARMAN
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

        self.assertEqual(distance(state.objets[0].objet, state.objets[1].objet), 2)

if __name__=='__main__':
    unittest.main()