public class ParallelMinSearch extends SensorSearchEngine {
    public ParallelMinSearch(double[] d, int s, int e) {
        super(d, s, e);
    }

    @Override
    public void processGrid() {
        double min = Double.MAX_VALUE;
        for (int i = startIdx; i < endIdx; i++) {
            if (sensorData[i] < min) min = sensorData[i];
        }
        result = min;
        System.out.println(Thread.currentThread().getName()
                + " local MIN [" + startIdx + "," + endIdx + ") = " + min);
    }
}