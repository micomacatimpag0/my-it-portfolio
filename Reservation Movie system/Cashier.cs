using MySql.Data.MySqlClient;
using System;
using System.Data;
using System.Drawing;
using System.Drawing.Printing;
using System.Windows.Forms;
using static System.Windows.Forms.VisualStyles.VisualStyleElement;

namespace WindowsFormsApp1
{
    public partial class Cashier : Form
    {
        public Cashier()
        {
            InitializeComponent();
        }
        string Myconnection2 = "Server=localhost;username=root;";
        string a,b,bc,cc, ac;
        public static string c, d;
        double price,cash,ch,intialprice,INTprice;
        int avilbleseats, amntseats, paidseat, newpaidseat,newavailableseat;
        public static string Ticket_ID, Movie_Name, Date_of_Show, Time_of_Show, Price, Cash, Change, Cinema_ID, Date, Cashier_Name;
        private void button3_Click(object sender, EventArgs e)
        {
            DialogResult msg = MessageBox.Show("Do Want leave? Any unsaved maybe discarded", "\n", MessageBoxButtons.YesNo);
            switch (msg)
            {
                case DialogResult.Yes:
                    Login opn = new Login();
                    opn.Show();
                    this.Hide();
                    break;

                case DialogResult.No:
                    break;
            }
        }

        public void Loadticket1()
        {
            MySqlConnection connection = new MySqlConnection(Myconnection2);
            connection.Open();
            string qry = "Select * From reservation_sytem.ticket";
            MySqlCommand cmd = new MySqlCommand(qry, connection);
            MySqlDataReader reader = cmd.ExecuteReader();
            DataTable dt = new DataTable();
            dt.Load(reader);
            dataGridView5.DataSource = dt;
            connection.Close();
        }
        public void Loadticket()
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
        public void Loadmovie1()
        {
            MySqlConnection connection = new MySqlConnection(Myconnection2);
            connection.Open();
            string qry = "Select * From reservation_sytem.movie";
            MySqlCommand cmd = new MySqlCommand(qry, connection);
            MySqlDataReader reader = cmd.ExecuteReader();
            DataTable dt = new DataTable();
            dt.Load(reader);
            dataGridView3.DataSource = dt;
            connection.Close();
        }
        public void Loadcinema1()
        {
            MySqlConnection connection = new MySqlConnection(Myconnection2);
            connection.Open();
            string qry = "Select * From reservation_sytem.cinema";
            MySqlCommand cmd = new MySqlCommand(qry, connection);
            MySqlDataReader reader = cmd.ExecuteReader();
            DataTable dt = new DataTable();
            dt.Load(reader);
            dataGridView4.DataSource = dt;
            connection.Close();
        }
        public void Loadmovie()
        {
            MySqlConnection connection = new MySqlConnection(Myconnection2);
            connection.Open();
            string qry = "Select * From reservation_sytem.movie";
            MySqlCommand cmd = new MySqlCommand(qry, connection);
            MySqlDataReader reader = cmd.ExecuteReader();
            DataTable dt = new DataTable();
            dt.Load(reader);
            dataGridView2.DataSource = dt;
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
            dataGridView7.DataSource = dt;
            connection.Close();
        }


        private void Cashier_Load(object sender, EventArgs e)
        {
            printDocument1 = new PrintDocument();
            printDocument1.PrintPage += printDocument1_PrintPage;
            textBox1.TextChanged += textBox1_TextChanged;
            textBox9.TextChanged += textBox1_TextChanged;
            textBox15.TextChanged += textBox1_TextChanged;
            textBox16.TextChanged += textBox1_TextChanged;
            textBox13.TextChanged += textBox1_TextChanged;
            textBox12.TextChanged += textBox1_TextChanged;
            textBox3.TextChanged += textBox1_TextChanged;
            textBox5.TextChanged += textBox1_TextChanged;
            textBox4.TextChanged += textBox1_TextChanged;
            Loadticket();
            panel1.Size = new Size(0, 0);
            Loadticket1();
            loadcenima();
            Loadmovie();
            Loadcinema1();
            Loadmovie1();
            textBox14.Text = Login.CashierID;
            textBox17.Text = Login.CashierName;
            timer1.Start();
            autoncrmnt();
        }

 

