import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ApiUtil {

    private static final String ID = Main.id;
    private static final String API_URL = "https://app.seker.live/fm1";
    public static Integer markSquare(Character[][] currentBoard, int row, int column) {
        try {
            // Create the URL for the API request
            URL url = new URL(API_URL + "/update-board?id=" + ID + "&row=" + row + "&column=" + column);

            // Open an HTTP connection
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Authorization", "Bearer " + API_URL);

            // Read the response content
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();

            // Close the connection
            connection.disconnect();

            // Parse the JSON response
            JSONObject jsonResponse = new JSONObject(response.toString());

            // Extract the marked square from the JSON response
            int markedSquare = jsonResponse.optInt("markedSquare", -1);

            // Update the current board if the move is valid
            if (markedSquare >= 0 && markedSquare < 9 && currentBoard[row][column] == null) {
                currentBoard[row][column] = 'X'; // Assuming 'X' represents the player's move
            }

            return markedSquare;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }
    public static boolean checkWinner() {
        try {
            // Create the URL for the API request to check the winner
            URL url = new URL(API_URL + "/check-winner?id=" + ID);

            // Open an HTTP connection
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Authorization", "Bearer " + API_URL);

            // Read the response content
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();

            // Close the connection
            connection.disconnect();

            // Parse the JSON response
            JSONObject jsonResponse = new JSONObject(response.toString());

            // Extract the winner status from the JSON response
            boolean isWinner = jsonResponse.optBoolean("winner", false);

            return isWinner;
        } catch (Exception e) {
            e.printStackTrace();
            return false; // Return false to indicate an error or no winner
        }
    }

    public static Character[][] getBoard () {

        final String GET_BOARD_URL = API_URL + "/get-board?id=" + ID;
        try {
            // Create URL for the API request
            URL url = new URL(GET_BOARD_URL);

            // Open an HTTP connection
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            // Read the response content
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();

            // Close the connection
            connection.disconnect();

            // Parse the JSON response
            JSONObject jsonResponse = new JSONObject(response.toString());

            // Extract the board data from the JSON response
            JSONArray boardArray = jsonResponse.getJSONArray("board");
            int numRows = boardArray.length();
            int numCols = boardArray.getJSONArray(0).length();
            Character[][] board = new Character[numRows][numCols];

            for (int i = 0; i < numRows; i++) {
                JSONArray row = boardArray.getJSONArray(i);
                for (int j = 0; j < numCols; j++) {
                    if (!row.isNull(j)) {
                        String cellValue = row.getString(j);
                        board[i][j] = cellValue.charAt(0);
                    } else {
                        board[i][j] = null;
                    }
                }
            }

            // Return the parsed board
            return board;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    public static void clearBoard() {
        try {
            // Create the URL for the API request to clear the board
            URL url = new URL(API_URL + "/clear-board?id=" + ID);

            // Open an HTTP connection
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Authorization", "Bearer " + API_URL);

            // Read the response content
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();

            // Close the connection
            connection.disconnect();

            // Optionally, you can check the response to confirm that the board was cleared successfully
            String jsonResponse = response.toString();
            if (jsonResponse.contains("Board cleared successfully")) {
                System.out.println("Board cleared successfully");
            } else {
                System.out.println("Failed to clear the board");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
