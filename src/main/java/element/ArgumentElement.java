package element;

public class ArgumentElement extends Element {
    private Element childElement;
    public ArgumentElement() {
        setType(ElementType.ARGUMENT);
    }

    public Element getChildElement() {
        return childElement;
    }

    public void setChildElement(Element childElement) {
        this.childElement = childElement;
    }
}
