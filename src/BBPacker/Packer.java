package BBPacker;

public interface Packer {
    Result packItems(Item[] var1, int var2, boolean var3);

    public interface Item {
        int getWeight();

        int getValue();
    }

    public static class Result {
        public Item[] items;
        public Stats stats;

        public Result(Item[] items, Stats stats) {
            this.items = items;
            this.stats = stats;
        }
    }

    public static class Stats {
        public int value;
        public int weight;
        public int numSlns;
        public long runTime;

        public Stats(int value, int weight, int numSlns, long rT) {
            this.value = value;
            this.weight = weight;
            this.numSlns = numSlns;
            this.runTime = rT;
        }
    }
}
