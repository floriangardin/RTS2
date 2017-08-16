
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
        self.final_verb = self.verb
        self.target = target
        self.final_target = self.target
        self.x = x
        self.y = y

    @property
    def name(self):
        """
        Very important : name of the action to be accounted in Q
        :return:
        """
        return str(self.verb)+str(self.target)

    def can_perform(self, subject):
        return True
    def set_subject(self, subject):
        self.subject = subject
    def get_action_from_verb(self):
        if self.verb == "attack_nearest":
            self.final_verb = "attack"
            # Use target as a name to retrieve building or character ...
            # FIXME : Implement proper behaviour
            subject_team = self.df.loc[self.subject, 'team']
            x = self.df.loc[self.subject, 'x']
            y = self.df.loc[self.subject, 'y']
            potential_target = self.df[(self.df['team']!= subject_team) & (self.df['name']== self.target)]
            potential_target['distance'] = (potential_target['x']-x)**2 + (potential_target['y']-y)**2
            potential_target.sort_values(by="distance")
            if len(potential_target>0):
                self.final_target = potential_target.iloc[0]['id']


    def perform_action(self, subject):
        self.set_subject(subject)
        self.get_action_from_verb()
        return self.to_dict()

    def to_dict(self):
        return {'subject' : self.subject,
                'verb': self.final_verb,
                'target': self.final_target
                }
