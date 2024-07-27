package element;

import java.util.List;

public class FunctionElement extends Element {
    private List<Element> argumentList;

    public FunctionElement() {
        setType(ElementType.FUNCTION);
    }

    public List<Element> getArgumentList() {
        return argumentList;
    }

    public void setArgumentList(List<Element> argumentList) {
        this.argumentList = argumentList;
    }
}
