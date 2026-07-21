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
    public partial class AddCashier : Form
    {
        public AddCashier()
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
        }
        public void loadacc()
        {
            MySqlConnection connection = new MySqlConnection(Myconnection2);
            connection.Open();
            string qry = "Select * From reservation_sytem.account where User  like'" + label8.Text + "%';";
            MySqlCommand cmd = new MySqlCommand(qry, connection);
            MySqlDataReader reader = cmd.ExecuteReader();
            DataTable dt = new DataTable();
            dt.Load(reader);
            dataGridView1.DataSource = dt;
            connection.Close();
        }

        private void AddCashier_Load(object sender, EventArgs e)
        {
            loadacc();
        }

        private void button1_Click(object sender, EventArgs e)
        {
            MySqlConnection connection = new MySqlConnection(Myconnection2);
            connection.Open();
            string qry = "insert into reservation_sytem.account(`Account_ID`, `Name`, `Last_Name`, `Username`, `Password`, `User`) " + "Values('" + textBox1.Text + "','" + textBox2.Text + "','" + textBox3.Text + "','" + textBox4.Text + "','" + textBox5.Text + "','" + comboBox1.Text + "');";
            MySqlCommand cmdd = new MySqlCommand(qry, connection);
            cmdd.ExecuteNonQuery();
            cmdd.Dispose();
            connection.Close();
            MessageBox.Show("Insert Succes");
            loadacc();
        }

        private void button3_Click(object sender, EventArgs e)
        {
            MySqlConnection connection = new MySqlConnection(Myconnection2);
            connection.Open();
            string qry = "Delete from reservation_sytem.account where Account_ID='" + textBox1.Text + "';";
            MySqlCommand cmdd = new MySqlCommand(qry, connection);
            cmdd.ExecuteNonQuery();
            cmdd.Dispose();
            connection.Close();
            MessageBox.Show("Update Succes");
            loadacc();
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
            CashierLobby cashierLobby = new CashierLobby();
            cashierLobby.Show();
            this.Hide();
        }

        private void button5_Click(object sender, EventArgs e)
        {
            Login login = new Login();
            login.Show();
            this.Hide();
        }
    }
}
