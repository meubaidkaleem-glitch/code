public class RequestQueue {
    private char[] data;
    private int front, rear, size, capacity;

    public RequestQueue(int capacity) {
        this.capacity = capacity;
        data = new char[capacity];
        front = 0; rear = -1; size = 0;
    }

    public void enqueue(char c) {
        if (size == capacity) { System.out.println("Queue overflow"); return; }
        rear = (rear + 1) % capacity;
        data[rear] = c;
        size++;
    }

    public char dequeue() {
        if (isEmpty()) throw new RuntimeException("Queue underflow");
        char c = data[front];
        front = (front + 1) % capacity;
        size--;
        return c;
    }

    public boolean isEmpty() { return size == 0; }
}