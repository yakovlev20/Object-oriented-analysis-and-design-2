import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CSVManager {
    private String filename;
    private List<String> headers;
    private List<Map<String, String>> data;

    public CSVManager(String filename) {
        this.filename = filename;
        this.data = new ArrayList<>();
        loadData();
    }

    private void loadData() {
        File file = new File(filename);
        if (!file.exists()) {
            createEmptyCSV();
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line = reader.readLine();
            if (line == null) {
                createEmptyCSV();
                return;
            }

            headers = Arrays.asList(line.split(","));
            String currentLine;
            while ((currentLine = reader.readLine()) != null) {
                String[] values = currentLine.split(",");
                Map<String, String> row = new HashMap<>();
                for (int i = 0; i < headers.size() && i < values.length; i++) {
                    row.put(headers.get(i), values[i]);
                }
                data.add(row);
            }
        } catch (IOException e) {
            throw new RuntimeException("Ошибка при чтении CSV файла", e);
        }
    }

    private void createEmptyCSV() {
        headers = Arrays.asList("id", "name", "type", "position_x", "position_y");
        saveData();
    }

    private void saveData() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
            writer.println(String.join(",", headers));
            for (Map<String, String> row : data) {
                List<String> values = headers.stream()
                    .map(header -> row.getOrDefault(header, ""))
                    .collect(Collectors.toList());
                writer.println(String.join(",", values));
            }
        } catch (IOException e) {
            throw new RuntimeException("Ошибка при сохранении CSV файла", e);
        }
    }

    public void addRecord(Map<String, String> record) {
        data.add(record);
        saveData();
    }

    public List<Map<String, String>> getAllRecords() {
        return new ArrayList<>(data);
    }

    public Map<String, String> getRecordById(String id) {
        return data.stream()
            .filter(row -> id.equals(row.get("id")))
            .findFirst()
            .orElse(null);
    }

    public boolean updateRecord(String id, Map<String, String> updatedFields) {
        for (Map<String, String> row : data) {
            if (id.equals(row.get("id"))) {
                row.putAll(updatedFields);
                saveData();
                return true;
            }
        }
        return false;
    }

    public boolean deleteRecord(String id) {
        boolean removed = data.removeIf(row -> id.equals(row.get("id")));
        if (removed) saveData();
        return removed;
    }
}