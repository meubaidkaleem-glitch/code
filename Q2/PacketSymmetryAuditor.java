// Pushes to stack and enqueues to queue, then compares LIFO vs FIFO order (palindrome check).
import java.util.Scanner;

public class PacketSymmetryAuditor {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter packet payload: ");
        String payload = sc.nextLine().toLowerCase().replaceAll("[^a-z0-9]", "");

        TransactionStack stack = new TransactionStack(payload.length() + 1);
        RequestQueue queue = new RequestQueue(payload.length() + 1);

        for (char c : payload.toCharArray()) {
            stack.push(c);
            queue.enqueue(c);
        }

        boolean symmetric = true;
        System.out.println("---- Verification Log ----");
        while (!stack.isEmpty() && !queue.isEmpty()) {
            char fromStack = stack.pop();
            char fromQueue = queue.dequeue();
            System.out.println("LIFO pop = " + fromStack + " | FIFO dequeue = " + fromQueue
                    + (fromStack == fromQueue ? "  MATCH" : "  MISMATCH"));
            if (fromStack != fromQueue) symmetric = false;
        }
        System.out.println("RESULT: Packet is " + (symmetric ? "SYMMETRIC (palindrome)" : "NOT symmetric"));
        sc.close();
    }
}