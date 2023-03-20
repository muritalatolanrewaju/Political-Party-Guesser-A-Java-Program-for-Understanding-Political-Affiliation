import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class PoliticalPartyGuesser {

    // Define options for each question
    private static final String[] Q1_OPTIONS = {"A. Increase funding for welfare programs.",
                                                "B. Encourage job creation through tax incentives.",
                                                "C. Provide universal basic income.",
                                                "D. Decrease government intervention."};
    private static final String[] Q2_OPTIONS = {"A. Increase taxes on the wealthy.",
                                                "B. Decrease regulations on businesses.",
                                                "C. Maintain current tax and regulation policies.",
                                                "D. Abolish the income tax."};
    private static final String[] Q3_OPTIONS = {"A. Implement stricter gun control laws.",
                                                "B. Protect Second Amendment rights.",
                                                "C. Support gun ownership for self-defense.",
                                                "D. Ban all guns."};
    private static final String[] Q4_OPTIONS = {"A. Increase government spending on social programs.",
                                                "B. Decrease government spending overall.",
                                                "C. Support a balanced budget.",
                                                "D. Abolish government programs."};

    // Define political parties and their corresponding answers
    private static final String[] PARTIES = {"Democratic Party", "Republican Party", "Libertarian Party", "Green Party"};
    private static final String[][] ANSWERS = {{"A", "B", "C"}, {"B", "C", "D"}, {"B", "D"}, {"A", "C"}};

    // Define data storage files for each party
    private static final String[] PARTY_FILES = {"democratic.txt", "republican.txt", "libertarian.txt", "green.txt"};

    // Define machine learning data structures
    private static final Map<String, Map<String, Integer>> PARTY_ANSWERS = new HashMap<>();
    private static final Map<String, Integer> PARTY_COUNTS = new HashMap<>();

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Initialize machine learning data structures
        for (String party : PARTIES) {
            PARTY_ANSWERS.put(party, new HashMap<>());
            PARTY_COUNTS.put(party, 0);
        }

        // Ask questions and record answers
        List<String> answers = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            String[] options = getOptions(i);
            System.out.println(getQuestion(i));
            String answer = askQuestion(scanner, options);
            answers.add(answer);
            updateMachineLearningData(answer, i);
        }

        // Guess political party affiliation
        String guessedParty = guessParty(answers);

        System.out.println("Your political party affiliation is " + guessedParty);

        // Ask for political party affiliation and store data
        System.out.println("Which political party do you affiliate with? Democratic Party or Republican Party or Libertarian Party or Green Party?");
        String partyAffiliation = scanner.nextLine();
        storeData(partyAffiliation, answers);
    }

    private static String getQuestion(int index) {
        switch (index) {
            case 0:
                return "What should the government do to help the poor?";
            case 1:
                return "What is your stance on taxes and regulations?";
            case 2:
                return "What is your stance on gun control?";
            case 3:
                return "What is your stance on government spending?";
            default:
                return "";
        }
    }

    private static String[] getOptions(int index) {
        switch (index) {
            case 0:
                return Q1_OPTIONS;
            case 1:
                return Q2_OPTIONS;
                case 2:
                return Q3_OPTIONS;
                case 3:
                return Q4_OPTIONS;
                default:
                return new String[0];
                }
                }
                private static String askQuestion(Scanner scanner, String[] options) {
                    for (String option : options) {
                        System.out.println(option);
                    }
                    String answer = "";
                    while (!isValidAnswer(answer, options)) {
                        System.out.print("Enter your answer: ");
                        answer = scanner.nextLine().toUpperCase();
                    }
                    return answer;
                }
                
                private static boolean isValidAnswer(String answer, String[] options) {
                    for (String option : options) {
                        if (answer.equals(option.substring(0, 1))) {
                            return true;
                        }
                    }
                    return false;
                }
                
                private static void updateMachineLearningData(String answer, int questionIndex) {
                    for (int i = 0; i < PARTIES.length; i++) {
                        if (Arrays.asList(ANSWERS[i]).contains(answer)) {
                            String party = PARTIES[i];
                            Map<String, Integer> partyAnswers = PARTY_ANSWERS.get(party);
                            partyAnswers.put(Integer.toString(questionIndex), partyAnswers.getOrDefault(Integer.toString(questionIndex), 0) + 1);
                            PARTY_COUNTS.put(party, PARTY_COUNTS.get(party) + 1);
                        }
                    }
                }
                
                private static String guessParty(List<String> answers) {
                    Map<String, Double> partyScores = new HashMap<>();
                    for (String party : PARTIES) {
                        double score = 0.0;
                        for (int i = 0; i < answers.size(); i++) {
                            Map<String, Integer> partyAnswers = PARTY_ANSWERS.get(party);
                            int count = partyAnswers.getOrDefault(Integer.toString(i), 0);
                            score += ((double) count / PARTY_COUNTS.get(party));
                        }
                        partyScores.put(party, score);
                    }
                    return Collections.max(partyScores.entrySet(), Map.Entry.comparingByValue()).getKey();
                }
                
                private static void storeData(String partyAffiliation, List<String> answers) {
                    for (int i = 0; i < PARTIES.length; i++) {
                        String party = PARTIES[i];
                        String filename = PARTY_FILES[i];
                        boolean isCorrectParty = party.equals(partyAffiliation);
                        try {
                            FileWriter writer = new FileWriter(filename, true);
                            for (String answer : answers) {
                                writer.write(answer + "," + (isCorrectParty ? "1" : "0") + "\n");
                            }
                            writer.close();
                        } catch (IOException e) {
                            System.out.println("An error occurred while storing data for " + party);
                            e.printStackTrace();
                        }
                    }              
            }
        }
                
