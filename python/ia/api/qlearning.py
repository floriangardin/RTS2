import pandas as pd
import numpy as np
import time
from collections import defaultdict


class ObjectClass:
    def __init__(self, name, data):
        self.name = name
        self.df = data

class QLearner:

    # CLASSQ-L implementation
    def __init__(self):
        self.is_init =False

    def init(self, s0=None, delta=1, Qglobal={}, classes={}, A=[], alpha=0.01, gamma=0.9, epsilon=0.01):
        self.s0 = s0
        self.delta = delta
        self.Qglobal = Qglobal
        self.classes = classes
        self.A = A # Set of all possible actions for each strictfp class (it is a dict with key strictfp class and value list of actions)
        self.alpha = alpha
        self.gamma = gamma # discount
        self.epsilon = epsilon
        self.L = defaultdict(list)
        self.is_init = True

    def learn_from_one_game(self):
        s = self.s0
        self.L = defaultdict(list)
        self.sc = '0'
        self.ac = 'no_action'
        while not self.end_condition(): # While game not finished
            self.step()
        # After the game is over, update the q-tables
        r = self.get_reward() # Get global cumulative reward at the end of the game
        # Pour toutes les unités et pour toutes les actions on ajuste les poids de Q
        # TODO : A modifier parcourir tous les L
        for C in self.classes.keys():
            Q = self.Qglobal[C]
            for c in C:
                for s, a, s1, C in self.L[c.name]:
                    Q[s, a] += self.alpha * (r + self.gamma * np.max((Q[s1, self.A])) - Q[s, a])  # Pas sûr pour cette ligne
        return self.Qglobal

    def end_condition(self):
        return False

    def step(self):
        time.sleep(self.delta)
        s_global = self.get_state()
        for C in self.classes.keys():
            s1 = self.get_abstract_states(s_global, C)  # Customizing the global state for strictfp class c, different strictfp class need different kind of info
            Ac = self.get_valid_actions(self.classes[C], s1)  # Get the set of possible actions for the step to reduce search space
            Q = self.Qglobal[C]  # get the current Q function for this specific class
            for index, c in self.classes[C]:  # For each characters of the class
                if not c['hasTarget']:
                    if np.random.random() >= self.epsilon:
                        state_index = s1.get_state(c.name)
                        actions_index = [ac_.name for ac_ in Ac]
                        # Find max idx
                        a = Ac[np.argmax([Q[state_index, act] for act in actions_index])]
                    else:
                        a = Ac[np.random.random(len(Ac))]
                    self.execute_action(index, a)  # Output to the world

                    self.L[c.name].append((self.sc, self.ac, s1.get_state(c.name), C))  # Keep trace of actions
                    self.sc = s1.get_state(c.name)
                    self.ac = a.name

    def get_state(self):
        """
        Method to get the current state of the game
        :return: The state of the plateau and players in current game
        """
        # 1°) GET query
        # 2°) Feature engineering
        # 2°) create classes (sub DF)
        return 0
    def get_abstract_states(self, s, C):
        """
        Given a state s and a strictfp class of object C, return the local state for class
        :param s: current state
        :param C: current strictfp class considered (eg : Crossbowman, Barracks
        :return: Return the state corresponding to the class
        """
        return []

    def get_valid_actions(self, Ac, s1):
        """
        Get valid actions for given state
        :param Ac:
        :param s1:
        :return: Set of valid actions to perform given state
        """
        return []

    def execute_action(self, index,  a):
        """
        Execute the action you specify
        :param a: action
        """
        return

class Qlearning:
    _qmatrix = None
    _learn_rate = None
    _discount_factor = None

    def __init__(self,
                 possible_states,
                 possible_actions,
                 initial_reward,
                 learning_rate,
                 discount_factor):
        """
        Initialise the q learning strictfp class with an initial matrix and the parameters for learning.
        :param possible_states: list of states the agent can be in
        :param possible_actions: list of actions the agent can perform
        :param initial_reward: the initial Q-values to be used in the matrix
        :param learning_rate: the learning rate used for Q-learning
        :param discount_factor: the discount factor used for Q-learning
        """
        # Initialize the matrix with Q-values
        init_data = [[float(initial_reward) for _ in possible_states]
                     for _ in possible_actions]
        self._qmatrix = pd.DataFrame(data=init_data,
                                     index=possible_actions,
                                     columns=possible_states)

        # Save the parameters
        self._learn_rate = learning_rate
        self._discount_factor = discount_factor

    def get_best_action(self, state):
        """
        Retrieve the action resulting in the highest Q-value for a given state.
        :param state: the state for which to determine the best action
        :return: the best action from the given state
        """
        # Return the action (index) with maximum Q-value
        return self._qmatrix[[state]].idxmax().iloc[0]

    def update_model(self, state, action, reward, next_state):
        """
        Update the Q-values for a given observation.
        :param state: The state the observation started in
        :param action: The action taken from that state
        :param reward: The reward retrieved from taking action from state
        :param next_state: The resulting next state of taking action from state
        """
        # Update q_value for a state-action pair Q(s,a):
        # Q(s,a) = Q(s,a) + α( r + γmaxa' Q(s',a') - Q(s,a) )
        q_sa = self._qmatrix.ix[action, state]
        max_q_sa_next = self._qmatrix.ix[self.get_best_action(next_state), next_state]
        r = reward
        alpha = self._learn_rate
        gamma = self._discount_factor

        # Do the computation
        new_q_sa = q_sa + alpha * (r + gamma * max_q_sa_next - q_sa)
        self._qmatrix.set_value(action, state, new_q_sa)