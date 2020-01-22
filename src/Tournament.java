import java.io.File;
import java.io.FileNotFoundException;
import java.text.DecimalFormat;
import java.util.*;

public class Tournament {
    private String tournamentName;
    private List<Round> rounds;
    private List<Judge> judges;
    private List<Team> teams;
    private List<School> schools;

    public Tournament(String name){
        tournamentName = name;
        rounds = new ArrayList<>();
        judges = new ArrayList<>();
        teams =  new ArrayList<>();
        schools = new ArrayList<>();
    }

    public void addRound(Round round){
        rounds.add(round);
        round.getAff().addRound(round);
        round.getNeg().addRound(round);
        round.getJudge().addRound(round);
        teams.add(round.getAff());
        teams.add(round.getNeg());
    }

    public void consolidate(){
        System.out.println("Consolidating...");
        //teams
        for(int a = 0; a < teams.size(); a++){
            Team aTeam = teams.get(a);
            for(int b = a+1; b < teams.size(); b++){
                Team bTeam = teams.get(b);
                if(aTeam.getFullName().equals(bTeam.getFullName())){
                    aTeam.combine(bTeam);
                    teams.remove(b--);
                }
            }
        }
        //judge
        for(Round r : rounds){
            Judge newJudge = r.getJudge();
            boolean alreadyJudged = false;
            for(Judge j : judges){
                if(j.getName().equals(newJudge.getName())){
                    j.addRound(r);
                    alreadyJudged = true;
                }
            }
            if(!alreadyJudged) judges.add(newJudge);
        }
        //schools
        for(Team t : teams){
            School newSchool = t.getSchool();
            boolean existingSchool = false;
            for(School s : schools){
                if(s.getName().equals(newSchool.getName())){
                    t.setSchool(s);
                    existingSchool = true;
                }
            }
            if(!existingSchool){
                schools.add(t.getSchool());
            }
            t.getSchool().addTeam(t);
        }

    }

    public School getSchool(String name){
        for(School s : getSchools()){
            if(s.getName().equals(name)){
                return s;
            }
        }
        return null;
    }
    public List<School> getSchools(){
        Collections.sort(schools);
        return schools;
    }

    public Judge getJudge(String name){
        for(Judge j : getJudges()){
            if(j.getName().equals(name)){
                return j;
            }
        }
        return null;
    }

    public List<Judge> getJudges(){
        List<Judge> output = new ArrayList<>();
        for(Round r : rounds){
            if(!output.contains(r.getJudge())){
                output.add(r.getJudge());
            }
        }
        Collections.sort(output);
        return output;
    }

    public List<Team> getTeams(){
        List<Team> output = new ArrayList<>();
        for(Round r : rounds){
            if(!output.contains(r.getAff())){
                output.add(r.getAff());
            }
            if(!output.contains(r.getNeg())){
                output.add(r.getNeg());
            }
        }
        Collections.sort(output);
        return output;
    }

    public Team getTeam(String name){
        Team target = new Team(name);
        for(Team t : getTeams()){
            if(t.equals(target)){
                return t;
            }
        }
        return null;
    }

    public void addFile(File round) throws FileNotFoundException {
        Scanner roundScanner = new Scanner(round);
        roundScanner.nextLine();
        while(roundScanner.hasNextLine()){
            String[] data = roundScanner.nextLine().split("\",");
            try{
                //speaker points
                try{
                    Round temp = new Round(data[0], data[1], data[2], data[3], data[4]);
                    addRound(temp);
                }catch(ArrayIndexOutOfBoundsException e){
                    Round temp = new Round(data[0], data[1], data[2], data[3]);
                    addRound(temp);
                }
            }catch(ArrayIndexOutOfBoundsException e){
                System.out.println("Could not add a round in " + round.getName());
            }
        }
    }

    public void addFiles(List<File> fileList) throws FileNotFoundException{
        for(File f : fileList){
            addFile(f);
        }
    }

    public double getAffPercentage(){
        double output = 0;
        for(Round r : rounds){
            if(r.getVote().equals("AFF")){
                output++;
            }
        }
        output /= rounds.size();
        return output;
    }

    public double getNegPercentage(){
        return 1 - getAffPercentage();
    }

