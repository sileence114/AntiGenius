package pub.silence.antigenius.events.operate;

public class Operator {
    protected final String name;
    protected Operator source = null;
    
    public Operator(String name){
        this.name = name;
    }
    public void setSource(Operator source){
        this.source = source;
    }
    public String toString() {
        return (source == null) ?
               String.format("#%s", name) :
               String.format("#%s\n Caused by: %s", name, source.toString());
    }
}
