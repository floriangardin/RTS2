#!/usr/bin/env python
# coding: utf8

import pandas as pd
import numpy as np
import sys
import pickle
from ..api.api import get,post
import time
from collections import defaultdict

"""
Modelling global state
"""

BARRACKS = "Barracks"
CROSSBOWMAN = "Crossbowman"
SPEARMAN = "Spearman"
MILL = "Mill"
MINE = "Mine"
HEADQUARTERS = "Headquarters"
TOWER = "Tower"
INQUISITOR = "Inquisitor"

### ACTIONS

ATTACK = "attack"
NEAREST = "nearest"
NEARESTFROMHEADQUARTERS = "nearestfromheadquarters"
NEARESTENEMY = "nearestenemy"
STOP = "stop"
PRODUCE = "produce"

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
            if state[0] == '1' and len(valid_actions) > 0: # Si l'objet considéré est idle
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



"""
2) Represent Objets, their states and associated actions
"""
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


"""
3) Features used in States , they are boolean features only
"""

class Feature:
    def __call__(self):
        return self.state

    def __add__(self, other):
        if isinstance(other, Feature):
            return self()+other
        else:
            return self()+other()
    def __radd__(self, other):
        return self.__add__(other)

class FoodBetween(Feature):
    def __init__(self, objet, low, up):
        self.state = '1' if low <= objet.teams[objet.team]['food'] < up else '0'
class PopBetween(Feature):
    def __init__(self, objet, low, up):
        self.state = '1' if low <= objet.teams[objet.team]['pop'] < up else '0'

class MaxPopBetween(Feature):
    def __init__(self, objet, low, up):
        self.state = '1' if low <= objet.teams[objet.team]['maxPop'] < up else '0'
class PopRemainingBetween(Feature):
    def __init__(self, objet, low, up):
        self.state = '1' if low <= objet.teams[objet.team]['maxPop']-objet.teams[objet.team]['pop'] < up else '0'

class NbMyObjetsBetween(Feature):
    def __init__(self, objet, to_count, low, up):
        quantity = len([val for key, val in objet.plateau.items() if val['name'] == to_count and val['team'] == objet.team])
        self.state = '1' if low <= quantity < up else '0'

class NbObjetsBetween(Feature):
    def __init__(self, objet, to_count, team, low, up):
        quantity = len([val for key, val in objet.plateau if val['name'] == to_count and val['team'] == team])
        self.state = '1' if low <= quantity < up else '0'

class IsIdle(Feature):
    def __init__(self, objet):

        has_target = objet.objet['target'] > -1
        if has_target:
            if objet.objet['target'] in objet.plateau:
                if objet.plateau[objet.objet['target']]['team'] != objet.team:
                    self.state = '0'
                    return
        self.state = '1'

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
        return ((objet.teams[objet.team]['maxPop']-objet.teams[objet.team]['pop']) > 0 ) \
               or (self.target in [MINE, SPEARMAN, CROSSBOWMAN, INQUISITOR])\
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

###################################################################
###### WARNING : ULTRA DEPRECATED PAS TOUCHE ######################
###################################################################

class State:
    """
    Represent the real state of the world as simple as possible
    The state should have a relatively simple complexity if we want to train on it ...
    """
    classes = ["Spearman",
               "Crossbowman",
               "Barracks"]  # only builder and army for the time being, spearman builds, crossbowman attack
    def __init__(self, world):
        df = world
        """
        Construct a simple state given world
        :param world:
        """
        self.C = {}
        for c in self.classes:
            # Binarize
            if c == "Spearman":
                self.C[c] = Spearmans(df)
            elif c == "Crossbowman":
                self.C[c] = Crossbowman(df)
            elif c == "Barracks":
                self.C[c] = Barracks(df)

