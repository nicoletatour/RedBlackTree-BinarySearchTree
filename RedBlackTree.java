//DIMITRA CHRISTINA GKARAVELA AM:5051
//NIKOLETA TOUROUNOGLOU AM:5106
//Group:6 ID:19

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
public class RedBlackTree<Key extends Comparable<Key>, Item> {

    BSTreeNode root;        // root of binary search tree

    class BSTreeNode {

        Key key;            // key associated with the item stored at node
        Item item;          // item stored at node
        BSTreeNode left;    // left child
        BSTreeNode right;   // right child
        BSTreeNode parent;  // node's parent
        int height;         // node's height
        int N;              // number of descendants
		boolean isRed ; 
		int data;
        // create new node
        BSTreeNode(Key key, Item item, BSTreeNode parent) {
            this.key = key;
            this.item = item;
            this.parent = parent;
            this.height = 1;
            this.N = 1;
			this.isRed = true ;
        }
    }

    // search for item with key; returns the last node on the search path 
    BSTreeNode searchNode(Key key) {
        BSTreeNode v = root;
        BSTreeNode pv = null; // parent of v
        while (v != null) {
            int c = key.compareTo(v.key);  // compare with the key of node v
            pv = v;
            if (c < 0) {
                v = v.left;
            } else if (c > 0) {
                v = v.right;
            } else {
                return v; // item found; return node that contains it
            }
        }
        return pv; // item not found; return last node on the search path
    }

    // search for item with key
    public Item search(Key key) {
        if (root == null) {
            return null; // tree is empty
        }
        BSTreeNode v = searchNode(key);
        int c = key.compareTo(v.key);
        if (c == 0) {
            return v.item;    // item found
        } else {
            return null;      // item not found
        }
    }

    // return the height of a node x; if x is null return 0
    private int getHeight(BSTreeNode x) {
        if (x == null) {
            return 0;
        } else {
            return x.height;
        }
    }

    // return the number of descendants of a node x; if x is null return 0
    private int getN(BSTreeNode x) {
        if (x == null) {
            return 0;
        } else {
            return x.N;
        }
    }
    
    // update the height and the number of descendants of a node
    private void updateNode(BSTreeNode x) {
        int leftHeight = getHeight(x.left);
        int rightHeight = getHeight(x.right);
        int bf = leftHeight - rightHeight; // balance factor
        if (bf < 0) {
            x.height = rightHeight + 1;
        } else {
            x.height = leftHeight + 1;
        }
        
        int leftN = getN(x.left);
        int rightN = getN(x.right);
        x.N = leftN + rightN + 1;
    }
    
    // update the height v's ancestors
    private void updatePath(BSTreeNode v) {
        BSTreeNode u = v;
        while (u != null) {
            updateNode(u);
            u = u.parent;
        }
    }
    
    // return the height of the binary search tree
    int getTreeHeight() {
        return getHeight(root);
    }

    // insert item with key and return inserted node
    BSTreeNode insertNode(Key key, Item item) {
        if (root == null) { // tree is empty
            root = new BSTreeNode(key, item, null);
			root.isRed=false ;
            return root;
        }
		
        BSTreeNode v = searchNode(key); // v is the last node on the search path
        int c = key.compareTo(v.key);
        if (c == 0) { // key already exists in v; overwrite node's item with new item
            v.item = item;
            return v;			
        }

        BSTreeNode u = new BSTreeNode(key, item, v); // new node becomes child of v
        u.isRed=true;
		if (c < 0) {
            v.left = u;
        } else {
            v.right = u;
			
        }
        return u;
    }

    // insert item with key
    public void insert(Key key, Item item) {
		//System.out.println("Inserting: "+key);
        BSTreeNode v = insertNode(key, item);
		fix(v);
    }
	private BSTreeNode sibling(BSTreeNode y){
		if(y.parent.left!=y){
			return y.parent.left;
		}
		else{
			return y.parent.right;
		}
	}
	private boolean isRed(BSTreeNode v) {
        if (v == null) {
            return false;
        } else {
            return (v.isRed);
        }
    }
	
