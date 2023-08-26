
public class BinarySearchTree {
    
     public static class Node{  
        int data;  
        Node left;  
        Node right;  
  
        public Node(int data){  
            this.data = data;  
            this.left = null;  
            this.right = null;  
        }  
      }  
  
      public Node root;  
  
      public BinarySearchTree(){  
          root = null;  
      } 

      public void insert(int data) {  
          
        Node newNode = new Node(data);  

        if(root == null){  
            root = newNode;  
            return;  
          }  
        else {  
            Node current = root, parent = null;  

            while(true) {  
                parent = current;  

                if(data < current.data) {  
                    current = current.left;  
                    if(current == null) {  
                        parent.left = newNode;  
                        return;  
                    }  
                }  
                else {  
                    current = current.right;  
                    if(current == null) {  
                        parent.right = newNode;  
                        return;  
                    }  
                }  
            }  
        }
    }
      
    public void BuildTree(int[] arr, int n){

        for(int i=0; i<n; i++){

            insert(arr[i]);

        }

    }

    public int PreOrderWalk(Node root, int[] preorder, int index){

        if(root == null){

            return index;

        }

        else{

            preorder[index++] = root.data;
            int ind = PreOrderWalk(root.left, preorder, index);
            ind = PreOrderWalk(root.right, preorder, ind);

            return ind;

        }

    }

    public int[] BuildPreOrder(int[] arr, int n){

        int [] preorderArray = new int[n];

        BuildTree(arr, n);

        PreOrderWalk(root, preorderArray, 0);

        return preorderArray;

    }
    
    public static void main(String[] args){

        int[] arr = {2,5,1,3,4,7,6,9,8};

        BinarySearchTree tree = new BinarySearchTree();

        int[] out = tree.BuildPreOrder(arr, 9);

        for(int i = 0; i<9; i++){

            System.out.println(out[i] + " ");
        }

    }
}
