import pandas as pd
import numpy as np
from .actions import Action

class ObjectClass:

    def __iter__(self):
        for index, row in self.df.iterrows():
            yield index, row
    def __init__(self, w):
        self.w = w


    def get_state(self, idx):
        return str.join("", [str(i) for i in self.df.loc[idx].astype(int).values.tolist()])
    def get_actions(self):
        return []
    def get_valid_actions(self):
        return self.get_actions()


"""
Modellling class
State provider with world and action list provider
"""
class Spearmans(ObjectClass):

    def __init__(self, w):
        self.w = w
        self.name = "Spearman"
        self.df = w.df[(w.df['name'] == self.name)]
        team = w.team_
        teams = w.teams
        # features :
        # food_between_0_50
        self.df['food_between_0_50'] = (w.teams.loc[team, 'food'] >= 0) & (w.teams.loc[team, 'food']<50)
        # food_between_50_100
        self.df['food_between_50_100'] = (w.teams.loc[team, 'food'] >= 50) & (w.teams.loc[team, 'food'] < 100)
        # food_between_100_200
        self.df['food_between_100_200'] = (w.teams.loc[team, 'food'] >= 100) & (w.teams.loc[team, 'food'] < 200)
        # food_superior_200
        self.df['food_superior_200'] = (w.teams.loc[team, 'food'] >= 200)
        # no_barracks
        self.df['no_barracks'] = w.df[(w.df['name'] == "Barracks") & w.df['team'] == team].shape[0] == 0
        # no_mill
        self.df['no_mill'] = w.df[(w.df['name'] == "Mill") & w.df['team'] == team].shape[0] == 0
        # no_farm
        self.df['no_farm'] = w.df[(w.df['name'] == "Mine") & w.df['team'] == team].shape[0] == 0
        # remaining_pop_inferior_2
        self.df["remaining_pop_inferior_2"] = (w.teams.loc[team, 'maxPop'] - w.teams.loc[team, 'pop'] <= 2)
        # remaining_pop_inferior_5
        self.df["remaining_pop_inferior_5"] = ~self.df["remaining_pop_inferior_2"] & ((w.teams.loc[team, 'maxPop'] - w.teams.loc[team, 'pop']) <= 5)
        # target_same_team
        self.df['target_same_team'] = (self.df['hasTarget'] == True)
        self.df['target_same_team'] &= (self.df['target'].apply(lambda x: (w.df.loc[x, 'team'] == team) if x in w.df.index else False))
        # Redefinition of hasTarget
        self.df['hasTarget'] = (~self.df['target_same_team']) & (self.df['hasTarget'])
        self.df['isPopFull'] = self.df['isPopFull'].astype(bool)

        # crossbowman_two_range
        # has_target
        # ennemy_towers_2
        # ennemy_towers_1
        # ennemy_towers_0

        # On ne garde que les objets de notre équipe
        self.df = self.df[self.df['team'] == team]
        self.df = self.df[['food_between_0_50', 'food_between_50_100', 'food_between_100_200',
                          'food_superior_200', 'no_barracks', 'no_mill', 'no_farm',
                            'remaining_pop_inferior_2', "remaining_pop_inferior_5", 'isPopFull',
                            'hasTarget', 'target_same_team']]

    def get_actions(self):
        return [
            Action("attack_nearest", "Barracks", self.w),
            Action("attack_nearest", "Mill", self.w),
            Action("attack_nearest", "Mine", self.w),
            Action("attack_nearest", "Headquarters", self.w),
            Action("attack_nearest", "Tower", self.w),
        ]

class Crossbowman(ObjectClass):

    def __init__(self, w):
        self.name = "Crossbowman"
        self.df = w[w['name'] == self.name]
        self.w = w
        # features :
        # ennemy_spearman_half_range
        # ennemy_crossbowman_half_range
        # threat_up
        # threat_down
        # threat_right
        # threat_left
        # ennemy_in_sight
        self.df = self.df[self.df['team'] == w.team_]
        self.df = self.df[self.df['hasTarget']]

    def get_actions(self):
        return [
            Action("attack_nearest", "Spearman", self.w),
            Action("attack_nearest", "Crossbowman", self.w)
        ]


class Barracks(ObjectClass):

    def __init__(self, w):
        self.name = "Barracks"
        self.w = w
        self.df = w[w['name'] == self.name]
        # food_between_0_50
        # food_between_50_100
        # food_between_100_200
        # food_superior_200
        # remaining_pop_inferior_2
        # remaining_pop_inferior_5
        self.df = self.df[self.df['team'] == w.team_]
        self.df = self.df[['hasTarget']]


    def get_actions(self):
        return [
            Action("produce", "Spearman", self.w),
            Action("produce", "Crossbowman", self.w),
            Action("produce", "Inquisitor", self.w)
        ]


