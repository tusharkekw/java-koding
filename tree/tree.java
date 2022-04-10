import java.io.*;
import java.util.*;

public class tree {
    public static class Node{
        int data = 0;
        Node left = null;
        Node right = null;

        Node(int data , Node left , Node right){
            this.data = data;
            this.left = left;
            this.right = right;
        }
    }

    public static class Pair {
        Node node;
        int state;// 1 = left , 2 = right , 3 = both done remove;

        Pair(Node node , int state){
            this.node = node;
            this.state = state;
        }
    }
    public static void display(Node node){
        if(node == null) return ;
        String str = "";
        str += node.left == null ? "." : node.left.data +" ";
        str += "<- " + node.data +" ->";
        str += node.right == null ? ".": node.right.data+" ";

        System.out.println(str);

        display(node.left);
        display(node.right);
    }
    public static void main(String[] args) throws Exception{
        Integer[] arr = {50 , 25 , 12 , null , null , 37 , 30 , null , null , null , 75 , 62, null , 70 , null, null , 87 , null , null};

        Node root = new Node(arr[0], null , null);
        Pair rp = new Pair(root , 1);

        Stack<Pair> st = new Stack<>();
        st.push(rp);

        int idx = 0;

        while(st.size() > 0){
            Pair top = st.peek();
            if(top.state == 1){
                idx++;
                if(arr[idx] != null){
                    Node leftNode = new Node(arr[idx] , null ,null);
                    top.node.left = leftNode;
                    Pair leftPair = new Pair(leftNode, 1);
                    st.push(leftPair);
                }else{
                    top.node.left = null;
                }
                top.state++;
            }
            else if(top.state == 2){
                idx++;
                if(arr[idx] != null){
                    Node rightNode = new Node(arr[idx],null, null);
                    top.node.right = rightNode;
                    Pair rightPair = new Pair(rightNode, 1);
                    st.push(rightPair);
                }else{
                    top.node.right = null;
                }
                top.state++;
            }
            else{
                st.pop();
            }
        }

        display(root);
    }

    static int idx = 0;

    public static Node constructTree(int[] arr){
        if(idx >= arr.length || arr[idx] == -1){
            idx++;
            return null;
        }

        Node node = new node(arr[idx++]);
        node.left = constructTree(arr);
        node.right = constructTree(arr);

        return node;
    }


    //size 
    public static int size(Node node){
        return node == null ? 0 : size(node.left) + size(node.right) + 1;
    }

    public static int height(Node node){
        return node == null ? -1 : Math.max(height(node.left), height(node.right)) + 1;
    }

    public static boolean find(Node node, int data){
        if(node == null) return false;

        if(node.data == data) return true;

        return find(node.left) || find(node.right);
    }

    public static boolean rootToNodePath(Node node , int data , ArrayList<Node> ans){
        if(node == null) return false;

        if(node.data == data){
            ans.add(node);
            return true;
        }

        boolean res = rootToNodePath(node.left, data, ans) || rootToNodePath(node.right, data, ans);
        
        if(res){
            ans.add(node);
        }

        return res;
    }

    public ArrayList<Node> rootToNodePath2(Node node , Node data){
        if(node == null) return new ArrayList<>();

        if(node == data){
            ArrayList<Node> base = new ArrayList<>();
            base.add(node);
            return base;
        }

        ArrayList<Node> left = rootToNodePath2(node.left, data);
        if(left.size() > 0){
            left.add(node);
            return left;
        }

        ArrayList<Node> right = rootToNodePath2(node.right, data);
        if(right.size() > 0){
            right.add(node);
            return right;
        }

        return new ArrayList<>();
    }

    public void lca(Node root, Node p , Node q){
        ArrayList<Node> list1 = new ArrayList<>();
        ArrayList<Node> list2 = new ArrayList<>();

        list1 = rootToNodePath2(root, p);
        list2 = rootToNodePath2(root, q);

        int i = list1.size() - 1;
        int j = list2.size() - 1; 

        Node ans = null;

        while(i >= 0 && j >= 0){
            if(list1.get(i) == list2.get(j)){
                break;
            }
            i--;
            j--;
        }

        ans = list1.get(i + 1);
    }

