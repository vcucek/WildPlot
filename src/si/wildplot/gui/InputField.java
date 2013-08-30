package si.wildplot.gui;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.GroupLayout.Alignment;
import javax.swing.GroupLayout.ParallelGroup;
import si.wildplot.core.Window;
import si.wildplot.core.render.Function;

/*
 * (C) Copyright 2013 Vito Čuček.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser General Public License
 * (LGPL) version 2.1 which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/lgpl-2.1.html
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * @author Vito Čuček <vito.cucek@xlab.si>
 */

public class InputField extends javax.swing.JPanel implements PropertyChangeListener{
	private static final long serialVersionUID = 1L;
	private Window wd;

	private Function currentFunction = null;
	private SlidersPanel sliderPanel;
	private javax.swing.JTextField jTextField1;
    private javax.swing.JToggleButton jToggleButton1;

    /** Creates new form ImputField */
    public InputField(Window wd) {
		this.wd = wd;
		this.sliderPanel = new SlidersPanel();

        initComponents();
		this.jTextField1.setText("add or select function");
		this.jToggleButton1.setEnabled(false);
		this.jTextField1.setEditable(false);
    }

    private void initComponents() {

        jTextField1 = new javax.swing.JTextField();
        jToggleButton1 = new javax.swing.JToggleButton();

        jTextField1.setText("f(x)=\"insert analitic function\"");
        jTextField1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTextField1MouseClicked(evt);
            }
        });
        jTextField1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextField1KeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField1KeyReleased(evt);
            }
        });

        jToggleButton1.setText("show");
        jToggleButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(jTextField1, javax.swing.GroupLayout.DEFAULT_SIZE, 443, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jToggleButton1))
				.addGroup(Alignment.TRAILING ,layout.createParallelGroup(Alignment.TRAILING, true)
					.addComponent(this.sliderPanel))
        );

		ParallelGroup vertical = layout.createParallelGroup(Alignment.LEADING, true);

		vertical.addGroup(Alignment.LEADING, layout.createSequentialGroup()
                	.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    	.addComponent(jTextField1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    	.addComponent(jToggleButton1, javax.swing.GroupLayout.Alignment.TRAILING))
        );

		vertical.addGroup(Alignment.TRAILING, layout.createSequentialGroup()
					.addGap(28)
					.addComponent(this.sliderPanel)
				);

		layout.setVerticalGroup(vertical);
    }

	private void jToggleButton1ActionPerformed(java.awt.event.ActionEvent evt)
	{
		if(this.currentFunction == null){
			this.jTextField1.setText("add or select function");
			this.jToggleButton1.setEnabled(false);
			this.jTextField1.setEditable(false);
			this.sliderPanel.clear();
			return;
		}
		if(!this.jToggleButton1.isSelected()){
			this.currentFunction.setEnabled(false);
			this.jToggleButton1.setText("show");
			this.sliderPanel.clear();
			this.firePropertyChange("Explorer.updateSelectionName", null, jTextField1.getText());
			this.firePropertyChange("InputField.currentFunction", this.currentFunction, null);
			this.wd.redraw();
		}
		else{
			this.currentFunction.setEnabled(true);
			this.jToggleButton1.setText("hide");
			this.sliderPanel.setSliders(this.wd, this.currentFunction.getSpecialParameters());
			this.wd.redraw();
			this.firePropertyChange("Explorer.updateSelectionName", null, jTextField1.getText());
			this.firePropertyChange("InputField.currentFunction", null, this.currentFunction);
		}
	}

	private void jTextField1KeyPressed(java.awt.event.KeyEvent evt)
	{

	}

	private void jTextField1MouseClicked(java.awt.event.MouseEvent evt)
	{

	}

	private void jTextField1KeyReleased(java.awt.event.KeyEvent evt)
	{
		if(this.currentFunction == null){
			this.jTextField1.setText("add or select function");
			this.jToggleButton1.setEnabled(false);
			this.jTextField1.setEditable(false);
			this.sliderPanel.clear();
			return;
		}
		if(evt.getKeyCode() == evt.VK_ENTER){
			this.jToggleButton1.setSelected(true);
			this.currentFunction.setEnabled(true);
			this.jToggleButton1.setText("hide");
			this.sliderPanel.setSliders(this.wd, this.currentFunction.getSpecialParameters());
			this.wd.redraw();
			this.firePropertyChange("Explorer.updateSelectionName", null, jTextField1.getText());
			this.firePropertyChange("InputField.currentFunction", null, this.currentFunction);
		}
		else{
			if(this.jToggleButton1.isSelected()){
				this.currentFunction.setEnabled(false);
				this.jToggleButton1.setSelected(false);
				this.jToggleButton1.setText("show");
				this.currentFunction.setFunction(jTextField1.getText());
					this.sliderPanel.clear();
				this.firePropertyChange("Explorer.updateSelectionName", null, jTextField1.getText());
				if(this.wd != null){this.wd.redraw();}
			}
			else{
				this.currentFunction.setFunction(jTextField1.getText());
				this.firePropertyChange("Explorer.updateSelectionName", null, jTextField1.getText());
			}
		}
	}

	public void propertyChange(PropertyChangeEvent evt)
	{
		if(evt.getPropertyName().equals("Explorer.selection")){
			this.currentFunction = (Function)evt.getNewValue();
			if(this.currentFunction != null){
				this.jToggleButton1.setSelected(this.currentFunction.isEnabled());
				this.jToggleButton1.setEnabled(true);
				this.jTextField1.setEditable(true);
				this.jTextField1.setText(this.currentFunction.getFunction());

				if(this.jToggleButton1.isSelected()){
					this.jToggleButton1.setText("hide");
					this.sliderPanel.setSliders(this.wd, this.currentFunction.getSpecialParameters());
				}
				else{
					this.jToggleButton1.setText("show");
					this.sliderPanel.clear();
				}
			}
			else{
				this.jToggleButton1.setSelected(false);
				this.jToggleButton1.setText("show");
				this.jToggleButton1.setEnabled(false);
				this.jTextField1.setText("add or select function");
				this.jTextField1.setEditable(false);

				this.sliderPanel.clear();
			}
		}
	}
}
