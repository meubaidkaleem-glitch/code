public class AsyncWebEngineApp {
    public static void main(String[] args) throws InterruptedException {
        System.out.println("Default JVM thread: " + Thread.currentThread().getName());

        TelemetryThread telemetry = new TelemetryThread();
        telemetry.setName("Telemetry-Worker");

        Thread logWorker = new Thread(new LogProcessorTask());
        logWorker.setName("Log-Worker");

        telemetry.start();
        logWorker.start();
        System.out.println("Both threads started. Telemetry alive=" + telemetry.isAlive()
                + ", Log alive=" + logWorker.isAlive());

        telemetry.join();
        logWorker.join();
        System.out.println("All workers finished. main() terminating.");
    }
}