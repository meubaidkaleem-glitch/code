public class ParallelMaxSearch extends SensorSearchEngine {
    public ParallelMaxSearch(double[] d, int s, int e) {
        super(d, s, e);
    }

    @Override
    public void processGrid() {
        double max = -Double.MAX_VALUE;
        for (int i = startIdx; i < endIdx; i++) {
            if (sensorData[i] > max) max = sensorData[i];
        }
        result = max;
        System.out.println(Thread.currentThread().getName()
                + " local MAX [" + startIdx + "," + endIdx + ") = " + max);
    }
}