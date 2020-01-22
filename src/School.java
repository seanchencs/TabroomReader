import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class School implements Comparable<School>{
    private String name;
    private List<Team> teams;

    public School(String name){
        this.name = name;
        teams = new ArrayList<>();
    }

    public String getName(){
        return name;
    }

    public void addTeam(Team t){
        if(!teams.contains(t)){
            teams.add(t);
        }
    }

    public int getTeamCount(){
        return teams.size();
    }

    public List<Team> getTeams(){
        return teams;
    }

    public int getRoundCount(){
        int output = 0;
        for(Team t : teams){
            output += t.getRounds().size();
        }
        return output;
    }

    public int getWinCount(){
        int output = 0;
        for(Team t : teams){
            output += t.getWinCount();
        }
        return output;
    }

    public int getLossCount(){
        return getRoundCount()-getWinCount();
    }

    public double getWinPercentage(){
        return (double)getWinCount()/getRoundCount();
    }

    public String toString(){
        StringBuilder output = new StringBuilder();
        DecimalFormat formatter = new DecimalFormat("00.00");
        output.append(name);
        output.append(" | ");
        output.append(getWinCount());
        output.append("W ");
        output.append(getLossCount());
        output.append("L (");
        output.append(formatter.format(getWinPercentage()*100));
        output.append("%)\n");
        for(Team t : teams){
            output.append(t);
            output.append("\n");
        }
        return output.toString();
    }

    @Override
    public int compareTo(School o) {
        //int diff = (int)(o.getWinPercentage()*1000 - getWinPercentage()*1000);
        int diff = o.getWinCount() - getWinCount();
        return diff != 0 ? diff : o.getTeamCount() - getTeamCount();
    }

    public boolean equals(Object o){
        if(!(o instanceof School)){
            return false;
        }
        School other = (School) o;
        return other.name.equals(this.name);
    }
}
