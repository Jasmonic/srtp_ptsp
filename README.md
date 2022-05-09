# srtp_ptsp
浙大SRTP项目，求解PTSP代码

## 目录概要

source是用pycharm写的（python）

srtp是用IntelliJ IDEA（Java）写的

source/data存放实例数据（TSPLIB），还有LKH.exe

connection.py用于画图

generate_par用于生成par文件用于LKH

## 程序调用
包含data data下包含LKH算法和各实例的tsp par 和 dis文件  
solve.py为整个主程序，在命令行中输入python solve.py 实例名 

```python
（i.e. python solve.py eil51）
```

## 程序框架

1.预先生成.tsp和.par文件

2.solve.py调用生成LKH.exe求得初始解，生成.tour文件

3.solve.py调用tsplib95库生成距离矩阵.dis文件，方便Java用

4.Java进行近似的k截断邻域搜索

5.solve.py的所用调用都是以命令行的方式，最终结果全部返回至python

## clone到本地需要修改的地方

1.solve.py调用java的原理是通过命令行（cmd），执行java **，因此先要生成.class文件，同时solve.py里需要将路径改为.class文件路径

2.java文件中读取.tour和.dis文件，用到绝对路径的部分需要自行修改。

