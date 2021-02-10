package pub.silence.antigenius.config;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import org.yaml.snakeyaml.Yaml;
import pub.silence.antigenius.AntiGenius;
import pub.silence.antigenius.lang.Language;

import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class Config {
    
    private static final HashMap<String, Object> data = new HashMap<>();
    private static final HashMap<String, ArrayList<String>> comment = new HashMap<>();
    
    public static void initialize() {
        initializeMaps();
        initializeCostumeData();
    }
    
    private static void initializeMaps() {
        HashMap<String, Object> template = new Yaml().load(new InputStreamReader(
            Objects.requireNonNull(Config.class.getClassLoader().getResourceAsStream("config_template.yml")),
            StandardCharsets.UTF_8
        ));
        toHashMap(template.get("language")).put("default", Language.getSystemLangCode());
        toHashMap(template.get("language")).put("advice", new ArrayList<>(Language.getAvailableLang()));
        data.clear();
        
        for (String nodePath : template.keySet()) { // aaa.bbb.ccc
            HashMap<String, Object> nodeTemplate = toHashMap(template.get(nodePath));
            String[] nodePathSplit = nodePath.split("(?<!\\\\)[.]"); // [aaa, bbb, ccc]
            HashMap<String, Object> nodeVal = data;
            for (int index = 0; index < nodePathSplit.length - 1; index++) { // aaa
                nodeVal.computeIfAbsent(nodePathSplit[index], k -> new HashMap<String, Object>());
                if (nodeVal.get(nodePathSplit[index]) instanceof HashMap) {
                    nodeVal = toHashMap(nodeVal.get(nodePathSplit[index]));
                }
            }
            nodeVal.put(nodePathSplit[nodePathSplit.length - 1], nodeTemplate.get("default"));
            
            String commentMsg = Language.getMessageWithCallback("config.comment." + nodePath, "");
            ArrayList<String> commentLines = new ArrayList<>();
            if (commentMsg.length() != 0) {
                commentLines.addAll(Arrays.asList(commentMsg.split("\\n")));
            }
            if (nodeTemplate.get("type") != null) {
                commentLines.add(Language.getMessage("config.dataType") + nodeTemplate.get("type").toString());
            }
            if (nodeTemplate.get("advice") != null && !(
                (ArrayList<String>) nodeTemplate.get("advice")
            ).isEmpty()) {
                commentLines.add(Language.getMessage(
                    ((boolean) nodeTemplate.get("force_advice")) ? "config.available" : "config.advise"
                ) + nodeTemplate.get("advice").toString());
            }
            if (nodeTemplate.get("default") != null) {
                commentLines.add(Language.getMessage("config.default") + nodeTemplate.get("default").toString());
            }
            comment.put(nodePath, commentLines);
        }
    }
    
    private static void initializeCostumeData() {
    
    }
    
    private static String toCommentString(HashMap<String, Object> node, int tab, String parentNode) {
        StringBuilder result = new StringBuilder();
        for (String nodeName : node.keySet()) {
            String nodePath = parentNode + ((tab == 0) ? "" : ".") + nodeName;
            ArrayList<String> commentLines = comment.get(nodePath);
            if (commentLines != null) {
                for (String line : commentLines) {
                    result.append(String.join("", Collections.nCopies(tab, "  ")))
                          .append("# ")
                          .append(line)
                          .append("\n");
                }
            }
            Object childNode = node.get(nodeName);
            if (childNode instanceof HashMap) {
                result.append(String.join("", Collections.nCopies(tab, "  ")))
                      .append(nodeName)
                      .append(": \n")
                      .append(toCommentString(
                          toHashMap(childNode),
                          tab + 1,
                          nodePath
                      ));
            }
            else {
                result.append(String.join("", Collections.nCopies(tab, "  ")))
                      .append(nodeName)
                      .append(": ")
                      .append(childNode.toString())
                      .append("\n");
            }
        }
        return result.toString();
    }
    
    private static HashMap<String, Object> toHashMap(Object obj) {
        if (obj instanceof HashMap) {
            return (HashMap<String, Object>) obj;
        }
        else {
            return null;
        }
    }
    
    public static String toCommentString() {
        return toCommentString(data, 0, "");
    }
    
    public static void saveConfigFile() {
        File configFile = AntiGenius.getInstance().getWorkingDir().resolve("config.yml").toFile();
        try {
            if (configFile.exists()) {
                Files.copy(
                    configFile.toPath(),
                    AntiGenius.getInstance()
                              .getWorkingDir()
                              .resolve(String.format(
                                  "config.%s.yml",
                                  new SimpleDateFormat("MMddHHmmss").format(new Date())
                              ))
                              .toAbsolutePath()
                );
                AntiGenius.debug("Backup old config file.");
            }
            PrintWriter out = new PrintWriter(configFile);
            String[] configLines = toCommentString().split("\n");
            for (String line : configLines) {
                out.println(line);
            }
            out.close();
        }
        catch (IOException e) {
            AntiGenius.error("UnExpected IOException happened when writing config.yml", e);
        }
    }
}
