package loggenerator;

import loggenerator.normal.instruments.RandomChooser;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

public class BreakpointChooser {
    private int logCount;
    private int troubleMakerCount;

    public BreakpointChooser(int logCount, int troubleMakerCount) {
        this.logCount = logCount;
        this.troubleMakerCount = troubleMakerCount;
    }

    public List<Integer> generateBreakPoints() {
        List<Unit> units = generateUnits(logCount);
        ListIterator<Unit> unitIterator = units.listIterator();
        List<Integer> breakpoints = new ArrayList<>();
        while (unitIterator.hasNext()) {
            Unit unit = unitIterator.next();
            int breakPoint = unit.chooseRandomPoint();
            breakpoints.add(breakPoint);
            if (unitIterator.hasNext()) {
                Unit nextUnit = unitIterator.next();
                nextUnit.mergeWith(unit);
                unitIterator.previous();
            }
        }
        return breakpoints;
    }

    private List<Unit> generateUnits(int unitsSize) {
        int unitCount = troubleMakerCount;
        int unitSize = unitsSize / unitCount;
        List<Unit> units = new LinkedList<>();
        int start = 0;
        int end = start + (unitSize - 1);
        for (int i = 0; i < unitCount; i++) {
            units.add(new Unit(start, end));
            start = end + 1;
            if (i == unitCount - 1) {
                int lastUnitSize = (unitsSize - 1) - end;
                end = start + (lastUnitSize - 1);
            } else {
                end = start + (unitSize - 1);
            }
        }
        return units;
    }

    private static class Unit {
        private int start;
        private int end;
        private int chosenPoint;

        public Unit(int start, int end) {
            this.start = start;
            this.end = end;
        }

        public int chooseRandomPoint() {
            chosenPoint = RandomChooser.chooseFrom(end - start + 1);
            return chosenPoint;
        }

        public void mergeWith(Unit other) {
            start = other.chosenPoint + 1;
        }
    }
}
