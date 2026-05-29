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
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

public class GameObjectGUI2 extends JFrame {
    private DirectCSVManager csvManager;
    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField idField, nameField, xField, yField, healthField, levelField;
    private JButton addButton, getButton, removeButton, refreshButton, updateButton;

    public GameObjectGUI2(DirectCSVManager csvManager) {
        this.csvManager = csvManager;
        initializeGUI();
    }

    private void initializeGUI() {
        setTitle("Управление игровыми объектами (Direct CSV)");
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
        updateButton = new JButton("Обновить");
        removeButton = new JButton("Удалить");
        refreshButton = new JButton("Обновить таблицу");

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(addButton);
        buttonPanel.add(getButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(removeButton);
        buttonPanel.add(refreshButton);

        // Таблица для отображения
        tableModel = new DefaultTableModel(
            new Object[]{"ID", "Имя", "X", "Y", "Здоровье", "Уровень"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        table = new JTable(tableModel);
        table.setPreferredScrollableViewportSize(new Dimension(800, 300));
        table.setFillsViewportHeight(true);
        
        // Настройка ширины столбцов
        table.getColumnModel().getColumn(0).setPreferredWidth(50);
        table.getColumnModel().getColumn(1).setPreferredWidth(150);
        table.getColumnModel().getColumn(2).setPreferredWidth(80);
        table.getColumnModel().getColumn(3).setPreferredWidth(80);
        table.getColumnModel().getColumn(4).setPreferredWidth(100);
        table.getColumnModel().getColumn(5).setPreferredWidth(80);
        
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Список объектов"));

        // Обработчики событий с безопасной обработкой
        addButton.addActionListener(new SafeActionListener(new AddButtonAction()));
        getButton.addActionListener(new SafeActionListener(new GetButtonAction()));
        updateButton.addActionListener(new SafeActionListener(new UpdateButtonAction()));
        removeButton.addActionListener(new SafeActionListener(new RemoveButtonAction()));
        refreshButton.addActionListener(new SafeActionListener(e -> refreshTable()));

        // Компоновка
        JPanel northPanel = new JPanel(new BorderLayout(10, 10));
        northPanel.add(inputPanel, BorderLayout.NORTH);
        northPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        northPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        scrollPane.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createEmptyBorder(0, 10, 10, 10),
            BorderFactory.createTitledBorder("Список объектов")
        ));

        setLayout(new BorderLayout(10, 10));
        add(northPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        // Инициализация таблицы в EDT
        SwingUtilities.invokeLater(this::refreshTable);
    }

    // Обновление таблицы с безопасным выполнением в EDT
    private void refreshTable() {
        SwingUtilities.invokeLater(() -> {
            try {
                tableModel.setRowCount(0);
                Collection<GameObject> objects = csvManager.getAll();

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
                
                if (tableModel.getRowCount() == 0) {
                    tableModel.addRow(new Object[]{"-", "Нет данных", "-", "-", "-", "-"});
                }
            } catch (Exception e) {
                showError("Ошибка при обновлении таблицы: " + e.getMessage());
                e.printStackTrace();
            }
        });
    }

    // Вспомогательный класс для безопасной обработки событий
    private class SafeActionListener implements ActionListener {
        private final ActionListener delegate;
        
        public SafeActionListener(ActionListener delegate) {
            this.delegate = delegate;
        }
        
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                delegate.actionPerformed(e);
            } catch (Exception ex) {
                showError("Ошибка при выполнении действия: " + ex.getMessage());
                ex.printStackTrace();
            }
        }
    }

    // Действия для кнопок
    private class AddButtonAction implements ActionListener {
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
                csvManager.put(newObject);
                showInfo("Объект добавлен: " + name);
                clearFields();
                refreshTable();
            } catch (NumberFormatException ex) {
                showError("Введите корректные числовые значения!");
            } catch (IllegalArgumentException ex) {
                showError(ex.getMessage());
            } catch (Exception ex) {
                showError("Ошибка при добавлении: " + ex.getMessage());
            }
        }
    }

    private class GetButtonAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                String id = idField.getText().trim();
                if (id.isEmpty()) {
                    showError("Введите ID объекта!");
                    return;
                }

                GameObject obj = csvManager.get(id);
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
            } catch (Exception ex) {
                showError("Ошибка при получении объекта: " + ex.getMessage());
            }
        }
    }

    private class UpdateButtonAction implements ActionListener {
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

                GameObject existingObj = csvManager.get(id);
                if (existingObj == null) {
                    showError("Объект с ID " + id + " не найден!");
                    return;
                }

                GameObject updatedObject = new Character(id, name, x, y, health, level);
                csvManager.update(updatedObject);
                showInfo("Объект с ID " + id + " обновлен");
                refreshTable();
            } catch (NumberFormatException ex) {
                showError("Введите корректные числовые значения!");
            } catch (IllegalArgumentException ex) {
                showError(ex.getMessage());
            } catch (Exception ex) {
                showError("Ошибка при обновлении: " + ex.getMessage());
            }
        }
    }

    private class RemoveButtonAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                String id = idField.getText().trim();
                if (id.isEmpty()) {
                    showError("Введите ID объекта!");
                    return;
                }

                GameObject obj = csvManager.get(id);
                if (obj == null) {
                    showInfo("Объект с ID " + id + " не найден!");
                    return;
                }

                csvManager.remove(id);
                showInfo("Объект с ID " + id + " удалён");
                clearFields();
                refreshTable();
            } catch (Exception ex) {
                showError("Ошибка при удалении: " + ex.getMessage());
            }
        }
    }

    // Вспомогательные методы
    private void clearFields() {
        SwingUtilities.invokeLater(() -> {
            idField.setText("");
            nameField.setText("");
            xField.setText("0.0");
            yField.setText("0.0");
            healthField.setText("100");
            levelField.setText("1");
        });
    }

    private void showError(String message) {
        SwingUtilities.invokeLater(() -> 
            JOptionPane.showMessageDialog(this, message, "Ошибка", JOptionPane.ERROR_MESSAGE));
    }

    private void showInfo(String message) {
        SwingUtilities.invokeLater(() -> 
            JOptionPane.showMessageDialog(this, message, "Информация", JOptionPane.INFORMATION_MESSAGE));
    }

    public static void main(String[] args) {
        // Запуск в EDT
        SwingUtilities.invokeLater(() -> {
            try {
                DirectCSVManager csvManager = new DirectCSVManager();
                GameObjectGUI2 gui = new GameObjectGUI2(csvManager);
                gui.setVisible(true);
            } catch (Exception e) {
                System.err.println("Ошибка при запуске приложения:");
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, 
                    "Ошибка при запуске приложения: " + e.getMessage(),
                    "Ошибка", JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}