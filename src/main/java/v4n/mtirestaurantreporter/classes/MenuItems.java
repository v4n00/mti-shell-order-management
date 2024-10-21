package v4n.mtirestaurantreporter.classes;

import v4n.mtirestaurantreporter.exceptions.InvalidFileFormat;

import java.io.*;
import java.util.HashMap;

public class MenuItems {
    private static HashMap<String, Float> items;

    /**
     * Gets the items in the menu as a singleton.
     * @return the items in the menu
     */
    public static HashMap<String, Float> get() {
        if(items == null) {
            File file = new File("items.csv");
            if (file.exists()) {
                loadItemsFromFile(file);
            } else {
                loadDefaultItems(file);
            }
        }
        return items;
    }

    /**
     * Loads the items from a file.
     * @param file the file to load the items from
     * @throws NumberFormatException if the price in the file is not a valid number
     */
    private static void loadItemsFromFile(File file) {
        items = new HashMap<>();
        try(BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)))) {
            String line;
            while((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 2) {
                    String itemName = parts[0].trim();
                    float itemPrice = Float.parseFloat(parts[1].trim());
                    items.put(itemName, itemPrice);
                }
                else {
                    throw new InvalidFileFormat("File is not in the correct format: itemName, itemPrice");
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch(NumberFormatException | InvalidFileFormat e) {
            loadDefaultItems(file);
            // TODO: add toast messages
        }
    }

    /**
     * Loads the default items and writes them to a file.
     * @param file the file to write the items to
     */
    private static void loadDefaultItems(File file) {
        items = new HashMap<>();
        items.put("Burger", 5.9f);
        items.put("Pizza", 7.2f);
        items.put("Pasta", 6.0f);
        items.put("Salad", 4.7f);
        items.put("Soda", 1.3f);
        items.put("Water", 0.5f);

        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file)))) {
            for (HashMap.Entry<String, Float> entry : items.entrySet()) {
                writer.write(entry.getKey() + "," + entry.getValue());
                writer.newLine();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
