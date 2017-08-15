from ..api.qlearning import QLearner
from ..api.api import get,post
from .model import World, State
import pandas as pd

class UltraMytheQLearner(QLearner):


    def init(self, s0=None, delta=1, Qglobal={}, classes={}, A=[], alpha=0.01, gamma=0.9, epsilon=0.01):
        self.s0 = s0
        self.delta = delta
        self.Qglobal = Qglobal
        self.classes = {"Barracks": None, "Crossbowman": None, "Spearman": None}
        self.Qglobal = {"Barracks": pd.DataFrame(), "Crossbowman": pd.DataFrame(), "Spearman": pd.DataFrame()}
        self.A = A # Set of all possible actions for each class (it is a dict with key class and value list of actions)
        self.alpha = alpha
        self.gamma = gamma # discount
        self.epsilon = epsilon
        self.L = {}
        self.is_init = True

    def get_state(self):
        """
        Method to get the current state of the game
        :return: The state of the plateau and players in current game
        """
        # 1°) GET query
        world = World(get())
        state = State(world)
        # Update classes from state
        self.classes = state.C

        # 2°) Feature engineering
        # 2°) create classes (sub DF)
        return self.classes
    def get_abstract_states(self, s, C):
        """
        Given a state s and a class of object C, return the local state for class
        :param s: current state
        :param C: current class considered (eg : Crossbowman, Barracks
        :return: Return the state corresponding to the class
        """
        return s[C]

    def get_valid_actions(self, Ac, s1):
        """
        Get valid actions for given state
        :param Ac:
        :param s1:
        :return: Set of valid actions to perform given state
        """
        return Ac.get_valid_actions()

    def execute_action(self, index, a):
        """
        Execute the action you specify
        :param a: action
        """
        post(a.perform_action(index))