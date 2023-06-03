package de.tum.node;

import org.w3c.dom.Node;

/**
 * ClassName: DeleteNode
 * Package: de.tum.service
 * Description: ECS deletes nodes to the cluster based on hash values
 *
 * @Author Weijian Feng, Jingyi Jia, Mingrun Ma
 * @Create 2023/6/1 23:20
 * @Version 1.0
 */
public class DeleteNode {
    public void deleteNode(Node node) {
        nodes.remove(node);
        updateRing();
    }
}
