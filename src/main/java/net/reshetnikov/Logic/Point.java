package net.reshetnikov.Logic;

/**
 * Created by Александр on 27.04.2015.
 */
public class Point {
    public int getXAxis() {
        return xAxis;
    }

    public void setXAxis(int xAxis) {
        this.xAxis = xAxis;
    }

    public int getYAxis() {
        return yAxis;
    }

    public void setYAxis(int yAxis) {
        this.yAxis = yAxis;
    }

    public int getZAxis() {
        return zAxis;
    }

    public void setZAxis(int zAxis) {
        this.zAxis = zAxis;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getRequirementCategory() {
        return requirementCategory;
    }

    public void setRequirementCategory(String requirementCategory) {
        this.requirementCategory = requirementCategory;
    }

    public boolean isUsed() {
        return isUsed;
    }

    public void setUsed(boolean isUsed) {
        this.isUsed = isUsed;
    }

    @Override
    public String toString() {
        return "Точка с " +
                "координатой x = " + xAxis +
                ", координатой y = " + yAxis +
                ", координатой z = " + zAxis +
                ", категория = " + category +
                ", требование к категории = " + requirementCategory;
    }

    private int xAxis;
    private int yAxis;
    private int zAxis;
    private String category;
    private String requirementCategory;
    private boolean isUsed;


}
