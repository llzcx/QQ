package JavaChat;

import java.util.HashMap;

class Solution {
    public static int[] twoSum(int[] nums, int target) {
        HashMap<Integer, Integer> hashMap = new HashMap<Integer, Integer>();
        //nums[i] + nums[j] = target => target - nums[i] = nums[j]
       for(int i = 0 ; i < nums.length ; i++){
            hashMap.put(target-nums[i],i);
        }
        for(int i = 0 ; i < nums.length ; i++){
            if(hashMap.get(nums[i])==null || hashMap.get(nums[i]) == i)continue;
            int[] ans = new int[2];
            ans[0] = hashMap.get(nums[i]);
            ans[1] = i;
            return ans;
        }
        return null;
    }

    static void show(int a,int b){

    }
    static void show(int... a){

    }

    public static void main(String[] args) {
        show(1,2);

    }
}
