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

public class SimpleGameObjectGUI extends JFrame {
    private SimpleGameObjectManager manager;
    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField idField, nameField;
    private JButton addButton, getButton, removeButton, refreshButton;

    public SimpleGameObjectGUI(SimpleGameObjectManager manager) {
        this.manager = manager;
        initializeGUI();
    }

    private void initializeGUI() {
        setTitle("Управление игровыми объектами (без Identity Map)");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 400);
        setLocationRelativeTo(null);

        JPanel inputPanel = new JPanel(new GridLayout(3, 2, 5, 5));
        inputPanel.setBorder(BorderFactory.createTitledBorder("Добавить/Получить объект"));

        inputPanel.add(new JLabel("ID:"));
        idField = new JTextField();
        inputPanel.add(idField);

        inputPanel.add(new JLabel("Имя:"));
        nameField = new JTextField();
        inputPanel.add(nameField);

        addButton = new JButton("Добавить");
        getButton = new JButton("Получить");
        removeButton = new JButton("Удалить");
        refreshButton = new JButton("Обновить");

        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(addButton);
        buttonPanel.add(getButton);
        buttonPanel.add(removeButton);
        buttonPanel.add(refreshButton);

        tableModel = new DefaultTableModel(new Object[]{"ID", "Имя"}, 0);
        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);

        addButton.addActionListener(new AddButtonListener());
        getButton.addActionListener(new GetButtonListener());
        removeButton.addActionListener(new RemoveButtonListener());
        refreshButton.addActionListener(e -> refreshTable());

        setLayout(new BorderLayout(5, 5));
        add(inputPanel, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.CENTER);
        add(scrollPane, BorderLayout.SOUTH);

        refreshTable();
    }

    private void refreshTable() {
        tableModel.setRowCount(0);
        Collection<GameObject> objects = manager.getAll();

        for (GameObject obj : objects) {
            tableModel.addRow(new Object[]{obj.getId(), obj.getName()});
        }
    }

    private class AddButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String id = idField.getText().trim();
            String name = nameField.getText().trim();

            if (id.isEmpty() || name.isEmpty()) {
                JOptionPane.showMessageDialog(SimpleGameObjectGUI.this,
                    "Заполните все поля!", "Ошибка", JOptionPane.ERROR_MESSAGE);
                return;
            }

            GameObject newObject = new Character(id, name);
            manager.add(newObject);

            JOptionPane.showMessageDialog(SimpleGameObjectGUI.this,
                "Объект добавлен: " + name);

            idField.setText("");
            nameField.setText("");
            refreshTable();
        }
    }

    private class GetButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String id = idField.getText().trim();

            if (id.isEmpty()) {
                JOptionPane.showMessageDialog(SimpleGameObjectGUI.this,
                    "Введите ID объекта!", "Ошибка", JOptionPane.ERROR_MESSAGE);
                return;
            }

            GameObject obj = manager.get(id);

            if (obj != null) {
                JOptionPane.showMessageDialog(SimpleGameObjectGUI.this,
                    "Найден объект: " + obj.getName());
            } else {
                JOptionPane.showMessageDialog(SimpleGameObjectGUI.this,
                    "Объект с ID " + id + " не найден!", "Информация",
                    JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }

    private class RemoveButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String id = idField.getText().trim();

            if (id.isEmpty()) {
                JOptionPane.showMessageDialog(SimpleGameObjectGUI.this,
                    "Введите ID объекта!", "Ошибка", JOptionPane.ERROR_MESSAGE);
                return;
            }

            manager.remove(id);
            JOptionPane.showMessageDialog(SimpleGameObjectGUI.this,
                "Объект с ID " + id + " удалён");
            refreshTable();
        }
    }
}