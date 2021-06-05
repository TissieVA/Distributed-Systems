package be.ua.fti.ei.utils.sockets;


import be.ua.fti.ei.utils.http.PublishBody;

public class NextPreviousBody extends PublishBody implements java.io.Serializable {

    private int previousId, nextId;

    public NextPreviousBody(int previous, int next) {
        this.previousId = previous;
        this.nextId = next;
    }

    public NextPreviousBody() { }

    public int getPreviousId() {
        return previousId;
    }

    public void setPreviousId(int previous) {
        this.previousId = previous;
    }

    public int getNextId() {
        return nextId;
    }

    public void setNextId(int nextId) {
        this.nextId = nextId;
    }
}
