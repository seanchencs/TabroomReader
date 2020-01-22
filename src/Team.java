import java.text.DecimalFormat;
import java.util.*;

public class Team implements Comparable<Team> {
    private School school;
    private String name;
    private List<Round> rounds;
    //private List<Double> speaker1;
    //private List<Double> speaker2;

    public Team(String name) {
        school = new School(name.substring(0, name.length() - 3));
        this.name = name.substring(name.length() - 2);
        rounds = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public String getFullName() {
        StringBuilder output = new StringBuilder();
        output.append(school.getName());
        output.append(" ");
        output.append(name);
        return output.toString();
    }

    public School getSchool() {
        return school;
    }

    public void setSchool(School s) {
        school = s;
        s.addTeam(this);
    }

    public List<Round> getRounds() {
        return rounds;
    }

    public void addRound(Round r) {
        rounds.add(r);
    }

    public void combine(Team t) {

        for (int i = 0; i < t.rounds.size(); i++) {
            Round r = t.rounds.get(i);
            this.rounds.add(r);
        }
    }

    public int getWinCount() {
        int output = 0;
        for (Round r : rounds) {
            if (r.getVote().equals("AFF") && r.getAff().equals(this)) {
                output++;
            }
            if (r.getVote().equals("NEG") && r.getNeg().equals(this)) {
                output++;
            }
        }
        return output;
    }

    public int getLossCount() {
        return rounds.size() - getWinCount();
    }

    public double getWinPercentage() {
        return getWinCount() / (double) rounds.size();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Team)) {
            return false;
        }
        Team other = (Team) obj;
        return other.name.equals(name) && other.school.equals(this.school);
    }

    public String toString() {
        StringBuilder output = new StringBuilder();
        DecimalFormat formatter = new DecimalFormat("00.00");
        output.append(school.getName());
        output.append(" ");
        output.append(name);
        output.append(" | ");
        output.append(getWinCount());
        output.append("W ");
        output.append(getLossCount());
        output.append("L (");
        output.append(formatter.format(getWinPercentage() * 100));
        output.append("%)");
        return output.toString();
    }

    @Override
    public int compareTo(Team o) {
        int diff = o.getWinCount() - getWinCount();
        if(diff == 0){
            return getFullName().compareTo(o.getFullName());
        }
        return diff;
    }

    public School worstRecordAgainst(){
        Map<School, Integer> lossCount = new LinkedHashMap<>();
        for(Round r : rounds){
            if(r.getWinner() != this){
                if(lossCount.get(r.getWinner().getSchool()) == null){
                    lossCount.put(r.getWinner().getSchool(), 1);
                }else{
                    lossCount.put(r.getWinner().getSchool(), lossCount.get(r.getWinner().getSchool()) + 1);
                }
            }
        }
        sortByValue(lossCount);
        for(School s : lossCount.keySet()){
            return s;
        }
        return null;
    }

    public static <K, V extends Comparable<? super V>> Map<K, V> sortByValue(Map<K, V> map) {
        List<Map.Entry<K, V>> list = new ArrayList<>(map.entrySet());
        list.sort(Map.Entry.comparingByValue());

        Map<K, V> result = new LinkedHashMap<>();
        for (Map.Entry<K, V> entry : list) {
            result.put(entry.getKey(), entry.getValue());
        }

        return result;
    }
}
