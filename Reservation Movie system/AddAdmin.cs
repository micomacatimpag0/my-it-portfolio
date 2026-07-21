using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;
using MySql.Data.MySqlClient;
using static System.Windows.Forms.VisualStyles.VisualStyleElement;

namespace WindowsFormsApp1
{
    public partial class AddAdmin : Form
    {
        public AddAdmin()
        {
            InitializeComponent();
        }
        string Myconnection2 = "Server=localhost;username=root;";
        private void textBox7_TextChanged(object sender, EventArgs e)
        {
            MySqlConnection connection = new MySqlConnection(Myconnection2);
            connection.Open();
            string qry = "Select * From reservation_sytem.account Where Name like'" + textBox7.Text + "%';";
            MySqlCommand cmd = new MySqlCommand(qry, connection);
            MySqlDataReader reader = cmd.ExecuteReader();
            DataTable dt = new DataTable();
            dt.Load(reader);
            dataGridView1.DataSource = dt;
            connection.Close();
        }

        private void dataGridView1_CellContentClick(object sender, DataGridViewCellEventArgs e)
        {

        }

        private void dataGridView1_CellClick(object sender, DataGridViewCellEventArgs e)
        {
            if (e.RowIndex >= 0)
            {
                DataGridViewRow row = dataGridView1.Rows[e.RowIndex];
                textBox1.Text = row.Cells[0].Value.ToString();
                textBox2.Text = row.Cells[1].Value.ToString();
                textBox3.Text = row.Cells[2].Value.ToString();
                textBox4.Text = row.Cells[3].Value.ToString();
                textBox5.Text = row.Cells[4].Value.ToString();
                comboBox1.Text = row.Cells[5].Value.ToString();
              
            }
            else if (e.RowIndex == 0)
            {
                dataGridView1.Enabled = false;
            }
        }
        public void loadacc()
        {
            MySqlConnection connection = new MySqlConnection(Myconnection2);
            connection.Open();
            string qry = "Select * From reservation_sytem.account";
            MySqlCommand cmd = new MySqlCommand(qry, connection);
            MySqlDataReader reader = cmd.ExecuteReader();
            DataTable dt = new DataTable();
            dt.Load(reader);
            dataGridView1.DataSource = dt;
            connection.Close();
        }
        private void button1_Click(object sender, EventArgs e)
        {
            string priorityKey = textBox1.Text;

            // Check if the priority key exists in the DataGridView
            bool keyExists = false;
            foreach (DataGridViewRow row in dataGridView1.Rows)
            {
                if (row.Cells[0].Value != null && row.Cells[0].Value.ToString() == priorityKey)
                {
                    keyExists = true;
                    break;
                }
            }

            // If the key exists, allow deletion
            if (keyExists)
            {
                MessageBox.Show("Cannot Insert. Account ID already exist in the DataGridView.");
            }
            else
            {
                DialogResult msg = MessageBox.Show("Do want to insert the Data?", "\n", MessageBoxButtons.YesNo);
                switch (msg)
                {
                    case DialogResult.Yes:
                        MySqlConnection connection = new MySqlConnection(Myconnection2);
                        connection.Open();
                        string qry = "insert into reservation_sytem.account(`Account_ID`, `Name`, `Last_Name`, `Username`, `Password`, `User`) " + "Values('" + textBox1.Text + "','" + textBox2.Text + "','" + textBox3.Text + "','" + textBox4.Text + "','" + textBox5.Text + "','" + comboBox1.Text + "');";
                        MySqlCommand cmdd = new MySqlCommand(qry, connection);
                        cmdd.ExecuteNonQuery();
                        cmdd.Dispose();
                        connection.Close();
                        MessageBox.Show("Insert Succes");
                        loadacc();
                        break;
                    case DialogResult.No:
                        break;
                }

            }
        }

        private void button3_Click(object sender, EventArgs e)
        {
            string priorityKey = textBox1.Text;

            bool keyExists = false;
            foreach (DataGridViewRow row in dataGridView1.Rows)
            {
                if (row.Cells[0].Value != null && row.Cells[0].Value.ToString() == priorityKey)
                {
                    keyExists = true;
                    break;
                }
            }

            if (keyExists)
            {
                DialogResult msg = MessageBox.Show("Do you want to delete the item?", "\n", MessageBoxButtons.YesNo);
                switch (msg)
                {
                    case DialogResult.Yes:

                        MySqlConnection connection = new MySqlConnection(Myconnection2);
                        connection.Open();
                        string qry = "Delete from reservation_sytem.account where Account_ID='" + textBox1.Text + "';";
                        MySqlCommand cmdd = new MySqlCommand(qry, connection);
                        cmdd.ExecuteNonQuery();
                        cmdd.Dispose();
                        connection.Close();
                        MessageBox.Show("Update Succes");
                        loadacc();
                        break;

                    case DialogResult.No:
                        break;
                }
            }
            else
            {
                MessageBox.Show("Cannot delete priority key. Key does not exist in the DataGridView.");
            }
        }

        private void button2_Click(object sender, EventArgs e)
        {
            MySqlConnection connection = new MySqlConnection(Myconnection2);
            connection.Open();
            string qry = "update reservation_sytem.account set Name='" + textBox2.Text + "',Last_Name='" + textBox3.Text + "',Username='" + textBox4.Text + "',Password='" + textBox5.Text + "',User='" + comboBox1.Text + "' where Account_ID='" + textBox1.Text + "';";
            MySqlCommand cmdd = new MySqlCommand(qry, connection);
            cmdd.ExecuteNonQuery();
            cmdd.Dispose();
            connection.Close();
            MessageBox.Show("Update Succes");
            loadacc();
        }

        private void button4_Click(object sender, EventArgs e)
        {
            Admin admin = new Admin();
            admin.Show();
            this.Hide();
        }

        private void button5_Click(object sender, EventArgs e)
        {
            Login login = new Login(); 
            login.Show();
            this.Hide();
        }

        private void AddAdmin_Load(object sender, EventArgs e)
        {
            textBox1.TextChanged += textBox1_TextChanged;
            textBox2.TextChanged += textBox1_TextChanged;
            textBox3.TextChanged += textBox1_TextChanged;
            textBox4.TextChanged += textBox1_TextChanged;
            textBox5.TextChanged += textBox1_TextChanged;
            comboBox1.TextChanged += textBox1_TextChanged;
            loadacc();
            autoncrmnt();

        }
        private void autoncrmnt()
        {
            int maxNuber = 0;

            foreach (DataGridViewRow row in dataGridView1.Rows)
            {
                int cellValue;
                if (row.Cells[0].Value != null && int.TryParse(row.Cells[0].Value.ToString(), out cellValue))
                {
                    if (cellValue > maxNuber)
                        maxNuber = cellValue;
                }
            }
            int incrementedNumber = maxNuber + 1;
            textBox1.Text = incrementedNumber.ToString();
        }
        private void label8_Click(object sender, EventArgs e)
        {

        }

        private void textBox1_TextChanged(object sender, EventArgs e)
        {
            bool allTextboxesEmpty = string.IsNullOrEmpty(textBox1.Text) || string.IsNullOrEmpty(textBox2.Text) || string.IsNullOrEmpty(textBox3.Text) || string.IsNullOrEmpty(textBox4.Text) || string.IsNullOrEmpty(textBox5.Text) || string.IsNullOrEmpty(comboBox1.Text);
            button1.Enabled = !allTextboxesEmpty;
        }
    }
}
