import java.util.ArrayList;
import java.util.List;

public class Round {
    private Team aff;
    private Team neg;
    private Judge judge;
    private boolean vote;
    private List<Double> speakerPoints;

    public Round(String affTeam, String negTeam, String judgeName, String win){
        aff = new Team(affTeam.substring(0, affTeam.indexOf("\t")).replaceAll("\"", ""));
        neg = new Team(negTeam.substring(0, negTeam.indexOf("\t")).replaceAll("\"", ""));
        judge = new Judge(judgeName.replaceAll("\t", "").replaceAll("\"", ""));
        vote = win.contains("Aff");
        speakerPoints = null;
    }

    public Round(String affTeam, String negTeam, String judgeName, String win, String speakers){
        aff = new Team(affTeam.substring(0, affTeam.indexOf("\t")).replaceAll("\"", ""));
        neg = new Team(negTeam.substring(0, negTeam.indexOf("\t")).replaceAll("\"", ""));
        judge = new Judge(judgeName.replaceAll("\t", "").replaceAll("\"", ""));
        vote = win.contains("Aff");

        //speaker
        System.out.println(speakers);
        speakerPoints = processSpeakerPoints(speakers);
    }

    private List<Double> processSpeakerPoints(String speakers){
        List<Double> output = new ArrayList<>();
        String[] raw = speakers.split("\t");
        for(int i = 1; i < speakers.length(); i++){
            System.out.println(raw[i]);
            if(raw[i].contains(".")){
                System.out.println(raw[i]);
                output.add(Double.parseDouble(raw[i]));
            }
        }
        return output;
    }


    public Team getAff() {
        return aff;
    }

    public Team getNeg() {
        return neg;
    }

    public Judge getJudge() {
        return judge;
    }

    public String getVote() {
        return vote ? "AFF" : "NEG";
    }

    public Team getWinner(){
        return vote ? getAff() : getNeg();
    }

    public Team getLoser(){
        return vote ? getNeg() : getAff();
    }

    public List<Double> getSpeakerPoints(){
        return speakerPoints;
    }

    public void setAff(Team aff) {
        this.aff = aff;
    }

    public void setNeg(Team neg) {
        this.neg = neg;
    }

    public void setJudge(Judge judge) {
        this.judge = judge;
    }

    public void setVote(boolean vote) {
        this.vote = vote;
    }

    public String toString(){
        StringBuilder output = new StringBuilder();
        output.append(aff);
        output.append(" | ");
        output.append(neg);
        output.append(" | ");
        output.append(judge);
        output.append(" | ");
        output.append(vote ? "AFF" : "NEG");
        return output.toString();
    }

    public String simple(){
        StringBuilder output = new StringBuilder();
        output.append(getWinner().getFullName());
        output.append(" (");
        output.append(getVote());
        output.append(") def. ");
        output.append(getVote().equals("AFF") ? neg.getFullName() : aff.getFullName());
        output.append(getVote().equals("AFF") ? " (NEG)" : " (AFF)");
        return output.toString();
    }
}
