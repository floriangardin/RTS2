
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
    def test_FoodBetween_ok(self):
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

    def test_FoodBetween_notok(self):
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

    def test_PopBetween_ok(self):
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
                    'hasLost' : False,
                    'pop': 1
                },
                2: {
                    'food': 10,
                    'hasLost': False
                },
            }
        }
        state.update(data=data)
        feature = PopBetween(state.objets[0], 1, 2)
        self.assertEqual(feature.state, '1')

    def test_PopBetween_notok(self):
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
                    'pop' : 3,
                    'hasLost' : False
                },
                2: {
                    'food': 10,
                    'hasLost': False
                },
            }
        }
        state.update(data=data)
        feature = PopBetween(state.objets[0], 1, 2)
        self.assertEqual(feature.state, '0')


    def test_MaxPopBetween_ok(self):
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
                    'hasLost' : False,
                    'maxPop': 1
                },
                2: {
                    'food': 10,
                    'hasLost': False
                },
            }
        }
        state.update(data=data)
        feature = MaxPopBetween(state.objets[0], 1, 2)
        self.assertEqual(feature.state, '1')

    def test_MaxPopBetween_notok(self):
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
                    'maxPop' : 3,
                    'hasLost' : False
                },
                2: {
                    'food': 10,
                    'hasLost': False
                },
            }
        }
        state.update(data=data)
        feature = MaxPopBetween(state.objets[0], 1, 2)
        self.assertEqual(feature.state, '0')

    def test_PopRemainingBetween_ok(self):
        data = {
            'plateau': {
                1: {
                    'team': 1,
                    'name': SPEARMAN
                }
            },
            'teams': {
                1: {
                    'food': 50,
                    'hasLost': False,
                    'maxPop': 1,
                    'pop': 0
                },
                2: {
                    'food': 10,
                    'hasLost': False
                },
            }
        }
        state.update(data=data)
        feature = PopRemainingBetween(state.objets[0], 1, 2)
        self.assertEqual(feature.state, '1')

    def test_PopRemainingBetween_notok(self):
        data = {
            'plateau': {
                1: {
                    'team': 1,
                    'name': SPEARMAN
                }
            },
            'teams': {
                1: {
                    'food': 50,
                    'maxPop': 3,
                    'pop': 0,
                    'hasLost': False
                },
                2: {
                    'food': 10,
                    'hasLost': False
                },
            }
        }
        state.update(data=data)
        feature = PopRemainingBetween(state.objets[0], 1, 2)
        self.assertEqual(feature.state, '0')

    def test_EnemyInRangeAtLeast_ok(self):
        data = {
            'plateau': {
                1  : {
                    'team' :1,
                    'name' : SPEARMAN,
                    'x':10,
                    'y': 15
                },
                2 : {
                    'team': 2,
                    'name': SPEARMAN,
                    'range': 100,
                    'x':10,
                    'y': 10
                },
                3: {
                    'team': 2,
                    'name': SPEARMAN,
                    'range': 6,
                    'x': 10,
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
        # from pdb import set_trace; set_trace()
        feature = EnemyInRangeAtLeast(state.objets[0], SPEARMAN, 2)

        self.assertEqual(feature.state, '1')
    def test_EnemyInRangeAtLeast_nok(self):
        data = {
            'plateau': {
                1  : {
                    'team' :1,
                    'name' : SPEARMAN,
                    'x':10,
                    'y': 15
                },
                2 : {
                    'team': 2,
                    'name': SPEARMAN,
                    'range': 10,
                    'x':10,
                    'y': 100
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
        # from pdb import set_trace; set_trace()
        feature = EnemyInRangeAtLeast(state.objets[0],SPEARMAN, 1)
        self.assertEqual(feature.state, '0')


    def test_NbMyObjetsBetween_ok(self):
        data = {
            'plateau': {
                1  : {
                    'team' :1,
                    'name' : SPEARMAN,
                    'x':10,
                    'y': 15
                },
                2 : {
                    'team': 1,
                    'name': SPEARMAN,
                    'range': 10,
                    'x':10,
                    'y': 100
                },
                3: {
                    'team': 2,
                    'name': SPEARMAN,
                    'range': 10,
                    'x': 10,
                    'y': 100
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
        # from pdb import set_trace; set_trace()
        feature = NbMyObjetsBetween(state.objets[0],SPEARMAN, 2, 3)
        self.assertEqual(feature.state, '1')

    def test_NbObjetsBetween_ok(self):
        data = {
            'plateau': {
                1  : {
                    'team' :1,
                    'name' : SPEARMAN,
                    'x':10,
                    'y': 15
                },
                2 : {
                    'team': 1,
                    'name': SPEARMAN,
                    'range': 10,
                    'x':10,
                    'y': 100
                },
                3: {
                    'team': 2,
                    'name': SPEARMAN,
                    'range': 10,
                    'x': 10,
                    'y': 100
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
        # from pdb import set_trace; set_trace()
        feature = NbObjetsBetween(state.objets[0],SPEARMAN,1, 2, 3)
        self.assertEqual(feature.state, '1')

if __name__=='__main__':
    unittest.main()