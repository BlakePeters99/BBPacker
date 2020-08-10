package BBPacker;

import BBPacker.Packer.Item;
import BBPacker.Packer.Result;
import BBPacker.Packer.Stats;
/*
import edu.calpoly.csc349.Knapsack.BBPacker;
import edu.calpoly.csc349.Knapsack.Packer.Item;
import edu.calpoly.csc349.Knapsack.Packer.Result;
import edu.calpoly.csc349.Knapsack.Packer.Stats;
*/
public class BBPackerTester {
   public static class MyItem implements Item {
      public int value;     // Total value of the pack
      public int weight;    // Total weight of the pack

      public MyItem (int value, int weight) {
         this.value = value;
         this.weight = weight;
      }

      @Override
      public int getWeight() {
         return this.weight;
      }

      @Override
      public int getValue() {
         return this.value;
      }
   }

   public static class BBTest {
      public String TestName;
      public Item[] items;
      public Item[] itemsClone;
      public Stats stats;

      public BBTest(String TestName, Item[] items, Stats stats) {
         this.TestName = TestName;
         this.items = items;
         this.itemsClone = items.clone();
         this.stats = stats;
      }
   }
   public static class RdmItemsDesc {
      public int amount;
      public int minRngV;
      public int maxRngV;
      public int minRngW;
      public int maxRngW;

      public RdmItemsDesc (int amount, int minRngV, int maxRngV, int minRngW,
       int maxRngW) {
         this.amount = amount;
         this.minRngV = minRngV;
         this.maxRngV = maxRngV;
         this.minRngW = minRngW;
         this.maxRngW = maxRngW;
      }
   }

   public static Item[] randomItemCreator(RdmItemsDesc desc) {
      Item[] itemArray = new Item[desc.amount];

      for (int i = 0; i < desc.amount; i++) {
         itemArray[i] = new MyItem((int) (Math.random() *
          (desc.maxRngV - desc.minRngV + 1) + desc.minRngV),
          (int) (Math.random() * (desc.maxRngW - desc.minRngW + 1)
          + desc.minRngW));
      }
      return itemArray;
   }

   public static void main(String[] args) {
      BBPacker packer = new BBPacker();
      
      BBTest test0 = new BBTest("Good BB Test", new Item[] {
       new MyItem(125, 12),
       new MyItem(71, 7),
       new MyItem(60, 6),
       new MyItem(49, 5),
       new MyItem(48, 5),
       new MyItem(38, 4)
      }, new Stats(135,14,24,0));
      
      BBTest test1 = new BBTest("Standard Test", new Item[] {
         new MyItem(14, 3),
         new MyItem(30, 6),
         new MyItem(3, 1),
         new MyItem(19, 4)
      }, new Stats(49,10,24,0));

      BBTest test2 = new BBTest("No Item Solution Test", new Item[] {
         new MyItem(14, 3),
         new MyItem(30, 6),
         new MyItem(3, 1),
         new MyItem(19, 4)
      }, new Stats(0,0,24,0));

      BBTest test3 = new BBTest("Large Weight Test", new Item[] {
         new MyItem(14, 3),
         new MyItem(30, 6),
         new MyItem(3, 1),
         new MyItem(19, 4)
      }, new Stats(49,100000,24,0));

      BBTest test4 = new BBTest("Random Item Test", randomItemCreator(
       new RdmItemsDesc(30000,0, 101, 1, 20)), new Stats(5,3000,24,0));

      BBTest[] testArray = { test0, test1, test2, test3, test4 };
      Item[] items, packItems;
      int maxWeight, totalValue, totalWeight;
      Result result;
      long time;

      for (BBTest test : testArray) {
         items = test.items;
         maxWeight = test.stats.weight;
         totalValue = 0;
         totalWeight = 0;

         System.out.println("\n\n" + test.TestName);
         time = System.currentTimeMillis();
         result = packer.packItems(items, maxWeight, true);
            result.stats.runTime = System.currentTimeMillis() - time;
            packItems = result.items;

            for (Item i : packItems) {
               System.out.printf("Item Weight: %d\t Item Val: %d\t"
                + "Item ratio: %.2f\n", i.getWeight(), i.getValue(),
                (float)i.getValue() / i.getWeight());
               totalValue += i.getValue();
               totalWeight += i.getWeight();
            }
            System.out.println("Runtime: " + result.stats.runTime);

            // Checking for correct solution
            if (totalValue < test.stats.value) {
               System.out.println("Test: " + test.TestName + " total value "
                + totalValue + " is not equal to the optimal value "
                + test.stats.value + "\n");
            } else if (totalWeight > test.stats.weight) {
               System.out.println("Test: " + test.TestName + " total weight "
                + totalWeight + " is greater than the max weight "
                + test.stats.weight + "\n");
            } else {
               System.out.println("Successful test for " + test.TestName
                + " with total value: " + totalValue + "\n");
            }
      }
   }
}
