package BBPacker;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Stack;

public class BBPacker implements Packer {
   public static class StkInfo {
      public int bestV;
      public int bestW;
      public Stack<Item> bestStack;
      public int end;
      
      public StkInfo (int bestV, int bestW, Stack<Item> bestStack, int end) {
         this.bestV = bestV;
         this.bestW = bestW;
         this.bestStack = bestStack;
         this.end = end;
      }
   }
   public Result packItems(Item[] items, int maxWeight, boolean verbose) {
      // sorting Items array by value/weight
      Collections.sort(Arrays.asList(items), new Comparator<Item>() {
         @Override
         public int compare(Item a, Item b) {
            float r1 = (float)a.getValue() / (float)a.getWeight();
            float r2 = (float)b.getValue() / (float)b.getWeight();
            // Reverse order
            return  Float.compare(r2, r1);
         }
      });
      
      
      Stack<Item> stack = new Stack<>(), bStack = new Stack<Item>();
      StkInfo startStack = new StkInfo(0,0, bStack, 0);
      
      // BB recursion
      StkInfo solution = BestStack(startStack, stack, items, maxWeight,
       0, 0, 0);

      System.out.println("Max Value:\t" + solution.bestV);

      Result result;
      if (solution.bestStack.isEmpty()) {
         Stats stats = new Stats(solution.bestV, solution.bestW, 0, 0);
         result = new Result(new Item[0], stats);
      }
      else {
         Stats stats = new Stats(solution.bestV, solution.bestW,
          solution.bestStack.size(), 0);
         Item[] arr = solution.bestStack.toArray(
          new Item[solution.bestStack.size()]);
         result = new Result(arr, stats);
      }

      return result;
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
   public static Stack<Item> Pop(Stack<Item> stack) {
      stack.pop();
      return stack;
   }
}

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
