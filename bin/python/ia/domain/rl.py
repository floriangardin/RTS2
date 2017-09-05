

"""
Questions :
Peut-t-on définir des actions continues ?
Dans le cas oui on peut faire des inptuts
Dans le cas non penser à la discrétisation des actions
--> Comment discrétiser les endroits où l'on peut cliquer ?

--> Il faut créer des checkpoints artificiels sur la carte comme ça on peut choisir parmis eux

Représentation d'un modèle interne ? Oui ou model free ?

Remember : previous actions, previous state

Create a markov decision process : Chose a state that reflect the past to make sure
the future state is only dependant of the present
"""

class RLSolver:
    def __init__(self):
        self.observations = []
        self.state = []
        self.actions = [] # Qu'est ce qu'une action ? Input ou phrase ?
        self.rewards = []
        pass
    def play(self):
        pass
    def plan(self):
        pass