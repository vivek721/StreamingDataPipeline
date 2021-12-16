# Streaming_data_pipeline
## Overview
End to End streaming data pipeline by designing and implementing an actor-model service using Akka that ingests data in real time and delivers it via an event-based service Kafka that streams the data to Spark for further processing and triggers a AWS email to target end users based on the insights given.

Please find the Walk-through video in the following link: 

[Walk-through](https://youtu.be/w-05sX_oT5o)  

## Team Members
Gnanamanickam Arumugaperumal (garumu3@uic.edu)

Vivek Mishra (vmishr8@uic.edu)

Sriman Cherukuru (scheru4@uic.edu)

## Prerequisites

* JDK >= 1.8, sbt >= 1.5, Unix-based OS
* AWS account to create a EC2 instance to run the files.
* Create MSK cluster that enables you to build and run applications that use Apache Kafka for streaming data.
* Setup AWS email service (SES) in your AWS account .

## Installation

* Clone the GIT repository by using git clone https://github.com/Gnanamanickam/Streaming_data_pipeline
* Run the following commands in the console

```
sbt clean compile test    # to run test
```
```
sbt clean compile run     # to start execution
```

* If you are using IntellIj clone the repository by using "Check out from Version Control and then Git."

* The scala version should be set in the Global Libraries under Project Structure in Files .
* The SBT configuration should be added by using Edit Configurations and then simulations can be ran in the IDE .
* Setup AWS credentials in your Environmental Variables to use AWS email Service .

## Execution

### File Monitoring System

Created a real-time file monitoring service using Java NIO 
that creates events in response to changes in the files in the filesystem.

Java NIO performs non-blocking IO operations. It means, the date is read in realtime whenever its readily available . Thread can ask a channel to read the data from a buffer and the thread can do another process during that period and continue again from the last point .
The File Monitoring system has a File Listener which has a onModified method and informs the FileWatcher method in case of any changes in the file and notify the listeners . The
File monitoring system has to be put inside Akka actors to monitor the file in a distributed environment .

### Akka 

The Actor Model provides a higher level of abstraction for writing concurrent and distributed systems. The Actor encapsulates its state and part of the application logic.
Each actor will have a unique address and communicates with the other actor through the same address .
The actor model has a extractor and a watcher file where in watcher file we will monitor the file for any changes and the extractor send the data to spark via kafka .

Akka actors write asynchronous code without the need for locks and synchronization.

### Kafka

Kafka is a messaging System to transfer data from one application to another by using Producers and Consumers .
It is s based on the concept of reliable message queuing. Messages are queued asynchronously between client applications and messaging system .
It is is built on top of the ZooKeeper synchronization service .

The major Components of Kafka are Producers, Topic , Broker and Consumer .

#### Producer 

Producers are the publisher of messages to one or more Kafka topics. Producers send data to Kafka brokers

#### Topic

A stream of messages belonging to a particular category is called a topic. Data is stored in topics. It is split into partitions.
To create a topic usse the below command.

```
kafka-topics.sh --create --zookeeper "ZookeeperConnectString" --replication-factor 2 --partitions 1 --topic AWSKafkaTutorialTopic
```

#### Broker

Brokers are responsible for maintaining the sent data. It can have as many partitions as required per topic.

#### Consumer

Consumers read data from brokers. It subscribes to one or more topics and consume published messages.

#### ZooKeeper

ZooKeeper is used for managing and coordinating Kafka broker and is mainly used to notify producer and consumer about the presence of broker in the Kafka system or failure of the broker in the Kafka system.
To start the zookeeper in local
```
.\bin\windows\zookeeper-server-start.bat .\config\zookeeper.properties
```
To start the kafka server in local

```
.\bin\windows\kafka-server-start.bat .\config\server.properties
```
### Spark

Spark is used for large scale data processing in real time . It can fetch data from multiple resources like kafka and mesos and can be executed via terminal or APIs in code .
To execute in shell command

```
./bin/spark-shell --master local[2]
```

For configuring spark in scala code and creating a spark stream , we have to create a conf

```
val conf = new SparkConf()
.setMaster("local[2]")
.setAppName("Spatk")
val sc = new SparkContext(conf)
```

This spark context should be used for spark operations

### Steps 

1. Create an EC2 instance and upload the LogFileGenerator and run it in EC2 instances in a periodic time .
2. Create MSK cluster and configure it in the ec2 instance with IAM access and also set the AWS credentials in environmental variable in EC2 instance.
3. List the cluster using aws command and copy the zookeeper path and create a cluster .
4. Copy the kafka server address from the created MSK cluster and modify the application.conf file.
5. Now the logFileGenerator running will be monitored in the EFS file system by the akka actor and it triggers a data to Spark from kafka producer to consumer .
6. The consumer receives the data in Spark application and do the necessary operations and triggers a AWS email to the targeted client address .

Please find the Walk-through video in the following link:

[Walk-through Video](https://youtu.be/w-05sX_oT5o)  


