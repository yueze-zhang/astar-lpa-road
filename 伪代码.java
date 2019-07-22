void main() {
  initialize();
  while (true) {
    computeShortestPath();
    while (!hasCostChanges())
      sleep;
    for (edge : getChangedEdges()) {
        edge.setCost(getNewCost(edge));
        updateNode(edge.endNode);
    }
  }
}

void initialize() {
  queue = new PriorityQueue();
  for (node : getAllNodes()) {
    node.g = INFINITY;
    node.rhs = INFINITY;
  }
  start.rhs = 0;
  queue.insert(start, calculateKey(start));
}

/** 优先级队列中扩展节点. */
void computeShortestPath() {
  while ((queue.getTopKey() < calculateKey(goal)) || (goal.rhs != goal.g)) {
    node = queue.pop();
    if ((node.g > node.rhs) {
      node.g = node.rhs;
      for (successor : node.getSuccessors())
        updateNode(successor);
    } else {
      node.g = INFINITY;
      updateNode(node);
      for (successor : node.getSuccessors())
        updateNode(successor);
    }
  }
}

/** 重新计算节点的rhs并将其从队列中删除。.
 * 如果节点在局部变得不一致，则使用其新key(re-)将节点插入队列。. */
void updateNode(node) {
  if (node != start) {
    node.rhs = INFINITY;
    for (predecessor: node.getPredecessors())
      node.rhs = min(node.rhs, predecessor.g + predecessor.getCostTo(node));
    if (queue.contains(node))
      queue.remove(node);
    if (node.g != node.rhs)
      queue.insert(node, calculateKey(node));
  }
}

int[] calculateKey(node) {
  return {min(node.g, node.rhs) + node.getHeuristic(goal), min(node.g, node.rhs)};
}
