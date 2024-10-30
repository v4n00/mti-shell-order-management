package classes;

import exceptions.InvalidFileFormat;

import java.io.*;
import java.util.HashMap;

public class MenuItems {
    private static MenuItems INSTANCE;
    private HashMap<String, Float> items;

    /**
     * Gets the items in the menu as a singleton.
     *
     * @return the items in the menu
     */
    public static HashMap<String, Float> get() throws InvalidFileFormat {
        if(INSTANCE == null) {
            INSTANCE = new MenuItems();
        }
        return INSTANCE.items;
    }

    private MenuItems() throws InvalidFileFormat {
        String menuItemsFileName = Restaurant.getMenuItemsFileName();
        File file = new File(menuItemsFileName);
        if (file.exists()) {
            loadItemsFromFile(file);
        } else {
            loadDefaultItems(file);
        }
    }

    /**
     * Loads the items from a file.
     *
     * @param file the file to load the items from
     * @throws InvalidFileFormat if the file is not in the correct format
     */
    private void loadItemsFromFile(File file) throws InvalidFileFormat {
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
        } catch(InvalidFileFormat e) {
            loadDefaultItems(file);
            throw e;
        }
    }

    /**
     * Loads the default items and writes them to a file.
     *
     * @param file the file to write the items to
     */
    private void loadDefaultItems(File file) {
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
