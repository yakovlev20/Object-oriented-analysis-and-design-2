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
    private JTextField idField, nameField;
    private JButton addButton, getButton, removeButton, refreshButton;

    public GameObjectGUI(IdentityMap identityMap) {
        this.identityMap = identityMap;
        initializeGUI();
    }

    private void initializeGUI() {
        setTitle("Управление игровыми объектами");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 400);
        setLocationRelativeTo(null);

        // Создаём панель для ввода данных
        JPanel inputPanel = new JPanel(new GridLayout(3, 2, 5, 5));
        inputPanel.setBorder(BorderFactory.createTitledBorder("Добавить/Получить объект"));

        inputPanel.add(new JLabel("ID:"));
        idField = new JTextField();
        inputPanel.add(idField);

        inputPanel.add(new JLabel("Имя:"));
        nameField = new JTextField();
        inputPanel.add(nameField);

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
        tableModel = new DefaultTableModel(new Object[]{"ID", "Имя"}, 0);
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
            tableModel.addRow(new Object[]{obj.getId(), obj.getName()});
        }
    }

    // Обработчик для кнопки "Добавить"
    private class AddButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String id = idField.getText().trim();
            String name = nameField.getText().trim();

            if (id.isEmpty() || name.isEmpty()) {
                JOptionPane.showMessageDialog(GameObjectGUI.this,
                    "Заполните все поля!", "Ошибка", JOptionPane.ERROR_MESSAGE);
                return;
            }

            GameObject newObject = new Character(id, name);
            identityMap.add(newObject);

            JOptionPane.showMessageDialog(GameObjectGUI.this,
                "Объект добавлен: " + name);

            idField.setText("");
            nameField.setText("");
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
                JOptionPane.showMessageDialog(GameObjectGUI.this,
                    "Найден объект: " + obj.getName());
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
}
