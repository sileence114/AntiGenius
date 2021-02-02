package pub.silence.antigenius.config;


import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.yaml.YamlConfigurationLoader;
import pub.silence.antigenius.lang.Language;
import pub.silence.antigenius.AntiGenius;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Objects;


public class Config {
    private static CommentedConfigurationNode root;

    public static void initialize(){
        if(!loadConfigFromFile()){
            AntiGenius.info(Language.getMessage("console.log.config.loadFailedInDict"));
            root = createDefault();
        }
    }

    public static boolean getBoolean(Object... node){
        return root.node(node).getBoolean();
    }

    public static int getInt(Object... node){
        return root.node(node).getInt();
    }

    public static double getDouble(Object... node){
        return root.node(node).getDouble();
    }

    public static String getString(Object... node){
        return root.node(node).getString();
    }

    public static Object get(Type type, Object... node){
        try {
            return root.node(node).get(type);
        }
        catch (SerializationException e) {
            e.printStackTrace();
            try {
                return type.getClass().newInstance();
            }
            catch (InstantiationException | IllegalAccessException ex) {
                return null;
            }
        }
    }

    private static boolean loadConfigFromFile(){
        try {
            CommentedConfigurationNode configRoot = YamlConfigurationLoader
                    .builder()
                    .path(AntiGenius.getInstance().getWorkingDir().resolve("config.yml"))
                    .build()
                    .load();
            checkConfig(configRoot);
        }
        catch (IOException e) {
            return false;
        }
        return !root.empty();
    }

    private static void checkConfig(CommentedConfigurationNode configRoot){
        try {
            CommentedConfigurationNode template = YamlConfigurationLoader
                    .builder()
                    .url(Objects.requireNonNull(Config.class.getClassLoader().getResource("config.yml")))
                    .build()
                    .load();
            AntiGenius.info(template.toString());
            for(Object key : template.childrenMap().keySet()){
                String node = key.toString();

            }
        }
        catch (IOException e){
            AntiGenius.error("Exception from Loading config template.",e);
            System.exit(1);
        }
    }

    private static CommentedConfigurationNode createDefault() {
        CommentedConfigurationNode defaultRoot = CommentedConfigurationNode.root();
        try {
            defaultRoot.node("datasource").set(String.class, "mysql")
                    .comment(Language.getMessage("config.comment.datasource"))
                    .comment(Language.getMessage("config.defaultValue") + "mysql")
                    .comment(Language.getMessage("config.available") + "mysql");
            int x = 1;
        } catch (SerializationException ignored) { }
        return defaultRoot;
    }
}
