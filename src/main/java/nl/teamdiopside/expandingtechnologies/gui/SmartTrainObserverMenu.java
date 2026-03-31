package nl.teamdiopside.expandingtechnologies.gui;

import com.mojang.datafixers.util.Pair;
import com.simibubi.create.foundation.gui.menu.GhostItemMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.neoforged.neoforge.items.SlotItemHandler;
import nl.teamdiopside.expandingtechnologies.blocks.observer.ObserverCondition;
import org.jetbrains.annotations.NotNull;

public class SmartTrainObserverMenu extends GhostItemMenu<Pair<BlockPos, ObserverCondition>> {

    public SmartTrainObserverMenu(MenuType<?> type, int id, Inventory inv, RegistryFriendlyByteBuf extraData) {
        super(type, id, inv, extraData);
    }

    public SmartTrainObserverMenu(MenuType<?> type, int id, Inventory inv, Pair<BlockPos, ObserverCondition> contentHolder) {
        super(type, id, inv, contentHolder);
    }

    @Override
    protected ItemStackHandler createGhostInventory() {
        return new ItemStackHandler(1);
    }

    @Override
    protected boolean allowRepeats() {
        return false;
    }

    @Override
    protected Pair<BlockPos, ObserverCondition> createOnClient(RegistryFriendlyByteBuf extraData) {
        BlockPos pos = extraData.readBlockPos();
        ObserverCondition condition = ObserverCondition.fromBuf(extraData);
        return new Pair<>(pos, condition);
    }

    @Override
    protected void addSlots() {
        this.addSlot(new SlotItemHandler(this.ghostInventory, 0, 56, 48) {
            @Override
            public boolean mayPlace(@NotNull ItemStack stack) {
                return false;
            }

            @Override
            public boolean mayPickup(@NotNull Player playerIn) {
                return false;
            }

            @Override
            public boolean isHighlightable() {
                return false;
            }
        });
        this.ghostInventory.setStackInSlot(0, new ItemStack(this.contentHolder.getSecond().entry().getIcon()));
    }

    @Override
    protected void saveData(Pair<BlockPos, ObserverCondition> condition) {}

    @Override
    public @NotNull ItemStack quickMoveStack(@NotNull Player player, int i) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean stillValid(@NotNull Player player) {
        return player.position().distanceTo(this.contentHolder.getFirst().getCenter()) < 5;
    }
}
