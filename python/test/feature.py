
"""
Test each feature,
pass a plateau state with expected result of feature
"""

import unittest
import sys
sys.path.append('../')
from ia.domain.feature import *
from ia.domain.state import StateManager
from ia.domain.constant import *

state = StateManager(team=1)

class TestFeatures(unittest.TestCase):
    def test_FoodBetween_OK(self):
        data = {
            'plateau': {
                1  : {
                    'team' :1,
                    'name' : SPEARMAN
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
        feature = FoodBetween(state.objets[0], 0, 100)
        self.assertEqual(feature.state, '1')

    def test_FoodBetween_NOTOK(self):
        data = {
            'plateau': {
                1  : {
                    'team' :1,
                    'name' : SPEARMAN
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
        feature = FoodBetween(state.objets[0], 0, 50)
        self.assertEqual(feature.state, '0')

if __name__=='__main__':
    unittest.main()