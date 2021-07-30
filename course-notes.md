#### Hive
基本概念

Hive执行过程: HQL转换为MapReduce过程

#### 安装hdfs
<configuration>
<property>
    <name>dfs.replication</name>
    <value>1</value>
</property>
<property>
    <name>dfs.namenode.name.dir</name>
    <value>file:/home/jixin/hadoop_data/dfs/name</value>
</property>
<property>
    <name>dfs.datanode.data.dir</name>
    <value>file:/home/jixin/hadoop_data/dfs/data</value>
</property>
</configuration>

链接：https://www.imooc.com/article/24911
#### hadoop

export JAVA_HOME  # todo

cd /usr/local/hadoop/etc/hadoop
vi hadoop-env.sh

hdfs-site.xml
<configuration>
<property>
    <name>dfs.replication</name>
    <value>1</value>
</property>
<property>
    <name>dfs.namenode.name.dir</name>
    <value>file:/home/hadoop/hadoop_data/dfs/name</value>
</property>
<property>
    <name>dfs.datanode.data.dir</name>
    <value>file:/home/hadoop/hadoop_data/dfs/data</value>
</property>
</configuration>

core-site.xml
<configuration>
<property>
    <name>hadoop.tmp.dir</name>
    <value>file:/home/hadoop/hadoop_data</value>
</property>
<property>
    <name>fs.defaultFS</name>
    <value>hdfs://0.0.0.0:9000</value>
</property>
</configuration>
mapred-site.xml

<configuration>
<property>
    <name>mapreduce.framework.name</name>
    <value>yarn</value>
</property>
</configuration>

yarn-site.xml

<configuration>
<!-- Site specific YARN configuration properties -->
<property>
    <name>yarn.nodemanager.aux-services</name>
    <value>mapreduce_shuffle</value>
</property>
</configuration>
Hadoop格式化及启动
经过前面的配置我们的hadoop基础配置已经完成了，接下来就是见证奇迹的时刻了，我们需要对Hadoop的namenode进行格式化，然后启动hadoop dfs服务。

NameNode格式化
我们跳转到hadoop的bin目录，并执行格式化命令
cd /usr/local/hadoop/bin
./hdfs namenode -format

jps

其他的: 得先安装jdk  
关于用户: 新建hadoop用户的命令  

#### 安装 配置hive

export HADOOP_HOME=/usr/local/hadoop

wget https://mirrors.tuna.tsinghua.edu.cn/apache/hive/hive-2.3.9/apache-hive-2.3.9-bin.tar.gz

hive-env.sh

# 大家注意修改成自己的配置目录及版本
export JAVA_HOME=/soft/home/jdk1.8.0_191
export HADOOP_HOME=/soft/home/hadoop-2.8.5
export HIVE_CONF_DIR=/soft/home/apache-hive-2.3.6-bin/conf

~~~xml
hive-site.xml
<configuration> 
  <property> 
    <name>javax.jdo.option.ConnectionURL</name>  
    <value>jdbc:mysql://localhost:3306/metastore?createDatabaseIfNotExist=true</value>  
    <description>the URL of the MySQL database</description> 
  </property>  
  <property> 
    <name>javax.jdo.option.ConnectionDriverName</name>  
    <value>com.mysql.jdbc.Driver</value>  
    <description>Driver class name for a JDBC metastore</description> 
  </property>  
  <property> 
    <name>javax.jdo.option.ConnectionUserName</name>  
    <value>hive</value> 
  </property>  
  <property> 
    <name>javax.jdo.option.ConnectionPassword</name>  
    <value>imooc@123</value> 
  </property>  
  <property> 
    <name>hive.metastore.warehouse.dir</name>  
    <value>/hive/warehouse</value> 
  </property>  
  <property> 
    <name>hive.exec.scratchdir</name>  
    <value>/hive/tmp</value> 
  </property>  
  <property> 
    <name>hive.querylog.location</name>  
    <value>/hive/log</value> 
  </property>  
  <property> 
    <name>hive.metastore.schema.verification</name>  
    <value>false</value> 
  </property> 
