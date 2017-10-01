#!/usr/bin/env python
# coding: utf8


import numpy as np
import sys
import pickle
from ..api.api import get,post
import time
from collections import defaultdict

# Relative import
from .constant import *
from .action import *
from .objet import *
from .feature import *

"""
0) QLearner
"""

class QLearnerClean:

    # CLASSQ-L implementation
    def __init__(self):
        self.is_init = False

    def init(self, delta=1, alpha=0.01, gamma=0.9, epsilon=0.01):
        self.delta = delta
        self.Qglobal = {BARRACKS: defaultdict(int), CROSSBOWMAN: defaultdict(int), SPEARMAN: defaultdict(int)}
        self.alpha = alpha
        self.gamma = gamma # discount
        self.epsilon = epsilon
        self.is_init = True

    def learn(self, team=2):
        while True:
            self.learn_from_one_game(team=team)
            pickle.dump(self, open('data/model'+'_'+str(team)+'_'+str(time.time(), 'wb')))

    def learn_from_one_game(self, team=2):
        self.state_manager = StateManager(team=team)
        self.L = defaultdict(list)
        self.sc = '0'
        self.ac = 'no_action'
        while not self.end_condition(): # While game not finished
            self.step()
        # After the game is over, update the q-tables
        r = self.get_reward() # Get global cumulative reward at the end of the game
        # Pour toutes les unités et pour toutes les actions on ajuste les poids de Q
        # TODO : A modifier ne marche pas tel quel
        for obj, name, state, actions, valid_actions in self.state_manager:
            Q = self.Qglobal[name]
            for s, a, s1, C in self.L[name]:
                Q[s, a] += self.alpha * (r + self.gamma * np.max(([Q[s1, a.name] for a in actions])) - Q[s, a])  # Pas sûr pour cette ligne
        return self.Qglobal

    def update_q(self, reward, alpha=1, gamma=0.9):
        """
        Update Q en live en utilisant une reward, permet de réajuster au cours de la partie
        :param reward:
        :param alpha:
        :param gamma:
        :return:
        """
        for obj, name, state, actions, valid_actions in self.state_manager:
            Q = self.Qglobal[name]
            for s, a, s1, C in self.L[name]:
                Q[s, a] += self.alpha * (reward + self.gamma * np.max(([Q[s1, a.name] for a in actions])) - Q[s, a])  # Pas sûr pour
        print(self.Qglobal[SPEARMAN])
    def end_condition(self):
        return self.state_manager.has_won or self.state_manager.has_lost

    def get_reward(self):
        # TODO : Global reward à mettre plus fort
        return 1 if self.state_manager.has_won else -1

    def get_step_reward(self):
        reward =  self.state_manager.players[self.state_manager.team]['reward']
        if reward >0:
            print('GET A REWARD')
        if reward <0:
            print('GET A PUNISHMENT')
        return reward
    def step(self):
        ## communication commodities
        sys.stdout.flush()
        time.sleep(self.delta)
        ## End communication commodities
        self.state_manager.update()
        reward = self.get_step_reward()  # get round reward
        if reward!=0: # Si on reçoit un feedback on le traite instantanément avec toute la pile d'évènements présente
            self.update_q(reward, alpha=1, gamma=0.9)
        for obj, name, state, actions, valid_actions in self.state_manager:
            Q = self.Qglobal[name]  # get the current Q function for this specific class
            if len(valid_actions) > 0: # Si l'objet considéré est idle rajouter : and  state[0] == '1'
                if np.random.random() >= self.epsilon:
                    actions_index = [ac_.name for ac_ in valid_actions]
                    # Find max idx
                    a = valid_actions[np.argmax([Q[state, act] for act in actions_index])]
                else:
                    a = valid_actions[np.random.randint(len(valid_actions))]
                a(obj)
                self.do_action(a)  # Output to the world
                self.L[name].append((self.sc, self.ac, state, name))  # Keep trace of state/action/reward tuples
                self.sc = state
                self.ac = a.name

    def do_action(self, a):
        post(a.to_dict(), self.state_manager.team)

"""
1) Handle states and inputs
"""

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

    def update(self):
        data = get(self.team)
        # Create state for each character
        plateau = data['plateau']
        players = data['teams']
        self.players = players
        self.check_victory(players)
        # Create objects and filter by spearman, crossbowman and barracks
        self.objets = [ObjetManager(plateau, players, idx) for idx in plateau.keys() if self.team == plateau[idx]['team'] and plateau[idx]['name'] in [SPEARMAN, CROSSBOWMAN, BARRACKS]]


class ObjetManager:

    def __new__(cls, plateau, players, idx):
        """
        :param data: Raw Data from get data
        """
        if plateau[idx]['name'] == BARRACKS:
            objet = Barracks()
        elif plateau[idx]['name'] == CROSSBOWMAN:
            objet = Crossbowman()
        elif plateau[idx]['name'] == SPEARMAN:
            objet = Spearman()
        else:
            objet = DefaultObjet()
        # Create features of state
        objet.init(plateau, players, idx)
        return objet



