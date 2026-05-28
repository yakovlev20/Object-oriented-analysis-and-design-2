import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class DirectCSVManager {
    private CSVManager csvManager;

    public DirectCSVManager() {
        this.csvManager = new CSVManager("game_objects.csv");
    }

    public Collection<GameObject> getAll() {
        List<GameObject> objects = new ArrayList<>();
        List<Map<String, String>> records = csvManager.getAllRecords();

        for (Map<String, String> record : records) {
            GameObject obj = createGameObjectFromRecord(record);
            if (obj != null) {
                objects.add(obj);
            }
        }
        return objects;
    }

    private GameObject createGameObjectFromRecord(Map<String, String> record) {
        try {
            String id = record.get("id");
            String name = record.get("name");
            String type = record.get("type");
            double x = Double.parseDouble(record.getOrDefault("position_x", "0"));
            double y = Double.parseDouble(record.getOrDefault("position_y", "0"));

            if ("character".equals(type)) {
                int health = Integer.parseInt(record.getOrDefault("health", "100"));
                int level = Integer.parseInt(record.getOrDefault("level", "1"));
                return new Character(id, name, x, y, health, level);
            }
        } catch (NumberFormatException e) {
            System.err.println("Ошибка парсинга данных для записи " + record.get("id") + ": " + e.getMessage());
        }
        return null;
    }

    public GameObject get(String id) {
        // ПРЯМОЕ ОБРАЩЕНИЕ К CSV БЕЗ КЭША
        Map<String, String> record = csvManager.getRecordById(id);
        if (record != null) {
            return createGameObjectFromRecord(record); // Каждый раз создаём НОВЫЙ объект
        }
        return null;
    }

    public void put(GameObject obj) {
        if (obj == null) {
            throw new IllegalArgumentException("Объект не может быть null");
        }
        String id = obj.getId();
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("ID объекта не может быть пустым");
        }

        // Проверка на дубликат через прямой запрос к CSV
        if (csvManager.recordExists(id)) {
            throw new IllegalArgumentException("ID занят");
        }

        Map<String, String> record = new HashMap<>();
        record.put("id", obj.getId());
        record.put("name", obj.getName());
        record.put("type", obj.getType());
        record.put("position_x", String.valueOf(obj.getX()));
        record.put("position_y", String.valueOf(obj.getY()));

        if (obj instanceof Character) {
            Character character = (Character) obj;
            record.put("health", String.valueOf(character.getHealth()));
            record.put("level", String.valueOf(character.getLevel()));
        }

        csvManager.addRecord(record);
    }

    public void update(GameObject obj) {
        Map<String, String> record = new HashMap<>();
        record.put("id", obj.getId());
        record.put("name", obj.getName());
        record.put("type", obj.getType());
        record.put("position_x", String.valueOf(obj.getX()));
        record.put("position_y", String.valueOf(obj.getY()));

        if (obj instanceof Character) {
            Character character = (Character) obj;
            record.put("health", String.valueOf(character.getHealth()));
            record.put("level", String.valueOf(character.getLevel()));
        }

        csvManager.updateRecord(obj.getId(), record);
    }

    public void remove(String id) {
        csvManager.deleteRecord(id);
    }
}

// Базовый класс для игровых объектов
abstract class GameObject {
    private String id;
    private String name;
    private String type;
    private double x, y;

    public GameObject(String id, String name, String type, double x, double y) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.x = x;
        this.y = y;
    }

    // Геттеры
    public String getId() { return id; }
    public String getName() { return name; }
    public String getType() { return type; }
    public double getX() { return x; }
    public double getY() { return y; }

    // Сеттеры
    public void setId(String id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setType(String type) { this.type = type; }
    public void setX(double x) { this.x = x; }
    public void setY(double y) { this.y = y; }
}


// Пример класса персонажа
class Character extends GameObject {
    private int health;
    private int level;

    public Character(String id, String name, double x, double y, int health, int level) {
        super(id, name, "character", x, y);
        this.health = health;
        this.level = level;
    }

