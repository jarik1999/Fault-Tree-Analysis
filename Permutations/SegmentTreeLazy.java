package Permutations;

class SegmentTreeLazy {
    private int size;
    private int[] max;
    private int[] lazy;


    SegmentTreeLazy(int[] array){
        size = array.length;
        max=new int[array.length*4];
        lazy=new int[array.length*4];
        buildTree(array,1,0,size-1);
    }

    private void buildTree(int[] array, int position, int front, int end){
        //Basis case for returning the elements itself
        if(front==end){
            max[position]=array[front];
            return;
        }
        //Building trees by dividing into two subtrees
        buildTree(array, position*2,front,(front+end)/2);
        buildTree(array, position*2+1,(front+end)/2+1,end);

        recalc(position,front,end);
    }

    private void recalc(int position, int front, int end) {
        max[position]=Math.max(getMax(position*2),getMax(position*2+1) );
    }

    private int getMax(int position){
        return max[position]+lazy[position];
    }

    public int queryMax(int queryFront, int queryEnd) {
        return queryMax(1,0,size-1,queryFront,queryEnd);
    }

    private int queryMax(int position,int front,int end, int queryFront, int queryEnd){
        //Case for entirely inclusive
        if(front>=queryFront&&queryEnd>=end){
            return getMax(position);
        }
        //Case for entirely exclusive
        if(end<queryFront||queryEnd<front){
            return Integer.MIN_VALUE;
        }
        propagate(position,front,end);
        int leftAns=queryMax(position*2,front,(front+end)/2,queryFront,queryEnd);
        int rightAns=queryMax(position*2+1,(front+end)/2+1,end,queryFront,queryEnd);
        return Math.max(leftAns, rightAns);
    }

    private void propagate(int position,int front, int end){
        lazy[position*2]+=lazy[position];
        lazy[position*2+1]+=lazy[position];
        max[position]=getMax(position);
        lazy[position]=0;
        //Set the propagation to 0 so that it will not propagate the value again and again
    }

    public void update(int updateFront, int updateEnd, int value){
        update(1,0,size-1,updateFront,updateEnd,value);
    }

    private void update(int position, int front, int end, int updateFront,int updateEnd,int value){
        //Case for entirely inclusive
        if(front>=updateFront&&updateEnd>=end){
            lazy[position]+=value;
            return;
        }
        //Case for entirely exclusive
        if(end<updateFront||updateEnd<front){
            return;
        }
        propagate(position,front,end);
        update(position*2,front,(front+end)/2,updateFront,updateEnd,value);
        update(position*2+1,(front+end)/2+1,end,updateFront,updateEnd,value);
        recalc(position,front,end);
    }
}