package ch.bailu.foc;

public class FocName extends FocAbstractName {


    private final String name;

    public FocName(String n) {
        name = n;
    }

    @Override
    public Foc child(String child_name) {
        return new FocName(name + "/" + child_name);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getPath() {
        return name;
    }



}
