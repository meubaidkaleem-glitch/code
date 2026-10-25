public class ReutersQuoteEvent extends TradeEvent {
    private double bidPrice;
    private double askPrice;

    public ReutersQuoteEvent(String senderId, long timestamp, double basePrice,
                             double bidPrice, double askPrice) {
        super(senderId, timestamp, basePrice);
        this.bidPrice = bidPrice;
        this.askPrice = askPrice;
    }

    @Override
    public void dispatch() {
        double spread = askPrice - bidPrice;
        double spreadPct = (spread / askPrice) * 100;
        System.out.println("[REUTERS QUOTE] Sender=" + senderId + " | Timestamp=" + timestamp);
        System.out.printf("  Bid=%.2f | Ask=%.2f | Spread=%.4f (%.4f%%)%n",
                bidPrice, askPrice, spread, spreadPct);
    }
}