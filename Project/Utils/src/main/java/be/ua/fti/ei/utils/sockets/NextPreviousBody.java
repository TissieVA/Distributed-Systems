package be.ua.fti.ei.utils.sockets;


import be.ua.fti.ei.utils.http.PublishBody;

public class NextPreviousBody extends PublishBody implements java.io.Serializable {

    private int previous,next;

    public NextPreviousBody(int previous, int next) {
        this.previous = previous;
        this.next = next;
    }

    public NextPreviousBody() { }

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
}
