using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Drawing.Printing;
using System.Linq;
using System.Reflection.Emit;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;

namespace WindowsFormsApp1
{
    public partial class receipt : Form
    {
        public receipt()
        {
            InitializeComponent();
        }
        private void controls()
        {
            this.Controls.Add(pictureBox1);
            this.Controls.Add(label1);
        }

        private void printDocument1_PrintPage(object sender, System.Drawing.Printing.PrintPageEventArgs e)
        {
            Graphics graphics = e.Graphics;

            // Draw the picture box image
            graphics.DrawImage(pictureBox1.Image, pictureBox1.Location);

            // Draw the label text
            graphics.DrawString(label1.Text, label1.Font, new SolidBrush(label1.ForeColor), label1.Location);
        }

        private void button1_Click(object sender, EventArgs e)
        {
            PrintDialog printDialog = new PrintDialog();
            printDialog.Document = printDocument1;

            if (printDialog.ShowDialog() == DialogResult.OK)
            {
                printDocument1.Print();
            }
        }
    }
}
