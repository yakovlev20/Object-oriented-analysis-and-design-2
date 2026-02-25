using System;
using System.Windows.Forms;

namespace PatternGUI
{
    static class Program
    {
        [STAThread]
        static void Main()
        {
            Application.EnableVisualStyles();
            Application.SetCompatibleTextRenderingDefault(false);
            Application.Run(new MainForm());
        }
    }

    public class MainForm : Form
    {
        private Button button;
        private Label label;

        public MainForm()
        {
            // Настройки формы
            this.Text = "Простой паттерн GUI";
            this.Width = 400;
            this.Height = 200;

            // Создаем кнопку
            button = new Button();
            button.Text = "Показать сообщение";
            button.Location = new System.Drawing.Point(120, 50);
            button.Click += Button_Click;

            // Создаем метку
            label = new Label();
            label.Text = "";
            label.Location = new System.Drawing.Point(100, 100);
            label.AutoSize = true;

            // Добавляем элементы на форму
            this.Controls.Add(button);
            this.Controls.Add(label);
        }

        private void Button_Click(object sender, EventArgs e)
        {
            label.Text = "Паттерн успешно реализован!";
        }
    }
}