
`ssh-keygen`

`cat id_rsa.pub  >> authorized_keys`

`chmod 600 authorized_keys`

## 配置Java环境变量

`vi /etc/profile`

```text
export JAVA_HOME=/data/jdk1.8.0_251
export JRE_HOME=${JAVA_HOME}/jre
export CLASSPATH=.:${JAVA_HOME}/lib:${JRE_HOME}/lib
export PATH=${JAVA_HOME}/bin:$PATH
```

`source /etc/profile`

## 安装Hadoop

`core-site.xml`, `hadoop-env.sh`, `hdfs-site.xml`

`cd etc/hadoop`

`vi hadoop-env.sh` 修改`JAVA_HOME`

`vi hdfs-site.xml`

```text
<property>
    <name>dfs.replication</name>
    <value>1</value>
</property>
<property>
    <name>dfs.namenode.name.dir</name>
    <value>file:/hadoop_data/dfs/name</value>
</property>
<property>
    <name>dfs.datanode.data.dir</name>
    <value>file:/hadoop_data/dfs/data</value>
</property>
```

`vi core-site.xml`

```text
<property>
    <name>hadoop.tmp.dir</name>
    <value>file:/hadoop_data</value>
</property>
<property>
    <name>fs.defaultFS</name>
    <value>hdfs://0.0.0.0:9000</value>
</property>
```

`cd bin`

./hdfs namenode -format

`cd ../sbin`

./start-dfs.sh

`cd ../bin`

./hdfs dfs -ls /

./hdfs dfs -mkdir /test

## 安装HBASE

`tar zxf hbase-1.2.7-bin.tar.gz`

`hbase-env.sh`, `hbase-site.xml`

`vi hbase-env.sh` 修改 `JAVA_HOME`

`vi hbase-site.xml`

```text
<property>
    <name>hbase.rootdir</name>
    <value>hdfs://localhost:9000/hbase</value>
</property>
<property>
    <name>hbase.zookeeper.property.dataDir</name>
    <value>/hadoop_data/zookeeper</value>
</property>
<property>
    <name>hbase.cluster.distributed</name>
    <value>true</value>
</property>
```

`cd ../bin`

`./hbase shell` 

status
