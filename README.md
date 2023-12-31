[Chinese REAMDE](docs/README_zh.md)

# Project Overview

## Server and Client

### Server Online CLI Parameters

+ -p: Set the port for server listening
+ -a: Set the IP address for server listening
+ -b: IP:Port of the ECS registry center
+ -d: Directory for persistent data storage
+ -l: Address for storing logs
+ -ll: Log level, such as INFO, ALL, etc.
+ -c: Cache size, e.g., 100 keys
+ -s: Cache update strategy
+ -h: Print help information

## Client Connection API

+ `connect <addrs> <port>`: Connect to a specific IP:Port server
+ `disconnect`: Disconnect from the server
+ `put <key> <val>`: Store data
+ `get <key>`: Retrieve data
+ `send <message>`: The server will echo back the sent message
+ `logLevel <level>`: Adjust log level
+ `help`: View help information
+ `quit`: Quit the client



## Network I/O

During the communication between the ECS registry center and the KVServer servers, their roles will be exchanged once.

The ECS registry center uses a BIO thread pool to provide registration services externally. It starts before all other KVServer nodes and waits for connections from KVServer nodes on a specific IP:Port. When a KVServer node connects, ECS will spawn a dedicated thread to handle the offline work of the KVServer node and then act as a client of the KVServer by constructing an NIO SocketChannel connection to the KVServer. This is followed by metadata synchronization, data transfer, and other operations.

There are two types of I/O communication in KVServer. When a KVServer comes online, it first builds a BIO socket to register with the ECS. At this time, the KVServer acts as a client of the ECS. After registration, when the KVServer acts as a server to provide services externally, it adopts the Java NIO communication mode. We consolidate the requests from ECS and the requests from clients connected to the KVServer into the NIO next.isReadable() detection state. At this point, ECS's role is transformed into a client of the KVServer.

Java NIO is an I/O model provided by Java, introducing a set of new APIs for efficient multiplexed I/O operations. Compared to traditional Java BIO, Java NIO provides a more flexible and high-performance way to perform I/O operations.

The core components of Java NIO include the following:

1. Channel: A channel is the conduit for data transfer and can be used to read and write data. Java NIO provides different types of channels, such as file channels and socket channels.
2. Buffer: A buffer is an object used to store data and enables reading and writing data. Buffers provide a structured way to access data based on different data types (e.g., bytes, characters).
3. Selector: A selector is a mechanism for multiplexing non-blocking channels. It manages multiple channel I/O operations in a single thread. The selector can listen to events on multiple channels and handle them accordingly when events occur.

## Data Storage

### Data Persistence

Data persistence is the process of storing data in non-volatile media (such as hard drives, solid-state drives, flash storage) to ensure data is retained and can be recovered and used after a computer system shutdown or power failure, without data loss.

For this project, considering the small scale of data, we have adopted a simple data persistence solution, which stores each key-value pair in a corresponding file to achieve data persistence.

To improve the efficiency of read and write operations, we have implemented a dual-database architecture: the main database and the backup database. The backup database is stored only on the hard disk and is typically not accessed externally. The main database is stored both on the hard disk and in the cache. When a client writes data, the data is first written to the main data cache and then asynchronously copied to the hard disk directory of the main database and backup database. When performing read operations, the system first checks the main data cache, and if the data is not found in the cache, it queries the data on the hard disk.

With this architecture, we can achieve data persistence and improve the efficiency of read and write operations. The presence of the backup database serves as redundant backup to provide higher data reliability. At the same time, the caching mechanism of the main database speeds up data retrieval and improves system response time.



### Cache Update Strategy

Regarding the replacement strategy for key-value pairs in the cache, the following three strategies can be implemented:

1. First-In-First-Out (FIFO): Key-value pairs that enter the cache first will be replaced based on the order of entry.
2. Least Recently Used (LRU): Key-value pairs that have been least recently used, based on their access time, will be replaced. We use Java's LinkedHashMap to implement the LRU strategy, where the most recently accessed key-value pairs are kept at the head of the linked list. When the cache is full, the key-value pair at the tail of the list is replaced.
3. Least Frequently Used (LFU): Key-value pairs that have been least frequently used, based on their access frequency, will be replaced. We use a hash table combined with a priority queue to implement the LFU strategy, tracking and managing key-value pairs and their corresponding access frequencies. When the cache is full, the key-value pair with the lowest access frequency is replaced.

When a GET request from the client leads to a cache miss, the corresponding key-value pair will be searched on the disk and moved to the cache. If the cache is full, based on the currently selected strategy, a specific key-value pair will be selected for replacement and swapped to the disk (which may involve updates on the disk). Similarly, when a PUT request is received from the client and the cache is full, a specific key-value pair will be selected for replacement based on the selected strategy.



