package pub.silence.antigenius.events;

import pub.silence.antigenius.events.operate.Operator;

public abstract class BlockEvent implements LoggedEvent {
    
    
    protected Operator operator;
    protected final String block;
    protected final String world;
    protected final int BlockPosX;
    protected final int BlockPosY;
    protected final int BlockPosZ;
    
    BlockEvent(String block, String world, int x, int y, int z, Operator operator){
        this.block = block;
        this.world = world;
        this.BlockPosX = x;
        this.BlockPosY = y;
        this.BlockPosZ = z;
        this.operator = operator;
    }
}
