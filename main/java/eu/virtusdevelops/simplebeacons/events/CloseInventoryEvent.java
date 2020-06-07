package eu.virtusdevelops.simplebeacons.events;


import eu.virtusdevelops.simplebeacons.SimpleBeacons;
import eu.virtusdevelops.simplebeacons.gui.Handler;
import eu.virtusdevelops.simplebeacons.gui.InventoryCreator;
import eu.virtusdevelops.simplebeacons.gui.actions.InventoryCloseAction;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class CloseInventoryEvent implements Listener {

    private SimpleBeacons simpleBeacons;
    private Handler handler;

    public CloseInventoryEvent(SimpleBeacons simpleBeacons, Handler handler){
        this.handler = handler;
        this.simpleBeacons = simpleBeacons;
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onInventoryClose(InventoryCloseEvent event){
        // remove player
        handler.removeFromList(event.getPlayer().getUniqueId());

        //Get our CustomHolder
        InventoryCreator customHolder = null;
        try {
            customHolder = (InventoryCreator) event.getView().getTopInventory().getHolder();
        }catch (Exception error){

        }
        if(customHolder != null){
            for(InventoryCloseAction closeAction : customHolder.getCloseActions()){
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        if(!handler.hasOpened(event.getPlayer().getUniqueId())) {
                            closeAction.execute((Player) event.getPlayer(), event.getInventory());
                        }
                    }
                }.runTaskLater(simpleBeacons, 2L);
            }
        }
    }
}
