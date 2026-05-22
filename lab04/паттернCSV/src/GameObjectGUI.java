import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

public class GameObjectGUI extends JFrame {
    private IdentityMap identityMap;
    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField idField, nameField, xField, yField, healthField, levelField;
    private JButton addButton, getButton, removeButton, refreshButton;

    public GameObjectGUI(IdentityMap identityMap) {
        this.identityMap = identityMap;
        initializeGUI();
    }

    private void initializeGUI() {
        setTitle("Управление игровыми объектами");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 500);
        setLocationRelativeTo(null);

        // Создаём панель для ввода данных с расширенными полями
        JPanel inputPanel = new JPanel(new GridLayout(6, 2, 5, 5));
        inputPanel.setBorder(BorderFactory.createTitledBorder("Добавить/Получить объект"));

        inputPanel.add(new JLabel("ID:"));
        idField = new JTextField();
        inputPanel.add(idField);

        inputPanel.add(new JLabel("Имя:"));
        nameField = new JTextField();
        inputPanel.add(nameField);

        inputPanel.add(new JLabel("X координата:"));
        xField = new JTextField("0.0");
        inputPanel.add(xField);

        inputPanel.add(new JLabel("Y координата:"));
        yField = new JTextField("0.0");
        inputPanel.add(yField);

        inputPanel.add(new JLabel("Здоровье:"));
        healthField = new JTextField("100");
        inputPanel.add(healthField);

        inputPanel.add(new JLabel("Уровень:"));
        levelField = new JTextField("1");
        inputPanel.add(levelField);

        // Создаём кнопки
        addButton = new JButton("Добавить");
        getButton = new JButton("Получить");
        removeButton = new JButton("Удалить");
        refreshButton = new JButton("Обновить");

        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(addButton);
        buttonPanel.add(getButton);
        buttonPanel.add(removeButton);
        buttonPanel.add(refreshButton);

        // Создаём таблицу для отображения данных
        tableModel = new DefaultTableModel(
            new Object[]{"ID", "Имя", "X", "Y", "Здоровье", "Уровень"}, 0);
        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);

        // Добавляем обработчики событий
        addButton.addActionListener(new AddButtonListener());
        getButton.addActionListener(new GetButtonListener());
        removeButton.addActionListener(new RemoveButtonListener());
        refreshButton.addActionListener(e -> refreshTable());

        // Компоновка элементов
        setLayout(new BorderLayout(5, 5));
        add(inputPanel, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.CENTER);
        add(scrollPane, BorderLayout.SOUTH);

        refreshTable();
    }

    // Обновляет таблицу данными из IdentityMap
    private void refreshTable() {
        tableModel.setRowCount(0); // Очищаем таблицу
        Collection<GameObject> objects = identityMap.getAll();

        for (GameObject obj : objects) {
            if (obj instanceof Character) {
                Character character = (Character) obj;
                tableModel.addRow(new Object[]{
                    obj.getId(),
            obj.getName(),
            obj.getX(),
            obj.getY(),
            character.getHealth(),
            character.getLevel()
        });
            } else {
                // Для других типов объектов (если появятся)
                tableModel.addRow(new Object[]{
            obj.getId(),
            obj.getName(),
            obj.getX(),
            obj.getY(),
            "N/A",
            "N/A"
        });
            }
        }
    }

    // Обработчик для кнопки "Добавить"
    private class AddButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String id = idField.getText().trim();
            String name = nameField.getText().trim();

            // Получаем и валидируем числовые значения
            double x, y;
            int health, level;

            try {
                x = Double.parseDouble(xField.getText().trim());
                y = Double.parseDouble(yField.getText().trim());
                health = Integer.parseInt(healthField.getText().trim());
                level = Integer.parseInt(levelField.getText().trim());
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(GameObjectGUI.this,
            "Введите корректные числовые значения для координат, здоровья и уровня!",
            "Ошибка", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (id.isEmpty() || name.isEmpty()) {
                JOptionPane.showMessageDialog(GameObjectGUI.this,
            "Заполните все поля!", "Ошибка", JOptionPane.ERROR_MESSAGE);
                return;
            }

            GameObject newObject = new Character(id, name, x, y, health, level);
            identityMap.put(newObject);

            JOptionPane.showMessageDialog(GameObjectGUI.this,
            "Объект добавлен: " + name);

            idField.setText("");
            nameField.setText("");
            xField.setText("0.0");
            yField.setText("0.0");
            healthField.setText("100");
            levelField.setText("1");
            refreshTable();
        }
    }

    // Обработчик для кнопки "Получить"
    private class GetButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String id = idField.getText().trim();

            if (id.isEmpty()) {
                JOptionPane.showMessageDialog(GameObjectGUI.this,
            "Введите ID объекта!", "Ошибка", JOptionPane.ERROR_MESSAGE);
                return;
            }

            GameObject obj = identityMap.get(id);

            if (obj != null) {
                if (obj instanceof Character) {
                    Character character = (Character) obj;
                    JOptionPane.showMessageDialog(GameObjectGUI.this,
                "Найден объект: " + obj.getName() +
                "\nX: " + obj.getX() +
                "\nY: " + obj.getY() +
                "\nЗдоровье: " + character.getHealth() +
                "\nУровень: " + character.getLevel());
                } else {
                    JOptionPane.showMessageDialog(GameObjectGUI.this,
                "Найден объект: " + obj.getName() +
                "\nX: " + obj.getX() +
                "\nY: " + obj.getY());
            }
        } else {
            JOptionPane.showMessageDialog(GameObjectGUI.this,
                "Объект с ID " + id + " не найден!", "Информация",
                JOptionPane.INFORMATION_MESSAGE);
        }
    }
}

// Обработчик для кнопки "Удалить"
private class RemoveButtonListener implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {
        String id = idField.getText().trim();

        if (id.isEmpty()) {
            JOptionPane.showMessageDialog(GameObjectGUI.this,
                "Введите ID объекта!", "Ошибка", JOptionPane.ERROR_MESSAGE);
            return;
        }

        identityMap.remove(id);
        JOptionPane.showMessageDialog(GameObjectGUI.this,
            "Объект с ID " + id + " удалён");
        refreshTable();
    }
}

public static void main(String[] args) {
    // Создаём экземпляр IdentityMap
    IdentityMap identityMap = new IdentityMap();

    // Запускаем GUI в потоке диспетчеризации событий (EDT)
    javax.swing.SwingUtilities.invokeLater(() -> {
        GameObjectGUI gui = new GameObjectGUI(identityMap);
        gui.setVisible(true);
    });
}
}