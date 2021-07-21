package pub.silence.antigenius.events;

import pub.silence.antigenius.events.operate.Operator;

public class BlockDestroy extends BlockEvent {
    
    public BlockDestroy(String block, String world, int x, int y, int z, Operator operator){
        super(block, world, x, y, z, operator);
    }
    
    @Override
    public String toString() {
        return String.format(
            "Block [%s] Destroy at [%s](%d,%d,%d) by %s.",
            block,
            world,
            BlockPosX, BlockPosY, BlockPosZ,
            operator.toString()
        );
    }
}
