
import numpy as np


def distance(o1,o2):
    return np.sqrt((o1['x']-o2['x'])**2 + (o1['y']-o2['y'])**2)