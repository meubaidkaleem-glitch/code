// Splits 50,000 readings across 4 chunks; min and max workers run simultaneously.
import java.util.Random;

public class SensorGridMonitorApp {
    public static void main(String[] args) throws InterruptedException {
        int N = 50_000;
        int WORKERS = 4;
        int chunk = N / WORKERS;

        double[] data = new double[N];
        Random rnd = new Random();
        for (int i = 0; i < N; i++) data[i] = -10 + rnd.nextDouble() * 120;

        ParallelMinSearch[] mins = new ParallelMinSearch[WORKERS];
        ParallelMaxSearch[] maxs = new ParallelMaxSearch[WORKERS];
        Thread[] threads = new Thread[WORKERS * 2];

        long start = System.nanoTime();
        for (int w = 0; w < WORKERS; w++) {
            int s = w * chunk;
            int e = (w == WORKERS - 1) ? N : s + chunk;
            mins[w] = new ParallelMinSearch(data, s, e);
            maxs[w] = new ParallelMaxSearch(data, s, e);
            threads[w] = new Thread(mins[w], "Min-Worker-" + w);
            threads[WORKERS + w] = new Thread(maxs[w], "Max-Worker-" + w);
        }

        for (Thread t : threads) t.start();   // min and max run simultaneously
        for (Thread t : threads) t.join();
        long elapsed = System.nanoTime() - start;

        double globalMin = Double.MAX_VALUE;
        double globalMax = -Double.MAX_VALUE;
        for (int w = 0; w < WORKERS; w++) {
            globalMin = Math.min(globalMin, mins[w].getResult());
            globalMax = Math.max(globalMax, maxs[w].getResult());
        }

        System.out.printf("GLOBAL CRITICAL MIN temperature = %.4f%n", globalMin);
        System.out.printf("GLOBAL CRITICAL MAX vibration   = %.4f%n", globalMax);
        System.out.printf("Execution time = %.3f ms%n", elapsed / 1e6);
    }
}