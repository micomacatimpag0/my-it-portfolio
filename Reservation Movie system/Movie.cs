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

namespace WindowsFormsApp1
{
    public partial class Movie : Form
    {
        public Movie()
        {
            InitializeComponent();
        }
        string from;
        string to;
        string Slect;

        string Myconnection2 = "Server=localhost;username=root;";
        public void Loadmovie()
        {
            MySqlConnection connection = new MySqlConnection(Myconnection2);
            connection.Open();
            string qry = "Select * From reservation_sytem.movie";
            MySqlCommand cmd = new MySqlCommand(qry, connection);
            MySqlDataReader reader = cmd.ExecuteReader();
            DataTable dt = new DataTable();
            dt.Load(reader);
            dataGridView1.DataSource = dt;
            connection.Close();
        }
   
        public void loadcenima()
        {
            MySqlConnection connection = new MySqlConnection(Myconnection2);
            connection.Open();
            string qry = "Select * From reservation_sytem.cinema";
            MySqlCommand cmd = new MySqlCommand(qry, connection);
            MySqlDataReader reader = cmd.ExecuteReader();
            DataTable dt = new DataTable();
            dt.Load(reader);
            dataGridView2.DataSource = dt;
            connection.Close();
        }
        private void textBox1_TextChanged(object sender, EventArgs e)
        {

        }

        private void label6_Click(object sender, EventArgs e)
        {

        }
  
 
        private void Movie_Load(object sender, EventArgs e)
        {
            textBox1.TextChanged += textBox2_TextChanged;
            textBox2.TextChanged += textBox2_TextChanged;
            textBox3.TextChanged += textBox2_TextChanged;
            textBox5.TextChanged += textBox2_TextChanged;
            textBox6.TextChanged += textBox2_TextChanged;
            textBox7.TextChanged += textBox2_TextChanged;
            textBox8.TextChanged += textBox2_TextChanged;
            richTextBox1.TextChanged += textBox2_TextChanged;
            panel1.Size = new Size(38, 23);
            panel1.Location = new Point(294, 301);
            Loadmovie();
            loadcenima();
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
        private void View_Click(object sender, EventArgs e)
        {
            Loadmovie();
        }
        public void clrtext()
        {
            textBox1.Text = "";
            textBox2.Text = "";
            textBox3.Text = "";
            textBox5.Text = "";
            textBox6.Text = "";
            textBox7.Text = "";
            textBox8.Text = "";
            richTextBox1.Text = "";
        }
        private void button2_Click(object sender, EventArgs e)
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
                MessageBox.Show("Cannot Insert.Movie ID already exist in the DataGridView.");
            }
            else
            {
                DialogResult msg = MessageBox.Show("Do want to insert the Data?", "\n", MessageBoxButtons.YesNo);
                switch (msg)
                {
                    case DialogResult.Yes:
                        MySqlConnection connection = new MySqlConnection(Myconnection2);
                        connection.Open();
                        string qry = "insert into reservation_sytem.movie(`Movie_ID`, `Movie_Name`, `Movie_Rated`, `Movie_Description`, `Genre`, `Price`, `Cinema_Id`, `Duration`) " + "Values('" + textBox1.Text + "','" + textBox2.Text + "','" + textBox3.Text + "','" + richTextBox1.Text + "','" + textBox5.Text + "','" + textBox6.Text + "','" + textBox7.Text + "','" + textBox8.Text + "');";
                        MySqlCommand cmdd = new MySqlCommand(qry, connection);
                        cmdd.ExecuteNonQuery();
                        cmdd.Dispose();
                        connection.Close();
                        MessageBox.Show("Insert Succes");
                        Loadmovie();
                        autoncrmnt();
                        break;
                    case DialogResult.No:
                        break;
                }
             
            }

        }

