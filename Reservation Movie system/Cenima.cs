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
    public partial class Cenima : Form
    {
        public Cenima()
        {
            InitializeComponent();
        }

        string Myconnection2 = "Server=localhost;username=root;";
        public void loadcinema()
        {
            MySqlConnection connection = new MySqlConnection(Myconnection2);
            connection.Open();
            string qry = "Select * From reservation_sytem.cinema";
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
                MessageBox.Show("Cannot Insert. Cenima ID already exist in the DataGridView.");
            }
            else
            {
                DialogResult msg = MessageBox.Show("Do want to insert the Data?", "\n", MessageBoxButtons.YesNo);
                switch (msg)
                {
                    case DialogResult.Yes:
                        MySqlConnection connection = new MySqlConnection(Myconnection2);
                        connection.Open();
                        string qry = "insert into reservation_sytem.cinema(`Cinema_ID`, `Number_of_Seats`, `Date_of_Show`, `Time_of_Show`, `Paid_seats`, `Avialable_Seats`)" + "Values('" + textBox1.Text + "','" + textBox3.Text + "','" + textBox5.Text + "','" + textBox6.Text + "','" + textBox4.Text + "','" + textBox2.Text + "');";
                        MySqlCommand cmdd = new MySqlCommand(qry, connection);
                        cmdd.ExecuteNonQuery();
                        cmdd.Dispose();
                        connection.Close();
                        MessageBox.Show("Insert Succes");
                        loadcinema();
                        autoncrmnt();
                        break;
                    case DialogResult.No:
                        break;
                }

            }

        }
      
        private void button2_Click(object sender, EventArgs e)
        {
            DialogResult msg = MessageBox.Show("Update all the data?", "\n", MessageBoxButtons.YesNo);
            switch (msg)
            {
                case DialogResult.Yes:

                    MySqlConnection connection = new MySqlConnection(Myconnection2);
                    connection.Open();
                    string qry = "update reservation_sytem.cinema set Number_of_Seats='" + textBox3.Text + "',Date_of_Show='" + textBox5.Text + "',Time_of_Show='" + textBox6.Text + "' ,Paid_seats='" + textBox4.Text + "',Avialable_Seats='" + textBox2.Text + "'where Cinema_ID='" + textBox1.Text + "';";
                    MySqlCommand cmdd = new MySqlCommand(qry, connection);
                    cmdd.ExecuteNonQuery();
                    cmdd.Dispose();
                    connection.Close();
                    MessageBox.Show("Update Succes");
                    loadcinema();
                    autoncrmnt();
                    break;

                case DialogResult.No:
                    break;
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
                        string qry = "Delete from reservation_sytem.cinema where Cinema_ID='" + textBox1.Text + "';";
                        MySqlCommand cmdd = new MySqlCommand(qry, connection);
                        cmdd.ExecuteNonQuery();
                        cmdd.Dispose();
                        connection.Close();
                        MessageBox.Show("Update Succes");
                        loadcinema();
                        autoncrmnt();
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

        private void textBox8_TextChanged(object sender, EventArgs e)
        {
            MySqlConnection connection = new MySqlConnection(Myconnection2);
            connection.Open();
            string qry = "Select * From reservation_sytem.cinema Where Cinema_ID like'" + textBox8.Text + "%';";
            MySqlCommand cmd = new MySqlCommand(qry, connection);
            MySqlDataReader reader = cmd.ExecuteReader();
            DataTable dt = new DataTable();
            dt.Load(reader);
            dataGridView1.DataSource = dt;
            connection.Close();
        }
        public void clrtext()
        {
            textBox1.Text = "";
            textBox3.Text = "";
            textBox5.Text = "";
            textBox6.Text = "";
        }
        private void button5_Click(object sender, EventArgs e)
        {
            DialogResult msg = MessageBox.Show("Clear all text?", "\n", MessageBoxButtons.YesNo);
            switch (msg)
            {
                case DialogResult.Yes:
                    clrtext();
                    break;

                case DialogResult.No:
                    break;
            }
        }
        private void Cenima_Load(object sender, EventArgs e)
        {
            textBox1.TextChanged += textBox1_TextChanged;
            textBox3.TextChanged += textBox1_TextChanged;
            textBox5.TextChanged += textBox1_TextChanged;
            textBox6.TextChanged += textBox1_TextChanged;
            loadcinema();
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
        private void button4_Click(object sender, EventArgs e)
        {
            DialogResult msg = MessageBox.Show("Do Want leave? Any unsaved maybe discarded", "\n", MessageBoxButtons.YesNo);
            switch (msg)
            {
                case DialogResult.Yes:
                    Admin opn = new Admin();
                    opn.Show();
                    this.Hide();
                    break;

                case DialogResult.No:
                    break;

            }
        }

        private void dataGridView1_CellClick(object sender, DataGridViewCellEventArgs e)
        {
            if (e.RowIndex >= 0)
            {
                DataGridViewRow row = dataGridView1.Rows[e.RowIndex];
                textBox1.Text = row.Cells[0].Value.ToString();
                textBox3.Text = row.Cells[1].Value.ToString();
                textBox5.Text = row.Cells[2].Value.ToString();
                textBox6.Text = row.Cells[3].Value.ToString();
            }
            else if (e.RowIndex == 0)
            {
                dataGridView1.Enabled = false;
            }
        }

        private void textBox5_TextChanged(object sender, EventArgs e)
        {
  
        }

        private void textBox6_TextChanged(object sender, EventArgs e)
        {
   
        }

        private void dateTimePicker1_ValueChanged(object sender, EventArgs e)
        {
           
        }

        private void dateTimePicker2_ValueChanged(object sender, EventArgs e)
        {

        }

        private void we(object sender, EventArgs e)
        {
            textBox2.Text = textBox3.Text;
        }

        private void button6_Click(object sender, EventArgs e)
        {
            textBox5.Text = dateTimePicker1.Text;
        }

        private void button7_Click(object sender, EventArgs e)
        {
            textBox6.Text = dateTimePicker2.Text;
        }

        private void textBox1_TextChanged(object sender, EventArgs e)
        {
            bool allTextboxesEmpty = string.IsNullOrEmpty(textBox1.Text) || string.IsNullOrEmpty(textBox3.Text) || string.IsNullOrEmpty(textBox5.Text) || string.IsNullOrEmpty(textBox6.Text);
            button1.Enabled = !allTextboxesEmpty;
        }
    }
}
