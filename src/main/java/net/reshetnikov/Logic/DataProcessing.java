package net.reshetnikov.Logic;

import net.reshetnikov.UI.OverviewController;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;


public class DataProcessing {
    private ArrayList<Point> mainPointCollection = new ArrayList<Point>();
    private ArrayList<Zone> ZPlusZoneCollection = new ArrayList<>();
    private ArrayList<Zone> ZMinusZoneCollection = new ArrayList<>();

    public void loadZone(File file){
        try {
            Scanner sc = new Scanner(file);
            while (sc.hasNextLine()){
                Zone zone = new Zone();
                if (sc.next().equals("Значимая")) zone.setSignificant(true);
                else zone.setSignificant(false);
                zone.setxMin(sc.nextInt());
                zone.setxMax(sc.nextInt());
                zone.setyMin(sc.nextInt());
                zone.setyMax(sc.nextInt());
                zone.setzMin(sc.nextInt());
                zone.setzMax(sc.nextInt());
                if(zone.isSignificant()) ZPlusZoneCollection.add(zone);
                else ZMinusZoneCollection.add(zone);
            }
        }
        catch (FileNotFoundException e){
            e.printStackTrace();
        }
    }

    public void loadPoints(File file) {
        try {
            Scanner sc = new Scanner(file);
            while (sc.hasNextLine()) {
                Point point = new Point();
                point.setXAxis(sc.nextInt());
                point.setYAxis(sc.nextInt());
                point.setZAxis(sc.nextInt());
                point.setCategory(sc.next());
                point.setRequirementCategory(sc.next());
                mainPointCollection.add(point);
            }
            sc.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }


    }

    private void calculateTheArea() {
        for (Zone zone:ZPlusZoneCollection){
            // форич по коллекции мейнпоинт дальше иф инрендж то добавить в зон
            mainPointCollection.stream().filter(point -> inRange(zone, point)).forEach(point -> zone.getPoints().add(point));        }
        for (Zone zone:ZMinusZoneCollection){
            mainPointCollection.stream().filter(point -> inRange(zone,point)).forEach(point -> zone.getPoints().add(point));
        }
    }
    private boolean inRange(Zone zone,Point point){
        return (point.getXAxis() <= zone.getxMax() && point.getXAxis() >= zone.getxMin()) &&
                (point.getYAxis() <= zone.getyMax() && point.getYAxis() >= zone.getyMin()) &&
                (point.getZAxis() <= zone.getzMax() && point.getYAxis() >= zone.getyMin());
    }

    private double evaluateMismatch(Point point) {
        switch (point.getCategory()) {
            case "A": return 0;

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

    private void approximatePlusArea(ArrayList<Point> zone,String string) {
        if (string.equals("OWA")){

        }
        if (string.equals("middle")){

        }

    }

    private double approximateMinusArea(Zone zone) {
        int numberOfPointWithSelectedCategory = 0;

        for (Point point : zone.getPoints() ){
            if (point.getCategory().equals("A") || point.getCategory().equals("B") ) numberOfPointWithSelectedCategory++;
        }
        return (double)numberOfPointWithSelectedCategory/zone.getPoints().size();
    }

    public void testMethod() {
        calculateTheArea();
        for (Zone zone:ZPlusZoneCollection){
            System.out.println(zone.toString());
        }
        for (Zone zone:ZMinusZoneCollection){
            System.out.println(zone.toString());
        }


        /*int i = 0;
        System.out.println("Значимые зоны " + "Количество зон со значимыми точками " + ZPlusZoneCollection.size());
        for (ArrayList<Point> zone : ZPlusZoneCollection) {
            System.out.println("Зона номер " + (++i));
            for (Point point : zone) {
                System.out.println(point.toString());
            }

        }
        System.out.println("Не значимые зоны " + "Количество зон с незначимыми точками " + ZMinusZoneCollection.size());
        for (ArrayList<Point> zone : ZMinusZoneCollection) {
            System.out.println("Зона номер " + (++i));
            for (Point point : zone) {
                System.out.println(point.toString());
            }
        }


        System.out.println("Воn такое несоответствие "+evaluateMismatch(ZPlusZoneCollection.get(0).get(0)));
        System.out.println("Воn такое несоответствие "+evaluateMismatch(ZPlusZoneCollection.get(0).get(1)));
        System.out.println("Воn такое несоответствие "+evaluateMismatch(ZPlusZoneCollection.get(0).get(2)));

        for (ArrayList<Point> zone: ZMinusZoneCollection){
            System.out.println(approximateMinusArea(zone));
        }*/
    }
}
