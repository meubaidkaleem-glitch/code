// Compares serial sum with ForkJoinPool sum and applies the 5 percent stress-test penalty.
import java.util.Random;
import java.util.concurrent.ForkJoinPool;

public class ForkJoinPortfolioApp {

    static double serialSum(double[] a) {
        double s = 0;
        for (double v : a) s += v;
        return s;
    }

    static void benchmark(String label, double[] assets, ForkJoinPool pool) {
        int n = assets.length;
        int RUNS = 20;

        // warm-up so the JIT compiler has optimized both paths
        for (int i = 0; i < 10; i++) {
            serialSum(assets);
            pool.invoke(new ParallelRiskCalculator(assets, 0, n));
        }

        long serialTotal = 0, parallelTotal = 0;
        double serialResult = 0, parallelResult = 0;
        for (int r = 0; r < RUNS; r++) {
            long t1 = System.nanoTime();
            serialResult = serialSum(assets);
            serialTotal += System.nanoTime() - t1;

            long t2 = System.nanoTime();
            parallelResult = pool.invoke(new ParallelRiskCalculator(assets, 0, n));
            parallelTotal += System.nanoTime() - t2;
        }
        double serialMs = serialTotal / (double) RUNS / 1e6;
        double parallelMs = parallelTotal / (double) RUNS / 1e6;

        System.out.println("=== " + label + " (" + n + " elements) ===");
        System.out.printf("Serial risk sum   : %.2f  (avg %.4f ms)%n", serialResult, serialMs);
        System.out.printf("Parallel risk sum : %.2f  (avg %.4f ms)%n", parallelResult, parallelMs);
        System.out.printf("Speedup (serial/parallel): %.2fx%n", serialMs / parallelMs);
    }

    public static void main(String[] args) {
        Random rnd = new Random(42);
        ForkJoinPool pool = new ForkJoinPool();
        System.out.println("Available cores: " + Runtime.getRuntime().availableProcessors());

        // Required part: 10,000-element portfolio
        double[] assets = new double[10_000];
        for (int i = 0; i < assets.length; i++) assets[i] = 50 + rnd.nextDouble() * 950;

        double riskCap = 800.0;
        double before = pool.invoke(new ParallelRiskCalculator(assets, 0, assets.length));

        long t = System.nanoTime();
        pool.invoke(new PortfolioThresholdTask(assets, 0, assets.length, riskCap));
        double stressMs = (System.nanoTime() - t) / 1e6;

        double after = pool.invoke(new ParallelRiskCalculator(assets, 0, assets.length));
        System.out.printf("Risk sum BEFORE stress test: %.2f%n", before);
        System.out.printf("Risk sum AFTER  stress test: %.2f (cap=%.0f, +5%% above cap, %.3f ms)%n",
                after, riskCap, stressMs);

        benchmark("Required size", assets, pool);

        // Larger dataset to show clear parallel speedup
        double[] big = new double[10_000_000];
        for (int i = 0; i < big.length; i++) big[i] = 50 + rnd.nextDouble() * 950;
        benchmark("Large dataset", big, pool);

        pool.shutdown();
    }
}