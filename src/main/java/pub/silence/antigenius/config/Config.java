package pub.silence.antigenius.config;



public class Config {
    
    private static ConfigValues root;
    
    public static void initialize() {
        root = new ConfigValues();
        //        loadConfigFromFile();
        //        if (root == null || root.isEmpty()) {
        //            AntiGenius.info(Language.getMessage("console.log.config.loadFailedInDict"));
        //            root = createDefault();
        //        }
    }
    
    //    private static void loadConfigFromFile() {
    //        try {
    //            root = new Yaml().load(
    //                    new FileInputStream(AntiGenius.getInstance().getWorkingDir().resolve("config_template.yml").toFile())
    //            );
    //        } catch (IOException e) {
    //            AntiGenius.error("Failed to read config_template.yml.", e);
    //        }
    //    }
    //
    //    private static HashMap createDefault() {
    //        return new HashMap();
    //    }
    
}