    public int getHealth() { return health; }
    public void setHealth(int health) { this.health = health; }
    public int getLevel() { return level; }
    public void setLevel(int level) { this.level = level; }
}

// Основной класс для демонстрации
public class RunPattern1 {
    public static void main(String[] args) {
        try {
            DirectCSVManager manager = new DirectCSVManager();

            // Выводим все объекты, загруженные из CSV
            System.out.println("=== ОБЪЕКТЫ, ЗАГРУЖЕННЫЕ ИЗ CSV ===");
            Collection<GameObject> allObjects = manager.getAll();
            if (allObjects.isEmpty()) {
                System.out.println("В базе данных нет объектов.");
            } else {
                for (GameObject obj : allObjects) {
                    printObjectInfo(obj);
                }
            }
            System.out.println(); // Пустая строка для разделения секций вывода

            // Пример использования — создаём экземпляр Character
            GameObject obj1 = new Character("7", "Player", 10.0, 20.0, 100, 1);
            manager.put(obj1);

            System.out.println("Добавлен новый объект: " + obj1.getId() + " " + obj1.getName());
            System.out.println();

            // Получаем объект по ID — каждый раз создаётся НОВЫЙ экземпляр
            GameObject retrieved1 = manager.get("1");
            GameObject retrieved2 = manager.get("1"); // Второй запрос — второй экземпляр!

            // Демонстрируем, что это разные объекты в памяти
            System.out.println("retrieved1 == retrieved2: " + (retrieved1 == retrieved2)); // false

            if (retrieved1 != null) {
                System.out.println("Retrieved: " + retrieved1.getName());

                // Дополнительно: демонстрируем работу с полями Character
                if (retrieved1 instanceof Character) {
                    Character character = (Character) retrieved1;
                    System.out.println("Health: " + character.getHealth());
                    System.out.println("Level: " + character.getLevel());
                }
            } else {
                System.out.println("Объект с ID '1' не найден.");
            }

            // Демонстрация обновления объекта
            System.out.println("\n=== ОБНОВЛЕНИЕ ОБЪЕКТА ===");
            if (retrieved1 != null && retrieved1 instanceof Character) {
                Character characterToUpdate = (Character) retrieved1;
                characterToUpdate.setHealth(150);
                characterToUpdate.setLevel(2);
                manager.update(characterToUpdate);
                System.out.println("Объект с ID " + characterToUpdate.getId() + " обновлён.");
            }

            // Проверка обновления: получаем объект заново
            GameObject updatedObj = manager.get("1");
            if (updatedObj instanceof Character) {
                Character updatedCharacter = (Character) updatedObj;
                System.out.println("После обновления: Health = " + updatedCharacter.getHealth() +
                                ", Level = " + updatedCharacter.getLevel());
            }

            // Демонстрация удаления объекта
            System.out.println("\n=== УДАЛЕНИЕ ОБЪЕКТА ===");
            String idToRemove = "7";
            manager.remove(idToRemove);
            System.out.println("Объект с ID '" + idToRemove + "' удалён.");

            // Проверяем, что объект удалён
            GameObject removedCheck = manager.get(idToRemove);
            if (removedCheck == null) {
                System.out.println("Проверка: объект с ID '" + idToRemove + "' действительно удалён.");
            } else {
                System.out.println("Ошибка: объект с ID '" + idToRemove + "' всё ещё существует.");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Вспомогательный метод для вывода информации об объекте
    private static void printObjectInfo(GameObject obj) {
        System.out.println("ID: " + obj.getId());
        System.out.println("Name: " + obj.getName());
        System.out.println("Type: " + obj.getType());
        System.out.println("Position: (" + obj.getX() + ", " + obj.getY() + ")");

        if (obj instanceof Character) {
            Character character = (Character) obj;
            System.out.println("  Health: " + character.getHealth());
            System.out.println("  Level: " + character.getLevel());
        }
        System.out.println("---"); // Разделитель между объектами
    }
}