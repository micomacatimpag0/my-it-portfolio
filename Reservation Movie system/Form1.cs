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
    public partial class Login : Form
    {
        public Login()
        {
            InitializeComponent();
        }
        string Myconnection2 = "Server=localhost;username=root;";
        public static string ID;
        public static string User;
        public static string name;
        public static string CashierID;
        public static string CashierName;
        private void label1_Click(object sender, EventArgs e)
        {

        }

        private void Login_Load(object sender, EventArgs e)
        {
            radioButton1.Checked = false; 
            radioButton2.Checked = false;
        }

        private void button1_Click_1(object sender, EventArgs e)
        {
         
                if (radioButton1.Checked)
                {
                    MySqlConnection connection = new MySqlConnection(Myconnection2);
                    connection.Open();
                    string qry = "select * from reservation_sytem.account where Username='" + textBox1.Text + "' and Password ='" + textBox2.Text + "' and User='" + radioButton1.Text + "';";
                    MySqlCommand cmd = new MySqlCommand(qry, connection);
                    cmd.ExecuteNonQuery();
                    DataTable dt = new DataTable();
                    MySqlDataAdapter apd = new MySqlDataAdapter(cmd);

                    apd.Fill(dt);

                    int count = 0;
                    count = Convert.ToInt32(dt.Rows.Count);

                    if (count != 0)
                    {
                        MessageBox.Show("Login As a Admin");
                        ID = dt.Rows[0]["Account_ID"].ToString();
                        User = dt.Rows[0]["User"].ToString();
                        name = dt.Rows[0]["Name"].ToString();
                    Admin frm = new Admin();
                        frm.Show();
                        this.Hide();
                    }
                    else
                    {
                        MessageBox.Show("Invalid Credentials");
                    }
                }
                else if (radioButton2.Checked)
                {
                    MySqlConnection connection = new MySqlConnection(Myconnection2);
                    connection.Open();
                    string qry = "select * from reservation_sytem.account where Username='" + textBox1.Text + "' and Password ='" + textBox2.Text + "' and User='" + radioButton2.Text + "';";
                    MySqlCommand cmd = new MySqlCommand(qry, connection);
                    cmd.ExecuteNonQuery();
                    DataTable dt = new DataTable();
                    MySqlDataAdapter apd = new MySqlDataAdapter(cmd);

                    apd.Fill(dt);

                    int count = 0;
                    count = Convert.ToInt32(dt.Rows.Count);

                    if (count != 0)
                    {
                        MessageBox.Show("Login As a Cashier");
                        CashierID = dt.Rows[0]["Account_ID"].ToString();
                        CashierName = dt.Rows[0]["Name"].ToString();
                    CashierLobby frm = new CashierLobby();
                        frm.Show();
                        this.Hide();
                    }
                    else
                    {
                        MessageBox.Show("Invalid Credentials");
                    }
                }
            
        }

        private void pictureBox1_MouseClick(object sender, MouseEventArgs e)
        {
            textBox2.PasswordChar = default;
        }

        private void textBox2_MouseHover(object sender, EventArgs e)
        {
            textBox2.PasswordChar = default;
        }

        private void textBox2_MouseLeave(object sender, EventArgs e)
        {
            textBox2.PasswordChar = '*';
        }
    }
}
