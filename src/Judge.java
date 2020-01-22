import java.text.DecimalFormat;
import java.util.*;

public class Judge implements Comparable<Judge> {
    private String name;
    private List<Round> rounds;
    private List<Double> points;


    public Judge(String name) {

        //hmmm
        if (name.equals("Chen, Yao")) {
            name = "Chen, Yao Yao";
        }

        this.name = capitalizeName(name);
        rounds = new ArrayList<>();
        points = new ArrayList<>();
    }

    private String capitalizeName(String s) {
        String[] dividedName = s.split(", ");
        StringBuilder output = new StringBuilder();
        for (int i = 0; i < dividedName.length; i++) {
            if (dividedName[i].length() <= 1) {
                dividedName[i] = dividedName[i].toUpperCase();
            } else {
                output.append(dividedName[i].substring(0, 1).toUpperCase() + dividedName[i].substring(1));
            }
            if (i == 0) {
                output.append(", ");
            }
        }
        return output.toString();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Round> getRounds() {
        return rounds;
    }

    public void setRounds(List<Round> rounds) {
        this.rounds = rounds;
    }

    public void addRound(Round r) {
        rounds.add(r);
        if (r.getSpeakerPoints() != null) {
            points.addAll(r.getSpeakerPoints());
        }
    }


    public double getAffPercentage() {
        double output = 0;
        for (Round r : rounds) {
            if (r.getVote().equals("AFF")) {
                output++;
            }
        }
        return output / rounds.size();
    }

    public double getNegPercentage() {
        return 1 - getAffPercentage();
    }

    public int getRoundCount() {
        return rounds.size();
    }

    public double getAverageSpeakerPoints() {
        double output = 0;
        for (Double point : points) {
            System.out.println(point);
            output += point;
        }
        if(output == 0){
            return -1;
        }
        return output / getRoundCount();
    }

    public String toString() {
        StringBuilder output = new StringBuilder();
        DecimalFormat formatter = new DecimalFormat("00.00");
        output.append(name);
        output.append(" | ");
        output.append(rounds.size());
        output.append(" rounds | AFF: ");
        output.append(formatter.format(getAffPercentage() * 100));
        output.append("% NEG: ");
        output.append(formatter.format(getNegPercentage() * 100));
        output.append("%");
        return output.toString();
    }

    public boolean equals(Object o) {
        if (!(o instanceof Judge)) {
            return false;
        }
        return name.equals(((Judge) o).getName());
    }

    @Override
    public int compareTo(Judge o) {
        int countDiff = o.rounds.size() - rounds.size();
        return countDiff != 0 ? countDiff : getName().compareTo(o.getName());
    }

}
