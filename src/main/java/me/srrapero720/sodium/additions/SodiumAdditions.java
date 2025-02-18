package me.srrapero720.sodium.additions;

import me.srrapero720.sodium.additions.util.EAConfig;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;

@Mod(SodiumAdditions.ID)
public class SodiumAdditions {
    public static final String ID = "sodium_additions";
    public static final Logger LOGGER = LogManager.getLogger("sodium++Additions");
    public static final Marker IT = MarkerManager.getMarker("Main");

    public SodiumAdditions() {
        LOGGER.info(IT, "Starting Sodium++Additions");
        EAConfig.init();
        LOGGER.info(IT, "Startup Finished");
    }
}