        private void tabPage2_Click(object sender, EventArgs e)
        {

        }

        private void button4_Click(object sender, EventArgs e)
        {
            panel1.Size = new Size(434, 151);
            richTextBox2.Text = "---------------------------------------------------------------------------" + textBox15.Text + "   " + textBox16.Text + "                               Cinema ID: " + textBox9.Text + "  Movie name: " + textBox1.Text + "          Price: "+textBox4.Text+" Cash: "+textBox5.Text+"  Change:"+textBox6.Text+ "           Date bought:"+label28.Text+"                      Cashier: "+textBox17.Text+"";

            using (PrintDialog printDialog = new PrintDialog())
            {
                printDialog.Document = printDocument1;
                if (printDialog.ShowDialog() == DialogResult.OK)
                {
                    printDocument1.Print();
                }
            }

        }

        private void button5_Click(object sender, EventArgs e)
        {
            DialogResult msg = MessageBox.Show("Do Want leave? Any unsaved maybe discarded", "\n", MessageBoxButtons.YesNo);
            switch (msg)
            {
                case DialogResult.Yes:
                    CashierLobby cashierLobby = new CashierLobby();
                    cashierLobby.Show();
                    this.Hide();
                    break;

                case DialogResult.No:
                    break;
            }
        }

        private void textBox1_TextChanged(object sender, EventArgs e)
        {
            bool allTextboxesEmpty = string.IsNullOrEmpty(textBox1.Text) || string.IsNullOrEmpty(textBox9.Text) || string.IsNullOrEmpty(textBox15.Text) || string.IsNullOrEmpty(textBox16.Text) || string.IsNullOrEmpty(textBox13.Text) || string.IsNullOrEmpty(textBox12.Text) || string.IsNullOrEmpty(textBox3.Text) || string.IsNullOrEmpty(textBox5.Text) || string.IsNullOrEmpty(textBox4.Text);
            button1.Enabled = !allTextboxesEmpty;
        }

        private void printDocument1_PrintPage(object sender, System.Drawing.Printing.PrintPageEventArgs e)
        {
            e.Graphics.DrawString(richTextBox2.Text, richTextBox2.Font, Brushes.Black, e.MarginBounds, StringFormat.GenericTypographic);
        }

        private void textBox20_TextChanged(object sender, EventArgs e)
        {
            MySqlConnection connection = new MySqlConnection(Myconnection2);
            connection.Open();
            string qry = "Select * From reservation_sytem.movie Where Movie_Name like'" + textBox20.Text + "%';";
            MySqlCommand cmd = new MySqlCommand(qry, connection);
            MySqlDataReader reader = cmd.ExecuteReader();
            DataTable dt = new DataTable();
            dt.Load(reader);
            dataGridView5.DataSource = dt;
            connection.Close();
        }

        private void textBox5_TextChanged(object sender, EventArgs e)
        {
            a = textBox4.Text;
            double.TryParse(a, out price);
            b = textBox5.Text;
            double.TryParse(b, out cash);
            ch = cash - price;
            textBox6.Text = ch.ToString();
        }

        private void button2_Click(object sender, EventArgs e)
        {
            DialogResult msg = MessageBox.Show("Clear all text?", "\n", MessageBoxButtons.YesNo);
            switch (msg)
            {
                case DialogResult.Yes:
                    textBox1.Text = "";
                    textBox9.Text = "";
                    textBox4.Text = "";
                    textBox15.Text = "";
                    textBox16.Text = "";
                    richTextBox1.Text = "";
                    textBox5.Text = "";
                    textBox12.Text = "";
                    textBox3.Text = "";
                    textBox13.Text = "";
                    break;

                case DialogResult.No:
                    break;
            }
        }
        private void textBox9_TextChanged(object sender, EventArgs e)
        {
            MySqlConnection connection = new MySqlConnection(Myconnection2);
            connection.Open();
            string qry = "Select * From reservation_sytem.movie Where Movie_ID like'" + textBox9.Text + "%';";
            MySqlCommand cmd = new MySqlCommand(qry, connection);
            MySqlDataReader reader = cmd.ExecuteReader();
            DataTable dt = new DataTable();
            dt.Load(reader);
            dataGridView2.DataSource = dt;
            connection.Close();

        }

  