## Distributed Cluster

### Overview

<img src="docs/asset/整体架构.png">

##### Components

+ Registry Center: Cluster management
+ KVServer: Server nodes
	+ KVStore: Database

### Bootstrap Server Registry

In distributed systems, a bootstrap server refers to a special server used to bootstrap (initialize) new nodes joining the system or remove nodes that need to be taken offline. It serves as a central node for managing various nodes in the hash ring and plays a crucial role, including updating the hash ring, guiding data migration among nodes, and monitoring the status of all nodes. By having a bootstrap server and its functionality, distributed systems can manage the process of node joining and exiting more stably and reliably, optimize data distribution and access efficiency, and provide a high-performance and scalable system architecture.

### Functionality

* Adding a KVServer: When a new KVServer connects to ECS, the following operations are performed:
  * Determine the position of the new storage server in the ring based on the server's port address used for communication with clients.
  * Recalculate and update the hash ring.
  * Initialize the new storage server using the updated hash ring.
  * Set write locks on successor nodes.
  * Data transfer: Transfer affected data items to the new storage server.
  * Once all affected data has been transferred, send the updated hash ring to all storage servers.
* Removing a KVServer: When a KVServer sends a removal notification to ECS or ECS detects a node failure, the following operations are performed:
  * Add the node to the removal queue in ECS and process them one by one.
  * Recalculate and update the hash ring.
  * Data transfer: Transfer affected data items to new storage servers.
  * Once all affected data has been transferred, send the updated hash ring to the remaining storage servers.
  * Continue shutting down the storage server.
* Monitoring the status of KVServer: ECS assigns a dedicated thread to periodically monitor if servers go offline. Specifically, when `socket.isclose() == true`, it indicates the server is offline, and the appropriate logic for handling the offline event is invoked.



### Cluster Communication

Cluster communication refers to the process of mutual communication and collaboration between multiple nodes in a distributed system through the network. In cluster communication, nodes can be physical servers, virtual machines, containers, etc., connected through the network to enable data transfer, task allocation, coordination, synchronization, and other operations.

In the context of cluster communication, we have implemented two distinct schemes: one employs JSON, while the other leverages the gRPC (gRPC Remote Procedure Call) framework.

In our implementation based on JSON, we use custom string messages for cluster communication. Each message is encoded and decoded as a string and transmitted to other nodes through the network. We use a message builder factory to generate message instances, creating corresponding message objects based on the message type and related data. This allows dynamic generation of different types of messages to meet the requirements of cluster communication.

To handle received messages, we use a unified message parser. The message parser is responsible for parsing received string messages and executing corresponding operations based on the message format and rules. This ensures consistency in message handling and improves code readability and maintainability.

In contrast to the JSON-based approach, the implementation using the gRPC framework offers a more structured and efficient way of achieving cluster communication. gRPC is a high-performance open-source framework that facilitates remote procedure calls (RPCs) across different nodes in a distributed system. Unlike the custom JSON messages, gRPC uses protocol buffers (protobufs) to define the service interface and message formats in a language-agnostic manner. With gRPC, communication is driven by service method calls. Each method call corresponds to a specific operation that can be executed on a remote node. The gRPC framework handles the underlying network communication, serialization, and deserialization of messages, making it a more efficient and reliable choice for cluster communication compared to the custom JSON-based approach.

Please refer to the API documentation for specific communication protocols.

### Transaction

We use Two-Phase Commit (2PC) and Two-Phase Locking (2PL) to ensure the atomicity, consistency, isolation, and durability of database operations, thereby ensuring data correctness and reliability.

When implementing transactions using the Two-Phase Commit (2PC) protocol, coordination occurs between a Transaction Coordinator and Participants. The process involves two phases:

- **Prepare Phase**: The Transaction Coordinator sends a prepare request to all Participants, asking if they are ready to commit the transaction. Participants perform their corresponding operations and respond with information about their preparedness.

- **Commit Phase**: If all Participants are prepared, the Transaction Coordinator sends a commit request, instructing all Participants to commit the transaction. Participants receive the commit request, execute the actual commit operation, and send back a message confirming the completion of the commit.

Through these two phases, the Transaction Coordinator ensures that either all Participants have successfully committed the transaction or all have rolled back the transaction, thereby maintaining transaction consistency and isolation.

In the context of the Two-Phase Locking (2PL) protocol, transaction execution involves two phases:

- **Locking Phase**: The transaction initially acquires all required locks. During this phase, the transaction can acquire locks but cannot release them.

