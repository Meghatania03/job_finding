package com.example.job_finding;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class FAQController implements Initializable {

    @FXML
    private Label FAQ_answer;

    @FXML
    private TextField searchField;

    @FXML
    private Button FAQ_submit_button;

    private List<FAQItem> allFAQs = new ArrayList<>();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        loadFAQData("https://api.myjson.online/v1/records/a9d78f0e-4b57-449f-a83b-33a6df018972"); // Use your actual API or local file


        FAQ_submit_button.setOnAction(event -> searchFAQ());
    }


    private void loadFAQData(String jsonUrl) {
        try {
            URL url = new URL(jsonUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");


            try (InputStreamReader reader = new InputStreamReader(connection.getInputStream())) {

                StringBuilder response = new StringBuilder();
                int ch;
                while ((ch = reader.read()) != -1) {
                    response.append((char) ch);
                }


                String rawJson = response.toString();
                System.out.println("Raw JSON response: " + rawJson);


                JsonObject jsonObject = JsonParser.parseString(rawJson).getAsJsonObject();


                JsonObject dataObject = jsonObject.getAsJsonObject("data");


                JsonArray questionsArray = dataObject.getAsJsonArray("questions");


                if (questionsArray == null || questionsArray.size() == 0) {
                    System.out.println("No FAQs found in the JSON.");
                } else {

                    allFAQs = parseFAQArray(questionsArray);
                }


                System.out.println("FAQ data loaded successfully.");
                System.out.println("Number of FAQs: " + allFAQs.size());
            }
        } catch (Exception e) {
            System.err.println("Error loading FAQ data: " + e.getMessage());
            e.printStackTrace();
        }
    }


    private List<FAQItem> parseFAQArray(JsonArray faqsArray) {
        List<FAQItem> faqList = new ArrayList<>();
        if (faqsArray != null) {
            for (int i = 0; i < faqsArray.size(); i++) {
                JsonObject faqObject = faqsArray.get(i).getAsJsonObject();
                String question = faqObject.get("question").getAsString();
                String answer = faqObject.get("answer").getAsString();
                faqList.add(new FAQItem(question, answer));
            }
        }
        return faqList;
    }


    private void searchFAQ() {

        if (allFAQs == null || allFAQs.isEmpty()) {
            FAQ_answer.setText("FAQ data is not loaded.");
            return;
        }

        String query = searchField.getText().trim().toLowerCase();


        for (FAQItem item : allFAQs) {
            if (item.getQuestion().toLowerCase().contains(query)) {
                FAQ_answer.setText(item.getAnswer()); // Display the answer in the Label
                return;
            }
        }


        FAQ_answer.setText("No answers found.");
    }

//    How do I reset my password on the app?
//    How do I apply for the job?
//    What is the minimum qualification required for the job?
}
