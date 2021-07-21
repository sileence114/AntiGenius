package pub.silence.antigenius.events.operate;

public class PlayerOperator extends Operator{
    protected final String playerName;
    protected final String uuid;
    
    public PlayerOperator(String name, String uuid){
        super("player");
        this.playerName = name;
        this.uuid = uuid;
    }
    @Override
    public String toString() {
        return String.format("%s(%s)", this.playerName, this.uuid);
    }
}
