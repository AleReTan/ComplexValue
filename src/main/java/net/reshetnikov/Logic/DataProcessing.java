package net.reshetnikov.Logic;

import javafx.scene.control.Alert;
import net.reshetnikov.UI.OverviewController;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.ListIterator;
import java.util.Scanner;


public class DataProcessing {
    private ArrayList<Point> mainPointCollection = new ArrayList<Point>();
    private ArrayList<Zone> ZPlusZoneCollection = new ArrayList<>();
    private ArrayList<Zone> ZMinusZoneCollection = new ArrayList<>();
    private ArrayList<Point> tempPointCollection = new ArrayList<Point>();

    public void loadZone(File file){
        try {
            Scanner sc = new Scanner(file);
            while (sc.hasNextLine()){
                Zone zone = new Zone();
                if (sc.nextLine().equals("Значимая зона")) zone.setSignificant(true);
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
                point.setSignificant(sc.nextBoolean());
                mainPointCollection.add(point);
            }
            sc.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }


    }

    private void calculateTheArea() {
        while (!mainPointCollection.isEmpty()) {
            ArrayList<Point> zone = new ArrayList<Point>();
            tempPointCollection.add(mainPointCollection.get(0));
            mainPointCollection.remove(0);
            System.out.println("Добавлена в темповую коллекцию  " + tempPointCollection.get(0).toString());
            if (tempPointCollection.get(0).isSignificant()) {
                while (!tempPointCollection.isEmpty()) {
                    ListIterator<Point> pointIteratorFromTemp = tempPointCollection.listIterator();
                    while (pointIteratorFromTemp.hasNext()) {
                        Point currentPointFromTemp = pointIteratorFromTemp.next();
                        pointIteratorFromTemp.remove();
//                        System.out.println("Взяли из темповой   " + currentPointFromTemp.toString());
                        ListIterator<Point> pointIteratorFromMain = mainPointCollection.listIterator();
                        while (pointIteratorFromMain.hasNext()) {
                            Point currentPointFromMain = pointIteratorFromMain.next();
                            if ((currentPointFromMain.isSignificant()) && (isNearPoint(currentPointFromTemp, currentPointFromMain))) {
                                pointIteratorFromTemp.add(currentPointFromMain);
//                                System.out.println("Добавлена в темповую коллекцию  " + currentPointFromMain.toString());
                                pointIteratorFromMain.remove();
//                                System.out.println("Удалена из основной коллекции   " + currentPointFromMain.toString());
                            }
                        }
                        zone.add(currentPointFromTemp);
//                        System.out.println("Добавлена в финальную коллекцию " + currentPointFromTemp.toString());
//                        System.out.println("коллекция темп пуста = " + tempPointCollection.isEmpty());
                    }
                    /*for (Point pointFromTemp : tempPointCollection) {
                        System.out.println("Взяли из темповой   " + pointFromTemp.toString());
                        for (Point pointFromMain : mainPointCollection) {
                            if ((pointFromMain.isSignificant()) && (isNearPoint(pointFromTemp, pointFromMain))) {
                                tempPointCollection.add(pointFromMain);
                                System.out.println("Добавлена в темповую коллекцию  " + pointFromMain.toString());
                                mainPointCollection.remove(pointFromMain);
                                System.out.println("Удалена из основной коллекции   " + pointFromMain.toString());
                            }
                        }
                        zone.add(pointFromTemp);
                        System.out.println("Добавлена в финальную коллекцию " + pointFromTemp.toString());
                        tempPointCollection.remove(pointFromTemp);
                    }*/
                }
                ZPlusZoneCollection.add(zone);
//                System.out.println("Добавлена значимая зона " + ZPlusZoneCollection.toString());
            } else {
                while (!tempPointCollection.isEmpty()) {
                    ListIterator<Point> pointIteratorFromTemp = tempPointCollection.listIterator();
                    while (pointIteratorFromTemp.hasNext()) {
                        Point currentPointFromTemp = pointIteratorFromTemp.next();
                        pointIteratorFromTemp.remove();
//                        System.out.println("Взяли из темповой   " + currentPointFromTemp.toString());
                        ListIterator<Point> pointIteratorFromMain = mainPointCollection.listIterator();
                        while (pointIteratorFromMain.hasNext()) {
                            Point currentPointFromMain = pointIteratorFromMain.next();
                            if (!(currentPointFromMain.isSignificant()) && (isNearPoint(currentPointFromTemp, currentPointFromMain))) {
                                pointIteratorFromTemp.add(currentPointFromMain);
//                                System.out.println("Добавлена в темповую коллекцию  " + currentPointFromMain.toString());
                                pointIteratorFromMain.remove();
//                                System.out.println("Удалена из основной коллекции   " + currentPointFromMain.toString());
                            }
                        }
                        zone.add(currentPointFromTemp);
//                        System.out.println("Добавлена в финальную коллекцию " + currentPointFromTemp.toString());
//                        System.out.println("коллекция темп пуста = " + tempPointCollection.isEmpty());
                    }
                /*while (!tempPointCollection.isEmpty()) {
                    for (Point pointFromTemp : tempPointCollection) {
                        System.out.println("Взяли из темповой   " + pointFromTemp.toString());
                        for (Point pointFromMain : mainPointCollection) {
                            if (!(pointFromMain.isSignificant()) && (isNearPoint(pointFromTemp, pointFromMain))) {
                                tempPointCollection.add(pointFromMain);
                                System.out.println("Добавлена в темповую коллекцию  " + pointFromMain.toString());
                                mainPointCollection.remove(pointFromMain);
                                System.out.println("Удалена из основной коллекции   " + pointFromMain.toString());
                            }
                        }
                        zone.add(pointFromTemp);
                        System.out.println("Добавлена в финальную коллекцию " + pointFromTemp.toString());
                        tempPointCollection.remove(pointFromTemp);
                    }
                }*/
                }
                ZMinusZoneCollection.add(zone);
//                System.out.println("Добавлена незначимая зона " + ZMinusZoneCollection.toString());
            }
        }
    }

    private boolean isNearPoint(Point first, Point second) {
        return (Math.abs(first.getXAxis() - second.getXAxis()) <= 1 && Math.abs(first.getYAxis() - second.getYAxis()) <= 1 && Math.abs(first.getZAxis() - second.getZAxis()) <= 1);
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

    private double approximateMinusArea(ArrayList<Point> zone) {
        int numberOfPointWithSelectedCategory = 0;

        for (Point point : zone ){
            if (point.getCategory().equals("A") || point.getCategory().equals("B") ) numberOfPointWithSelectedCategory++;
        }
        return (double)numberOfPointWithSelectedCategory/zone.size();
    }

    public void testMethod() {
        for (Zone zone:ZPlusZoneCollection){
            System.out.println(zone.toString());
        }
        for (Zone zone:ZMinusZoneCollection){
            System.out.println(zone.toString());
        }
        //calculateTheArea();
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