- **Unlocking Phase**: Once the transaction has acquired all necessary locks, it begins executing database operations. After completing its operations, the transaction releases the locks it holds.

### Load Balancing

Load balancing is a technique that distributes the workload, such as network traffic, requests, or computing tasks, across multiple computing resources to improve system performance, reliability, and scalability. Load balancing ensures that each computing resource is utilized properly, avoids resource overload, and enhances system fault tolerance.

Consistent hashing algorithm is applied to our distributed system to address load balancing and node expansion requirements. We plan to introduce virtual nodes in the future.

Consistent hashing is an algorithm used to solve the load balancing problem. Traditional hashing algorithms map resources and requests to a fixed hash table. However, in a distributed environment, when the number of nodes changes, a large number of remappings occur, which negatively affects the stability and performance of the system.

Consistent hashing algorithm maps resources and requests to a hash ring, represented as a circular structure. Each resource node has a corresponding position on the ring, and requests are mapped to the nearest resource node based on a hash function. When a new request arrives, the consistent hashing algorithm maps it to the resource node closest to its position.

Consistent hashing solves the issue of remapping a large portion of requests when nodes change in traditional hashing algorithms. When a node is added or removed from the system, only requests in the vicinity of that node are affected, while the mapping relationship between other nodes and requests remains unchanged. By adjusting the positions of nodes on the ring, the impact of adding or removing nodes is minimized.

As shown in the diagram, consistent hashing effectively handles node scaling up or down.

<img src="docs/asset/一致性哈希的扩容和缩容.drawio.png">

To further improve system scalability and fault tolerance, we plan to extend our distributed system to support virtual nodes in the future. Virtual nodes will be mapped to physical nodes and enable finer-grained distribution of data and requests, further enhancing load balancing effectiveness.



### Fault Tolerance

Fault tolerance is the ability of a distributed system to handle node failures or errors, ensuring system availability and reliability. It aims to enable the system to continue normal operation in the event of node failures and minimize the impact on users as much as possible through backup mechanisms, fault detection, and recovery strategies.

Selection of Backup Nodes: In our implementation, we have chosen a simple and effective backup strategy, which designates the previous node on the hash ring as the backup node for each current node. This means that each node has a backup node to store the same data replica.

Backup Node Data Synchronization: When a write request occurs, we asynchronously propagate the data update to the backup node. Specifically, when a write request arrives at the current node, we first write the data to the storage of the current node and then asynchronously pass the same write operation to the backup node to maintain data eventual consistency. (See AP eventual consistency)

Fault Detection and Recovery: In our implementation, we use polling to detect node disconnections. We periodically poll the connection status of each thread to quickly detect node failures. Once a failure is detected, the central node adds the faulty node to the pending removal node list and processes each node in the list: remove the node from the hash ring and update the hash ring of all nodes. Additionally, since we use a backup node strategy based on the hash ring, the corresponding backup node automatically becomes responsible for the data and takes over the work of the failed node. This means that after a failure occurs, the system can continue to operate seamlessly.

We also plan to implement other fault detection and recovery strategies, such as heartbeat mechanism and fault recovery protocols, to further improve system reliability and fault tolerance.



### Consistency Guarantees

In distributed systems, data is often replicated on multiple nodes to improve system availability and fault tolerance. When performing update operations on data, due to network latency, node failures, or concurrent operations, data replication may not be instantaneous. Therefore, different nodes in the system may have different data replicas during the data replication process. The eventual consistency model assumes that, without new update operations, after a period of synchronization and data exchange, the system will eventually reach a consistent state. This means that if no new write operations occur, all nodes in the system will eventually converge to the same data replica.

In this system, CompletableFuture is used for asynchronous replication, which has the advantages of low latency and high performance. However, it also introduces some issues.

Our cluster cannot guarantee strong consistency. Although strong consistency is generally ensured in most cases, there is a possibility of data loss in an extreme scenario: if a client writes some data, the master node confirms the write, but before propagating the newly written data to any replica and successfully flushing it to the disk, the master crashes. As a result, the write is permanently lost.

Improving consistency can be achieved by forcing the database to flush data to the disk before replying to the client, but this often leads to low performance. Therefore, it is a trade-off between performance and consistency.



# Future Plan

The single point of failure in ECS needs to be addressed by consensus algorithms like Raft or Paxos, which distribute the functionality of ECS directly among the servers. Implementing Raft or similar algorithms directly can be complex, so for now, an ECS registry center is abstracted to handle this task.

The current data persistence method is suitable for small-scale data. For large-scale data, we plan to adopt a tabular storage approach inspired by relational databases to store data.