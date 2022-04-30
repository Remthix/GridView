package com.sdremthix.com.gridview.domain;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public final class KDSearchTree {

    public static final class Node {
        private Node left = null;
        private Node right = null;

        final int numDimensions;

        final NodePoint nodePoint;

        public Node(List<Float> points) {
            this.nodePoint = new NodePoint(points);
            numDimensions = points.size();
        }

        public Node(final NodePoint nodePoint) {
            this.nodePoint = nodePoint;
            this.numDimensions = nodePoint.values.size();
        }

        public void add(Node node) {
            this.add(node, 0);
        }

        private void add(Node node, int k) {
            if (node.nodePoint.get(k) < nodePoint.get(k)) {
                if (left == null) {
                    left = node;
                } else {
                    left.add(node, k + 1);
                }
            } else {
                if (right == null) {
                    right = node;
                } else {
                    right.add(node, k + 1);
                }
            }
        }

        public NodePoint getNodePoint() {
            return nodePoint;
        }

        @Override
        public String toString() {
            return "Node node point data: " + this.nodePoint.toString();
        }
    }

    /**
     * A single data point with n different properties.
     */
    public static final class NodePoint {
        final List<Float> values;

        public NodePoint(List<Float> values) {
            this.values = values;
        }

        public Float get(int depth) {
            return values.get(depth % values.size());
        }

        public int size() {
            return values.size();
        }

        @Override
        public String toString() {
            return "NodePoint{" +
                    "values=" + values +
                    '}';
        }
    }

    Node root = null;
    final int numDimensions;

    public KDSearchTree(int numDimensions) {
        this.numDimensions = numDimensions;
    }

    public KDSearchTree(Node root, int numDimensions) {
        this.root = root;
        this.numDimensions = numDimensions;
    }

    public KDSearchTree(List<List<Float>> points) {
        numDimensions = points.get(0).size();
        root = new Node(points.get(0));
        int numPoints = points.size();
        for (int i = 1; i < numPoints; i++) {
            List<Float> point = points.get(i);
            Node node = new Node(point);
            root.add(node);
        }
    }

    public void add(Node point) {
        if (root == null) {
            root = point;
        } else {
            root.add(point);
        }
    }

    public void add(List<Float> point) {
        Node node = new Node(point);
        if (root == null) {
            root = node;
        } else {
            root.add(node);
        }
    }

    public boolean isEmpty() {
        return root == null;
    }

    public void clear() {
        root = null;
    }

    /**
     * Tree traversal
     *
     * @param targetPoint target search param. Represents a single point.
     * @return The nearest existing point in the structure.
     */
    public Node findNearestNeighbor(NodePoint targetPoint) {
        return nearestNeighbor(root, targetPoint, 0);
    }

    private Node nearestNeighbor(Node root, NodePoint targetPoint, int depth) {
        if (root == null) {
            return null;
        }

        Node nextBranch;
        Node otherBranch;

        if (targetPoint.get(depth) < root.nodePoint.get(depth)) {
            nextBranch = root.left;
            otherBranch = root.right;
        } else {
            nextBranch = root.right;
            otherBranch = root.left;
        }

        Node temp = nearestNeighbor(nextBranch, targetPoint, depth + 1);
        Node best = closest(temp, root, targetPoint);

        long radiusSquared = distSquared(targetPoint, best.nodePoint);
        //Check the other side too
        float dist = targetPoint.get(depth) - root.nodePoint.get(depth);

        if (radiusSquared >= dist * dist) {
            temp = nearestNeighbor(otherBranch, targetPoint, depth + 1);
            best = closest(temp, best, targetPoint);
        }

        return best;
    }

    private Node closest(Node n0, Node n1, NodePoint nodePoint) {
        if (n0 == null) {
            return n1;
        }
        if (n1 == null) {
            return n0;
        }

        float dist0 = distSquared(n0.nodePoint, nodePoint);
        float dist1 = distSquared(n1.nodePoint, nodePoint);

        if (dist0 < dist1) {
            return n0;
        } else {
            return n1;
        }
    }

    public float dist(NodePoint np0, NodePoint np1) {
        return (float) Math.sqrt(distSquared(np0, np1));
    }

    private long distSquared(NodePoint np0, NodePoint np1) {
        long total = 0;
        int numDimensions = np0.values.size();

        for (int i = 0; i < numDimensions; i++) {
            float diff = Math.abs(np0.get(i) - np1.get(i));
            total += Math.pow(diff, 2);
        }

        return total;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        Queue<Node> nodeQueue = new LinkedList<>();
        nodeQueue.add(this.root);

        while (!nodeQueue.isEmpty()) {
            int size = nodeQueue.size();
            for (int i = 0; i < size; i++) {
                Node n = nodeQueue.poll();
                if (n != null) {
                    stringBuilder.append(n.nodePoint).append(", ");
                    nodeQueue.add(n.left);
                    nodeQueue.add(n.right);
                } else {
                    stringBuilder.append("null, ");
                }
            }
            stringBuilder.append("\n");
        }
        return "KDSearchTree: " + stringBuilder;
    }
}
