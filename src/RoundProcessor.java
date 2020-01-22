import java.io.File;
import java.io.FileNotFoundException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class RoundProcessor {

    public static void main(String[] args) throws FileNotFoundException {

        final String TOURNAMENT_NAME = "TOC";

        Stopwatch stopwatch = new Stopwatch();
        DecimalFormat timeFormatter = new DecimalFormat("0.00");
        stopwatch.start();
        Tournament t = new Tournament(TOURNAMENT_NAME);
        File tournament = new File(TOURNAMENT_NAME);
        for (File round : getRounds(tournament)) {
            t.addFile(round);
            System.out.println(round.getName());
        }
        t.consolidate();
        stopwatch.stop();
        System.out.println("Processing Time: " + timeFormatter.format(stopwatch.time()) + "s");

        /*File tournament2 = new File("Bid Tournaments 2018");
        for(File round : tournament2.listFiles()){
            t.addFile(round);
            System.out.println(round.getName());
        }
        t.consolidate();*/

        System.out.println("----------------------------------------------------");

        //stats
        DecimalFormat formatter = new DecimalFormat("0.00");
        double affBias = t.getAffPercentage() * 100 - 50;
        if (affBias > 0) {
            System.out.println(formatter.format(affBias) + "% Aff Bias\n");
        } else {
            System.out.println(formatter.format(-affBias) + "% Neg Bias\n");
        }

        int count = 20;
        System.out.println("Top " + count + " teams by win rate (Min. 10 rounds):");
        for (Team team : t.topWinRate(count)) {
            System.out.println(team);
        }
        System.out.println();

        System.out.println("Top " + count + " most active teams: ");
        for (Team team : t.mostActiveTeams(count)) {
            System.out.println(team);
        }
        System.out.println();

        System.out.println("Top " + count + " most active schools:");
        for (School s : t.mostActiveSchools(count)) {
            System.out.println(s.getName() + ": " + s.getRoundCount());
        }
        System.out.println();

        System.out.println("Top " + count + " most successful schools:");
        for (School s : t.mostSuccessfulSchools(count)) {
            System.out.println(s.getName() + ": " + formatter.format(s.getWinPercentage() * 100) + "% - " + s.getWinCount() + "W " + s.getLossCount() + "L");
        }
        System.out.println();

        System.out.println("Top " + count + " most active judges:");
        for (Judge j : t.mostActiveJudges(count)) {
            System.out.println(j.getName() + ": " + j.getRoundCount());
        }
        System.out.println();

        System.out.println("Top " + count + " most aff biased judges:");
        for (Judge j : t.mostAffBiasedJudges(count)) {
            System.out.println(j.getName() + ": " + formatter.format(j.getAffPercentage() * 100) + "% - " + j.getRoundCount() + " Rounds");
        }
        System.out.println();

        System.out.println("Top " + count + " most neg biased judges:");
        for (Judge j : t.mostNegBiasedJudges(count)) {
            System.out.println(j.getName() + ": " + formatter.format(j.getNegPercentage() * 100) + "% - " + j.getRoundCount() + " Rounds");
        }
        System.out.println();

        System.out.println("Top " + count + " highest speaker point judges:");
        for (Judge j : t.highestSpeakerPointJudges(count)) {
            System.out.println(j.getName() + ": " + formatter.format(j.getAverageSpeakerPoints()) + " - " + j.getRoundCount() + " Rounds");
        }
        System.out.println();

        System.out.println("Top " + count + " lowest speaker point judges:");
        for (Judge j : t.lowestSpeakerPointJudges(count)) {
            System.out.println(j.getName() + ": " + formatter.format(j.getAverageSpeakerPoints()) + " - " + j.getRoundCount() + " Rounds");
        }
        System.out.println();

        queryMenu(t);
    }

    public static void queryMenu(Tournament t) {
        Scanner in = new Scanner(System.in);
        while (true) {
            System.out.println("(1) Search Team \n" +
                    "(2) Search School \n" +
                    "(3) Search Judge \n");
            System.out.print("Enter Choice: ");
            switch (in.nextLine()) {
                case "1":
                    System.out.println("Team Name: ");
                    Team match = searchTeam(in.nextLine(), t);
                    if (match == null) {
                        System.out.println("Team not found.");
                    } else {
                        System.out.println(match);
                        for (Round r : match.getRounds()) {
                            System.out.println(r.simple());
                        }
                        //System.out.println("Nemesis: " + match.worstRecordAgainst());
                    }
                    break;
                case "2":
                    System.out.println("School Name: ");
                    School school = t.getSchool(in.nextLine());
                    if (school == null) {
                        System.out.println("School not found.");
                    } else {
                        System.out.println(school);
                    }
                    break;
                case "3":
                    System.out.println("Judge Name: ");
                    Judge j = t.getJudge(in.nextLine());
                    if (j == null) {
                        System.out.println("Judge not found.");
                    } else {
                        System.out.println(j);
                        for (Round r : j.getRounds()) {
                            System.out.println(r.simple());
                        }
                    }
            }
        }
    }

    private static Team searchTeam(String name, Tournament t) {
        for (Team team : t.getTeams()) {
            if (team.getFullName().equals(name)) {
                return team;
            }
        }
        return null;
    }

    private static List<File> getRounds(File folder) {
        List<File> output = new ArrayList<>();
        for (File f : folder.listFiles()) {
            if (f.isDirectory()) {
                for (File f2 : getRounds(f)) {
                    output.add(f2);
                }
            }
            output.add(f);
        }
        return output;
    }

}
