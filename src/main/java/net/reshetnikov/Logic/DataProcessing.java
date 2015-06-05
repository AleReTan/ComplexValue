package net.reshetnikov.Logic;

import javafx.scene.control.Alert;


import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;


public class DataProcessing {
    private ArrayList<Point> mainPointCollection = new ArrayList<>();
    private ArrayList<Zone> ZPlusZoneCollection = new ArrayList<>();
    private ArrayList<Zone> ZMinusZoneCollection = new ArrayList<>();

    public void loadZone(File file) {
        ZMinusZoneCollection.clear();
        ZMinusZoneCollection.trimToSize();
        ZPlusZoneCollection.clear();
        ZPlusZoneCollection.trimToSize();
        try {
            Scanner sc = new Scanner(file);
            while (sc.hasNextLine()) {
                Zone zone = new Zone();
                if (sc.next().equals("Значимая")) zone.setSignificant(true);
                else zone.setSignificant(false);
                zone.setxMin(sc.nextInt());
                zone.setxMax(sc.nextInt());
                zone.setyMin(sc.nextInt());
                zone.setyMax(sc.nextInt());
                zone.setzMin(sc.nextInt());
                zone.setzMax(sc.nextInt());
                if (zone.isSignificant()) ZPlusZoneCollection.add(zone);
                else ZMinusZoneCollection.add(zone);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void loadPoints(File file) {
        mainPointCollection.clear();
        mainPointCollection.trimToSize();
        try {
            Scanner sc = new Scanner(file);
            while (sc.hasNextLine()) {
                Point point = new Point();
                point.setXAxis(sc.nextInt());
                point.setYAxis(sc.nextInt());
                point.setZAxis(sc.nextInt());
                point.setCategory(sc.next().toUpperCase());
                point.setRequirementCategory(sc.next().toUpperCase());
                mainPointCollection.add(point);
            }
            sc.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }


    }

    private void calculateTheArea() {
        for (Zone zone : ZPlusZoneCollection) {
            for (Point point : mainPointCollection) {
                if (inRange(zone, point) && !point.getRequirementCategory().equals("NULL") && !point.isUsed()) {
                    zone.getPoints().add(point);
                    point.setUsed(true);
                }
            }
        }
        for (Zone zone : ZMinusZoneCollection) {
            for (Point point : mainPointCollection) {
                if (inRange(zone, point) && point.getRequirementCategory().equals("NULL") && !point.isUsed()) {
                    zone.getPoints().add(point);
                    point.setUsed(true);
                }
            }
        }
    }

    private boolean inRange(Zone zone, Point point) {
        return (point.getXAxis() <= zone.getxMax() && point.getXAxis() >= zone.getxMin()) &&
                (point.getYAxis() <= zone.getyMax() && point.getYAxis() >= zone.getyMin()) &&
                (point.getZAxis() <= zone.getzMax() && point.getZAxis() >= zone.getzMin());
    }

    private double evaluateMismatch(Point point) {
        switch (point.getCategory()) {
            case "A":
                return 0;

            case "B":
                switch (point.getRequirementCategory()) {
                    case "A":
                        return Math.abs((2 - 1) / 3.0);
                    case "B":
                        return 0;
                    case "C":
                        return 0;
                    case "D":
                        return 0;
                    default:
                        break;
                }
                break;
            case "C":
                switch (point.getRequirementCategory()) {
                    case "A":
                        return Math.abs((3 - 1) / 3.0);
                    case "B":
                        return Math.abs((3 - 2) / 3.0);
                    case "C":
                        return 0;
                    case "D":
                        return 0;
                    default:
                        break;
                }
                break;
            case "D":
                switch (point.getRequirementCategory()) {
                    case "A":
                        return Math.abs((4 - 1) / 3.0);
                    case "B":
                        return Math.abs((4 - 2) / 3.0);
                    case "C":
                        return Math.abs((4 - 3) / 3.0);
                    case "D":
                        return 0;
                    default:
                        break;
                }
                break;

            default:
                break;
        }
        return 13.37;
    }

    private double approximatePlusArea(Zone zone, String method, String quantifier) {
        if (method.equals("OWA")) {
            ArrayList<Double> weight = new ArrayList<>();
            double value = 0;
            if (quantifier.equals("x" + (char) 178)) {
                for (int i = 1; i <= zone.getPoints().size(); i++) {
                    weight.add(Math.pow((double) i / zone.getPoints().size(), 2) - Math.pow(((double) i - 1) / zone.getPoints().size(), 2));
                }
            }
            if (quantifier.equals("x" + (char) 179)) {
                for (int i = 1; i <= zone.getPoints().size(); i++) {
                    weight.add(Math.pow((double) i / zone.getPoints().size(), 3) - Math.pow(((double) i - 1) / zone.getPoints().size(), 3));
                }
            }
            for (int i = 0; i < zone.getPoints().size(); i++) {
                value += weight.get(i) * evaluateMismatch(zone.getPoints().get(i));
            }
            return value;
        }
        if (method.equals("middle")) {
            ArrayList<Double> mismatch = new ArrayList<>();
            double sum = 0;
            for (Point point : zone.getPoints()) {
                mismatch.add(evaluateMismatch(point));
            }
            for (double value : mismatch) {
                sum += value;
            }
            return sum / mismatch.size();
        }
        return 13.37;
    }

    private double approximateMinusArea(Zone zone, String category) {
        int numberOfPointWithSelectedCategory = 0;

        for (Point point : zone.getPoints()) {
            switch (category) {
                case "A":
                    if (point.getCategory().equals("A")) numberOfPointWithSelectedCategory++;
                    break;
                case "A,B":
                    if (point.getCategory().equals("A") ||
                            point.getCategory().equals("B")) numberOfPointWithSelectedCategory++;
                    break;
                case "A,B,C":
                    if (point.getCategory().equals("A") ||
                            point.getCategory().equals("B") ||
                            point.getCategory().equals("C")) numberOfPointWithSelectedCategory++;
                    break;
                default:
                    break;
            }
        }
        return (double) numberOfPointWithSelectedCategory / zone.getPoints().size();
    }

    public void calculateMethod(String forMinus, String forPlus, String forPlusQuantifier, String alpha) {
        ArrayList<Double> valueMinusArea = new ArrayList();
        ArrayList<Double> valuePlusArea = new ArrayList();
        calculateTheArea();
        for (Zone zone : ZMinusZoneCollection) {
            valueMinusArea.add(approximateMinusArea(zone, forMinus));
        }
        for (Zone zone : ZPlusZoneCollection) {
            valuePlusArea.add(approximatePlusArea(zone, forPlus, forPlusQuantifier));
        }
        double weightPlus = 1.0 / ZPlusZoneCollection.size();
        double weightMinus = 1.0 / ZMinusZoneCollection.size();
        double zPlusValue = 0;
        double zMinusValue = 0;

        for (double value : valueMinusArea) {
            zMinusValue += weightMinus * value;
        }
        for (double value : valuePlusArea) {
            zPlusValue += weightPlus * value;
        }
        double complexValue = Double.parseDouble(alpha) * zPlusValue + (1 - Double.parseDouble(alpha)) * zMinusValue;

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Итоговая оценка");
        alert.setHeaderText(null);
        alert.setContentText("Комплексная оценка помещения равна " + complexValue);
        alert.showAndWait();

        System.out.println(valueMinusArea.toString());
        System.out.println(valuePlusArea.toString());
        for (Point point : mainPointCollection) {
            point.setUsed(false);
        }
        for (Zone zone : ZPlusZoneCollection) {
            zone.getPoints().clear();
            zone.getPoints().trimToSize();
        }
        for (Zone zone : ZMinusZoneCollection) {
            zone.getPoints().clear();
            zone.getPoints().trimToSize();
        }
    }
}