class World:
    """
    Representing the world and many helper to caracterize state
    So World only need present world for initialisation
    You can interact with world as you will do in a pandas DataFrame using composition
    """
    def __init__(self, data, team=2, previous_world=None):
        self.reward = None
        self.team_ = team
        self.teams = pd.DataFrame.from_dict(data['teams'], orient='index')
        self.teams.index = self.teams.index.astype(int)
        # Convert plateau to dataframe
        self.df = pd.DataFrame.from_dict(data['plateau'], orient='index')
        self.df.index = self.df.index.astype(int)

        # Enrich plateau with teams data
        self.df = pd.merge(self.df, self.teams, how='left', left_on="team", right_index=True)
        # Enrich plateau with feature engineering
        self.df['hasTarget'] = 1.0 * (self.df['target'] != -1) # IsIdle ...
        # TODO
        # isTargeted
        # nbNearbyAllies
        # isPopFull
        self.df['isPopFull'] = (self.df['maxPop'] <= self.df['pop']).astype(np.float32)
        # Towers lifepoints
        # HeadQuarters lifepoints

        # Enrich plateau with historical data (from previous state)
        if previous_world is None:
            previous_world = self.df
        # gainedFood
        self.df['deltaFood'] = (self.df['food'] - previous_world['food']).astype(np.float32)
        # gainedPop
        self.df['deltaPop'] = (self.df['pop'] - previous_world['pop']).astype(np.float32)
        # gaindedMaxPop
        self.df['deltaMaxPop'] = (self.df['maxPop'] - previous_world['maxPop']).astype(np.float32)
        # hasLostLP
        self.df['deltaLifepoints'] = (self.df['lifepoints'] - previous_world['lifepoints']).astype(np.float32)
        # hasMoved
        self.df['deltaMoved'] = (((self.df['x'] - previous_world['x'])**2 + (self.df['y'] - previous_world['y'])**2)**0.5).astype(np.float32)


    def get_reward(self):
        if self.reward is not None:
            return self.reward
        return reward(self)

    def __getattr__(self, item):
        return self.df.__getattr__(item)

    def __getitem__(self, item):
        return self.df[item]




#deprecated
def reward(world):
        """
        Reward as R[t] = F(world[t])
        (Deprecated currently because reward at the end of the game)
        :param world:
        :return:
        """
        # Return reward considering current state
        # Normalize numeric df and multiply by reward vector (coeff for each)
        mask = (world.df['team'] != 0) & (~world.df['name']=="Checkpoint")
        df = world.df[mask]  # We remove neutrals

        # Create handcrafted indicators
        mask_allies = (df['team'] == world.team_)
        mask_ennemies = (df['team'] != world.team_)

        df_allies = df[mask_allies]
        df_ennemies = df[mask_ennemies]
        from pdb import set_trace ; set_trace()

        features = {
        "allies_tower_lp" : df[mask_allies & (df['name_Tower'] == 1)]['lifepoints'].sum(),
        "ennemies_tower_lp" : df[mask_ennemies & (df['name_Tower'] == 1)]['lifepoints'].sum(),
        "allies_hq_lp" : df[mask_allies & (df['name_Headquarters'] == 1)]['lifepoints'].sum(),
        "ennemies_hq_lp" : df[mask_ennemies & (df['name_Headquarters'] == 1)]['lifepoints'].sum(),
        "allies_food" : df[mask_allies & (df['name_Headquarters'] == 1)]['food'].sum(),
        "ennemies_food" : df[mask_ennemies & (df['name_Headquarters'] == 1)]['food'].sum(),
        "allies_pop" : df[mask_allies & (df['name_Headquarters'] == 1)]['pop'].sum(),
        "ennemies_pop" : df[mask_ennemies & (df['name_Headquarters'] == 1)]['pop'].sum(),
        "allies_max_pop" : df[mask_allies & (df['name_Headquarters'] == 1)]['maxPop'].sum(),
        "ennemies_max_pop" : df[mask_ennemies & (df['name_Headquarters'] == 1)]['maxPop'].sum()
        }

        features_coeffs = {
        "allies_tower_lp" : 5,
        "ennemies_tower_lp" : -5,
        "allies_hq_lp" : 10,
        "ennemies_hq_lp" : -10,
        "allies_food" : 1,
        "ennemies_food" : -1,
        "allies_pop" : 1,
        "ennemies_pop" : -1,
        "allies_max_pop" : 1,
        "ennemies_max_pop" : -1
        }


        team_modifier = 1.0*(df['team'] == world.team_) \
                        -1.0*((df['team'] != world.team_) & (df['team'] != 0))
        df = (
            df[['lifepoints', 'hasTarget', 'isPopFull', 'deltaMoved', 'deltaFood', 'deltaPop', 'deltaMaxPop']]

                # .pipe(lambda x: (x - x.min()) / (x.max()- x.min())) # FIXME : Normalization may be important
                .fillna(0)
                .replace(-np.inf, 0.0)
                .replace(np.inf, 0.0)
                .apply(lambda x: team_modifier * x)  # Bonus or malus if it is you team or not
                .mean()
        )
        # print(df)
        # TODO : Custom features that are more independent, here pretty much everything is correlated to nb_pop ...

        coeffs = pd.Series({
            'lifepoints': 1,
            'hasTarget': 3,
            'deltaFood': 1,
            'deltaPop': 1,
            'deltaMaxPop': 1,
            'deltaLifepoints': 1,
            'deltaMoved': 1,
            'isPopFull': -1
        })
        # print("stats : ", (coeffs*df))
        # print("reward : ", (coeffs * df).sum())
        partial_reward = (coeffs*df).sum()
        final_reward = partial_reward
        for key in features.keys():
            final_reward+= features[key]*features_coeffs[key]
        return final_reward
