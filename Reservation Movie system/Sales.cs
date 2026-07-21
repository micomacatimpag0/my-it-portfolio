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
    public partial class Sales : Form
    {
        public Sales()
        {
            InitializeComponent();
        }
        string Myconnection2 = "Server=localhost;username=root;";

        public void Loadsales()
        {
            MySqlConnection connection = new MySqlConnection(Myconnection2);
            connection.Open();
            string qry = "Select * From reservation_sytem.pos";
            MySqlCommand cmd = new MySqlCommand(qry, connection);
            MySqlDataReader reader = cmd.ExecuteReader();
            DataTable dt = new DataTable();
            dt.Load(reader);
            dataGridView1.DataSource = dt;
            connection.Close();
        }
        public void loadticket()
        {
            MySqlConnection connection = new MySqlConnection(Myconnection2);
            connection.Open();
            string qry = "Select * From reservation_sytem.ticket";
            MySqlCommand cmd = new MySqlCommand(qry, connection);
            MySqlDataReader reader = cmd.ExecuteReader();
            DataTable dt = new DataTable();
            dt.Load(reader);
            dataGridView2.DataSource = dt;
            connection.Close();
        }

        private void Sales_Load(object sender, EventArgs e)
        {
            Loadsales();
            loadticket();
        }


        private void dateTimePicker1_ValueChanged(object sender, EventArgs e)
        {
            MySqlConnection connection = new MySqlConnection(Myconnection2);
            connection.Open();
            string qry = "Select * From reservation_sytem.ticket Where Date like'" + dateTimePicker1.Text + "%';";
            MySqlCommand cmd = new MySqlCommand(qry, connection);
            MySqlDataReader reader = cmd.ExecuteReader();
            DataTable dt = new DataTable();
            dt.Load(reader);
            dataGridView2.DataSource = dt;
            connection.Close();
        }

        private void dateTimePicker2_ValueChanged(object sender, EventArgs e)
        {
            MySqlConnection connection = new MySqlConnection(Myconnection2);
            connection.Open();
            string qry = "Select * From reservation_sytem.pos Where Date like'" + dateTimePicker2.Text + "%';";
            MySqlCommand cmd = new MySqlCommand(qry, connection);
            MySqlDataReader reader = cmd.ExecuteReader();
            DataTable dt = new DataTable();
            dt.Load(reader);
            dataGridView1.DataSource = dt;
            connection.Close();
        }

        private void button1_Click(object sender, EventArgs e)
        {
            DialogResult msg = MessageBox.Show("Do Want leave?", "\n", MessageBoxButtons.YesNo);
            switch (msg)
            {
                case DialogResult.Yes:
                    CashierLobby opn = new CashierLobby();
                    opn.Show();
                    this.Hide();
                    break;

                case DialogResult.No:
                    break;

            }
        }
    }
}
