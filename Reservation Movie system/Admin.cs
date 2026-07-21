using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;

namespace WindowsFormsApp1
{
    public partial class Admin : Form
    {
        public Admin()
        {
            InitializeComponent();
        }

        private void radioButton2_CheckedChanged(object sender, EventArgs e)
        {

        }

        private void Admin_Load(object sender, EventArgs e)
        {
            textBox1.Text = Login.ID;
            textBox2.Text = Login.User;
            textBox3.Text = Login.name;
        }

        private void button6_Click(object sender, EventArgs e)
        {
            DialogResult msg = MessageBox.Show("Do Want to Log out?", "\n", MessageBoxButtons.YesNo);
            switch(msg)
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

        private void button1_Click(object sender, EventArgs e)
        {
            Movie aw = new Movie();
            aw.Show();
            this.Hide();
        }

        private void button2_Click(object sender, EventArgs e)
        {
            Cenima cenima = new Cenima();
            cenima.Show();
            this.Hide();
        }

        private void button5_Click(object sender, EventArgs e)
        {
            AddAdmin addAdmin = new AddAdmin();
            addAdmin.Show();
            this.Hide();
        }

        private void button4_Click(object sender, EventArgs e)
        {
            POS pOS = new POS();
            pOS.Show();
            this.Hide();
        }
    }
}
