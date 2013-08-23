/*
 * AddFunction.java
 *
 * Created on May 8, 2011, 2:43:50 AM
 */

package si.wildplot.gui;

import si.wildplot.core.SceneProvider;
import si.wildplot.core.Window;
import si.wildplot.core.render.Function;
import si.wildplot.core.render.Model;
import si.wildplot.core.render.Plot2D;
import si.wildplot.core.render.Plot3D;

/**
 *
 * @author vito
 */
public class AddFunction extends javax.swing.JFrame {

	private Window wd;
	private Explorer explorer;

    /** Creates new form AddFunction */
    public AddFunction(Window wd, Explorer explorer) {
		this.wd = wd;
		this.explorer = explorer;
        initComponents();
		this.setLocationRelativeTo(explorer);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        button2dExplicit = new javax.swing.JButton();
        button2dImplicit = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        button3d = new javax.swing.JButton();

        setAlwaysOnTop(true);

        button2dExplicit.setText("add explicit 2d function");
        button2dExplicit.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                button2dExplicitMousePressed(evt);
            }
        });

        button2dImplicit.setText("add implicit 2d function");
        button2dImplicit.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                button2dImplicitMousePressed(evt);
            }
        });

        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jTextArea1.setText("1.0 Explicit functions:\n    - after clicking  \"add explicit 2d function\"\n       button, you need to type appropriate\n       function in top-moust text field of Mafi\n       program window. \n    - example: if desired function is \n       \"f(x)=sin(x)\", then type in text field \n       \"sin(x)\".\n\n2.0 Implicit functions:\n    - after clicking  \"add implicit 2d function\"\n       button, you need to type whole\n       function (equation) in top-moust \n       text field of Mafi program window. \n    - example: if desired function is \n       \"5=x^2+y^2\", then type in text field \n       \"x*x+y*y=5\" or \"pow(x,2)+pow(y,2)=5\".\n\n3.0 Common operations:\n    - if speed is crucial, dont use \"pow(x,y)\"\n       function unless you realy need it. ");
        jScrollPane1.setViewportView(jTextArea1);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 298, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 274, Short.MAX_VALUE)
                    .addContainerGap()))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 341, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                    .addGap(11, 11, 11)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 318, Short.MAX_VALUE)
                    .addContainerGap()))
        );

        button3d.setText("add 3d function");
        button3d.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button3dActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(button2dExplicit, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(button2dImplicit, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(button3d, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(button2dExplicit)
                .addGap(12, 12, 12)
                .addComponent(button2dImplicit)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(button3d)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

	private void button2dExplicitMousePressed(java.awt.event.MouseEvent evt)//GEN-FIRST:event_button2dExplicitMousePressed
	{//GEN-HEADEREND:event_button2dExplicitMousePressed
		Function f = new Plot2D("", Function.TYPE_2D_EXPLICIT);
		f.setEnabled(false);
		SceneProvider.getInstance().model2D.addRenderable(f);
		explorer.update();
		explorer.selectLast();
		this.setVisible(false);
	}//GEN-LAST:event_button2dExplicitMousePressed

	private void button2dImplicitMousePressed(java.awt.event.MouseEvent evt)//GEN-FIRST:event_button2dImplicitMousePressed
	{//GEN-HEADEREND:event_button2dImplicitMousePressed
		Function f = new Plot2D("", Function.TYPE_2D_IMPLICIT);
		f.setEnabled(false);
		SceneProvider.getInstance().model2D.addRenderable(f);
		explorer.update();
		explorer.selectLast();
		this.setVisible(false);
	}//GEN-LAST:event_button2dImplicitMousePressed

    private void button3dActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button3dActionPerformed
		Function f = new Plot3D("");
		f.setEnabled(false);
		SceneProvider.getInstance().model3D.addRenderable(f);
		explorer.update();
		explorer.selectLast();
		this.setVisible(false);
    }//GEN-LAST:event_button3dActionPerformed

//    /**
//    * @param args the command line arguments
//    */
//    public static void main(String args[]) {
//        java.awt.EventQueue.invokeLater(new Runnable() {
//            public void run() {
//                new AddFunction().setVisible(true);
//            }
//        });
//    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton button2dExplicit;
    private javax.swing.JButton button2dImplicit;
    private javax.swing.JButton button3d;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextArea jTextArea1;
    // End of variables declaration//GEN-END:variables

}
