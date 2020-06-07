package eu.virtusdevelops.simplebeacons.gui;

import eu.virtusdevelops.simplebeacons.gui.actions.*;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class Icon {

    public final ItemStack itemStack;

    public final List<ClickAction> clickActions = new ArrayList<>();
    public final List<LeftClickAction> leftclickActions = new ArrayList<>();
    public final List<RightClickAction> rightclickActions = new ArrayList<>();
    public final List<ShiftRClickAction> shiftRClickActions = new ArrayList<>();
    public final List<ShiftLClickAction> shiftLClickActions = new ArrayList<>();
    public final List<DragItemIntoAction> dragItemIntoActions = new ArrayList<>();

    public Icon(ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    public Icon addClickAction(ClickAction clickAction) {
        this.clickActions.add(clickAction);
        return this;
    }
    public Icon addLeftClickAction(LeftClickAction leftClickAction){
        this.leftclickActions.add(leftClickAction);
        return this;
    }
    public Icon addRightClickAction(RightClickAction rightClickAction){
        this.rightclickActions.add(rightClickAction);
        return this;
    }
    public Icon addShiftRClickAction(ShiftRClickAction shiftRClickAction){
        this.shiftRClickActions.add(shiftRClickAction);
        return this;
    }
    public Icon addShiftLClickAction(ShiftLClickAction shiftLClickAction){
        this.shiftLClickActions.add(shiftLClickAction);
        return this;
    }
    public Icon addDragItemIntoAction(DragItemIntoAction dragItemIntoAction){
        this.dragItemIntoActions.add(dragItemIntoAction);
        return this;
    }

    public List<ClickAction> getClickActions() {
        return this.clickActions;
    }
    public List<LeftClickAction> getLeftClickActions(){
        return this.leftclickActions;
    }
    public List<RightClickAction> getRightclickActions(){
        return this.rightclickActions;
    }
    public List<ShiftRClickAction> getShiftRclickActions(){
        return this.shiftRClickActions;
    }
    public List<ShiftLClickAction> getShiftLclickActions(){
        return this.shiftLClickActions;
    }
    public List<DragItemIntoAction> getDragItemIntoActions() {
        return this.dragItemIntoActions;
    }
}
