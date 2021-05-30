package pub.silence.antigenius.config.tree;

import java.util.ArrayList;
import java.util.HashMap;
import pub.silence.antigenius.config.Language;

public class ConfigTree{
    private static final HashMap<String, ConfigItem<?>> data = new HashMap<>();
    static {
        data.put("data.datasource", new ConfigItem<>("mysql").setSuggest(new String[]{
            "mysql"
        }).forceSuggest());
        data.put("data.prefix", new ConfigItem<>("ag_").setSuggest(new String[]{
            "log_", "prism_"
        }));
        data.put("data.mysql.hostname", new ConfigItem<>("127.0.0.1").setSuggest(new String[]{
            "127.0.0.1", "localhost"
        }));
        data.put("data.mysql.port", new ConfigItem<>(3306).setRange(0,65535));
        data.put("data.mysql.username", new ConfigItem<>("root"));
        data.put("data.mysql.password", new ConfigItem<>(""));
        data.put("data.mysql.database", new ConfigItem<>(""));
        data.put("data.advance.max-failures-before-wait", new ConfigItem<>(5));
        data.put("data.advance.actions-per-insert-batch", new ConfigItem<>(300));
        data.put("data.advance.force-write-queue-on-shutdown", new ConfigItem<>(true));
        
        data.put("language", new ConfigItem<>(Language.getSuggestLanguage()).setSuggest(
            new ArrayList<>(Language.getAvailableLang())
        ));
    }
    public void initialize(){
    
    }
    
}