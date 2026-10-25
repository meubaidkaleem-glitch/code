// Row-major vs column-major traversal of a 2000x2000 matrix to show cache locality.
public class CacheLocalityBenchmark {
    static final int N = 2000;
    static double[][] matrix = new double[N][N];

    static double traverseRowMajor() {
        double sum = 0;
        for (int i = 0; i < N; i++)
            for (int j = 0; j < N; j++)
                sum += matrix[i][j];
        return sum;
    }

    static double traverseColumnMajor() {
        double sum = 0;
        for (int j = 0; j < N; j++)
            for (int i = 0; i < N; i++)
                sum += matrix[i][j];
        return sum;
    }

    public static void main(String[] args) {
        for (int i = 0; i < N; i++)
            for (int j = 0; j < N; j++)
                matrix[i][j] = (i * 31 + j) % 100 / 10.0;

        // warm-up so JIT compilation doesn't skew the first measurement
        traverseRowMajor();
        traverseColumnMajor();

        int RUNS = 10;
        double totalRow = 0, totalCol = 0, checksum = 0;

        System.out.println("Run | Row-Major (ms) | Column-Major (ms)");
        for (int r = 1; r <= RUNS; r++) {
            long t1 = System.nanoTime();
            checksum += traverseRowMajor();
            double rowMs = (System.nanoTime() - t1) / 1e6;

            long t2 = System.nanoTime();
            checksum += traverseColumnMajor();
            double colMs = (System.nanoTime() - t2) / 1e6;

            totalRow += rowMs;
            totalCol += colMs;
            System.out.printf("%3d | %14.3f | %17.3f%n", r, rowMs, colMs);
        }

        double avgRow = totalRow / RUNS;
        double avgCol = totalCol / RUNS;
        System.out.printf("Average Row-Major    : %.3f ms%n", avgRow);
        System.out.printf("Average Column-Major : %.3f ms%n", avgCol);
        System.out.printf("Speedup (Column/Row) : %.2fx%n", avgCol / avgRow);
        System.out.println("Checksum (prevents dead-code elimination): " + checksum);
    }
}