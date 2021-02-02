package pub.silence.antigenius.lang;

import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.gson.GsonConfigurationLoader;
import pub.silence.antigenius.AntiGenius;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.*;
import java.util.regex.Pattern;

public class Language {
    private static final HashMap<String, ConfigurationNode> LANG_MAP = new HashMap<>();
    private static final HashSet<String> AVAILABLE_LANG = new HashSet<>();

    private static final String SYSTEM_LANG = Locale.getDefault().getLanguage();
    private static final String SYSTEM_AREA = Locale.getDefault().getCountry().toLowerCase();

    // Lang code of operate system.
    private static final String SYSTEM_LANG_CODE = SYSTEM_LANG + "_" + SYSTEM_AREA;
    // Lang code setting by config.yml
    private static String configLangCode;

    private static boolean codeConfigEqualsSystem = false;
    private static boolean systemLangCodeAvailable = false;

    public static void initialize(){
        loadAllLang();
        AntiGenius.info(getMessageWithCallback(
                "console.log.language.setLanguageToYourSystem",
                "Try to set the language to the system language."
        ));
        setLanguage(SYSTEM_LANG_CODE);
    }

    /**
     * If some lang doesn't in available lang set,
     * this methode will suggest a lang code.
     * It will return same language in other countries/area.
     * Otherwise it can return other language in the country/area.
     * Firstly try to match language_countries/area uses the value in config.
     * Then try to use value from system.
     * In the worst case(could neither match country nor language), use English(en_us).
     * @return Language code
     */
    private static String getSuggestLanguage(){
        ArrayList<String> matchCode = new ArrayList<>();
        if(!SYSTEM_LANG_CODE.equals(configLangCode)){
            matchCode.addAll(Arrays.asList(configLangCode.split("_")));
        }
        matchCode.add(SYSTEM_LANG);
        matchCode.add(SYSTEM_AREA);
        for(String code : matchCode){
            for(String lang : AVAILABLE_LANG){
                if(Pattern.matches("(.*)" + code + "(.*)", lang)){
                    return lang;
                }
            }
        }
        return "en_us";
    }

    /**
     * Load all lang file from resources directory "resources\assets\antigenius\lang"
     * and costume lang directory ".\config\antigenius\lang".
     */
    public static void loadAllLang() {
        FileFilter langFileFiller = pathname -> Pattern.matches("[a-z]{2}_[a-z]{2}.json", pathname.getName());
        try {
            // Directory: resources\assets\antigenius\lang
            File resourcesLangDirectory = new File(
                    Objects.requireNonNull(Language.class.getClassLoader()
                            .getResource("assets\\antigenius\\lang\\")
                    ).toURI()
            );
            // Directory: .\config\antigenius\lang
            File costumeLangDirectory = new File(
                    AntiGenius.getInstance().getWorkingDir().resolve("lang").toUri()
            );
            ArrayList<File> langFiles = new ArrayList<>();
            if (costumeLangDirectory.exists() && costumeLangDirectory.isDirectory()) {
                langFiles.addAll(Arrays.asList(costumeLangDirectory.listFiles(langFileFiller)));
            } else {
                AntiGenius.info(getMessageWithCallback(
                        "console.log.language.noCostumeLangDirectory",
                        "Costume lang directory not found."
                ));
            }
            if (resourcesLangDirectory.isDirectory()) {
                langFiles.addAll(Arrays.asList(resourcesLangDirectory.listFiles(langFileFiller)));
            }
            for (File item : langFiles) {
                if (item.getName().endsWith(".json")) {
                    LANG_MAP.put(
                            item.getName().replaceAll(".json", ""),
                            GsonConfigurationLoader.builder().file(item).build().load()
                    );
                }
            }
            AVAILABLE_LANG.clear();
            AVAILABLE_LANG.addAll(LANG_MAP.keySet());
            systemLangCodeAvailable = AVAILABLE_LANG.contains(SYSTEM_LANG_CODE);
        } catch (URISyntaxException | IOException e) {
            AntiGenius.warn(getMessageWithCallback(
                    "console.log.language.exceptionHappenWhenLoadLangFile",
                    "Error happened when loading lang files."
            ), e);
        }
        AntiGenius.info(String.format(getMessageWithCallback(
                "console.log.language.langFileLoadComplete",
                "%d lang file load complete."
        ), LANG_MAP.size()));
    }

    /**
     * Set a language.
     * Load suggest language if specify lang didn't exist.
     * @param specifyLangCode Code of specify lang.
     */
    public static void setLanguage(String specifyLangCode){
        configLangCode = specifyLangCode;
        if(!AVAILABLE_LANG.contains(specifyLangCode)){
            configLangCode = getSuggestLanguage();
            codeConfigEqualsSystem = configLangCode.equals(SYSTEM_LANG_CODE);
            AntiGenius.info(String.format(getMessageWithCallback(
                    "console.log.language.suggestALanguageForSpecifyLangCodeNotFound",
                    "The '%s' language you configured didn't found! With reference to your settings and operating system information, switch to %s."
            ), specifyLangCode, configLangCode));
            AntiGenius.info(getMessageWithCallback(
                    "console.log.language.askForTranslate",
                    "If you understand the current language, please come to Github to help translate language files!"
            ));
        }
        codeConfigEqualsSystem = configLangCode.equals(SYSTEM_LANG_CODE);
    }

    /**
     * Trying to get message with getMessage(key).
     * If failed, return callback message.
     * Most of time just getMessage(key), because lang file is loaded.
     * @param key      Message Key.
     * @param callback Callback Message.
     * @return Result of getMessage(key) or callback message.
     */
    public static String getMessageWithCallback(String key, String callback) {
        String message = getMessage(key);
        if (message.equals(key)) {
            AntiGenius.debug(String.format("Getting message failed from <%s>, using callback value <%s>.", key, callback));
            return callback;
        }
        else {
            return message;
        }
    }


    /**
     * Trying to get message from config lang.
     * @param key Key of Message in lang file.
     * @return Message, or key if failed.
     */
    public static String getMessage(String key) {
        return getMessage(key, configLangCode);
    }


    /**
     * Get message from specify lang code.
     * If failed, will tried: config lang -> system lang -> en_us
     * @param key Key of Message in lang file.
     * @param specifyLangCode Code of specify lang.
     * @return Message, or key if failed.
     */
    public static String getMessage(String key, String specifyLangCode) {
        if(LANG_MAP.size()!=0){
            String msg;
            if(AVAILABLE_LANG.contains(specifyLangCode)){
                msg = LANG_MAP.get(specifyLangCode).node(key).getString();
                if(msg != null){
                    return msg;
                }
            }
            if(codeConfigEqualsSystem){
                msg = LANG_MAP.get(configLangCode).node(key).getString();
                if(msg != null){
                    return msg;
                }
            }
            if(systemLangCodeAvailable){
                msg = LANG_MAP.get(SYSTEM_LANG_CODE).node(key).getString();
                if(msg != null){
                    return msg;
                }
            }
            msg = LANG_MAP.get("en_us").node(key).getString();
            if(msg != null){
                return msg;
            }
        }
        return key;
    }
}
