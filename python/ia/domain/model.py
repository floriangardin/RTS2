import pandas as pd
import numpy as np


"""
Modelling performing an action
"""

class Action:
    def __init__(self, verb, target=-1, df=None, x=None, y=None):
        """
        Given a DataFrame find the corresponding action
        :param subject:
        :param verb:
        :param df:
        :param target:
        :param x:
        :param y:
        """
        self.df = df
        self.subject = None
        self.verb = verb
        self.target = target
        self.x = x
        self.y = y

    def can_perform(self, subject):
        return True
    def set_subject(self, subject):
        self.subject = subject
    def get_action_from_verb(self):
        if self.verb == "attack_nearest":
            self.verb = "attack"
            # Use target as a name to retrieve building or character ...
            # FIXME : Implement proper behaviour
            subject_team = self.df.loc[self.subject, 'team']
            x = self.df.loc[self.subject, 'x']
            y = self.df.loc[self.subject, 'y']
            potential_target = self.df[(self.df['team']!= subject_team) & (self.df['name']== self.target)]
            potential_target['distance'] = (potential_target['x']-x)**2 + (potential_target['y']-x)**2
            potential_target.sort_values(by="distance")
            self.target = potential_target.iloc[0].index

    def perform_action(self, subject):
        self.set_subject(subject)
        self.get_action_from_verb()
        return self.to_dict()

    def to_dict(self):
        return {'subject' : self.subject,
                'verb': self.verb,
                'target': self.target
                }

"""
Modelling global state
"""

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


"""
Modellling class
State provider with world and action list provider
"""
class Spearmans:

    def __iter__(self):
        for index, row in self.df.iterrows():
            yield index, row
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
        self.df["remaining_pop_inferior_5"] = ~self.df["remaining_pop_inferior_2"] & (w.teams.loc[team, 'maxPop'] - w.teams.loc[team, 'pop'] <= 5)
        # target_same_team
        ## TODO : FIXME : HERE BUG BECAUSE loc[self.df['target'] does not work
        self.df['target_same_team'] = (self.df['hasTarget'] == True) & (w.df.loc[self.df['target'], 'team'] == team)
        # Redefinition of hasTarget
        self.df['hasTarget'] = (~self.df['target_same_team']) & (self.df['hasTarget'])
        # crossbowman_two_range
        # has_target
        # ennemy_towers_2
        # ennemy_towers_1
        # ennemy_towers_0

        # On ne garde que les objets de notre équipe
        self.df = self.df[self.df['team'] == team]
        self.df = self.df['food_between_0_50', 'food_between_50_100', 'food_between_100_200',
                          'food_superior_200', 'no_barracks', 'no_mill', 'no_farm',
                            'remaining_pop_inferior_2', "remaining_pop_inferior_5", 'isPopFull',
                            'hasTarget', 'target_same_team']
    def get_state(self, idx):
        return self.df.loc[idx]
    def get_actions(self):
        return [
            Action("attack_nearest", "Barracks", self.w),
            Action("attack_nearest", "Mill", self.w),
            Action("attack_nearest", "Mine", self.w),
            Action("attack_nearest", "Headquarters", self.w),
            Action("attack_nearest", "Tower", self.w),
        ]

    def get_valid_actions(self):
        return self.get_actions()

class Crossbowman:
    def __iter__(self):
        for index, row in self.df.iterrows():
            yield index, row
    def __init__(self, w):
        self.name = "Crossbowman"
        self.df = w[w['name'] == self.name]
        # features :
        # ennemy_spearman_half_range
        # ennemy_crossbowman_half_range
        # threat_up
        # threat_down
        # threat_right
        # threat_left
        # ennemy_in_sight
        self.df = self.df[self.df['team'] == w.df.team_]
        self.df = self.df[self.df['hasTarget']]
    def get_state(self, idx):
        return self.df.loc[idx]

    def get_actions(self):
        return [
            Action("attack_nearest", "Spearman", self.w),
            Action("attack_nearest", "Crossbowman", self.w)
        ]

    def get_valid_actions(self):
        return self.get_actions() # Temporary

class Barracks:
    def __iter__(self):
        for index, row in self.df.iterrows():
            yield index, row
    def __init__(self, w):
        self.name = "Barracks"
        self.df = w[w['name'] == self.name]
        # food_between_0_50
        # food_between_50_100
        # food_between_100_200
        # food_superior_200
        # remaining_pop_inferior_2
        # remaining_pop_inferior_5

    def get_state(self, idx):
        return self.df.loc[idx]

    def get_actions(self):
        return [
            Action("produce", "Spearman", self.w),
            Action("produce", "Crossbowman", self.w),
            Action("produce", "Inquisitor", self.w)
        ]

    def get_valid_actions(self):
        return self.get_actions()




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