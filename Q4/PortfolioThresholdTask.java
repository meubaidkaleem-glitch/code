import java.util.concurrent.RecursiveAction;

public class PortfolioThresholdTask extends RecursiveAction {
    private static final int THRESHOLD = 1000;
    private double[] assets;
    private int start, end;
    private double riskCap;

    public PortfolioThresholdTask(double[] assets, int start, int end, double riskCap) {
        this.assets = assets;
        this.start = start;
        this.end = end;
        this.riskCap = riskCap;
    }

    @Override
    protected void compute() {
        if (end - start <= THRESHOLD) {
            for (int i = start; i < end; i++) {
                if (assets[i] > riskCap) assets[i] *= 1.05; // 5% volatility penalty
            }
            return;
        }
        int mid = (start + end) / 2;
        invokeAll(new PortfolioThresholdTask(assets, start, mid, riskCap),
                  new PortfolioThresholdTask(assets, mid, end, riskCap));
    }
}