	private void fix(BSTreeNode x) {
		if (x == root){
			return; 
		}
		BSTreeNode y, z, uncle, w;
		y = x.parent;
		if (!y.isRed) { updatePath(y); return; }
		z =w= y.parent;
		if (x.isRed==y.isRed==true){
			uncle = sibling(y); 
				if (!isRed(uncle)){
					if (z.left==y && y.left==x){
						rotateRight(z);
						y.isRed=false;
						z.isRed=true;
						updateNode(z);
						updateNode(y);
						if(y.parent==null){root=y;}
						fix(y);
					}else if(z.left==y && y.right==x){
						rotateLeft(y);
						rotateRight(z);
						z.isRed=true;
						x.isRed=false;
						updateNode(x);
						updateNode(z);
						updateNode(y);
						if(x.parent==null){root=x;}
						fix(x);
					}else if(z.right==y && y.left==x){
						rotateRight(y);
						rotateLeft(z);
						z.isRed=true;
						x.isRed=false;
						updateNode(z);
						updateNode(x);
						updateNode(y);
						if(x.parent==null){root=x;}
						fix(x);
					}else if(z.right==y && y.right==x){
						rotateLeft(z);
						y.isRed=false;
						x.isRed=true;
						z.isRed=true;
						updateNode(x);
						updateNode(z);
						updateNode(y);
						if(y.parent==null){root=y;}
						fix(y);
					}
				}else {
					y.isRed=false;
					z.isRed=true;
					uncle.isRed=false;
					updateNode(y);
					updateNode(z);
					x=y.parent;
					if(x.parent==null){
						x.isRed=false;
						w=x;
					}
					z=y.parent;
					uncle=sibling(y);
					fix(z);
				}
		}
		if (w.parent==null){
			root=w;
		}
		updatePath(w);
	}
	//y =  parent y .... y.right parent = y 
	public void swap(BSTreeNode x, BSTreeNode y){
		if(y ==null){
			System.out.println("Error catched");
			return;
		}
		if(x==null){
			return;
		}
		if (x.parent == null) { 
			root = y;
		} else if (x == x.parent.left) {  
			x.parent.left = y;
		} else {
			x.parent.right = y; 
		}
		y.parent = x.parent; 
	}
	 BSTreeNode deleteNode(Key key) {

        if (root == null) { // tree is empty
            return null;
        }

        BSTreeNode v = searchNode(key);
        int c = key.compareTo(v.key);
        if (c != 0) { // key not found; nothing to delete
            System.out.println("key not found");
            return v;
        }

        BSTreeNode z;       // node to delete; z = successor of v, if v has 2 children; z = v, otherwise
        BSTreeNode parent;  // parent of z
        BSTreeNode child;   // only child of z that may not be null
        if (v.left == null) { // v has no left child
            System.out.println("node with key " + key + " has no left child; can be deleted");
            z = v;
            parent = v.parent;
            child = v.right;
        } else if (v.right == null) { // v has no right child
            System.out.println("node with key " + key + " has no right child; can be deleted");
            z = v;
            parent = v.parent;
            child = v.left;
        } else { // find v's sucessor in the right subtree of v
            z = min(v.right);  // successor of v
            System.out.println("node with key " + key + " has 2 children");
            System.out.println("successor node has key " + z.key);
            parent = z.parent;
            child = z.right;

            // copy z's information to v
            v.item = z.item;
            v.key = z.key;
        }

        if (parent == null) { // z == v == root
            root = child;
            root.parent = null;
            return root;
        }

        if (parent.left == z) { // link parent to child
            parent.left = child;
        } else {
            parent.right = child;
        }
        if (child != null) { // link child to parent
            child.parent = parent;
        }

        return parent;
    }
	public void fixDelete(BSTreeNode x){
		BSTreeNode s;
		while(x!= root && x.isRed==false){
			if(x==x.parent.left){
				s = x.parent.right;
				if(s.isRed==true){
					s.isRed=false;
					x.parent.isRed=true;
					rotateLeft(x.parent);
					s = x.parent.right;
				}
				if (s.left.isRed == false && s.right.isRed ==false) {
					s.isRed = true;
					x = x.parent;
				}else{
					if(s.right.isRed==false){
						s.left.isRed=false;
						s.isRed = true;
						rotateRight(s);
						s = x.parent.right;
					}
					s.isRed = x.parent.isRed;
					x.parent.isRed = false;
					s.right.isRed = false;
					rotateLeft(x.parent);
					x = root;
				}
			}else{
				s = x.parent.left;
				if (s.isRed == true) {
					s.isRed = false;
					x.parent.isRed = true;
					rotateRight(x.parent);
					s = x.parent.left;
				}
				if (s.left.isRed == false && s.right.isRed == false) {
					s.isRed = true;
					x = x.parent;
				}else{
					if (s.left.isRed == false){
						s.right.isRed = false;
						s.isRed = true;
						rotateLeft(s);
						s = x.parent.left;
					}
					s.isRed = x.parent.isRed;
					x.parent.isRed = false;
					s.left.isRed = false;
					rotateRight(x.parent);
					x = root;
				}
			}
		}
		x.isRed = false;
	}
	
