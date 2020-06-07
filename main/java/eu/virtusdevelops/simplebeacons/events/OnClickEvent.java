package eu.virtusdevelops.simplebeacons.events;

import eu.virtusdevelops.simplebeacons.gui.Icon;
import eu.virtusdevelops.simplebeacons.gui.InventoryCreator;
import eu.virtusdevelops.simplebeacons.gui.actions.*;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class OnClickEvent implements Listener {

    public OnClickEvent(){

    }

    @EventHandler
    public void onClick(InventoryClickEvent event){
        if (event.getWhoClicked() instanceof Player) {
            Player player = (Player) event.getWhoClicked();

            //Check if the item the player clicked on is valid
            ItemStack itemStack = event.getCurrentItem();
            if (itemStack == null || itemStack.getType() == Material.AIR) return;

            //Get our CustomHolder
            InventoryCreator customHolder = null;
            try {
                customHolder = (InventoryCreator) event.getView().getTopInventory().getHolder();
            }catch (Exception error){
                return;
            }

            //Check if the clicked slot is any icon
            if(customHolder == null) return;
            Icon icon = customHolder.getIcon(event.getRawSlot());
            if (icon == null) return;

            event.setCancelled(true);
            if(event.getClick() == ClickType.LEFT){
                for(LeftClickAction leftClickAction : icon.getLeftClickActions()){
                    leftClickAction.execute(player);
                }
            }
            if(event.getClick() == ClickType.RIGHT){
                for(RightClickAction rightClickAction : icon.getRightclickActions()){
                    rightClickAction.execute(player);
                }
            }
            if(event.getClick() == ClickType.SHIFT_LEFT){
                for(ShiftLClickAction action: icon.getShiftLclickActions()){
                    action.execute(player);
                }
            }
            if(event.getClick() == ClickType.SHIFT_RIGHT){
                for(ShiftRClickAction action: icon.getShiftRclickActions()){
                    action.execute(player);
                }
            }
            for(DragItemIntoAction action : icon.getDragItemIntoActions()){
                ItemStack item = event.getCursor();
                //event.getCursor().setAmount(0);
                action.execute(player, item);
            }

            //Execute all the actions
            for (ClickAction clickAction : icon.getClickActions()) {
                clickAction.execute(player);
            }

        }
    }
}
