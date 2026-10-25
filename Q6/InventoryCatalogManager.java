// Benchmarks a synchronized HashMap against ConcurrentHashMap with 10 threads.
import java.util.*;
import java.util.concurrent.*;

public class InventoryCatalogManager {
    private static final int THREADS = 10;
    private static final int OPS_PER_THREAD = 50_000;
    private static final int PRODUCTS = 1000;

    private static void runBenchmark(String label, Map<String, Integer> map) throws Exception {
        for (int i = 0; i < PRODUCTS; i++) map.put("P" + i, 1000);

        ExecutorService pool = Executors.newFixedThreadPool(THREADS);
        CountDownLatch ready = new CountDownLatch(THREADS);
        CountDownLatch go = new CountDownLatch(1);
        CountDownLatch done = new CountDownLatch(THREADS);

        for (int t = 0; t < THREADS; t++) {
            pool.submit(() -> {
                ThreadLocalRandom r = ThreadLocalRandom.current();
                ready.countDown();
                try { go.await(); } catch (InterruptedException ignored) {}
                for (int i = 0; i < OPS_PER_THREAD; i++) {
                    String key = "P" + r.nextInt(PRODUCTS);
                    if (r.nextBoolean()) {
                        map.get(key);                       // lookup
                    } else {
                        map.merge(key, -1, Integer::sum);   // update stock
                    }
                }
                done.countDown();
            });
        }
        ready.await();
        long start = System.nanoTime();
        go.countDown();
        done.await();
        long elapsed = System.nanoTime() - start;
        pool.shutdown();

        long totalOps = (long) THREADS * OPS_PER_THREAD;
        double seconds = elapsed / 1e9;
        System.out.printf("%-20s | Time: %8.2f ms | Throughput: %,.0f ops/sec%n",
                label, elapsed / 1e6, totalOps / seconds);
    }

    public static void benchmarkSynchronizedMap() throws Exception {
        runBenchmark("SynchronizedMap", Collections.synchronizedMap(new HashMap<>()));
    }

    public static void benchmarkConcurrentMap() throws Exception {
        runBenchmark("ConcurrentHashMap", new ConcurrentHashMap<>());
    }

    public static void main(String[] args) throws Exception {
        System.out.println("Threads=" + THREADS + ", Ops/thread=" + OPS_PER_THREAD
                + ", Products=" + PRODUCTS);
        // warm-up run so JIT compilation doesn't skew the first measurement
        runBenchmark("Warm-up (ignore)", new ConcurrentHashMap<>());
        System.out.println("---- Real results ----");
        benchmarkSynchronizedMap();
        benchmarkConcurrentMap();
    }
}