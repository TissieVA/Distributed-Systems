package be.ua.fti.ei;


public class NextPrevious {

    private int previous,next,numberOfNodes;

    public NextPrevious(int previous, int next, int numberOfNodes) {
        this.previous = previous;
        this.next = next;
        this.numberOfNodes = numberOfNodes;
    }

    public NextPrevious() { }

    public int getPrevious() {
        return previous;
    }

    public void setPrevious(int previous) {
        this.previous = previous;
    }

    public int getNext() {
        return next;
    }

    public void setNext(int next) {
        this.next = next;
    }

    public int getNumberOfNodes() {
        return numberOfNodes;
    }

    public void setNumberOfNodes(int numberOfNodes) {
        this.numberOfNodes = numberOfNodes;
    }
}
