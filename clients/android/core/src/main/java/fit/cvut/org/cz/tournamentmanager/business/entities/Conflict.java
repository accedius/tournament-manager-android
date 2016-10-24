package fit.cvut.org.cz.tournamentmanager.business.entities;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by kevin on 24.10.2016.
 */
public class Conflict {
    private String title;
    private ArrayList<String> attributes;
    private HashMap<String, String> leftValues;
    private HashMap<String, String> rightValues;

    public Conflict(String title, ArrayList<String> attributes, HashMap<String, String> leftValues, HashMap<String, String> rightValues) {
        this.title = title;
        this.attributes = attributes;
        this.leftValues = leftValues;
        this.rightValues = rightValues;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ArrayList<String> getAttributes() {
        return attributes;
    }

    public void setAttributes(ArrayList<String> attributes) {
        this.attributes = attributes;
    }

    public HashMap<String, String> getLeftValues() {
        return leftValues;
    }

    public void setLeftValues(HashMap<String, String> leftValues) {
        this.leftValues = leftValues;
    }

    public HashMap<String, String> getRightValues() {
        return rightValues;
    }

    public void setRightValues(HashMap<String, String> rightValues) {
        this.rightValues = rightValues;
    }
}
