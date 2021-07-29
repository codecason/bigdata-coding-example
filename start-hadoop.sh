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