    //tounament analysis
    public String toString(){
        StringBuilder output = new StringBuilder();
        DecimalFormat formatter = new DecimalFormat("00.00");
        for(Round r : rounds){
            output.append(r.toString());
            output.append("\n");
        }
        output.append("\n");
        output.append("AFF: ");
        output.append(formatter.format(getAffPercentage()*100));
        output.append(" | NEG: ");
        output.append(formatter.format(getNegPercentage()*100));
        return output.toString();
    }


    public List<Team> topWinRate(int count){
        Collections.sort(teams, (o1, o2) -> (int)(o2.getWinPercentage()*10000-o1.getWinPercentage()*10000));
        List<Team> output = new ArrayList<>();
        for(Team t : teams){
            if(t.getRounds().size() >= 10){
                output.add(t);
            }
            if(output.size() == count) break;
        }
        Collections.sort(teams);
        return output;
    }

    public List<School> mostActiveSchools(int count){
        Collections.sort(schools, (o1, o2) -> (o2.getRoundCount()-o1.getRoundCount()));
        List<School> output = new ArrayList<>();
        for(School t : schools){
            output.add(t);
            if(output.size() == count) break;
        }
        Collections.sort(schools);
        return output;
    }

    public List<School> leastActiveSchools(int count){
        Collections.sort(schools, (o1, o2) -> (o1.getRoundCount()-o2.getRoundCount()));
        List<School> output = new ArrayList<>();
        for(School t : schools){
            output.add(t);
            if(output.size() == count) break;
        }
        Collections.sort(schools);
        return output;
    }

    public List<School> mostSuccessfulSchools(int count){
        Collections.sort(schools, (o1, o2) -> (int)(o2.getWinPercentage()*1000-o1.getWinPercentage()*1000));
        List<School> output = new ArrayList<>();
        for(School t : schools){
            if(t.getRoundCount() > 20){
                output.add(t);
                if(output.size() == count) break;
            }
        }
        Collections.sort(schools);
        return output;
    }

    public List<Judge> mostActiveJudges(int count){
        Collections.sort(judges, (o1, o2) -> (o2.getRoundCount()-o1.getRoundCount()));
        List<Judge> output = new ArrayList<>();
        for(Judge j : judges){
            output.add(j);
            if(output.size() == count) break;
        }
        Collections.sort(judges);
        return output;
    }

    public List<Team> mostActiveTeams(int count){
        Collections.sort(teams, (o1, o2) -> (o2.getRounds().size() - o1.getRounds().size()));
        List<Team> output = new ArrayList<>();
        for(Team t : teams){
            if(t.getRounds().size() >= 10){
                output.add(t);
            }
            if(output.size() == count) break;
        }
        Collections.sort(teams);
        return output;
    }

    public List<Judge> mostAffBiasedJudges(int count){
        Collections.sort(judges, (o1, o2) -> (int)(o2.getAffPercentage()*10000 - o1.getAffPercentage()*10000));
        List<Judge> output = new ArrayList<>();
        for(Judge j : judges){
            if(j.getRounds().size() >= 10){
                output.add(j);
            }
            if(output.size() == count) break;
        }
        Collections.sort(judges);
        return output;
    }

    public List<Judge> mostNegBiasedJudges(int count){
        Collections.sort(judges, (o1, o2) -> (int)(o1.getAffPercentage()*10000 - o2.getAffPercentage()*10000));
        List<Judge> output = new ArrayList<>();
        for(Judge j : judges){
            if(j.getRounds().size() >= 10){
                output.add(j);
            }
            if(output.size() == count) break;
        }
        Collections.sort(judges);
        return output;
    }

    public List<Judge> highestSpeakerPointJudges(int count){
        Collections.sort(judges, (o1, o2) -> (int)(o2.getAverageSpeakerPoints()*10000 - o1.getAverageSpeakerPoints()*10000));
        List<Judge> output = new ArrayList<>();
        for(Judge j : judges){
            if(j.getRounds().size() >= 10 && j.getAverageSpeakerPoints() > 0){
                output.add(j);
            }
            if(output.size() == count) break;
        }
        Collections.sort(judges);
        return output;
    }

    public List<Judge> lowestSpeakerPointJudges(int count){
        Collections.sort(judges, (o1, o2) -> (int)(o1.getAverageSpeakerPoints()*10000 - o2.getAverageSpeakerPoints()*10000));
        List<Judge> output = new ArrayList<>();
        for(Judge j : judges){
            if(j.getRounds().size() >= 10 && j.getAverageSpeakerPoints() > 0){
                output.add(j);
            }
            if(output.size() == count) break;
        }
        Collections.sort(judges);
        return output;
    }


}
