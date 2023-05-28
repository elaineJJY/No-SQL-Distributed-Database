# Background

## *Consistent Hashing*

## *BASE*

# Components

## *ECS*

### Bootstrap server

A bootstrap server in a distributed system refers to a special node or service that is used to bootstrap (initialize) newly joining nodes. It plays a crucial role during system startup or when new nodes join the system

In a distributed system, when a new node joins, it needs to be aware of the existence and configuration of other nodes in order to communicate and collaborate with them. This process is often referred to as the bootstrap process. The bootstrap server acts as a central node or service that provides node discovery and configuration information to assist new nodes in joining the system

When a new node starts up, it contacts the bootstrap server to obtain a list of known nodes in the system, their network addresses, role information, and other relevant details. With this information, the new node can establish connections with other nodes and participate in the overall operation of the system

The bootstrap server can be a standalone server or a specific node within the distributed system. It typically exhibits stability and high availability to ensure the reliability and scalability of the system. In some systems, specialized services such as ZooKeeper or etcd are used to implement the bootstrap server, providing node management and configuration services

In summary, a bootstrap server in a distributed system is a special node or service used to bootstrap newly joining nodes. It provides node discovery and configuration information, helping new nodes establish connections and collaborate with other nodes in the system

### Add KVServer

### Remove KVServer

### Functionality

## *KVServer*

The position of a particular storage server is calculated by hashing its address and port (i.e. `<IP>:<Port>`)

### Functionality

## *KVStore*

### Functionality

## *Client*

### Functionality

# Protocol