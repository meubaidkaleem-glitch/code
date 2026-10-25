// Driver: superclass references call overridden dispatch() at run time (polymorphism).
public class TradingEngineApp {
    public static void main(String[] args) {
        TradeEvent e1 = new TradeEvent("GENERIC-01", System.currentTimeMillis(), 100.0);
        TradeEvent e2 = new FIXOrderEvent("FIX-GATEWAY", System.currentTimeMillis(), 250.0, "ORD-1001");
        TradeEvent e3 = new ReutersQuoteEvent("REUTERS-FEED", System.currentTimeMillis(), 250.0, 249.50, 250.75);

        TradeEvent[] events = { e1, e2, e3 };
        for (TradeEvent e : events) {
            e.dispatch(); // run-time polymorphism
        }
    }
}