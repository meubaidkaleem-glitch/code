public class TelemetryThread extends Thread {
    @Override
    public void run() {
        Runtime rt = Runtime.getRuntime();
        for (int i = 1; i <= 5; i++) {
            long usedMB = (rt.totalMemory() - rt.freeMemory()) / (1024 * 1024);
            System.out.println("[Telemetry] Update " + i + " | CPU cores=" + rt.availableProcessors()
                    + " | Memory used=" + usedMB + " MB | Thread=" + getName());
            try {
                Thread.sleep(800);
            } catch (InterruptedException e) {
                System.out.println("Interrupted: " + e.getMessage());
            }
        }
    }
}
