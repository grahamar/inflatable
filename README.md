inflatable
==========

Inflatable Raft - A Scala based PnP leader election library for use in AWS, built on [Akka](http://akka.io/) & using the [Raft](http://raftconsensus.github.io/) 
algorithm to come to a consensus regarding a leader.

Designed for use in a distributed Play application, deployed in an auto-scaling cluster in Amazon EC2 (**in a single region only**).
Uses AWS's auto-scaling java client to discover EC2 instances and creates an akka-cluster from the auto-scaling group members.
A partial implementation of the [Raft](http://raftconsensus.github.io/) algorithm is then used to perform leader election.

Settings
-------
Settings read from `application.conf` at the root of the classpath, or as JVM run parameters.
- akka.port - The port to look for akka-cluster instances on.
- inflatable.local - `true` to run locally and bypass AWS discovery.
- inflatable.single-node-cluster - `true` to set leader even if only local node is available
- aws.credentials - `access-key` & `secret-key` to pass to the AWS client for EC2 instance discovery

License
-------

*Apache 2.0*

Links & kudos
-------------

* [akka-ec2 - Example setup of an Akka cluster in an Amazon EC2 AutoScaling group](https://github.com/chrisloy/akka-ec2)
* [akka-raft - An akka-cluster ready implementation of the raft consesus alg](https://github.com/ktoso/akka-raft)
