package eu.virtusdevelops.simplebeacons.gui;

import eu.virtusdevelops.simplebeacons.gui.actions.InventoryCloseAction;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InventoryCreator implements InventoryHolder {


    private final Map<Integer, Icon> icons = new  HashMap<>();
    public final List<InventoryCloseAction> closeActionList = new ArrayList<>();

    private final int size;
    private String title;

    public InventoryCreator(int size, String title) {
        this.size = size;
        this.title = title;
    }

    public void setIcon(int position, Icon icon) {
        this.icons.put(position, icon);
    }

    public Icon getIcon(int position) {
        return this.icons.get(position);
    }

    @Override
    public Inventory getInventory() {
        Inventory inventory = Bukkit.createInventory(this, this.size, this.title);

        //You should check for inventory size so you don't get errors
        for (Map.Entry<Integer, Icon> entry : this.icons.entrySet()) {
            inventory.setItem(entry.getKey(), entry.getValue().itemStack);
        }

        return inventory;
    }

    public void clean(){
        this.icons.clear();
        this.getInventory().clear();
    }

    public String getTitle() {
        return title;
    }
    public void setTitle(String title){
        this.title = title;
    }

    public InventoryCreator addCloseActions(InventoryCloseAction action){
        this.closeActionList.add(action);
        return this;
    }

    public List<InventoryCloseAction> getCloseActions() {
        return this.closeActionList;
    }

    
}
