import java.net.*;
import java.io.*;
import java.text.DecimalFormat;



public class SortJobs {
    public static int count = 0;

    static String[] array1 = {
        "Boston",
        "San+Francisco",
        "Los+Angeles",
        "Denver",
        "Boulder",
        "Chicago",
        "New+York"
    };
    static String[] array2 = {
        "Java",
        "Ruby",
        "Scala",
        "Node"
    };
    static String[] array3 = {
        "Boston",
        "San Francisco",
        "Los Angeles",
        "Denver",
        "Boulder",
        "Chicago",
        "New York"
    };
    static int[][][]
    finalArray = new int[7][4][4];


    public static void main(String args[]) throws MalformedURLException, IOException {
        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < 4; j++) {
                callRest(array1[i], array2[j], i, j);
            }
        }
        print();
        runTests();
    }

    public static void callRest(String location, String lang, int loc1, int loc2) throws MalformedURLException, IOException {
        if (location == null) {
            System.out.println("Location was null");
            return;
        } else if (lang == null) {
            System.out.println("Language was null");
            return;
        } else if (loc1 < 0 || loc1 >= array1.length) {
            System.out.println("Array index out of bounds for location array.");
            return;
        } else if (loc2 < 0 || loc2 >= array2.length) {
            System.out.println("Array index out of bounds for language array.");
            return;
        }

        if (location.contains(" ")) {
            location = location.replaceAll("\\s", "+");
        } else if (lang.contains(" ")) {
            lang = lang.replaceAll("\\s", "+");
        }


        String url2 = "https://jobs.github.com/positions.json?description=" + lang + "&location=" + location;
        URL url = new URL(url2);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        BufferedReader in = new BufferedReader(
            new InputStreamReader(connection.getInputStream()));
        String inputLine;
        StringBuffer content = new StringBuffer();
        while ((inputLine = in .readLine()) != null) {
            content.append(inputLine);
        } in .close();
        String s = content.toString();
        String[] splitted = s.split("\\s+");
        for (int i = 0; i < splitted.length; i++) {
            String str = splitted[i].toLowerCase();

            if (str.contains("full") || str.contains("full-time")) {
                finalArray[loc1][loc2][0]++;
                finalArray[loc1][loc2][3]++;
            } else if (str.contains("part") || str.contains("part-time")) {
                finalArray[loc1][loc2][1]++;
                finalArray[loc1][loc2][3]++;

            } else if (str.contains("contract")) {
                finalArray[loc1][loc2][2]++;
                finalArray[loc1][loc2][3]++;
            }
        }
    }

    public static void print() {
        int countTotal = 0;
        for (int i = 0; i < array1.length; i++) {
            System.out.println(array3[i] + ":");
            for (int j = 0; j < array2.length; j++) {
                System.out.println("    -" + array2[j] + ":");
                for (int k = 0; k < finalArray[i][j].length; k++) {
                    double percent = (((double) finalArray[i][j][k] / (double) finalArray[i][j][3])) * 100;

                    DecimalFormat df = new DecimalFormat("#.##");

                    if (k == 0) {
                        System.out.println("         -Full time: " + df.format(percent) + "%");
                    } else if (k == 1) {
                        System.out.println("         -Part time: " + df.format(percent) + "%");
                    } else if (k == 2) {
                        System.out.println("         -Unknown: " + df.format(percent) + "%");
                    } else {
                        countTotal += finalArray[i][j][3];
                    }
                }
            }
        }
        System.out.println("\nSourced: " + countTotal + " programming jobs.");
    }

    public static void runTests() throws MalformedURLException, IOException {
        //test calling with a null string for the location and observe error handling
        callRest(null, "Java", 0, 0);

        //test calling with a null string for the language and observe error handling
        callRest("Boston", null, 0, 0);

        //test calling an out of bounds index for the location array
        callRest("Boston", "Java", -1, 3);
        callRest("Boston", "Java", 75, 3);

        //test calling an out of bounds index for the language array
        callRest("Boston", "Java", 3, -1);
        callRest("Boston", "Java", 3, 75);

        //test calling with a location that has a space in it - will get an exception if we
        //dont replace the space with a plus
        callRest("New York", "Java", 0, 0);

    }
}
