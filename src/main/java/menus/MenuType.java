package menus;

public enum MenuType {
    MAIN(0),
    VIEW_ORDER(1),
    ADD_ORDER(2),
    SORT(3),
    GENERATE_REPORT(4),
    EXIT(5);

    private final int i;

    MenuType(int i) {
        this.i = i;
    }
}
