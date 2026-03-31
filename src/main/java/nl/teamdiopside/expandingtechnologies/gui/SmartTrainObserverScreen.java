package nl.teamdiopside.expandingtechnologies.gui;

import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.content.trains.schedule.DestinationSuggestions;
import com.simibubi.create.foundation.gui.AllGuiTextures;
import com.simibubi.create.foundation.gui.AllIcons;
import com.simibubi.create.foundation.gui.ModularGuiLine;
import com.simibubi.create.foundation.gui.ModularGuiLineBuilder;
import com.simibubi.create.foundation.gui.menu.AbstractSimiContainerScreen;
import com.simibubi.create.foundation.gui.widget.*;
import net.createmod.catnip.data.IntAttached;
import net.createmod.catnip.gui.UIRenderHelper;
import net.createmod.catnip.gui.element.GuiGameElement;
import net.createmod.catnip.platform.CatnipServices;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import nl.teamdiopside.expandingtechnologies.mixin.DestinationSuggestionsAccessor;
import nl.teamdiopside.expandingtechnologies.net.packet.SmartTrainObserverConfigurePacket;
import nl.teamdiopside.expandingtechnologies.registry.ETBlocks;
import nl.teamdiopside.expandingtechnologies.registry.ETObserverConditions;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class SmartTrainObserverScreen extends AbstractSimiContainerScreen<SmartTrainObserverMenu> {

    private ETObserverConditions.ObserverConditionRegistryEntry currentEntry;
    private final List<IntAttached<String>> suggestions = new ArrayList<>();
    private ModularGuiLine regexBox;
    private IconButton invertButton;
    private DestinationSuggestions regexSuggestions;
    private String filter;

    public SmartTrainObserverScreen(SmartTrainObserverMenu container, Inventory inv, Component title) {
        super(container, inv, title);
    }

    @Override
    protected void init() {
        this.imageWidth = 256;
        this.imageHeight = 104;
        super.init();
        ArrayList<ETObserverConditions.ObserverConditionRegistryEntry> entries = ETObserverConditions.REGISTRY.getEntriesArray();
        this.currentEntry = this.menu.contentHolder.getSecond().entry();
        this.filter = this.menu.contentHolder.getSecond().filter();
        this.suggestions.clear();
        this.suggestions.addAll(this.currentEntry.getSuggestions(this.menu.contentHolder.getFirst(), this.menu.player));

        // Add the label.
        Label typeLabel = (new Label(this.leftPos + 59, this.topPos + 29, CommonComponents.EMPTY)).withShadow();

        // Add a type selector, based on the registry.
        ScrollInput typeSelector = new SelectionScrollInput(this.leftPos + 56, this.topPos + 25, 143, 16)
                .forOptions(entries.stream().map(ETObserverConditions.ObserverConditionRegistryEntry::getTitle).toList())
                .withRange(0, entries.size())
                .calling(i -> {
                    this.currentEntry = entries.get(i);
                    this.menu.ghostInventory.setStackInSlot(0, new ItemStack(this.currentEntry.getIcon()));
                    this.suggestions.clear();
                    if (this.regexBox != null && this.regexSuggestions != null) {
                        ((DestinationSuggestionsAccessor)this.regexSuggestions).setCurrentSuggestions(new ArrayList<>());
                        this.regexBox.forEach(widget -> {
                            if (widget instanceof EditBox editBox) {
                                editBox.setCursorPosition(0);
                                editBox.setHighlightPos(0);
                                ((DestinationSuggestionsAccessor)this.regexSuggestions).setPrevious("§§");
                                editBox.setFocused(false);
                                editBox.setValue("");
                                editBox.setSuggestion("");
                                ((DestinationSuggestionsAccessor)this.regexSuggestions).setPrevious("§§");
                            }
                        });
                    }
                    this.suggestions.addAll(this.currentEntry.getSuggestions(this.menu.contentHolder.getFirst(), this.menu.player));
                })
                .writingTo(typeLabel)
                .setState(entries.indexOf(this.menu.contentHolder.getSecond().entry()));

        // Add the confirm button for people that can't find the escape key.
        IconButton confirmButton = new IconButton(this.leftPos + 228, this.topPos + 80, AllIcons.I_CONFIRM);
        confirmButton.withCallback(() -> {
            if (this.minecraft == null || this.minecraft.player == null) return;
            this.minecraft.player.closeContainer();
        });

        // Add filter input box, Create has cool rendering within ModularGuiLine, but I don't recommend using it if you just want the EditBox.
        this.regexBox = new ModularGuiLine();
        ModularGuiLineBuilder regexBuilder = new ModularGuiLineBuilder(this.font, this.regexBox, this.leftPos + 79, this.topPos + 52);
        regexBuilder.addTextInput(0, 121, (e, t) -> e.setFilter(s -> StringUtils.countMatches(s, "*") <= 3), "Unused").speechBubble();

        this.regexBox.forEach(widget -> {
            if (widget instanceof TooltipArea tooltipArea) {
                addRenderableOnly(tooltipArea);
                return;
            }
            if (!(widget instanceof EditBox editBox)) return;
            editBox.setValue(this.filter);
            addRenderableWidget(editBox);

            // We'll also misuse the DestinationSuggestions to just be any suggestion :)
            this.regexSuggestions = new DestinationSuggestions(this.minecraft, this, editBox, this.font, this.suggestions, false, this.topPos - 7);
            this.regexSuggestions.setAllowSuggestions(true);
            this.regexSuggestions.updateCommandInfo();

            editBox.setResponder(filter -> {
                this.regexSuggestions.updateCommandInfo();
                this.filter = filter;
            });
        });

        // Button to invert the condition
        invertButton = new IconButton(this.leftPos + 19, this.topPos + 80, AllIcons.I_ROTATE_CCW);
        invertButton.withCallback(() -> invertButton.green = !invertButton.green);
        invertButton.setToolTip(Component.translatable("expandingtechnologies.gui.invert_condition.title"));
        invertButton.getToolTip().add(Component.translatable("expandingtechnologies.gui.invert_condition.description").withStyle(ChatFormatting.DARK_GRAY));
        invertButton.green = this.menu.contentHolder.getSecond().inverted();

        // Actually add everything
        addRenderableWidgets(typeLabel, typeSelector, confirmButton, invertButton);
    }

    @Override
    protected void renderBg(@NotNull GuiGraphics guiGraphics, float a, int b, int c) {
        // Render background
        AllGuiTextures.SCHEDULE_EDITOR.render(guiGraphics, this.leftPos, this.topPos);
        // Get Title
        FormattedCharSequence formattedCharSequence = this.title.getVisualOrderText();
        // Get Center
        int center = this.leftPos + AllGuiTextures.SCHEDULE_EDITOR.getWidth() / 2;
        // Render Title
        guiGraphics.drawString(this.font, formattedCharSequence, (float)(center - this.font.width(formattedCharSequence) / 2), this.topPos + 4, 0x505050, false);

        // Render EditBox Background
        PoseStack pPoseStack = guiGraphics.pose();
        pPoseStack.pushPose();
        pPoseStack.translate(0, this.topPos + 47, 0);
        this.regexBox.renderWidgetBG(this.leftPos + 81, guiGraphics);
        pPoseStack.popPose();

        // Render Bottom
        CreateUIParts.GUI_BOTTOM_START.render(guiGraphics, this.leftPos + 3, this.topPos + 74);
        UIRenderHelper.drawStretched(guiGraphics, this.leftPos + 6, this.topPos + 74, 216, 30, 0, CreateUIParts.GUI_BOTTOM_STRETCH);
        CreateUIParts.GUI_BOTTOM_END.render(guiGraphics, this.leftPos + 222, this.topPos + 74);

        // Render Block
        ((GuiGameElement.GuiRenderBuilder)GuiGameElement.of(ETBlocks.SMART_TRAIN_OBSERVER).at(this.leftPos + 268, this.topPos + 48, 100)).scale(5).render(guiGraphics);
    }

    @Override
    protected void containerTick() {
        // Actually tick the suggestions
        if (this.regexSuggestions != null) this.regexSuggestions.tick();
        super.containerTick();
    }

    @Override
    protected void renderForeground(@NotNull GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        // Actually render the suggestions, but only if it's active ofc
        if (this.regexSuggestions != null && ((DestinationSuggestionsAccessor)this.regexSuggestions).isActive()) {
            PoseStack matrixStack = graphics.pose();
            matrixStack.pushPose();
            matrixStack.translate(0, 0, 500);
            this.regexSuggestions.render(graphics, mouseX, mouseY);
            matrixStack.popPose();
        }
        super.renderForeground(graphics, mouseX, mouseY, partialTicks);
    }

    @Override
    protected void renderTooltip(@NotNull GuiGraphics graphics, int mouseX, int mouseY) {}

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        // We would like to use TAB on our suggestions.
        if (this.regexSuggestions != null && this.regexSuggestions.keyPressed(keyCode, scanCode, modifiers)) {
            return true;
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        // We would also like to click the suggestion.
        if (this.regexSuggestions != null && this.regexSuggestions.mouseClicked(mouseX, mouseY, button)) {
            return true;
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public void removed() {
        // Remove GUI and update condition
        super.removed();
        CatnipServices.NETWORK.sendToServer(new SmartTrainObserverConfigurePacket(this.menu.contentHolder.getFirst(), this.currentEntry.buildCondition(this.filter, this.invertButton.green)));
    }
}
