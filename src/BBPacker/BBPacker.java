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

        int bestValue = 0;
        int currentValue = 0;
        int currentWeight = 0;
        Item item;
        Stack<Item> stack = new Stack<Item>(), bestStack = new Stack<Item>();

        for (Item i : items) {
            System.out.printf("Item Weight: %d\t Item Val: %d\tItem ratio: %.2f\n",
             i.getWeight(), i.getValue(), (float)i.getValue()/i.getWeight());

            while (!stack.isEmpty() && currentWeight + i.getWeight() > maxWeight) {
                item = stack.pop();
                currentWeight -= item.getWeight();
                currentValue -= item.getValue();
            }

            if (currentWeight + i.getWeight() <= maxWeight) {
                stack.push(i);
                currentWeight += i.getWeight();
                currentValue += i.getValue();
            }

            if (currentValue > bestValue) {

                bestStack = (Stack<Item>) stack.clone();
                bestValue = currentValue;
            }
        }

        Iterator<Item> itr = bestStack.iterator();
        Item currentItem;
        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~BEST STACK~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        while (itr.hasNext()) {
            currentItem = itr.next();
            System.out.printf("Item Weight: %d\t Item Val: %d\tItem ratio: %.2f\n",
             currentItem.getWeight(), currentItem.getValue(), (float)currentItem.getValue()/currentItem.getWeight());
        }

        return null;
    }
}