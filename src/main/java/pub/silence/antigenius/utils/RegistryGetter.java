package pub.silence.antigenius.utils;

import java.util.List;
import java.util.stream.Collectors;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import pub.silence.antigenius.AntiGenius;

public class RegistryGetter {
   
    public static List<String> get(String registryName) {
        try {
            Registry<?> registry = (Registry<?>)Registry.class.getField(registryName).get(null);
            return registry.getIds().stream().map(Identifier::toString).collect(Collectors.toList());
        }
        catch (IllegalAccessException | NoSuchFieldException e) {
            AntiGenius.LOGGER.debug("Registry set " + registryName + " not found.");
        }
        return null;
    }
    
    @Deprecated
    public static List<String> blocks(){
        return Registry.BLOCK.getIds().stream().map(Identifier::toString).collect(Collectors.toList());
    }
    @Deprecated
    public static List<String> items(){
        return Registry.ITEM.getIds().stream().map(Identifier::toString).collect(Collectors.toList());
    }
    @Deprecated
    public static List<String> dimensions(){
        return Registry.DIMENSION_TYPE.getIds().stream().map(Identifier::toString).collect(Collectors.toList());
    }
}
