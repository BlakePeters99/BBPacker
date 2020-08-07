package BBPacker;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Stack;

public class BBPacker implements Packer {
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

      int bestValue = 0, bestWeight = 0, currValue = 0, currWeight = 0;
      Item item;
      Stack<Item> stack = new Stack<Item>(), bestStack = new Stack<Item>();

      for (Item i : items) {
         System.out.printf("Item Wgt: %d\t Item Val: %d\tItem ratio: %.2f\n",
          i.getWeight(), i.getValue(), (float)i.getValue()/i.getWeight());

         while (!stack.isEmpty() && currWeight + i.getWeight() > maxWeight) {
            item = stack.pop();
            currWeight -= item.getWeight();
            currValue -= item.getValue();
         }

         if (currWeight + i.getWeight() <= maxWeight) {
            stack.push(i);
            currWeight += i.getWeight();
            currValue += i.getValue();
         }

         if (currValue > bestValue) {
            bestStack = (Stack<Item>) stack.clone();
            bestValue = currValue;
            bestWeight = currWeight;
         }
      }

      System.out.println("Max Value:\t" + bestValue);

      Stats stats = new Stats(bestValue, bestWeight, stack.size(), 0);
      Result result;
      if (bestStack.isEmpty())
         result = new Result(new Item[0], stats);
      else {
         Item[] arr = bestStack.toArray(new Item[bestStack.size()]);
         result = new Result(arr, stats);
      }

      return result;
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

 */
