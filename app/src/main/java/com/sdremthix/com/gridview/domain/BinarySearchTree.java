package com.sdremthix.com.gridview.domain;

import java.util.Arrays;

/**
 * Binary search tree implementation.
 * Adjusted for finding the nearest node.
 */
public final class BinarySearchTree {

    class Node {
        int key;
        Node left;
        Node right;

        public Node(int position) {
            this.key = position;
            this.left = null;
            this.right = null;
        }
    }

    Node root = null;

    public BinarySearchTree() {
    }

    public void deleteKey(int key) {
        root = deleteRecursive(root, key);
    }

    private Node deleteRecursive(Node root, int key) {
        //tree is empty
        if (root == null) {
            return root;
        }

        //Tree traversal
        if (key < root.key) {
            root.left = deleteRecursive(root.left, key);
        } else if (key > root.key) {
            root.right = deleteRecursive(root.right, key);
        } else {
            //Root has one leaf
            if (root.left == null) {
                return root.right;
            } else if (root.right == null) {
                return root.left;
            }

            //Root has two leaf nodes
            root.key = minValue(root.right);

            //delete inorder successor
            root.right = deleteRecursive(root.right, root.key);
        }
        return root;
    }

    private int minValue(Node root) {
        int minimum = root.key;
        //find min
        while (root.left != null) {
            minimum = root.left.key;
            root = root.left;
        }

        return minimum;
    }

    public void insert(int key) {
        root = insertRecursive(root, key);
    }

    private Node insertRecursive(Node root,int key){
        //tree is empty
        if(root == null){
            root = new Node(key);
            return root;
        }

        //traverse tree
        if(key< root.key){
            //insert left subtree
        //    Arrays.binarySearch()
        }
        return root;
    }
}
