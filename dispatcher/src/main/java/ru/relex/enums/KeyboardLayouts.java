package ru.relex.enums;

import java.util.HashMap;

public enum KeyboardLayouts {
    START_LAYOUT(
            new HashMap[]{
                    new HashMap<String, String>() {{
                        put("Random Trap", "get_random_trap");
                        put("Random Character", "get_random_character");
                    }}
            }
    ),
    TRAPS_LAYOUT(
            new HashMap[]{
                    new HashMap<String, String>() {{
                        put("Save", "save_trap");
                        put("Next Trap", "get_next_trap");
                    }},
                    new HashMap<String, String>() {{
                        put("Back to menu", "get_to_menu");
                    }}
            }
    );
final HashMap[] KeyboardLayout;
    KeyboardLayouts(HashMap[] keyboardLayout) {
        this.KeyboardLayout = keyboardLayout;
    }

    public HashMap[] getKeyboardLayout() {
        return KeyboardLayout;
    }
}


