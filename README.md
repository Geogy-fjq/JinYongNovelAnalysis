# JinYongNovelAnalysis
## 基于 hadoop ——金庸江湖人物关系网分析  
### 项目介绍  
    将金庸小说以及金庸小说中出现的人物作为输入，输出金庸小说人物关系网，从而探索小说中更深层次的关系。  
### 涉及技术  
    Hadoop（MapReduce+HDFS+Yarn），PageRank算法，LPA算法。  
### 实现过程  
1.数据预处理  
  从原始的金庸小说文本中，抽取出与人物互动相关的数据，而屏蔽掉与人物关系无关的文本内容。  
2.特征抽取  
  完成基于单词同现算法的人物同现统计。在人物同现分析中，如果两个人在原文的同一段落中出现，则认为两个人发生了一次同现关系。  
3.特征处理  
  根据人物共现关系，生成人物之间的关系图。人物关系使用邻接表的形式表示，人物是顶点，人物之间关系是边，两个人的关系的密切程度由共现次数体现，共现次数越高，边权重越高。另外需要对共现次数进行归一化处理，确保某个顶点的出边权重和为1。  
4.数据分析：基于人物关系图的PageRank计算  
  根据人物之间的关系图，对人物关系图作数据分析。通过计算 PageRank，我们就可以定量地获知金庸武侠江湖中的“主角”们是哪些。
5.数据分析：基于人物关系图的LPA标签传播计算
  标签传播是一种半监督的图分析算法，他能为图上的顶点打标签，进行图顶点的聚类分析，从而在一张类似社交网络图中完成社区发现。在人物关系图中，通过标签传播算法可以将关联度比较大的人物分到同一标签，可以直观地分析人物间的关系。
