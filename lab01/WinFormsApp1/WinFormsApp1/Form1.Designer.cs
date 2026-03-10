namespace WinFormsApp1
{
    partial class Form1
    {
        private System.Windows.Forms.ComboBox comboBoxCharacterType;
        private System.Windows.Forms.Button buttonCreate;
        private System.Windows.Forms.TextBox textBoxInfo;

        /// <summary>
        ///  Required designer variable.
        /// </summary>
        private System.ComponentModel.IContainer components = null;

        /// <summary>
        ///  Clean up any resources being used.
        /// </summary>
        /// <param name="disposing">true if managed resources should be disposed; otherwise, false.</param>
        protected override void Dispose(bool disposing)
        {
            if (disposing && (components != null))
            {
                components.Dispose();
            }
            base.Dispose(disposing);
        }

        #region Windows Form Designer generated code

        /// <summary>
        ///  Required method for Designer support - do not modify
        ///  the contents of this method with the code editor.
        /// </summary>
        private void InitializeComponent()
        {
            this.comboBoxCharacterType = new System.Windows.Forms.ComboBox();
            this.buttonCreate = new System.Windows.Forms.Button();
            this.textBoxInfo = new System.Windows.Forms.TextBox();

            // Настройка ComboBox
            this.comboBoxCharacterType.Location = new System.Drawing.Point(20, 20);
            this.comboBoxCharacterType.Size = new System.Drawing.Size(100, 40);
            this.comboBoxCharacterType.DropDownStyle = System.Windows.Forms.ComboBoxStyle.DropDownList;

            // Настройка Button
            this.buttonCreate.Location = new System.Drawing.Point(200, 20);
            this.buttonCreate.Size = new System.Drawing.Size(100, 30);
            this.buttonCreate.Text = "Создать";

            // Настройка TextBox
            this.textBoxInfo.Location = new System.Drawing.Point(20, 70);
            this.textBoxInfo.Size = new System.Drawing.Size(500, 400);
            this.textBoxInfo.Multiline = true;
            this.textBoxInfo.ScrollBars = System.Windows.Forms.ScrollBars.Vertical;

            // Добавляем элементы на форму
            this.Controls.Add(this.comboBoxCharacterType);
            this.Controls.Add(this.buttonCreate);
            this.Controls.Add(this.textBoxInfo);

            // Настраиваем свойства формы
            this.ClientSize = new System.Drawing.Size(900, 700);
            this.Text = "Создание персонажей";

            // Обработчик события загрузки формы
            this.Load += new System.EventHandler(this.Form1_Load);
        }
        #endregion

        private void Form1_Load(object sender, EventArgs e)
        {
            // Заполняем ComboBox вариантами
            comboBoxCharacterType.Items.AddRange(new string[] { "Питомец", "NPC", "Босс" });
            comboBoxCharacterType.SelectedIndex = 0;

            // Связываем обработчик кнопки
            buttonCreate.Click += new EventHandler(this.ButtonCreate_Click);
        }
    }
}
