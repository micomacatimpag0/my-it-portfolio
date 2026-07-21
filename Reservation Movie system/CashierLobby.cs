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
    public partial class CashierLobby : Form
    {
        public CashierLobby()
        {
            InitializeComponent();
        }

        private void button1_Click(object sender, EventArgs e)
        {
            Cashier cashier = new Cashier();
            cashier.Show();
            this.Hide();
        }

        private void button2_Click(object sender, EventArgs e)
        {
            Sales sales = new Sales();
            sales.Show();
            this.Hide();
        }

        private void CashierLobby_Load(object sender, EventArgs e)
        {

        }

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
    }
}
