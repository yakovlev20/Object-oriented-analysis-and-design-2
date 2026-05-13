import javax.swing.JButton;
import javax.swing.JFrame;

public class Main {
    public static void main(String[] args) {
        // Создаем окно
        JFrame frame = new JFrame("Мое приложение");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);
        
        // Добавляем компоненты
        JButton button = new JButton("Нажми меня");
        frame.getContentPane().add(button);
        
        // Делает окно видимым
        frame.setVisible(true);
    }
}