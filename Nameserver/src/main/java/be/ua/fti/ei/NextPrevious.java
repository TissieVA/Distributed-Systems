package be.ua.fti.ei;

/**
 * Created by asif on 03/05/2021
 */
public class NextPrevious {

    private Integer previous,next,numberOfNodes;

    public NextPrevious(Integer previous, Integer next, Integer numberOfNodes) {
        this.previous = previous;
        this.next = next;
        this.numberOfNodes = numberOfNodes;
    }

    public Integer getPrevious() {
        return previous;
    }

    public void setPrevious(Integer previous) {
        this.previous = previous;
    }

    public Integer getNext() {
        return next;
    }

    public void setNext(Integer next) {
        this.next = next;
    }

    public Integer getNumberOfNodes() {
        return numberOfNodes;
    }

    public void setNumberOfNodes(Integer numberOfNodes) {
        this.numberOfNodes = numberOfNodes;
    }
}
