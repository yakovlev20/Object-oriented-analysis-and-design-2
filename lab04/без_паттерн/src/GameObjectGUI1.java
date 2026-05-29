import java.awt.BorderLayout;
import java.awt.Dimension;
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

public class GameObjectGUI1 extends JFrame {
    private DirectCSVManager csvManager;  // Изменено с IdentityMap на DirectCSVManager
    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField idField, nameField, xField, yField, healthField, levelField;
    private JButton addButton, getButton, removeButton, refreshButton, updateButton; // Добавлена кнопка обновления

    public GameObjectGUI1(DirectCSVManager csvManager) {  // Изменен параметр конструктора
        this.csvManager = csvManager;
        initializeGUI();
    }

    private void initializeGUI() {
        setTitle("Управление игровыми объектами (Direct CSV)");  // Изменен заголовок
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 600);
        setLocationRelativeTo(null);

        // Панель ввода данных
        JPanel inputPanel = new JPanel(new GridLayout(6, 2, 10, 10));
        inputPanel.setBorder(BorderFactory.createTitledBorder("Введите данные"));
        inputPanel.setPreferredSize(new Dimension(400, 200));

        inputPanel.add(new JLabel("ID:"));
        idField = new JTextField();
        inputPanel.add(idField);

        inputPanel.add(new JLabel("Имя:"));
        nameField = new JTextField();
        inputPanel.add(nameField);

        inputPanel.add(new JLabel("X:"));
        xField = new JTextField("0.0");
        inputPanel.add(xField);

        inputPanel.add(new JLabel("Y:"));
        yField = new JTextField("0.0");
        inputPanel.add(yField);

        inputPanel.add(new JLabel("Здоровье:"));
        healthField = new JTextField("100");
        inputPanel.add(healthField);

        inputPanel.add(new JLabel("Уровень:"));
        levelField = new JTextField("1");
        inputPanel.add(levelField);

        // Кнопки управления
        addButton = new JButton("Добавить");
        getButton = new JButton("Получить");
        updateButton = new JButton("Обновить");  // Новая кнопка
        removeButton = new JButton("Удалить");
        refreshButton = new JButton("Обновить таблицу");

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(addButton);
        buttonPanel.add(getButton);
        buttonPanel.add(updateButton);  // Добавляем кнопку обновления
        buttonPanel.add(removeButton);
        buttonPanel.add(refreshButton);

        // Таблица для отображения
        tableModel = new DefaultTableModel(
            new Object[]{"ID", "Имя", "X", "Y", "Здоровье", "Уровень"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Сделать таблицу нередактируемой
            }
        };
        
        table = new JTable(tableModel);
        table.setPreferredScrollableViewportSize(new Dimension(800, 300));
        table.setFillsViewportHeight(true);
        
        // Настройка ширины столбцов
        table.getColumnModel().getColumn(0).setPreferredWidth(50);  // ID
        table.getColumnModel().getColumn(1).setPreferredWidth(150); // Имя
        table.getColumnModel().getColumn(2).setPreferredWidth(80);  // X
        table.getColumnModel().getColumn(3).setPreferredWidth(80);  // Y
        table.getColumnModel().getColumn(4).setPreferredWidth(100); // Здоровье
        table.getColumnModel().getColumn(5).setPreferredWidth(80);  // Уровень
        
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Список объектов"));

        // Обработчики событий
        addButton.addActionListener(new AddButtonListener());
        getButton.addActionListener(new GetButtonListener());
        updateButton.addActionListener(new UpdateButtonListener());  // Обработчик для обновления
        removeButton.addActionListener(new RemoveButtonListener());
        refreshButton.addActionListener(e -> refreshTable());

