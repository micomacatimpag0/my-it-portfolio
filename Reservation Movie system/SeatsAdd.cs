using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Reflection.Emit;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;
using MySql.Data.MySqlClient;
using static System.Windows.Forms.VisualStyles.VisualStyleElement;

namespace WindowsFormsApp1
{
    public partial class SeatsAdd : Form
    {
        public SeatsAdd()
        {
            InitializeComponent();
        }
        int Id;
        string Myconnection2 = "Server=localhost;username=root;";
        public void loadSeats()
        {
            MySqlConnection connection = new MySqlConnection(Myconnection2);
            connection.Open();
            string qry = "Select * From reservation_sytem.seats";
            MySqlCommand cmd = new MySqlCommand(qry, connection);
            MySqlDataReader reader = cmd.ExecuteReader();
            DataTable dt = new DataTable();
            dt.Load(reader);
            dataGridView2.DataSource = dt;
            connection.Close();
        }
        public void LoadCenima()
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
        private void SeatsAdd_Load(object sender, EventArgs e)
        {
            
            loadSeats();
            LoadCenima();
        }

        private void dataGridView2_CellContentClick(object sender, DataGridViewCellEventArgs e)
        {

        }

        private void dataGridView1_CellClick(object sender, DataGridViewCellEventArgs e)
        {
            if (e.RowIndex >= 0)
            {
                DataGridViewRow row = dataGridView1.Rows[e.RowIndex];
                textBox1.Text = row.Cells[0].Value.ToString();
                textBox2.Text = row.Cells[1].Value.ToString();
                textBox4.Text = row.Cells[2].Value.ToString();
            }
        }

