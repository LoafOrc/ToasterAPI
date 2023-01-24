package com.loafofbread.toasterapi.armor;

import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import com.loafofbread.toasterapi.item.CustomItem;

public abstract class CustomArmor {
    private final String prefix;
    private final CustomItem helmet, chestplate, leggings, boots;
    
    public String getPrefix() { return prefix; }
    
    public CustomItem getHelmet() { return helmet; }
    public CustomItem getChestplate() { return chestplate; }
    public CustomItem getLeggings() { return leggings; }
    public CustomItem getBoots() { return boots; }

    protected CustomArmor(final String prefix) {
        this.prefix = prefix;
<<<<<<< Updated upstream
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
=======

        this.helmet = createHelmet();
        this.chestplate = createChesplate();
        this.leggings = createLeggings();
        this.boots = createBoots();
>>>>>>> Stashed changes
    }

    protected String createName(final String name) {
        return prefix + " " + name;
    }
    protected abstract CustomItem createHelmet();
    protected abstract CustomItem createChesplate();
    protected abstract CustomItem createLeggings();
    protected abstract CustomItem createBoots();

    // No more setBonus() and resetBonus() methods, in favour of minecrafts in-build attribute system
    // Which is infintely more robust
}