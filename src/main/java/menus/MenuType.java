package menus;

public enum MenuType {
    MAIN(0),
    VIEW_ORDER(1),
    ADD_ORDER(2),
    EDIT_ORDER(3),
    DELETE_ORDER(4),
    SORT(5),
    GENERATE_REPORT(6),
    EXIT(7);

    private final int i;

    MenuType(int i) {
        this.i = i;
    }
}