</configuration>
~~~


在hive-site.xml的配置当中，我们主要设置了hive元数据库的链接信息，我们使用的是mysql数据库，所以制定了mysql数据库的jdbc地址、驱动、用户和密码等等。 还配置了Hive在HDFS上的一些相关的目录。接下来我们需要在HDFS上创建相关的目录。

export JAVA_HOME=/soft/home/jdk1.8.0_191
export HADOOP_HOME=/soft/home/hadoop-2.8.5
export HIVE_CONF_DIR=/soft/home/apache-hive-2.3.6-bin/conf

hdfs dfs -mkdir /hive/warehouse
hdfs dfs -mkdir /hive/log
hdfs dfs -mkdir /hive/tmp
hdfs dfs -chmod -R 777 /hive


#### idea 连接docker开发环境
https://www.jianshu.com/p/41c55bfbcc68

~~~shell
hive安装目录  
    /home/hadoop/soft/apache-hive-2.3.9-bin  

hadoop安装目录  
    /usr/local/hadoop  

java安装目录  
    /usr/bin/java  

jdk安装目录
    /usr/local/jdk

hive配置目录  
    HIVE_CONF_DIR=/home/hadoop/soft/apache-hive-2.3.9-bin/conf

hdfs目录

~~~

在HIVE_HOME目录下新建:
    hive-env.sh  
    hive-site.xml  

#### hive内置函数和自定义函数
``UDF: User-Defined Function``

udf  user-defined functions  

udaf  aggregate functions  

udtf  table-generating functions  


- 注册临时函数
hdfs dfs -mkdir /udfs
hdfs dfs -put filename /udfs

in hive:
add jar hive-udf-test-1.0-SNAPSHOT.jar;

CREATE TEMPORARY FUNCTION avg_score as "com.imooc.code.hive.udf.AvgScore";

drop temporary function udf_name;

Q: 如何注册永久函数?
create function UDF_NAME as 'com.imooc.code.hive.udf.AvgScore' using jar 'hdfs:///udfs/hive-udf-test-1.0-SNAPSHOT.jar';

#### Hive 存储格式

|| 优点 | 缺点 | 
|-|-|-|
|TextFile|简单|不支持分片|
|Sequence File|可压缩、可分割|需要合并、不易查看|
|OrcFile|分片、按列存储||

列式存储: 对于更新和插入的支持不好, 对查询友好

OrcFile: 列式存储格式的一种实现(也是一个项目)  
    带索引
    支持有限的ACID, 查询效率较Parquet高

#### Hive 面试知识点总结

SQL到Hive的流程

    在SQL层面跟MySQL大同小异;
        Rule-Based Optimizer  
        Cost-Based Optimizer  

    在存储层要参考HDFS

数据倾斜 

    解决方式:  
        参数调节: hive.map.aggr, hive.groupby.skewindata (具体不清楚)  
        调整SQL语句: Join方式、空值、大小表join  

#### 离线数仓 VS 实时数仓

上面讲的都是离线数仓:

    对外提供T+1数据仓库: 离线数仓 (不是硬性标准 )

    ODS、DWD、DM
    ODS: Operational Data Store  
        运营数据存储  
    DWD: Data Warehouse Detail  
        数据明细层  

Lambda架构:  
    在离线数据架构基础上加入一个加速层, 使用流处理技术完成实时性较高的指标计算  

    批处理层+加速层  

    实际场景: 广告、推荐  

    缺点:  

Kappa架构:  
    本身就是以实时事件为核心的

    缺点: 
        效率没有批处理高  
        每次计算历史都要重放数据  
        跟批处理的结果可能不一致  

实际场景:
    Lambda和Kappa混合

#### 实际架构

阿里菜鸟架构
    Spark/Flink -> AnalyticDB for MySQL -> DataV大屏

美团实时数仓

    ODS层 (binglog/消息队列/系统日志) -> DWD (数据明细层)
        -> 数据汇总层 -> 应用层

OLAP 领域Presto、Druid、Clickhouse、Greenplum