    public void printKDown(Node node , Node block, int depth , List<Integer> ans){
        if(node == null || depth < 0 || node == block){
            return;
        }

        if(depth == 0){
            ans.add(node.data);
            return;
        }

        printKDown(node.left, block, depth - 1, ans);
        printKDown(node.right, block, depth - 1, ans);
    }

    public List<Integer> distanceK(Node node , Node target , int k){
        ArrayList<Node> list = new ArrayList<>();
        boolean res = rootToNodePath(node, target.data, list);

        List<Integer> ans = new ArrayList<>();
        Node blockNode = null;

        for(int i = 0 ; i < list.size() ; i++){
            printKDown(list.get(i), blockNode, k - i, ans);
            blockNode = list.get(i);
        }

        return ans;
    }

    public static int rootToNodeDistance(Node node , Node data){
        if(node == null){
            return -1;
        }

        if(node == data) return 0;

        int lans = rootToNodeDistance(node.left, data);
        if(lans != -1) return lans + 1;

        int rans = rootToNodeDistance(node.right, data);
        if(rans != -1) return rans + 1;

        return -1;
    }

    public int diameter(Node root){
        if(root == null){
            return -1;
        }

        int leftAns = diameter(root.left);
        int rightAns = diameter(root.right);

        int leftHeight = height(root.left);
        int rightHeight = height(root.right);

        return Math.max(Math.max(leftAns , rightAns), leftHeight + rightHeight + 2);
    }

    //{dia , height}
    public int[] diameter2(Node root){
        if(root == null){
            return new int[]{-1,-1};
        }

        int [] lans = diameter2(root.left);
        int [] rans = diameter2(root.right);

        int[] ans = new int[2];
        ans[0] = Math.max(Math.max(lans[0],rans[0]) , lans[1] + rans[1] + 2);
        ans[1] = Math.max(lans[1], rans[1]) + 1;

        return ans;
    }

    int maxDia = 0 ;
    public int diameter3(Node root){
        if(root == null) return -1;

        int lh = diameter(root.left);
        int rh = diameter(root.right);

        maxDia = Math.max(maxDia , lh + rh + 2);
        return Math.max(lh , rh) + 1;
    }

    public boolean hasPathSum(Node root , int targetSum){
        if(root == null) return false;

        if(root.left == null && root.right == null){
            return targetSum - root.data == 0;
        }

        return hasPathSum(root.left , targetSum - root.data) || hasPathSum(root.right, targetSum - root.data);
    }

    public void pathSum(Node root, int tar , List<List<Integer>> res , List<Integer> smallAns){
        if(root == null){
            return;
        }

        if(root.left == null && root.right == null){
            if(tar - root.data == 0){
                ArrayList<Integer> temp = new ArrayList<>(smallAns);
                temp.add(root.data);
                res.add(temp);
                return;
            }
        }
        
        smallAns.add(root.data);
        pathSum(root.left , tar - root.data , res , smallAns);
        pathSum(root.right, tar - root.data, res , smallAns);
        smallAns.remove(smallAns.size() - 1);
    }

    public class BSTpair{
        boolean isBST = true;
        long max = -(long)1e13;
        long min = (long)1e13;

        BSTpair(long max , long min ,boolean isBST){
            this.max = max;
            this.min = min;
            this.isBST = isBST;
        }
        BSTpair(){
            
        }
    }

    public BSTpair isValidBST(Node root){
        if(root == null) return new BSTpair();

        BSTpair lres = isValidBST(root.left);
        BSTpair rres = isValidBST(root.right);

        BSTpair myRes = new BSTpair();

        myRes.isBST = lres.isBST && rres.isBST && lres.max < root.data && root.data < rres.min;

        myRes.max = Math.max(rres.max , root.data);
        myRes.min = Math.min(lres.min , root.data);

        return myRes;
    }

    Node a = null , b = null , prev = null;
    public void recoverTree(Node root){
        if(root == null) return;

        recoverTree(root.left);

        if(prev != null && prev.data > root.data){
            if(a == null){
                a = prev;
            }
            else b = root;
        }
        prev = root;
        recoverTree(root.right);
        return;
    }
}











//