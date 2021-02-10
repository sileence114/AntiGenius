package pub.silence.antigenius.config;



public class Config {
    
    private static ConfigNodes root;
    
    public static void initialize() {
        root = new ConfigNodes();
    }
    
    //        private static void loadConfigFromFile() {
    //            try {
    //                root = new Yaml().load(
    //                        new FileInputStream(AntiGenius.getInstance().getWorkingDir().resolve("config_template.yml")
    //                        .toFile())
    //                );
    //            } catch (IOException e) {
    //                AntiGenius.error("Failed to read config_template.yml.", e);
    //            }
    //        }
    //
    //        private static HashMap createDefault() {
    //            return new HashMap();
    //        }
    //
}