        // Компоновка с использованием панелей
        JPanel northPanel = new JPanel(new BorderLayout(10, 10));
        northPanel.add(inputPanel, BorderLayout.NORTH);
        northPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        // Добавляем отступы
        northPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        scrollPane.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createEmptyBorder(0, 10, 10, 10),
            BorderFactory.createTitledBorder("Список объектов")
        ));

        // Компоновка основного окна
        setLayout(new BorderLayout(10, 10));
        add(northPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        refreshTable(); // Первоначальное заполнение таблицы
    }

    // Обновление таблицы данными из CSV
    private void refreshTable() {
        tableModel.setRowCount(0);
        Collection<GameObject> objects = csvManager.getAll();  // Используем DirectCSVManager

        for (GameObject obj : objects) {
            if (obj instanceof Character) {
                Character character = (Character) obj;
                tableModel.addRow(new Object[]{
                    obj.getId(),
                    obj.getName(),
                    String.format("%.2f", obj.getX()),
                    String.format("%.2f", obj.getY()),
                    character.getHealth(),
                    character.getLevel()
                });
            }
        }
        
        // Если таблица пустая, показываем сообщение
        if (tableModel.getRowCount() == 0) {
            tableModel.addRow(new Object[]{"-", "Нет данных", "-", "-", "-", "-"});
        }
    }

    // Обработчик кнопки «Добавить»
    private class AddButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                String id = idField.getText().trim();
                String name = nameField.getText().trim();
                double x = Double.parseDouble(xField.getText().trim());
                double y = Double.parseDouble(yField.getText().trim());
                int health = Integer.parseInt(healthField.getText().trim());
                int level = Integer.parseInt(levelField.getText().trim());

                if (id.isEmpty() || name.isEmpty()) {
                    showError("Заполните ID и имя!");
                    return;
                }

                GameObject newObject = new Character(id, name, x, y, health, level);
                csvManager.put(newObject);  // Используем DirectCSVManager
                showInfo("Объект добавлен: " + name);
                clearFields();
                refreshTable();
            } catch (NumberFormatException ex) {
                showError("Введите корректные числовые значения!");
            } catch (IllegalArgumentException ex) {
                showError(ex.getMessage());
            }
        }
    }

    // Обработчик кнопки «Получить»
    private class GetButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String id = idField.getText().trim();
            if (id.isEmpty()) {
                showError("Введите ID объекта!");
                return;
            }

            GameObject obj = csvManager.get(id);  // Используем DirectCSVManager
            if (obj != null) {
                if (obj instanceof Character) {
                    Character character = (Character) obj;
                    showInfo(String.format(
                        "Найден объект:\n" +
                        "ID: %s\n" +
                        "Имя: %s\n" +
                        "Координаты: (%.2f, %.2f)\n" +
                        "Здоровье: %d\n" +
                        "Уровень: %d",
                        obj.getId(), obj.getName(), obj.getX(), obj.getY(), 
                        character.getHealth(), character.getLevel()));
                }
            } else {
                showInfo("Объект с ID " + id + " не найден!");
            }
        }
    }

    // Обработчик кнопки «Обновить»
    private class UpdateButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                String id = idField.getText().trim();
                String name = nameField.getText().trim();
                double x = Double.parseDouble(xField.getText().trim());
                double y = Double.parseDouble(yField.getText().trim());
                int health = Integer.parseInt(healthField.getText().trim());
                int level = Integer.parseInt(levelField.getText().trim());

                if (id.isEmpty()) {
                    showError("Введите ID объекта для обновления!");
                    return;
                }

                // Сначала проверяем, существует ли объект
                GameObject existingObj = csvManager.get(id);
                if (existingObj == null) {
                    showError("Объект с ID " + id + " не найден!");
                    return;
                }

                // Создаем обновленный объект
                GameObject updatedObject = new Character(id, name, x, y, health, level);
                csvManager.update(updatedObject);  // Используем DirectCSVManager
                showInfo("Объект с ID " + id + " обновлен");
                refreshTable();
            } catch (NumberFormatException ex) {
                showError("Введите корректные числовые значения!");
            } catch (IllegalArgumentException ex) {
                showError(ex.getMessage());
            }
        }
    }

    // Обработчик кнопки «Удалить»
    private class RemoveButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String id = idField.getText().trim();
            if (id.isEmpty()) {
                showError("Введите ID объекта!");
                return;
            }

            // Проверяем, существует ли объект
            GameObject obj = csvManager.get(id);
            if (obj == null) {
                showInfo("Объект с ID " + id + " не найден!");
                return;
            }

            csvManager.remove(id);  // Используем DirectCSVManager
            showInfo("Объект с ID " + id + " удалён");
            clearFields();
            refreshTable();
        }
    }

    // Вспомогательные методы
    private void clearFields() {
        idField.setText("");
        nameField.setText("");
        xField.setText("0.0");
        yField.setText("0.0");
        healthField.setText("100");
        levelField.setText("1");
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Ошибка", JOptionPane.ERROR_MESSAGE);
    }

    private void showInfo(String message) {
        JOptionPane.showMessageDialog(this, message, "Информация", JOptionPane.INFORMATION_MESSAGE);
    }

    public static void main(String[] args) {
        // Создаем DirectCSVManager вместо IdentityMap
        DirectCSVManager csvManager = new DirectCSVManager();
        
        javax.swing.SwingUtilities.invokeLater(() -> {
            GameObjectGUI1 gui = new GameObjectGUI1(csvManager);
            gui.setVisible(true);
        });
    }
}