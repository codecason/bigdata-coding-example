# cat << EOF >> ~/.bashrc
# export JAVA_HOME=/usr/local/jdk
# export PATH=$JAVA_HOME/bin:$JAVA_HOME/jre/bin:$PATH
# EOF

export HADOOP_HOME=/usr/local/hadoop

cd ${HADOOP_HOME}/bin && ./hdfs namenode -format
cd ${HADOOP_HOME}/sbin && ./start-dfs.sh && ./start-yarn.sh # 启动YARN
jps

# 创建hive的HDFS(不是宿主机的)
hdfs dfs -mkdir /hive/ && \
hdfs dfs -mkdir /hive/warehouse && \
hdfs dfs -mkdir /hive/log && \
hdfs dfs -mkdir /hive/tmp && \
hdfs dfs -chmod -R 777 /hive

