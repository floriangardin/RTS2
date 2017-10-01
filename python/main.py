#!/usr/bin/env python
# coding: utf8
from ia.domain.model import QLearnerClean
import time
import sys
import pickle
"""
Trois applications potentiels à une api :
1) Confort de python pour faire un outil de statistique de la partie
2) Outil de debug du plateau et de son état
3) Outil pour développer l'IA avec le confort de python
"""

team = int(sys.argv[1])
learner = QLearnerClean()
with open("python/data/q", "rb") as f:
    Q = pickle.load(f)
learner.init()
Q = learner.learn_from_one_game(team=team)
# Save Q
with open("python/data/q", 'wb') as f:
    pickle.dump(Q,f)
# Second learner


# Add world to state and calculate reward

# plan = think()
# post(plan)

