package global.digital.signage.model.company;

public enum StorageType {

    GB_1("1gb"),
    GB_2("2gb"),
    GB_4("4gb"),
    GB_8("8gb"),
    OTHER("Other");

    private final String title;

    StorageType(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

}
