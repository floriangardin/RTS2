
from ia.domain.model import QLearnerClean
import time

"""
Trois applications potentiels à une api :
1) Confort de python pour faire un outil de statistique de la partie
2) Outil de debug du plateau et de son état
3) Outil pour développer l'IA avec le confort de python
"""

def think():
    return [{
            "subject": 77,
            "action": "attack",
            "target": 69
      }]

learner = QLearnerClean()
learner.init()
learner.learn_from_one_game(team=2)
# Add world to state and calculate reward

# plan = think()
# post(plan)

from pdb import set_trace as pause; pause()