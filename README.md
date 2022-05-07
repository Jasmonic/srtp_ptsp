# srtp_ptsp
SRTP求解PTSP代码
## source
包含data data下包含LKH算法和各实例的tsp par 和 dis文件
solve.py为整个主程序，在命令行中输入python solve.py 实例名 （i.e. python solve.py eil51）
首先调用LKH获得初始解
然后通过命令行调用srtp目录下的main.java（路径为我本地笔记本的目录，需要自行改一下），用近似的k截断邻域搜索
然后所有控制台的信息返回至python输出。
