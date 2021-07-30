# cat << EOF >> ~/.bashrc
# export JAVA_HOME=/usr/local/jdk
# export PATH=$JAVA_HOME/bin:$JAVA_HOME/jre/bin:$PATH
# EOF

export HADOOP_HOME=/usr/local/hadoop

cd ${HADOOP_HOME}/bin && ./hdfs namenode -format # 格式化HDFS
cd ${HADOOP_HOME}/sbin && ./start-dfs.sh && ./start-yarn.sh # 启动YARN
jps

# 创建hive的HDFS(不是宿主机的)
hdfs dfs -mkdir /hive/ && \
hdfs dfs -mkdir /hive/warehouse && \
hdfs dfs -mkdir /hive/log && \
hdfs dfs -mkdir /hive/tmp && \
hdfs dfs -chmod -R 777 /hive

# hive start
前置条件:
${HIVE_HOME}/conf/hive-site.xml

hive --service metastore &
hive --service hiveserver2 &

# 最终的 bashrc 配置结果
export JAVA_HOME=/usr/local/jdk
export HADOOP_HOME=/usr/local/hadoop
export HIVE_HOME=/home/hadoop/soft/apache-hive-2.3.9-bin
export PATH=${JAVA_HOME}/bin:$PATH
export PATH=${JAVA_HOME}/jre/bin:$PATH
export PATH=${HIVE_HOME}/bin:$PATH
export PATH=${HADOOP_HOME}/bin:$PATH
export PATH=${HADOOP_HOME}/sbin:$PATH

# hive连接
hive
> show tables;

内部表和外部表  
    Use external tables when files are already present or in remote locations, and the files should remain even if the table is dropped.


- 分桶表和分区表
    为什么要有分区表?
        创建的时候就有根据指定字段分区的特性, 查询的时候可以减少遍历  

    为什么要有分桶?  
        一个分区内的数据根据字段进行哈希,在join操作等分析操作中可以直接访问桶,再次减少访问数据量;

# 重启metastore
killall metastore  
killall hiveserver2  
