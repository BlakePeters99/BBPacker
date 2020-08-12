package BBPacker;

import java.util.*;

public class BBPacker implements Packer {
   public Result packItems(Item[] items, int maxWeight, boolean verbose) {
      // sorting Items array by value/weight
      Collections.sort(Arrays.asList(items), (a, b) -> {
         float r1 = (float) a.getValue() / (float) a.getWeight();
         float r2 = (float) b.getValue() / (float) b.getWeight();
         // Reverse order
         return Float.compare(r2, r1);
      });
      
      LinkedList<Item> stack = new LinkedList<>();
      LinkedList<Item> bestStack = new LinkedList<>();
      LinkedList<Integer> index = new LinkedList();
      int currV = 0, currW = 0, bestV = 0, bestW = 0, idx = 0;
      int cutV, cutW;
      int optimalV;
      Item item;
      
      // pushing on first elements to get first optimum solution
      System.out.printf("Start: ");
      
      while(true) {
         // tries to add items if possible
         for (; idx < items.length; idx++) {
            optimalV = currV;
            cutV = currV;
            cutW = currW;
            // Find Optimal Value
            for (int i = idx; i < items.length
             && cutW + items[i].getWeight() <= maxWeight; i++) {
               cutV += items[i].getValue();
               cutW += items[i].getWeight();
               // Optimal = previous val + ratio * weight remaining
               optimalV = (int)((float)cutV + (float) items[i].getValue()
                / (float)cutW * ((float)maxWeight - (float)cutW));
            }
            // Cuts for optimization
            if (optimalV <= bestV) {
               System.out.printf("best possible is %d ... cut\n", optimalV);
               break;
            }
            
            // Push new item onto stack
            if (currW + items[idx].getWeight() <= maxWeight) {
               currV += items[idx].getValue();
               currW += items[idx].getWeight();
               System.out.printf("use (%d, %d), ", items[idx].getValue(),
                items[idx].getWeight());
               stack.push(items[idx]);
               index.push(idx);
            }
         }
         // If current is better than best, new best
         if (bestV < currV) {
            bestStack = (LinkedList<Item>) stack.clone();
            bestV = currV;
            bestW = currW;
            System.out.println("new best solution at " + bestV);
         }
         else if (idx == items.length) {
            System.out.printf("%d doesn't beat %d\n", currV, bestV);
         }
         
         if (!stack.isEmpty()) {
            item = stack.pop();
            System.out.printf("Drop (%d, %d) and retry: ", item.getValue(),
             item.getWeight());
            currV -= item.getValue();
            currW -= item.getWeight();
            idx = index.pop() + 1;
         }
         // Finished
         else
            break;
      }
      
      System.out.println("Max Value:\t" + bestV);
      
      Result result;
      if (bestStack.isEmpty()) {
         Stats stats = new Stats(bestV, bestW, 0, 0);
         result = new Result(new Item[0], stats);
      } else {
         Stats stats = new Stats(bestV, bestW,
          bestStack.size(), 0);
         Item[] arr = bestStack.toArray(
          new Item[bestStack.size()]);
         result = new Result(arr, stats);
      }
      
      return result;
   }
}
   
   
   /*
      
      // BB recursion
      // StkInfo startStack = new StkInfo(0, 0, bStack, 0);
      // StkInfo solution = BestStack(startStack, stack, items, maxWeight,
      //0, 0, 0);
      
   public static class StkInfo {
      public int bestV;
      public int bestW;
      public LinkedList<Item> bestStack;
      public int end;
      
      public StkInfo(int bestV, int bestW, LinkedList<Item> bestStack, int end) {
         this.bestV = bestV;
         this.bestW = bestW;
         this.bestStack = bestStack;
         this.end = end;
      }
   }
   public StkInfo BestStack (StkInfo best, Stack<Item> stack, Item[] items,
    int maxW, int idx, int currV, int currW) {
      Item item;
      while (idx < items.length) {

         if (currW + items[idx].getWeight() <= maxW) {
            currV += items[idx].getValue();
            currW += items[idx].getWeight();
            //System.out.printf("Use (%d, %d), ", items[idx].getValue(), items[idx].getWeight());
            stack.push(items[idx]);
            
            if (best.bestV < currV) {
               best.bestStack = (Stack<Item>) stack.clone();
               best.bestV = currV;
               best.bestW = currW;
               System.out.println("new best solution at " + best.bestV);
            }
            
            if (currW > 0 && currV + (float)currV/currW *(maxW-currW) <= (float)best.bestV) {
               //System.out.printf("Cut ");
               best.end = 2;
               return best;
            }
            // Recursion
            StkInfo ret = BestStack(best, stack, items, maxW, idx + 1, currV, currW);
            if (ret.end > 0){
               best = ret;
            }
            if (ret.end == 2 && !stack.isEmpty()) {
               best.end = 1;
               return best;
            }
            if (!stack.isEmpty()){
               item = stack.lastElement();
               Pop(stack);
               currV -= item.getValue();
               currW -= item.getWeight();
               //System.out.printf("Drop (%d, %d)\t", item.getValue(), item.getWeight());
            }
            
         }

         idx++;
      }
      if (stack.isEmpty()) {
         best.end = 1;
         return best;
      }
      
      best.end = 0;
      return best;
   }
   */


/*    Possible code needed
   // Debugging Code ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
      Iterator<Item> itr = bestStack.iterator();
      Item currentItem;

      System.out.println("Max Value:\t" + bestValue);
      System.out.println("~~~~~~~~~~~~~~~~~~~~BEST STACK~~~~~~~~~~~~~~~~~~~");
      while (itr.hasNext()) {
         currentItem = itr.next();
         System.out.printf("Item Weight: %d\t Item Val: %d\t"
          + "Item ratio: %.2f\n", currentItem.getWeight(),
          currentItem.getValue(),
          (float)currentItem.getValue() / currentItem.getWeight());
      }
   // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
for (int i = 0; i < items.length; i++) {
         //System.out.printf("Item Wgt: %d\t Item Val: %d\tItem ratio: %.2f\n",
         // i.getWeight(), i.getValue(), (float)i.getValue()/i.getWeight());
         while (!stack.isEmpty() && currWeight + items[i].getWeight() > maxWeight) {
            item = stack.pop();
            currWeight -= item.getWeight();
            currValue -= item.getValue();
            System.out.printf("Drop (%d, %d)\n", item.getValue(), item.getWeight());
         }

         if (currWeight + items[i].getWeight() <= maxWeight) {
            System.out.printf("Use (%d, %d)\t", items[i].getValue(), items[i].getWeight());
            stack.push(items[i]);
            currWeight += items[i].getWeight();
            currValue += items[i].getValue();
            System.out.printf("CurV %d, CurW %d\n", currValue, currWeight);
         }

         if (currValue > bestValue) {
            System.out.println("Best possible is " + currValue);
            bestStack = (Stack<Item>) stack.clone();
            bestValue = currValue;
            bestWeight = currWeight;
         }
      }
      
       // Trying to find a way to make this recursive
      for (int i = 0; i < items.length; i++) {
         for (int j = i; j < items.length; j++) {
            if (currWeight + items[j].getWeight() <= maxWeight) {
               stack.push(items[j]);
               System.out.printf("Use (%d, %d), ", items[j].getValue(), items[j].getWeight());
   
            }
         }
      }
 */