        private void textBox10_TextChanged(object sender, EventArgs e)
        {
            MySqlConnection connection = new MySqlConnection(Myconnection2);
            connection.Open();
            string qry = "Select * From reservation_sytem.cinema Where Cinema_ID like'" + textBox10.Text + "%';";
            MySqlCommand cmd = new MySqlCommand(qry, connection);
            MySqlDataReader reader = cmd.ExecuteReader();
            DataTable dt = new DataTable();
            dt.Load(reader);
            dataGridView4.DataSource = dt;
            connection.Close();
        }

        private void tabPage1_Click(object sender, EventArgs e)
        {

        }

        private void textBox19_TextChanged(object sender, EventArgs e)
        {
            MySqlConnection connection = new MySqlConnection(Myconnection2);
            connection.Open();
            string qry = "Select * From reservation_sytem.ticket Where Ticket_ID like'"+textBox19.Text+"'";
            MySqlCommand cmd = new MySqlCommand(qry, connection);
            MySqlDataReader reader = cmd.ExecuteReader();
            DataTable dt = new DataTable();
            dt.Load(reader);
            dataGridView1.DataSource = dt;
            connection.Close();
        }

        private void label3_Click(object sender, EventArgs e)
        {

        }

        private void textBox4_TextChanged(object sender, EventArgs e)
        {

        }
        string f, g;


        private void timer1_Tick(object sender, EventArgs e)
        {
            label27.Text = DateTime.Now.ToShortTimeString();
            label28.Text = DateTime.Now.ToLongDateString();
        }

        private void label10_Click(object sender, EventArgs e)
        {

        }

  
        private void autoncrmnt()
        {
            int maxNuber = 0;

            foreach(DataGridViewRow row in dataGridView1.Rows)
            {
                int cellValue;
                if (row.Cells[0].Value != null && int.TryParse(row.Cells[0].Value.ToString(), out cellValue))
                {
                    if(cellValue > maxNuber)
                        maxNuber = cellValue;
                }
            }
            int incrementedNumber = maxNuber + 1;
            textBox3.Text = incrementedNumber.ToString();
        }


        private void label5_Click(object sender, EventArgs e)
        {

        }

        private void dataGridView7_CellContentClick(object sender, DataGridViewCellEventArgs e)
        {

        }

        private void dataGridView7_CellClick(object sender, DataGridViewCellEventArgs e)
        { 
            if (e.RowIndex >= 0)
            {
                    DataGridViewRow row = dataGridView7.Rows[e.RowIndex];
                    textBox9.Text = row.Cells[0].Value.ToString();
                    textBox15.Text = row.Cells[2].Value.ToString();
                    textBox16.Text = row.Cells[3].Value.ToString();
                    textBox13.Text = row.Cells[4].Value.ToString();
                    textBox12.Text = row.Cells[5].Value.ToString();
            }
            else if (e.RowIndex == 0)
            {
                dataGridView7.Enabled = false;
            }
        }

        private void button1_Click(object sender, EventArgs e)
        {

            string priorityKey = textBox3.Text;

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
                MessageBox.Show("Cannot Insert. Ticket ID already exist in the DataGridView.");
            }
            else
            {
                DialogResult msg = MessageBox.Show("Do want to insert the Data?", "\n", MessageBoxButtons.YesNo);
                switch (msg)
                {
                    case DialogResult.Yes:
                        ac = textBox13.Text;
                        int.TryParse(ac, out paidseat);

                        bc = textBox12.Text;
                        int.TryParse(bc, out avilbleseats);


                        newpaidseat = 1 + paidseat;
                        newavailableseat = avilbleseats - 1;
                        updatecinema();
                        loadcenima();
                        ticcket();
                        autoncrmnt();
                        break;
                    case DialogResult.No:
                        break;
                }

            }
   
        }