	public void delete(Key key) {
        BSTreeNode v = deleteNode(key);
        fixDelete(v);
    }

	BSTreeNode min(BSTreeNode v) {
        BSTreeNode u = v;
        while (u.left != null) {
            u = u.left;
        }
        return u;
    }
	private BSTreeNode rotateLeft(BSTreeNode x) {
		BSTreeNode y = x.right;
		x.right = y.left;
		if(x.right != null) {
			x.right.parent = x;
		}
		y.left = x;
		y.parent = x.parent;
		if(x.parent != null) {
			if(x.parent.left == x) {
				x.parent.left = y;
			} else{
				x.parent.right = y;
			}
		}
		x.parent = y;
		updateNode(x);
		updateNode(y);
		return y;
	}
	private BSTreeNode rotateRight(BSTreeNode y) {
		BSTreeNode x = y.left;
		y.left = x.right;
		if (y.left != null) {
			y.left.parent = y;
		}
		x.right = y;
		x.parent = y.parent;
		if (y.parent != null) {
			if (y.parent.left == y) {
				y.parent.left = x;
			} else {
				y.parent.right = x;
			}
		}
		y.parent = x;
		updateNode(y);
		updateNode(x);
		return x;
	}
    // inorder traversal: prints the key of each node
    void printNode(BSTreeNode v, int level) {
		String x;
        if (v == null) {
            return;
        }
        printNode(v.right, level + 1);
        for (int i = 0; i < level; i++){
            System.out.print("\t");
        }
		if(v.isRed){
			x="Red";
		}else{
			x="Black";
		}
        System.out.println("" + v.key + "(" + v.height + "," + v.N + ")"+'['+x+']');
        printNode(v.left, level + 1);
    }
	
    // print binary tree
    public void print() {
        System.out.println("Printing binary search tree");
        System.out.println("");
        printNode(root, 0);
        System.out.println("");
    }
    public static void main(String[] args) {
        System.out.println("Test Binary Search Tree");
        int n = Integer.parseInt(args[0]);
        System.out.println("number of keys n = " + n);

        RedBlackTree T = new RedBlackTree<Integer, String>();

        Random rand = new Random(0);
        int[] keys = new int[n];
        for (int i = 0; i < n; i++) { // store n random numbers in [0,2n)
        	keys[i] = rand.nextInt(2*n);
        }

        long startTime = System.currentTimeMillis();
        for (int i = 0; i < n; i++) {
            String item = "item" + i;
            T.insert(keys[i], item);
        }
        long endTime = System.currentTimeMillis();
        long totalTime = endTime - startTime;
        System.out.println("construction time = " + totalTime);
        //T.print(); 
        System.out.println("tree height = " + T.getTreeHeight());
        
		List list = new ArrayList();  
		int k = n - n/4;
		int j = n/4;
		for(int i = 0; i< n; i++){
			if(keys[i] > j && keys[i] <= k){
				if(list.contains(keys[i])){
					continue;
				}	
				list.add(keys[i]);
			}
		}
		
		System.out.println("keys in range" + "[" + n/4 + "," + k + "] "  + "= " + list.size());
       
	   startTime = System.currentTimeMillis();
        for (int i = 0; i < n; i++) {
            if (T.search(keys[i]) == null) {
                System.out.println("key " + keys[i] + " not found!");
            }
        }
		
		int g = keys[n/2];
		System.out.println("deleting key " + g);
		T.delete(g);
		//T.print();
		
        endTime = System.currentTimeMillis();
        totalTime = endTime - startTime;
        System.out.println("search time = " + totalTime);
    }
}
