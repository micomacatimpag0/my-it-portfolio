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
    public partial class POS : Form
    {
        public POS()
        {
            InitializeComponent();
        }
        string Myconnection2 = "Server=localhost;username=root;";
        private void POS_Load(object sender, EventArgs e)
        {
            textBox1.TextChanged += textBox1_TextChanged;
            textBox2.TextChanged += textBox1_TextChanged;
            textBox6.TextChanged += textBox1_TextChanged;
            textBox5.TextChanged += textBox1_TextChanged;
            textBox4.TextChanged += textBox1_TextChanged;
            loadticket();
            loadsales();
            autoncrmnt();
        }
        private void loadsales()
        {
            MySqlConnection connection = new MySqlConnection(Myconnection2);
            connection.Open();
            string qry = "Select * From reservation_sytem.pos";
            MySqlCommand cmd = new MySqlCommand(qry, connection);
            MySqlDataReader reader = cmd.ExecuteReader();
            DataTable dt = new DataTable();
            dt.Load(reader);
            dataGridView2.DataSource = dt;
            connection.Close();
        }
        private void loadticket()
        {
            MySqlConnection connection = new MySqlConnection(Myconnection2);
            connection.Open();
            string qry = "Select * From reservation_sytem.ticket";
            MySqlCommand cmd = new MySqlCommand(qry, connection);
            MySqlDataReader reader = cmd.ExecuteReader();
            DataTable dt = new DataTable();
            dt.Load(reader);
            dataGridView1.DataSource = dt;
            connection.Close();
        }
        private void dataGridView1_CellClick(object sender, DataGridViewCellEventArgs e)
        {
            if (e.RowIndex >= 0)
            {
                DataGridViewRow row = dataGridView1.Rows[e.RowIndex];
                textBox2.Text = row.Cells[8].Value.ToString();
                textBox6.Text = row.Cells[7].Value.ToString();

            }

        }

        private void countdata()
        {
            int count = 0;
            int sum = 0;
            int columnIndex = 5;
            foreach (DataGridViewRow row in dataGridView1.Rows)
            {
                if (row.Cells[columnIndex].Value != null)
                {
                    int cellvalue;
                    if (int.TryParse(row.Cells[columnIndex].Value.ToString(), out cellvalue))
                    {
                        count++;
                        sum += cellvalue;
                    }
                }
            }
            textBox5.Text = count.ToString();
            textBox4.Text = sum.ToString();
        }

        private void textBox2_TextChanged(object sender, EventArgs e)
        {
        
        }

        private void button1_Click(object sender, EventArgs e)
        {
            string priorityKey = textBox1.Text;

            // Check if the priority key exists in the DataGridView
            bool keyExists = false;
            foreach (DataGridViewRow row in dataGridView2.Rows)
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
                MessageBox.Show("Cannot Insert.Sales ID already exist in the DataGridView.");
            }
            else
            {
                DialogResult msg = MessageBox.Show("Do want to insert the Data?", "\n", MessageBoxButtons.YesNo);
                switch (msg)
                {
                    case DialogResult.Yes:
                        MySqlConnection connection = new MySqlConnection(Myconnection2);
                        connection.Open();
                        string qry = "insert into reservation_sytem.pos(`Sales_ID`, `Cinema_ID`, `Date`, `Total_Income`, `Total_Ticket`)" + "Values('" + textBox1.Text + "','" + textBox6.Text + "','" + textBox2.Text + "','" + textBox4.Text + "','" + textBox5.Text + "');";
                        MySqlCommand cmdd = new MySqlCommand(qry, connection);
                        cmdd.ExecuteNonQuery();
                        cmdd.Dispose();
                        connection.Close();
                        MessageBox.Show("Insert Succes");
                        loadsales();
                        autoncrmnt();
                        break;
                    case DialogResult.No:
                        break;
                }

            }

        }

        private void button2_Click(object sender, EventArgs e)
        {
            countdata();
        }


        private void textBox3_TextChanged(object sender, EventArgs e)
        {
            textBox6.Text = textBox3.Text;
            MySqlConnection connection = new MySqlConnection(Myconnection2);
            connection.Open();
            string qry = "Select * From reservation_sytem.ticket where Cinema_ID like'" + textBox3.Text+ "%' and Date like'" + dateTimePicker1.Text+ "%';";
            MySqlCommand cmd = new MySqlCommand(qry, connection);
            MySqlDataReader reader = cmd.ExecuteReader();
            DataTable dt = new DataTable();
            dt.Load(reader);
            dataGridView1.DataSource = dt;
            connection.Close();

        }
 

        private void label4_Click(object sender, EventArgs e)
        {

        }
        private void autoncrmnt()
        {
            int maxNuber = 0;

            foreach (DataGridViewRow row in dataGridView2.Rows)
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
          
            
            
            DialogResult msg = MessageBox.Show("Do Want to go back? Items maybe discarded", "\n", MessageBoxButtons.YesNo);
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

        private void dateTimePicker1_ValueChanged(object sender, EventArgs e)
        {
            textBox2.Text = dateTimePicker1.Text;
        }

        public void clrtxt()
        {
            textBox1.Text = "";
            textBox2.Text = "";
            textBox6.Text = "";
            textBox5.Text = "";
            textBox4.Text = "";
        }


        private void dateTimePicker2_ValueChanged(object sender, EventArgs e)
        {
            MySqlConnection connection = new MySqlConnection(Myconnection2);
            connection.Open();
            string qry = "Select * From reservation_sytem.pos where Date like'" + dateTimePicker2.Text+ "%'";
            MySqlCommand cmd = new MySqlCommand(qry, connection);
            MySqlDataReader reader = cmd.ExecuteReader();
            DataTable dt = new DataTable();
            dt.Load(reader);
            dataGridView2.DataSource = dt;
            connection.Close();
        }

        private void textBox1_TextChanged(object sender, EventArgs e)
        {
            bool allTextboxesEmpty = string.IsNullOrEmpty(textBox1.Text) || string.IsNullOrEmpty(textBox2.Text) || string.IsNullOrEmpty(textBox6.Text) || string.IsNullOrEmpty(textBox5.Text) || string.IsNullOrEmpty(textBox4.Text);
            button1.Enabled = !allTextboxesEmpty;
        }

        private void button3_Click(object sender, EventArgs e)
        {
            MySqlConnection connection = new MySqlConnection(Myconnection2);
            connection.Open();
            string qry = "update reservation_sytem.pos set Cinema_ID='" + textBox6.Text + "',Date='" + textBox2.Text + "',Total_Income='" + textBox4.Text + "',Total_Ticket='" + textBox5.Text + "'where Sales_ID='" + textBox1.Text + "';";
            MySqlCommand cmdd = new MySqlCommand(qry, connection);
            cmdd.ExecuteNonQuery();
            cmdd.Dispose();
            connection.Close();
            MessageBox.Show("Update Succes");
            loadsales();
            autoncrmnt();
        }

        private void button5_Click(object sender, EventArgs e)
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
                DialogResult msg = MessageBox.Show("Do you want to delete the item?", "\n", MessageBoxButtons.YesNo);
                switch (msg)
                {
                    case DialogResult.Yes:
                        MySqlConnection connection = new MySqlConnection(Myconnection2);
                        connection.Open();
                        string qry = "Delete from reservation_sytem.pos where Sales_ID ='" + textBox1.Text + "';";
                        MySqlCommand cmdd = new MySqlCommand(qry, connection);
                        cmdd.ExecuteNonQuery();
                        cmdd.Dispose();
                        connection.Close();
                        MessageBox.Show("Deleted successfully.");
                        loadsales();
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

        private void button6_Click(object sender, EventArgs e)
        {
            DialogResult msg = MessageBox.Show("Clear all text?", "\n", MessageBoxButtons.YesNo);
            switch (msg)
            {
                case DialogResult.Yes:
                    clrtxt();
                    break;

                case DialogResult.No:
                    break;
            }
           
        }

        private void dataGridView2_CellClick(object sender, DataGridViewCellEventArgs e)
        {
            if (e.RowIndex >= 0)
            {
                DataGridViewRow row = dataGridView2.Rows[e.RowIndex];
                textBox1.Text = row.Cells[0].Value.ToString();
                textBox2.Text = row.Cells[2].Value.ToString();
                textBox6.Text = row.Cells[1].Value.ToString();
                textBox5.Text = row.Cells[4].Value.ToString();
                textBox4.Text = row.Cells[3].Value.ToString();
               
            }
            else if (e.RowIndex == 0)
            {
                dataGridView2.Enabled = false;
            }
        }
    }
}
