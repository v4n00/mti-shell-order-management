package menus;

public enum MenuType {
    MAIN(0),
    VIEW_ORDER(1),
    ADD_ORDER(2),
    GENERATE_REPORT(3),
    EXIT(4);

    private final int i;

    MenuType(int i) {
        this.i = i;
    }
}
