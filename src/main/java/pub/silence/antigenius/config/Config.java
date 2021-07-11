package pub.silence.antigenius.config;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang3.ArrayUtils;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.scanner.ScannerException;
import pub.silence.antigenius.AntiGenius;

public class Config {
    private Config() {}
    
    private static final Map<String, ConfigItem<?>> DATA = Collections.synchronizedMap(new HashMap<>());
    private static final String[] KEY_SORT = new String[]{
        "data",
            "data.prefix",
            "data.datasource",
            "data.mysql",
                "data.mysql.hostname",
                "data.mysql.port",
                "data.mysql.database",
                "data.mysql.username",
                "data.mysql.password",
            "data.advance",
                "data.advance.max-failures-before-wait",
                "data.advance.actions-per-insert-batch",
                "data.advance.force-write-queue-on-shutdown",
        "tracking",
            "tracking.block-break",
            "tracking.block-burn",
            "tracking.block-dispense",
            "tracking.block-fade",
            "tracking.block-fall",
            "tracking.block-form",
            "tracking.block-place",
            "tracking.block-shift",
            "tracking.block-spread",
            "tracking.block-use",
    };
    static {
        DATA.put("data.prefix", new ConfigItem<>("ag_").setSuggest(new String[]{
            "log_", "prism_"
        }));
        DATA.put("data.datasource", new ConfigItem<>("mysql").setSuggest(new String[]{
            "mysql"
        }).forceSuggest());
        DATA.put("data.mysql.hostname", new ConfigItem<>("127.0.0.1").setSuggest(new String[]{
            "127.0.0.1", "localhost"
        }));
        DATA.put("data.mysql.port", new ConfigItem<>(3306).setRange(0, 65535));
        DATA.put("data.mysql.database", new ConfigItem<>("minecraft"));
        DATA.put("data.mysql.username", new ConfigItem<>("root"));
        DATA.put("data.mysql.password", new ConfigItem<>(""));
        
        DATA.put("data.advance.max-failures-before-wait", new ConfigItem<>(5));
        DATA.put("data.advance.actions-per-insert-batch", new ConfigItem<>(300));
        DATA.put("data.advance.force-write-queue-on-shutdown", new ConfigItem<>(true));
        DATA.put("language", new ConfigItem<>(Language.getSuggestLanguage()).setSuggest(
            new ArrayList<>(Language.getAvailableLang())
        ).forceSuggest());
        
        DATA.put("tracking.block-break", new ConfigItem<>(true));
        DATA.put("tracking.block-burn", new ConfigItem<>(true));
        DATA.put("tracking.block-dispense", new ConfigItem<>(true));
        DATA.put("tracking.block-fade", new ConfigItem<>(true));
        DATA.put("tracking.block-fall", new ConfigItem<>(true));
        DATA.put("tracking.block-form", new ConfigItem<>(true));
        DATA.put("tracking.block-place", new ConfigItem<>(true));
        DATA.put("tracking.block-shift", new ConfigItem<>(true));
        DATA.put("tracking.block-spread", new ConfigItem<>(true));
        DATA.put("tracking.block-use", new ConfigItem<>(true));
    }
    public static void initialize(){
        if(!loadCostumeData()){
            saveConfigFile();
        }
        AntiGenius.LOGGER.debug("\n" + toCommentString());
    }
    private static boolean loadCostumeData() {
        File configFile = AntiGenius.getInstance().getWorkingDir().resolve("config.yml").toFile();
        if (!configFile.exists()) {
            AntiGenius.LOGGER.info(Language.get("console.log.config.loadFailed"));
            return false;
        }
        try {
            merge(new Yaml().load(new FileReader(configFile)), "");
            return true;
        }
        catch (IOException | ScannerException | NullPointerException e) {
            AntiGenius.LOGGER.error("Load config.yml failed. Backup it and create a new file.\n" + e.getMessage());
            return false;
        }
    }
    private static String toCommentString() {
        return toCommentString(getMapWithNodeStruct(), 0, "");
    }
    private static String toCommentString(HashMap<String, Object> node, int tab, String parentNode) {
        StringBuilder result = new StringBuilder();
        ArrayList<String> keySet = new ArrayList<>(node.keySet());
        keySet.sort(Comparator.comparingInt(o -> ArrayUtils.indexOf(KEY_SORT, o)));
        for (String nodeName : keySet) {
            String nodePath = parentNode + (tab == 0 ? "" : ".") + nodeName;
            String tabIndentation  = String.join("", Collections.nCopies(tab, "  "));
            String messageNode = "config.comment." + nodePath;
            String nodeComment = Language.get(messageNode);
            // Node comment in Language file.
            if(!nodeComment.equals(messageNode) && !nodeComment.isEmpty()){
                for (String line : nodeComment.split("\n")){
                    result.append(tabIndentation).append("# ").append(line).append('\n');
                }
            }
            Object nodeValue = node.get(nodeName);
            if (nodeValue instanceof HashMap) {
                result.append(tabIndentation).append(nodeName).append(": \n").append(toCommentString(
                    (HashMap<String, Object>)nodeValue, tab + 1, nodePath
                ));
            }
            else if (nodeValue instanceof ConfigItem) {
                ConfigItem<?> configItem = (ConfigItem<?>) nodeValue;
                // Config item data type
                result.append(tabIndentation).append("# ").append(Language.get("config.dataType"))
                      .append(configItem.type().getSimpleName().toLowerCase()).append('\n');
                // Suggest
                if(!configItem.getSuggest().isEmpty()){
                    result.append(tabIndentation).append("# ").append(Language.get(
                        configItem.isForceSuggest() ? "config.available": "config.advise"
                    )).append(configItem.getSuggest().toString()).append('\n');
                }
                if(configItem.isForceRange()){
                    double[] range = configItem.getRange();
                    result.append(tabIndentation).append("# ").append(Language.get("config.range")).append(
                        configItem.type().equals(Integer.class) ?
                        String.format(
                            "[%d, %d)"
                            , (int)range[0], (int)range[1]
                        ):String.format(
                            "[%f, %f)"
                            , range[0], range[1]
                        )
                    ).append('\n');
                }
                // Default
                result.append(tabIndentation).append("# ").append(
                    Language.get("config.default")
                ).append(configItem.getDefaultValue().toString()).append('\n');
                // CommentAbove
                // Value
                result.append(tabIndentation).append(nodeName).append(": ").append(
                    configItem.get()
                ).append("\n");
            }
        }
        return result.toString();
    }
    private static HashMap<String, Object> getMapWithNodeStruct(){
        HashMap<String, Object> result = new HashMap<>();
        for (String nodePath: DATA.keySet()) { // aaa.bbb.ccc
            String[] nodePathSplit = nodePath.split("(?<!\\\\)[.]"); // [aaa, bbb, ccc]
            HashMap<String, Object> nodeVal = result;
            for (int index = 0; index < nodePathSplit.length - 1; index++) { // aaa
                nodeVal.computeIfAbsent(nodePathSplit[index], k -> new HashMap<String, Object>());
                if (nodeVal.get(nodePathSplit[index]) instanceof HashMap) {
                    nodeVal = (HashMap<String, Object>)nodeVal.get(nodePathSplit[index]);
                }
            }
            nodeVal.put(nodePathSplit[nodePathSplit.length - 1], DATA.get(nodePath));
        }
        return result;
    }
    private static void merge(HashMap<String, Object> root, String nodePath){
        for (String node : root.keySet()) {
            Object nodeValue = root.get(node);
            String childNodePath = (nodePath.isEmpty() ? "" : (nodePath + ".")) + node;
            if (nodeValue instanceof HashMap) {
                merge((HashMap<String, Object>)nodeValue, childNodePath);
            }
            else {
                ConfigItem<?> configItem = DATA.get(childNodePath);
                if(configItem != null){
                    configItem.set(root.get(node));
                }
            }
        }
    }
    private static void saveConfigFile() {
        File configFile = AntiGenius.getInstance().getWorkingDir().resolve("config.yml").toFile();
        try {
            if (configFile.exists()) {
                Files.copy(
                    configFile.toPath(),
                    AntiGenius.getInstance().getWorkingDir().resolve(String.format(
                        "config.%s.yml",
                        new SimpleDateFormat("MMddHHmmss").format(new Date())
                    )).toAbsolutePath()
                );
                AntiGenius.LOGGER.debug("Backup old config file.");
            }
            PrintWriter out = new PrintWriter(configFile);
            String[] configLines = toCommentString().split("\n");
            for (String line : configLines) {
                out.println(line);
            }
            out.close();
        }
        catch (IOException e) {
            AntiGenius.LOGGER.error("UnExpected IOException happened when writing config.yml.\n" + e.getMessage());
            System.exit(0);
        }
    }
    public static void reset(){
        for (String key: DATA.keySet()) {
            DATA.get(key).reset();
        }
    }
    public static ConfigItem<?> configItem(String node){
        return DATA.get(node);
    }
    public static int getInt(String node){
        return (int)configItem(node).get();
    }
    public static double getDouble(String node){
        return (double)configItem(node).get();
    }
    public static boolean getBoolean(String node){
        return (boolean)configItem(node).get();
    }
    public static String getString(String node){
        return (String)configItem(node).get();
    }
    public static <T> T get(String node){
        return ( (ConfigItem<T>)configItem(node) ).get();
    }
}