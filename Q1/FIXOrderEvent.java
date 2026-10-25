public class FIXOrderEvent extends TradeEvent {
    private String clOrdId;
    private static final double FEE_RATE = 0.0015; // 0.15%

    public FIXOrderEvent(String senderId, long timestamp, double basePrice, String clOrdId) {
        super(senderId, timestamp, basePrice);
        this.clOrdId = clOrdId;
    }

    @Override
    public void dispatch() {
        double fee = basePrice * FEE_RATE;
        double total = basePrice + fee;
        System.out.println("[FIX ORDER] ClOrdID=" + clOrdId + " | Sender=" + senderId
                + " | Timestamp=" + timestamp);
        System.out.printf("  BUY order executed. Base=%.2f | Fee(0.15%%)=%.4f | Total=%.4f%n",
                basePrice, fee, total);
    }
}