        private void button1_Click(object sender, EventArgs e)
        {
            DataTable dt = new DataTable();

            dt.Columns.Add("Seat_ID", typeof(int));
            dt.Columns.Add("Seat_No", typeof(int));
            dt.Columns.Add("Reserve", typeof(string));
            dt.Columns.Add("Paid_Seat", typeof(string));
            dt.Columns.Add("Cinema_id", typeof(String));
            dt.Columns.Add("Cinema_Room", typeof(string));

            dt.Rows.Add(1,100,"","",textBox1.Text,textBox2.Text);
            dt.Rows.Add(2,101,"","",textBox1.Text,textBox2.Text);
            dt.Rows.Add(3,102,"","",textBox1.Text,textBox2.Text);
            dt.Rows.Add(4,103,"","",textBox1.Text,textBox2.Text);
            dt.Rows.Add(5,104,"","",textBox1.Text,textBox2.Text);
            dt.Rows.Add(6,105,"","",textBox1.Text,textBox2.Text);
            dt.Rows.Add(7,106,"","",textBox1.Text,textBox2.Text);
            dt.Rows.Add(8,107,"","",textBox1.Text,textBox2.Text);
            dt.Rows.Add(9,108,"","",textBox1.Text,textBox2.Text);
            dt.Rows.Add(10,109,"","",textBox1.Text,textBox2.Text);
            dt.Rows.Add(11,110,"","",textBox1.Text,textBox2.Text);
            dt.Rows.Add(12,111,"","",textBox1.Text,textBox2.Text);
            dt.Rows.Add(13,112,"","",textBox1.Text,textBox2.Text);
            dt.Rows.Add(14,113,"","",textBox1.Text,textBox2.Text);
            dt.Rows.Add(15,114,"","",textBox1.Text,textBox2.Text);
            dt.Rows.Add(16,115,"","",textBox1.Text,textBox2.Text);
            dt.Rows.Add(17,116,"","",textBox1.Text,textBox2.Text);
            dt.Rows.Add(18,117,"","",textBox1.Text,textBox2.Text);
            dt.Rows.Add(19,118,"","",textBox1.Text,textBox2.Text);
            dt.Rows.Add(20,119,"","",textBox1.Text,textBox2.Text);
            dt.Rows.Add(21,120,"","",textBox1.Text,textBox2.Text);
            dt.Rows.Add(22,121,"","",textBox1.Text,textBox2.Text);
            dt.Rows.Add(23,122,"","",textBox1.Text,textBox2.Text);
            dt.Rows.Add(24,123,"","",textBox1.Text,textBox2.Text);
            dt.Rows.Add(25,124,"","",textBox1.Text,textBox2.Text);
            dt.Rows.Add(26,125,"","",textBox1.Text,textBox2.Text);
            dt.Rows.Add(27,126,"","",textBox1.Text,textBox2.Text);
            dt.Rows.Add(28,127,"","",textBox1.Text,textBox2.Text);
            dt.Rows.Add(29,128,"","",textBox1.Text,textBox2.Text);
            dt.Rows.Add(30,129,"","",textBox1.Text,textBox2.Text);
            dt.Rows.Add(31,130,"","",textBox1.Text,textBox2.Text);
            dt.Rows.Add(32,131,"","",textBox1.Text,textBox2.Text);
            dt.Rows.Add(33,132,"","",textBox1.Text,textBox2.Text);
            dt.Rows.Add(34,133,"","",textBox1.Text,textBox2.Text);
            dt.Rows.Add(35,134,"","",textBox1.Text,textBox2.Text);
            dt.Rows.Add(36,135,"","",textBox1.Text,textBox2.Text);
            dt.Rows.Add(37,136,"","",textBox1.Text,textBox2.Text);
            dt.Rows.Add(38,137,"","",textBox1.Text,textBox2.Text);
            dt.Rows.Add(39,138,"","",textBox1.Text,textBox2.Text);
            dt.Rows.Add(40,139,"","",textBox1.Text,textBox2.Text);
            dt.Rows.Add(41,140,"","",textBox1.Text,textBox2.Text);
            dt.Rows.Add(42,141,"","",textBox1.Text,textBox2.Text);
            dt.Rows.Add(43,142,"","",textBox1.Text,textBox2.Text);
            dt.Rows.Add(44,143,"","",textBox1.Text,textBox2.Text);
            dt.Rows.Add(45,144,"","",textBox1.Text,textBox2.Text);
            dt.Rows.Add(46,145,"","",textBox1.Text,textBox2.Text);
            dt.Rows.Add(47,146,"","",textBox1.Text,textBox2.Text);
            dt.Rows.Add(48,147,"","",textBox1.Text,textBox2.Text);
            dt.Rows.Add(49,148,"","",textBox1.Text,textBox2.Text);
            dt.Rows.Add(50,149,"","",textBox1.Text,textBox2.Text);
            dt.Rows.Add(51,150,"","",textBox1.Text,textBox2.Text);
            dt.Rows.Add(52,151,"","",textBox1.Text,textBox2.Text);
            dt.Rows.Add(53,152,"","",textBox1.Text,textBox2.Text);
            dt.Rows.Add(54,153,"","",textBox1.Text,textBox2.Text);
            dt.Rows.Add(55,154,"","",textBox1.Text,textBox2.Text);
            dt.Rows.Add(56,155,"","",textBox1.Text,textBox2.Text);
            dt.Rows.Add(57,156,"","",textBox1.Text,textBox2.Text);
            dt.Rows.Add(58,157,"","",textBox1.Text,textBox2.Text);
            dt.Rows.Add(59,158,"","",textBox1.Text,textBox2.Text);
            dt.Rows.Add(60,159,"","",textBox1.Text,textBox2.Text);
            dt.Rows.Add(61,160,"","",textBox1.Text,textBox2.Text);
            dt.Rows.Add(62,161,"","",textBox1.Text,textBox2.Text);
            dt.Rows.Add(63,162,"","",textBox1.Text,textBox2.Text);
            dt.Rows.Add(64,163,"","",textBox1.Text,textBox2.Text);
            dt.Rows.Add(65,164,"","",textBox1.Text,textBox2.Text);
            dt.Rows.Add(66,165,"","",textBox1.Text,textBox2.Text);
            dt.Rows.Add(67,166,"","",textBox1.Text,textBox2.Text);
            dt.Rows.Add(68,167,"","",textBox1.Text,textBox2.Text);
            dt.Rows.Add(69,168,"","",textBox1.Text,textBox2.Text);
            dt.Rows.Add(70,169,"","",textBox1.Text,textBox2.Text);
            dt.Rows.Add(71,170,"","",textBox1.Text,textBox2.Text);
            dt.Rows.Add(72,171,"","",textBox1.Text,textBox2.Text);
            dt.Rows.Add(73,172,"","",textBox1.Text,textBox2.Text);
            dt.Rows.Add(74,173,"","",textBox1.Text,textBox2.Text);
            dt.Rows.Add(75,174,"","",textBox1.Text,textBox2.Text);
            dt.Rows.Add(76,175,"","",textBox1.Text,textBox2.Text);
            dt.Rows.Add(77,176,"","",textBox1.Text,textBox2.Text);
            dt.Rows.Add(78,177,"","",textBox1.Text,textBox2.Text);
            dt.Rows.Add(79,178,"","",textBox1.Text,textBox2.Text);
            dt.Rows.Add(80,179,"","",textBox1.Text,textBox2.Text);
            dt.Rows.Add(81,180,"","",textBox1.Text,textBox2.Text);
            dt.Rows.Add(82,181,"","",textBox1.Text,textBox2.Text);
            dt.Rows.Add(83,182,"","",textBox1.Text,textBox2.Text);
            dt.Rows.Add(84,183,"","",textBox1.Text,textBox2.Text);
            dt.Rows.Add(85,184,"","",textBox1.Text,textBox2.Text);
            dt.Rows.Add(86,185,"","",textBox1.Text,textBox2.Text);
            dt.Rows.Add(87,186,"","",textBox1.Text,textBox2.Text);
            dt.Rows.Add(88,187,"","",textBox1.Text,textBox2.Text);
            dt.Rows.Add(89,188,"","",textBox1.Text,textBox2.Text);
            dt.Rows.Add(90,189,"","",textBox1.Text,textBox2.Text);
            dt.Rows.Add(91,190,"","",textBox1.Text,textBox2.Text);
            dt.Rows.Add(92,191,"","",textBox1.Text,textBox2.Text);
            dt.Rows.Add(93,192,"","",textBox1.Text,textBox2.Text);
            dt.Rows.Add(94,193,"","",textBox1.Text,textBox2.Text);
            dt.Rows.Add(95,194,"","",textBox1.Text,textBox2.Text);
            dt.Rows.Add(96,195,"","",textBox1.Text,textBox2.Text);
            dt.Rows.Add(97,196,"","",textBox1.Text,textBox2.Text);
            dt.Rows.Add(98,197,"","",textBox1.Text,textBox2.Text);
            dt.Rows.Add(99,198,"","",textBox1.Text,textBox2.Text);
            dt.Rows.Add(100,199,"","",textBox1.Text,textBox2.Text);
            dt.Rows.Add(101,200,"","",textBox1.Text,textBox2.Text);
            dt.Rows.Add(102,201,"","",textBox1.Text,textBox2.Text);
            dt.Rows.Add(103,202,"","",textBox1.Text,textBox2.Text);
            dt.Rows.Add(104,203,"","",textBox1.Text,textBox2.Text);
            dt.Rows.Add(105,204,"","",textBox1.Text,textBox2.Text);
            dt.Rows.Add(106,205,"","",textBox1.Text,textBox2.Text);
            dt.Rows.Add(107,206,"","",textBox1.Text,textBox2.Text);
            dt.Rows.Add(108,207,"","",textBox1.Text,textBox2.Text);
            dt.Rows.Add(109,208,"","",textBox1.Text,textBox2.Text);
            dt.Rows.Add(110,209,"","",textBox1.Text,textBox2.Text);
            dt.Rows.Add(111,210,"","",textBox1.Text,textBox2.Text);
            dt.Rows.Add(112,211,"","",textBox1.Text,textBox2.Text);
            dt.Rows.Add(113,212,"","",textBox1.Text,textBox2.Text);
            dt.Rows.Add(114,213,"","",textBox1.Text,textBox2.Text);
            dt.Rows.Add(115,214,"","",textBox1.Text,textBox2.Text);
            dt.Rows.Add(116,215,"","",textBox1.Text,textBox2.Text);
            dt.Rows.Add(117,216,"","",textBox1.Text,textBox2.Text);
            dt.Rows.Add(118,217,"","",textBox1.Text,textBox2.Text);
            dt.Rows.Add(119,218,"","",textBox1.Text,textBox2.Text);
            dt.Rows.Add(120,219,"","",textBox1.Text,textBox2.Text);
            dt.Rows.Add(121,220,"","",textBox1.Text,textBox2.Text);
            dt.Rows.Add(122,221,"","",textBox1.Text,textBox2.Text);
            dt.Rows.Add(123,222,"","",textBox1.Text,textBox2.Text);
            dt.Rows.Add(124,223,"","",textBox1.Text,textBox2.Text);
            dt.Rows.Add(125,224,"","",textBox1.Text,textBox2.Text);
            dt.Rows.Add(126,225,"","",textBox1.Text,textBox2.Text);
            dt.Rows.Add(127,226,"","",textBox1.Text,textBox2.Text);
            dt.Rows.Add(128,227,"","",textBox1.Text,textBox2.Text);
            dt.Rows.Add(129,228,"","",textBox1.Text,textBox2.Text);
            dt.Rows.Add(130,229,"","",textBox1.Text,textBox2.Text);
            dt.Rows.Add(131,230,"","",textBox1.Text,textBox2.Text);
            dt.Rows.Add(132,231,"","",textBox1.Text,textBox2.Text);
            dt.Rows.Add(133,232,"","",textBox1.Text,textBox2.Text);
            dt.Rows.Add(134,233,"","",textBox1.Text,textBox2.Text);
            dt.Rows.Add(135,234,"","",textBox1.Text,textBox2.Text);
            dt.Rows.Add(136,235,"","",textBox1.Text,textBox2.Text);
            dt.Rows.Add(137,236,"","",textBox1.Text,textBox2.Text);
            dt.Rows.Add(138,237,"","",textBox1.Text,textBox2.Text);
            dt.Rows.Add(139,238,"","",textBox1.Text,textBox2.Text);
            dt.Rows.Add(140,239,"","",textBox1.Text,textBox2.Text);
            dt.Rows.Add(141,240,"","",textBox1.Text,textBox2.Text);
            dt.Rows.Add(142,241,"","",textBox1.Text,textBox2.Text);
            dt.Rows.Add(143,242,"","",textBox1.Text,textBox2.Text);
            dt.Rows.Add(144,243,"","",textBox1.Text,textBox2.Text);
            dt.Rows.Add(145,244,"","",textBox1.Text,textBox2.Text);
            dt.Rows.Add(146,245,"","",textBox1.Text,textBox2.Text);
            dt.Rows.Add(147,246,"","",textBox1.Text,textBox2.Text);
            dt.Rows.Add(148,247,"","",textBox1.Text,textBox2.Text);
            dt.Rows.Add(149,248,"","",textBox1.Text,textBox2.Text);
            dt.Rows.Add(150,249,"","",textBox1.Text,textBox2.Text);
            dt.Rows.Add(151,250,"","",textBox1.Text,textBox2.Text);
            dt.Rows.Add(152,251,"","",textBox1.Text,textBox2.Text);
            dt.Rows.Add(153,252,"","",textBox1.Text,textBox2.Text);
            dt.Rows.Add(154,253,"","",textBox1.Text,textBox2.Text);
            dt.Rows.Add(155,254,"","",textBox1.Text,textBox2.Text);
            dt.Rows.Add(156,255,"","",textBox1.Text,textBox2.Text);
            dt.Rows.Add(157,256,"","",textBox1.Text,textBox2.Text);
            dt.Rows.Add(158,257,"","",textBox1.Text,textBox2.Text);
            dt.Rows.Add(159,258,"","",textBox1.Text,textBox2.Text);
            dt.Rows.Add(160,259,"","",textBox1.Text,textBox2.Text);
            dt.Rows.Add(161,260,"","",textBox1.Text,textBox2.Text);
            dt.Rows.Add(162,261,"","",textBox1.Text,textBox2.Text);
            dt.Rows.Add(163,262,"","",textBox1.Text,textBox2.Text);
            dt.Rows.Add(164,263,"","",textBox1.Text,textBox2.Text);
            dt.Rows.Add(165,264,"","",textBox1.Text,textBox2.Text);
            dt.Rows.Add(166,265,"","",textBox1.Text,textBox2.Text);
            dt.Rows.Add(167,266,"","",textBox1.Text,textBox2.Text);
            dt.Rows.Add(168,267,"","",textBox1.Text,textBox2.Text);
            dt.Rows.Add(169,268,"","",textBox1.Text,textBox2.Text);
            dt.Rows.Add(170,269,"","",textBox1.Text,textBox2.Text);
            dt.Rows.Add(171,270,"","",textBox1.Text,textBox2.Text);
            dt.Rows.Add(172,271,"","",textBox1.Text,textBox2.Text);
            dt.Rows.Add(173,272,"","",textBox1.Text,textBox2.Text);
            dt.Rows.Add(174,273,"","",textBox1.Text,textBox2.Text);
            dt.Rows.Add(175,274,"","",textBox1.Text,textBox2.Text);
            dt.Rows.Add(176,275,"","",textBox1.Text,textBox2.Text);
            dt.Rows.Add(177,276,"","",textBox1.Text,textBox2.Text);
            dt.Rows.Add(178,277,"","",textBox1.Text,textBox2.Text);
            dt.Rows.Add(179,278,"","",textBox1.Text,textBox2.Text);
            dt.Rows.Add(180,279,"","",textBox1.Text,textBox2.Text);
            dt.Rows.Add(181,280,"","",textBox1.Text,textBox2.Text);
            dt.Rows.Add(182,281,"","",textBox1.Text,textBox2.Text);
            dt.Rows.Add(183,282,"","",textBox1.Text,textBox2.Text);
            dt.Rows.Add(184,283,"","",textBox1.Text,textBox2.Text);
            dt.Rows.Add(185,284,"","",textBox1.Text,textBox2.Text);
            dt.Rows.Add(186,285,"","",textBox1.Text,textBox2.Text);
            dt.Rows.Add(187,286,"","",textBox1.Text,textBox2.Text);
            dt.Rows.Add(188,287,"","",textBox1.Text,textBox2.Text);
            dt.Rows.Add(189,288,"","",textBox1.Text,textBox2.Text);
            dt.Rows.Add(190,289,"","",textBox1.Text,textBox2.Text);
            dt.Rows.Add(191,290,"","",textBox1.Text,textBox2.Text);
            dt.Rows.Add(192,291,"","",textBox1.Text,textBox2.Text);
            dt.Rows.Add(193,292,"","",textBox1.Text,textBox2.Text);
            dt.Rows.Add(194,293,"","",textBox1.Text,textBox2.Text);
            dt.Rows.Add(195,294,"","",textBox1.Text,textBox2.Text);
            dt.Rows.Add(196,295,"","",textBox1.Text,textBox2.Text);
            dt.Rows.Add(197,296,"","",textBox1.Text,textBox2.Text);
            dt.Rows.Add(198,297,"","",textBox1.Text,textBox2.Text);
            dt.Rows.Add(199,298,"","",textBox1.Text,textBox2.Text);
            dt.Rows.Add(200,299,"","",textBox1.Text,textBox2.Text);

            dataGridView2.DataSource = dt;
 

        }

        private void button4_Click(object sender, EventArgs e)
        {
            MySqlCommand Cmd;
            Myconnection2.Open();

        }
    }
}
