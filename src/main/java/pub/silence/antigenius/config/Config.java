package pub.silence.antigenius.config;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.scanner.ScannerException;
import pub.silence.antigenius.AntiGenius;

public class Config {
    private static final HashMap<String, ConfigItem<?>> data = new HashMap<>();
    static {
        data.put("data.datasource", new ConfigItem<>("mysql", 1).setSuggest(new String[]{
            "mysql"
        }).forceSuggest());
        data.put("data.prefix", new ConfigItem<>("ag_", 2).setSuggest(new String[]{
            "log_", "prism_"
        }));
    
        data.put("data.mysql.hostname", new ConfigItem<>("127.0.0.1", 3).setSuggest(new String[]{
            "127.0.0.1", "localhost"
        }));
    
        data.put("data.mysql.port", new ConfigItem<>(3306, 4).setRange(0,65535));
        data.put("data.mysql.database", new ConfigItem<>("minecraft", 5));
        data.put("data.mysql.username", new ConfigItem<>("root", 6));
        data.put("data.mysql.password", new ConfigItem<>("", 7));
        
        data.put("data.advance.max-failures-before-wait", new ConfigItem<>(5, 8));
        data.put("data.advance.actions-per-insert-batch", new ConfigItem<>(300, 9));
        data.put("data.advance.force-write-queue-on-shutdown", new ConfigItem<>(true, 10));
        
        data.put("language", new ConfigItem<>(Language.getSuggestLanguage(), 11).setSuggest(
            new ArrayList<>(Language.getAvailableLang())
        ).forceSuggest());
    }
    public static void initialize(){
        // AntiGenius.logger().debug("\n" + toCommentString());
        if(!loadCostumeData()){
            saveConfigFile();
        }
    }
    private static boolean loadCostumeData() {
        File configFile = AntiGenius.getInstance().getWorkingDir().resolve("config.yml").toFile();
        if (!configFile.exists()) {
            AntiGenius.logger().info(Language.get("console.log.config.loadFailed"));
            return false;
        }
        try {
            merge(new Yaml().load(new FileReader(configFile)), "");
//            AntiGenius.debug("\n" + toCommentString());
            return true;
        }
        catch (IOException | ScannerException e) {
            AntiGenius.logger().error("Load config.yml failed. Backup it and create a new file.\n" + e.getMessage());
            return false;
        }
    }
    private static String toCommentString() {
        return toCommentString(getMapWithNodeStruct(), 0, "");
    }
    private static String toCommentString(HashMap<String, Object> node, int tab, String parentNode) {
        StringBuilder result = new StringBuilder();
        ArrayList<String> keySet = new ArrayList<>(node.keySet());
        keySet.sort((o1, o2) -> {
            ConfigItem<?> ci1 = data.get(parentNode + (tab == 0 ? "" : ".") + o1);
            ConfigItem<?> ci2 = data.get(parentNode + (tab == 0 ? "" : ".") + o2);
            int i1 = ci1==null ? 0 : ci1.getSortId();
            int i2 = ci2==null ? 0 : ci2.getSortId();
            return i1 - i2;
        });
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
        for (String nodePath: data.keySet()) { // aaa.bbb.ccc
            String[] nodePathSplit = nodePath.split("(?<!\\\\)[.]"); // [aaa, bbb, ccc]
            HashMap<String, Object> nodeVal = result;
            for (int index = 0; index < nodePathSplit.length - 1; index++) { // aaa
                nodeVal.computeIfAbsent(nodePathSplit[index], k -> new HashMap<String, Object>());
                if (nodeVal.get(nodePathSplit[index]) instanceof HashMap) {
                    nodeVal = (HashMap<String, Object>)nodeVal.get(nodePathSplit[index]);
                }
            }
            nodeVal.put(nodePathSplit[nodePathSplit.length - 1], data.get(nodePath));
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
                ConfigItem<?> configItem = data.get(childNodePath);
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
                AntiGenius.logger().debug("Backup old config file.");
            }
            PrintWriter out = new PrintWriter(configFile);
            String[] configLines = toCommentString().split("\n");
            for (String line : configLines) {
                out.println(line);
            }
            out.close();
        }
        catch (IOException e) {
            AntiGenius.logger().error("UnExpected IOException happened when writing config.yml.\n" + e.getMessage());
            System.exit(0);
        }
    }
    public static void reset(){
        for (String key: data.keySet()) {
            data.get(key).reset();
        }
    }
    public static ConfigItem<?> configItem(String node){
        return data.get(node);
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
}