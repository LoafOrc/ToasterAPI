package com.loafofbread.toasterapi.armor;

public abstract class RecipeShapes {
    public static enum Armor {
        HELMET("MMM", "M M"),
        CHESTPLATE("M M", "MMM", "MMM"),
        LEGGINGS("MMM", "M M", "M M"),
        BOOTS("M M", "M M");

        String[] Shape;
        Character Char;
        Armor(String... _Shape) {
            Shape = _Shape;
        }

        public String[] getShape(){
            return Shape;
        }

        public Character getChar(){
            return 'M';
        }
    }

}
