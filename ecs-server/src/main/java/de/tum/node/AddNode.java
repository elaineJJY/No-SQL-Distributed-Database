package de.tum.node;

import org.w3c.dom.Node;

/**
 * ClassName: AddNode
 * Package: de.tum.service
 * Description: ECS adds nodes to the cluster based on hash values
 *
 * @Author Weijian Feng, Jingyi Jia, Mingrun Ma
 * @Create 2023/6/1 23:20
 * @Version 1.0
 */
public class AddNode {
    public void addNode(Node node) {
        nodes.add(node);
        updateRing();
    }
}
