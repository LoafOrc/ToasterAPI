package com.loafofbread.toasterapi.armor;

import com.loafofbread.toasterapi.ToasterAPI;
import com.loafofbread.toasterapi.item.CustomItem;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.Player;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Locale;
import java.util.UUID;

public abstract class CustomArmor {
    public final CustomItem helmet;
    public final CustomItem chestplate;
    public final CustomItem leggings;
    public final CustomItem boots;

    @Deprecated
    protected final JavaPlugin plugin;

    public final String prefix;
    private final ItemStack ingredient;

    private final CustomItem.Rarity rarity;

    protected final String id;

    protected CustomArmor(JavaPlugin plugin, CustomItem.Rarity rarity, String type, String prefix, ItemStack ingredient) {
        this.plugin = plugin;
        this.prefix = prefix;
        this.rarity = rarity;
        this.ingredient = ingredient;
        this.id = prefix.toLowerCase(Locale.ROOT).replace(' ', '_') + "_armor";
        helmet = generatePiece(type, "Helmet", "Head");
        chestplate = generatePiece(type, "Chestplate", "Chest");
        leggings = generatePiece(type, "Leggings", "Legs");
        boots = generatePiece(type, "Boots", "Feet");

        HashMap<String, CustomArmor> pluginArmor = ToasterAPI.pluginArmor.get(plugin);
        if(pluginArmor == null) {
            pluginArmor = new HashMap<>();
        }
        pluginArmor.put(id, this);
        ToasterAPI.allArmor.put(id, this);
    }
    public Color getLeatherColor() { return null; }

    protected ItemMeta applyMeta(ItemMeta before, String piece) { return before; }

    private Recipe generateRecipe(String Piece, ItemStack Result) {
        if(ingredient == null) return null;
        ShapedRecipe _result = new ShapedRecipe(new NamespacedKey(plugin, prefix + "_" + Piece.toLowerCase()), Result);
        _result.shape(RecipeShapes.Armor.valueOf(Piece.toUpperCase()).getShape());
        _result.setIngredient(RecipeShapes.Armor.valueOf(Piece.toUpperCase()).getChar(), new RecipeChoice.ExactChoice(ingredient));
        return _result;
    }
    protected CustomItem generatePiece(String type, String _Piece, String _Short) {
        Material _mat = Material.valueOf(type.toUpperCase() + "_" + _Piece.toUpperCase());
        CustomItem _result = new CustomItem(plugin, prefix.toLowerCase() + "_" + _Piece.toLowerCase(), _mat, prefix + " " + _Piece) {
            @Override
            protected Recipe getRecipe(NamespacedKey recipeKey) {
                return generateRecipe(_Piece.toLowerCase(), getItem());
            }

            @Override
            public Rarity getRarity() {
                return rarity;
            }
        };

        ItemMeta _meta = _result.getItem().getItemMeta();


        //AttributeModifier armor = new AttributeModifier(UUID.randomUUID(), "generic.armor", Armor, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.valueOf(_Short.toUpperCase()));
        //_meta.addAttributeModifier(Attribute.GENERIC_ARMOR, armor);


        if(type.equalsIgnoreCase("LEATHER")) {  //If the item is Leather
            if(getLeatherColor()  == null) { //No Color
                plugin.getLogger().warning("Leather Armor Piece being created with out color, are you sure you meant to do this?");
            } else { //There is a color
                LeatherArmorMeta _leatherMeta = (LeatherArmorMeta) _meta;

                _leatherMeta.setColor(getLeatherColor());

                _meta = _leatherMeta;
            }
        } else { // Isn't leather
            if(getLeatherColor() != null) { // There is a color
                plugin.getLogger().warning("LeatherColor has been assigned but the armor piece isn't leather, are you sure you meant to do this?");
            }
        }

        _meta.getPersistentDataContainer().set(ToasterAPI.armor, PersistentDataType.STRING, id);
        _result.setItemMeta(applyMeta(_meta, _Piece));
        _result.createRecipe();
        return _result;
    }

    public boolean onEquip(Player _Player) {
        final ItemStack[] _ArmorContents = _Player.getInventory().getArmorContents();
        int _SetPieces = 0;
        for(int i = 0; i < _ArmorContents.length; i++){
            if(_ArmorContents[i] == null) {
                continue;
            }

            ItemMeta _armorMeta = _ArmorContents[i].getItemMeta();
            if(!_armorMeta.getPersistentDataContainer().has(ToasterAPI.armor, PersistentDataType.STRING)) continue;
            if(_armorMeta.getPersistentDataContainer().get(ToasterAPI.armor, PersistentDataType.STRING).equals(id)) {
                _SetPieces++;
            }
        }


        if(_SetPieces > 0){
            setBonus(_Player, _SetPieces);
        }
        if(_SetPieces == 0){
            resetBonus(_Player);
        }
        if(_SetPieces == 4) return true;
        return false;
    }

    protected abstract void setBonus(Player player, int Pieces);
    protected abstract void resetBonus(Player player);
}
