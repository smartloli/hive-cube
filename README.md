# Hive Cube

This is a data export platform based on hive. At the same time, it also integrates the functions of Hadoop cluster resource monitoring management, as well as the query function of MySQL, HBase and other storage media.

When you install [Hive Cube](http://download.smartloli.org/), you can write hive SQL in accordance with your business logic, and submit tasks to Hadoop cluster computing, and you can export the results of calculations. At the same time, we can monitor and manage the resources consumed by the task in the process of operation. In addition, you can modify, delete, or set the task to be a scheduled task. This helps facilitate the export of the data we need. At the same time, it also allows us to view the health status of the cluster and the consumption of resources at any time.

The system can use SQL to filter data from HBase quickly through rowkey conditional filtering to get the data we want.

Here are a few Hive Cube system screenshots:

# List of Consumer Groups & Active Group Graph
![Consumer & Active Graph](https://ke.smartloli.org/res/consumer@2x.png)

# List of Topics Detail
![Topics](https://ke.smartloli.org/res/list@2x.png)

# Consumer & Producer Rate Chart
![Rate Chart](https://ke.smartloli.org/res/consumer_producer_rate@2x.png)

# Kafka Offset Types

Kafka is designed to be flexible on how the offsets are managed. Consumer can choose arbitrary storage and format to persist kafka offsets. Kafka Eagle currently support following popular storage format:
  * Zookeeper. Old version of Kafka (0.8.2 before) default storage in Zookeeper.
  * Kafka. New version of Kafka (0.10.0 in the future) default recommend storage in Kafka Topic(__consumer_offsets).
  
Each runtime instance of Kafka Eagle can only support a single type of storage format.

# Quickstart

Please read [Hive Cube Install](https://ke.smartloli.org/2.Install/2.Installing.html) for setting up and running Hive Cube.

# Deploy

The project is a maven project that uses the Maven command to pack the deployment as follows:
```bash
mvn clean && mvn package
```
# More Information

Please see the [Hive Cube Manual](https://ke.smartloli.org) for for more information including:
  * System environment settings and installation instructions.
  * Information about how to use script command.
  * Visual group,topic,offset metadata information etc.
  * Metadata collection and log change information.
 
# Contributing

The Hive Cube is released under the Apache License and we welcome any contributions within this license. Any pull request is welcome and will be reviewed and merged as quickly as possible.

Since this is an open source tool, please comply with the relevant laws and regulations, the use of civilization.

# Committers

Thanks to the following members for maintaining the project.

|Alias |Github |Email |
|:-- |:-- |:-- |
|smartloli|[smartloli](https://github.com/smartloli)|smartloli.org@gmail.com|