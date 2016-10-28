package fit.cvut.org.cz.tmlibrary.business.entities;

/**
 * Created by kevin on 26.10.2016.
 */
public class ConflictValue {
    private String attribute;
    private String leftValue;
    private String rightValue;

    public ConflictValue(String attribute, String leftValue, String rightValue) {
        this.attribute = attribute;
        this.leftValue = leftValue;
        this.rightValue = rightValue;
    }

    public String getAttribute() {
        return attribute;
    }

    public void setAttribute(String attribute) {
        this.attribute = attribute;
    }

    public String getLeftValue() {
        return leftValue;
    }

    public void setLeftValue(String leftValue) {
        this.leftValue = leftValue;
    }

    public String getRightValue() {
        return rightValue;
    }

    public void setRightValue(String rightValue) {
        this.rightValue = rightValue;
    }
}
