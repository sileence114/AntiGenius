package pub.silence.antigenius.utils;

import java.util.List;
import pub.silence.antigenius.AntiGenius;

public class CollectionUtils {
    public static void printSet(List<String> list, String title){
        final int LINE_COUNT = 4;
        
        int maxLength = list.stream().mapToInt(String::length).summaryStatistics().getMax();
        int count = 0;
        StringBuilder sb = new StringBuilder();
        sb.append("\n\n").append(title);
        for(String id : list){
            if(count++ % LINE_COUNT == 0){
                sb.append('\n');
            }
            sb.append(String.format("%1$-" + maxLength + "s", id)).append(" ");
        }
        AntiGenius.LOGGER.info(sb);
    }
}
