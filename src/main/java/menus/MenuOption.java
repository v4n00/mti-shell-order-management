package menus;

public enum MenuOption {
    MAIN("\uF015 Main Menu", MenuType.MAIN),
    VIEW_ORDER("\uF002 View Order", MenuType.VIEW_ORDER),
    ADD_ORDER("\uF457 Add Order", MenuType.ADD_ORDER),
    EDIT_ORDER("\uF044 Edit Order", MenuType.EDIT_ORDER),
    DELETE_ORDER("\uDB80\uDDB4 Delete Order", MenuType.DELETE_ORDER),
    SORT("\uF0DC Sort Orders", MenuType.SORT),
    GENERATE_REPORT("\uDB85\uDD17 Generate Report", MenuType.GENERATE_REPORT),
    EXIT("\uDB80\uDE06 Exit", MenuType.EXIT);

    private final String label;
    private final MenuType menuType;

    MenuOption(String label, MenuType menuType) {
        this.label = label;
        this.menuType = menuType;
    }

    public String getLabel() {
        return label;
    }

    public MenuType toMenuType() {
        return menuType;
    }

    public static MenuOption fromIndex(int index) {
        return values()[index];
    }

    public static int count() {
        return values().length;
    }
}
