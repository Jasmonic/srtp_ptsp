import tsplib95
import networkx
import numpy as np
import pandas as pd
import  matplotlib.pyplot as plt
import random

def Caldis(solution):
    cost=0
    for i in range(len(solution)-1):
        cost+=distance[solution[i]][solution[i+1]]
    return cost
def CalDistPtsp(solution):
    cost=0
    for i in range(len(solution)):
        for j in range(i + 1, len(solution)):
            t = 1
            for k in range(i + 1, j):
                t = t * (1 - p[k])

            cost+= p[i] * p[j] * distance[solution[i]][solution[j]] * t
    return cost
def Local_search(solution):

    s = solution.copy()
    best_solution = s.copy()
    best_dis = CalDistPtsp(s)
    neiNum = 50
    iterx = 1

    # 邻域1
    while iterx < neiNum:
        i = random.randint(1, n - 1)
        j = random.randint(1, n - 1)
        while i == j:
            j = random.randint(1, n - 1)
        s = solution.copy()
        x = s[j]
        s[j] = s[i]
        s[i] = x
        current_dis = CalDistPtsp(s)
        if current_dis < best_dis:
            best_solution = s.copy()
            best_dis = current_dis
        iterx = iterx + 1

    # 邻域2
    iterx = 1
    while iterx < neiNum:
        i = random.randint(1, n - 1 - 3)
        j = random.randint(i, n - 1)
        while i >= j:
            j = random.randint(i, n - 1)
        s = solution.copy()
        s[j - 3], s[j - 2], s[j - 1], s[j] = s[j], s[j - 1], s[j - 2], s[j - 3]
        current_dis = CalDistPtsp(s)
        if current_dis < best_dis:
            best_solution = s.copy()
            best_dis = current_dis
        iterx = iterx + 1

    return best_dis, best_solution
input_data='att48'
problem = tsplib95.load('./data/' + input_data + '.tsp')
# convert into a networkx.Graph
graph = problem.get_graph()
# convert into a numpy distance matrix
distance = networkx.to_numpy_matrix(graph).tolist()
n=len(distance)
solution=[]
for i in range(n):
    solution.append(i)
solution.append(0)
p=[1]+[random.random()*0.3+0.7 for i in range(1,n)]+[1]
x=[i for i in range(100)]
ptsp=[]
tsp=[]
for i in range(100):
    newDis,newSol=Local_search(solution)
    solution=newSol.copy()
    ptsp.append(newDis)
    tsp.append(Caldis(newSol))
plt.plot(ptsp)
plt.plot(tsp)
plt.xlabel("Iteration")
plt.ylabel("Cost")
plt.legend(["$E(L_T)$","$L_T$"])
plt.show()