        private void updatecinema()
        {
            MySqlConnection connection = new MySqlConnection(Myconnection2);
            connection.Open();
            string qry = "update reservation_sytem.cinema set Avialable_Seats='" + newavailableseat + "',Paid_seats='" + newpaidseat + "' where Cinema_ID='" + textBox9.Text + "';";
            MySqlCommand cmdd = new MySqlCommand(qry, connection);
            cmdd.ExecuteNonQuery();
            cmdd.Dispose();
            connection.Close();
        }
        private void ticcket()
        {
            MySqlConnection connection = new MySqlConnection(Myconnection2);
            connection.Open();
            string qry = "insert into reservation_sytem.ticket(`Ticket_ID`, `Movie_Name`, `Date_of_Show`, `Time_of_Show`, `Price`, `Cash`, `Change`, `Cinema_ID`, `Date`, `Cashier_Name`)" + "Values('" + textBox3.Text + "','" + textBox1.Text + "','" + textBox15.Text + "','" + textBox16.Text + "','" + textBox4.Text + "','" + textBox5.Text + "','" + textBox6.Text + "','"+textBox9.Text+"','"+label28.Text+"','"+textBox17.Text+"');";
            MySqlCommand cmdd = new MySqlCommand(qry, connection);
            cmdd.ExecuteNonQuery();
            cmdd.Dispose();
            connection.Close();
            Loadticket();
        }
        private void textBox11_TextChanged(object sender, EventArgs e)
        {
            MySqlConnection connection = new MySqlConnection(Myconnection2);
            connection.Open();
            string qry = "Select * From reservation_sytem.movie Where Movie_Name like'" + textBox11.Text + "%';";
            MySqlCommand cmd = new MySqlCommand(qry, connection);
            MySqlDataReader reader = cmd.ExecuteReader();
            DataTable dt = new DataTable();
            dt.Load(reader);
            dataGridView3.DataSource = dt;
            connection.Close();
        }

        private void dataGridView2_CellClick(object sender, DataGridViewCellEventArgs e)
        {
            if (e.RowIndex >= 0)
            {
                DataGridViewRow row = dataGridView2.Rows[e.RowIndex];
                textBox1.Text = row.Cells[1].Value.ToString();
                richTextBox1.Text = row.Cells[3].Value.ToString();
                textBox4.Text = row.Cells[5].Value.ToString();
 
            }
            else if (e.RowIndex == 0)
            {
                dataGridView2.Enabled = false;
            }
        }

        private void textBox7_TextChanged_1(object sender, EventArgs e)
        {
            MySqlConnection connection = new MySqlConnection(Myconnection2);
            connection.Open();
            string qry = "Select * From reservation_sytem.movie Where Movie_Name like'" + textBox7.Text + "%';";
            MySqlCommand cmd = new MySqlCommand(qry, connection);
            MySqlDataReader reader = cmd.ExecuteReader();
            DataTable dt = new DataTable();
            dt.Load(reader);
            dataGridView2.DataSource = dt;
            connection.Close();
        }

        private void textBox8_TextChanged_1(object sender, EventArgs e)
        {
            MySqlConnection connection = new MySqlConnection(Myconnection2);
            connection.Open();
            string qry = "Select * From reservation_sytem.cinema Where Cinema_ID like'" + textBox8.Text + "%';";
            MySqlCommand cmd = new MySqlCommand(qry, connection);
            MySqlDataReader reader = cmd.ExecuteReader();
            DataTable dt = new DataTable();
            dt.Load(reader);
            dataGridView7.DataSource = dt;
            connection.Close();
        }


    }
}
