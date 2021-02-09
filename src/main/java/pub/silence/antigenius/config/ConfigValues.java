package pub.silence.antigenius.config;

import org.yaml.snakeyaml.Yaml;
import pub.silence.antigenius.AntiGenius;
import pub.silence.antigenius.lang.Language;

import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class ConfigValues {
    private final HashMap<String, Object> data = new HashMap<>();
    private final HashMap<String, ArrayList<String>> comment = new HashMap<>();

    public ConfigValues() {
        initializeMap();
        AntiGenius.debug("\n" + toCommentString());
    }

    private void initializeMap() {
        HashMap<String, Object> template = new Yaml().load(new InputStreamReader(Objects.requireNonNull(
                this.getClass().getClassLoader().getResourceAsStream("config_template.yml")
        ), StandardCharsets.UTF_8));
        data.clear();

        for (String nodePath : template.keySet()) { // aaa.bbb.ccc
            HashMap<String, Object> nodeTemplate = (HashMap<String, Object>) template.get(nodePath);
            String[] nodePathSplit = nodePath.split("(?<!\\\\)[.]"); // [aaa, bbb, ccc]
            HashMap<String, Object> nodeVal = data;
            for (int index = 0; index < nodePathSplit.length - 1; index++) { // aaa
                nodeVal.computeIfAbsent(nodePathSplit[index], k -> new HashMap<String, Object>());
                // Equals to: if (nodeVal.get(nodePathSplit[index]) == null) { nodeVal.put(nodePathSplit[index], new HashMap<String, Object>()); }
                if (nodeVal.get(nodePathSplit[index]) instanceof HashMap) {
                    nodeVal = (HashMap<String, Object>) nodeVal.get(nodePathSplit[index]);
                }
            }
            nodeVal.put(
                    nodePathSplit[nodePathSplit.length - 1],
                    nodeTemplate.get("default")
            );

            String commentMsg = Language.getMessageWithCallback(
                    "config.comment." + nodePath,
                    ""
            );
            ArrayList<String> commentLines = new ArrayList<>();
            if (commentMsg.length() != 0) {
                commentLines.addAll(Arrays.asList(commentMsg.split("\\n")));
            }
            if (nodeTemplate.get("type") != null) {
                commentLines.add(Language.getMessage("config.dataType") + nodeTemplate.get("type").toString());
            }
            if (nodeTemplate.get("advice") != null && !((ArrayList<String>) nodeTemplate.get("advice")).isEmpty()) {
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

    public String toCommentString() {
        return toCommentString(data, 0, "");
    }

    private String toCommentString(HashMap<String, Object> node, int tab, String parentNode) {
        StringBuilder result = new StringBuilder();
        for (String nodeName : node.keySet()) {
            String nodePath = parentNode + ((tab == 0) ? "" : ".") + nodeName;
            ArrayList<String> commentLines = comment.get(nodePath);
            if (commentLines != null) {
                for (String line : commentLines) {
                    result.append(String.join("", Collections.nCopies(tab, "  ")))
                            .append("# ").append(line).append("\n");
                }
            }
            Object childNode = node.get(nodeName);
            if (childNode instanceof HashMap) {
                result.append(String.join("", Collections.nCopies(tab, "  ")))
                        .append(nodeName).append(": \n")
                        .append(toCommentString(
                                (HashMap<String, Object>) childNode,
                                tab + 1,
                                nodePath
                        ));
            } else {
                result.append(String.join("", Collections.nCopies(tab, "  ")))
                        .append(nodeName).append(": ")
                        .append(childNode.toString()).append("\n");
            }
        }
        return result.toString();
    }
}
