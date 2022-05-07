# -*- coding: utf-8 -*-
"""
Created on Wed Apr 27 16:49:31 2022

@author: 冯纪轩
"""
import os
from subprocess import Popen, PIPE, check_output, run
import tsplib95
import networkx
import numpy as np


def LKH(input_data):
    import time
    time_start = time.time()
    process = Popen(['cd', 'data', '&', 'LKH-3.exe', input_data + '.par'], stdout=PIPE, stdin=PIPE,
                    universal_newlines=True,
                    shell=True)

    '''process = Popen(['java','-version'], stdout=PIPE, universal_newlines=True)'''
    (stdout, stderr) = process.communicate()
    time_end = time.time()
    return "LKH done in %.3f" % (time_end - time_start)


def get_distance_matrix(input_data):
    problem = tsplib95.load('./data/' + input_data + '.tsp')

    # convert into a networkx.Graph
    graph = problem.get_graph()

    # convert into a numpy distance matrix
    distance_matrix = networkx.to_numpy_matrix(graph)
    file_path = "./data/" + input_data + ".dis"
    np.savetxt(file_path, distance_matrix, fmt="%.3f", delimiter=' ')
    with open(file_path, "r+") as f:
        old = f.read()
        f.seek(0)
        f.write(str(len(distance_matrix)))
        f.write("\n")
        f.write(old)
    return "distance matrix done"


def solveJava(input_data):
    cplexdir = "C:/Program Files/IBM/ILOG/CPLEX_Studio1263/cplex/"
    cpopdir = "C:/Program Files/IBM/ILOG/CPLEX_Studio1263/cpoptimizer/"
    tools = 'D:/java/srtp/out/production/srtp'

    process = Popen(['java', '-cp', cplexdir + 'lib/cplex.jar;' +
                     cpopdir + 'lib/ILOG.CP.jar;' +
                     tools,
                     '-Djava.library.path=' + cplexdir + 'bin/x64_win64;'
                     + cpopdir + 'bin/x64_win64',
                     'main', '-file=' + input_data], stdout=PIPE, stdin=PIPE, universal_newlines=True, shell=True)
    (stdout, stderr) = process.communicate()
    return stdout.strip()

if __name__ == '__main__':
    import sys
    import re
    if len(sys.argv) > 1:
        file_location = sys.argv[1].strip()
        #   print(file_location)
        #      print(sys.argv)
    s1=LKH(file_location)
    print(s1)
    print(get_distance_matrix(file_location))
    s2 = solveJava(file_location)
    print(s2)
    print("total time=",float(re.findall(r"\d+\.?\d*", s1)[0])+float(re.findall(r"\d+\.?\d*", s2)[0]))