        private void button3_Click(object sender, EventArgs e)
        {
            DialogResult msg = MessageBox.Show("Do Want to update the Data?", "\n", MessageBoxButtons.YesNo);
            switch (msg)
            {
                case DialogResult.Yes:

                    MySqlConnection connection = new MySqlConnection(Myconnection2);
                    connection.Open();
                    string qry = "update reservation_sytem.movie set Movie_Name='" + textBox2.Text + "',Movie_Rated='" + textBox3.Text + "',Movie_Description='" + richTextBox1.Text + "',Genre='" + textBox5.Text + "',Price='" + textBox6.Text + "',Cinema_Id='" + textBox7.Text + "',Duration='" + textBox8.Text + "' where Movie_ID='" + textBox1.Text + "';";
                    MySqlCommand cmdd = new MySqlCommand(qry, connection);
                    cmdd.ExecuteNonQuery();
                    cmdd.Dispose();
                    connection.Close();
                    MessageBox.Show("Update Succes");
                    Loadmovie();
                    autoncrmnt();
                    break;

                case DialogResult.No:
                    break;

            }
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
                richTextBox1.Text = row.Cells[3].Value.ToString();
                textBox5.Text = row.Cells[4].Value.ToString();
                textBox6.Text = row.Cells[5].Value.ToString();
                textBox7.Text = row.Cells[6].Value.ToString();
                textBox8.Text = row.Cells[7].Value.ToString();
            }
            else if (e.RowIndex == 0)
            {
                dataGridView1.Enabled = false;
            }
        }

        private void button1_Click(object sender, EventArgs e)
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

        private void button4_Click(object sender, EventArgs e)
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
                        string qry = "Delete from reservation_sytem.movie where Movie_ID='" + textBox1.Text + "';";
                        MySqlCommand cmdd = new MySqlCommand(qry, connection);
                        cmdd.ExecuteNonQuery();
                        cmdd.Dispose();
                        connection.Close();
                        MessageBox.Show("Deleted successfully.");
                        Loadmovie();
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

        private void textBox13_TextChanged(object sender, EventArgs e)
        {
            MySqlConnection connection = new MySqlConnection(Myconnection2);
            connection.Open();
            string qry = "Select * From reservation_sytem.movie Where Movie_Name like'"+textBox13.Text+"%';";
            MySqlCommand cmd = new MySqlCommand(qry, connection);
            MySqlDataReader reader = cmd.ExecuteReader();
            DataTable dt = new DataTable();
            dt.Load(reader);
            dataGridView1.DataSource = dt;
            connection.Close();
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

        private void dataGridView2_CellContentClick(object sender, DataGridViewCellEventArgs e)
        {

        }

        private void label11_Click(object sender, EventArgs e)
        {

        }

        private void panel1_Paint(object sender, PaintEventArgs e)
        {

        }

        private void panel1_MouseClick(object sender, MouseEventArgs e)
        {
            panel1.Size = new Size(249, 89);
            panel1.Location = new Point(134, 330);
        }

        private void button8_Click(object sender, EventArgs e)
        {
            from = dateTimePicker3.Text;
            to = dateTimePicker4.Text;
            textBox8.Text = "From:" + from + " - To:" + to;
        }

        private void button9_Click(object sender, EventArgs e)
        {
            panel1.Size = new Size(38, 23);
            panel1.Location = new Point(294, 301);
        }
  
        private void textBox2_TextChanged(object sender, EventArgs e)
        {
            bool allTextboxesEmpty = string.IsNullOrEmpty(textBox1.Text) ||
                               string.IsNullOrEmpty(textBox2.Text) || string.IsNullOrEmpty(textBox3.Text) || string.IsNullOrEmpty(textBox5.Text) || string.IsNullOrEmpty(textBox6.Text) || string.IsNullOrEmpty(textBox7.Text) || string.IsNullOrEmpty(textBox8.Text) || string.IsNullOrEmpty(richTextBox1.Text);
            button2.Enabled = !allTextboxesEmpty;
        }

        private void textBox3_TextChanged(object sender, EventArgs e)
        {

        }

        private void textBox5_TextChanged(object sender, EventArgs e)
        {

        }

        private void textBox6_TextChanged(object sender, EventArgs e)
        {

        }

        private void textBox7_TextChanged(object sender, EventArgs e)
        {

        }

        private void textBox8_TextChanged(object sender, EventArgs e)
        {

        }
    }
}
