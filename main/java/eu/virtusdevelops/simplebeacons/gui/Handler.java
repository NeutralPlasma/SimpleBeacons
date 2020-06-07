package eu.virtusdevelops.simplebeacons.gui;

import eu.virtusdevelops.simplebeacons.SimpleBeacons;
import eu.virtusdevelops.simplebeacons.data.BeaconData;
import eu.virtusdevelops.simplebeacons.events.beacons.BeaconInteract;
import eu.virtusdevelops.simplebeacons.gui.guis.EffectSelectGUI;
import eu.virtusdevelops.simplebeacons.gui.guis.MainGUI;
import eu.virtusdevelops.simplebeacons.gui.guis.ModulesSelectGUI;
import eu.virtusdevelops.simplebeacons.storage.BeaconHandler;
import eu.virtusdevelops.simplebeacons.storage.MessagesHandler;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Handler {

    private SimpleBeacons simpleBeacons;
    private MessagesHandler messagesHandler;
    private MainGUI mainGUI;
    private EffectSelectGUI effectSelectGUI;
    private ModulesSelectGUI modulesSelectGUI;

    private List<UUID> openedInv = new ArrayList<>();

    public Handler(SimpleBeacons simpleBeacons, MessagesHandler messagesHandler, BeaconHandler beaconHandler){
        mainGUI = new MainGUI(simpleBeacons, messagesHandler, beaconHandler, this);
        effectSelectGUI = new EffectSelectGUI(simpleBeacons, messagesHandler, beaconHandler, this);
        modulesSelectGUI = new ModulesSelectGUI(simpleBeacons, messagesHandler, beaconHandler, this);
        this.simpleBeacons = simpleBeacons;
    }

    public void openMainGUI(Player player, BeaconData beaconData){
        mainGUI.construct(player, beaconData);
    }

    public void openEffectGUI(Player player, BeaconData beaconData, int page){
        effectSelectGUI.construct(player, beaconData, page);
    }
    public void openModulesGUI(Player player, BeaconData beaconData, int page){
        modulesSelectGUI.construct(player, beaconData, page);
    }

    public void addToList(UUID uuid){
        openedInv.add(uuid);
    }
    public void removeFromList(UUID uuid){
        openedInv.remove(uuid);
    }
    public boolean hasOpened(UUID uuid) {
        return openedInv.contains(uuid);
    }
}
