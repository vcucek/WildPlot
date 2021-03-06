/*
 * Explorer.java
 *
 * Created on May 1, 2011, 3:57:15 AM
 */

package si.wildplot.gui;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Arrays;
import javax.swing.AbstractListModel;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import si.wildplot.core.SceneProvider;
import si.wildplot.core.Window;
import si.wildplot.core.render.Function;
import si.wildplot.core.render.Model;
import si.wildplot.core.render.Plot2D;
import si.wildplot.core.render.Renderable;

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
public class Explorer extends javax.swing.JPanel implements PropertyChangeListener {
	private static final long serialVersionUID = 1L;

	private EntryListModel list = new EntryListModel();
	private Function currentFunction = null;
	private Window wd;
	private SelectionListener selectionListener;

	private AddFunction addFunctionFrame;

    /** Creates new form Explorer */
    public Explorer(Window wd) {
		this.wd = wd;
		this.addFunctionFrame = new AddFunction(wd, this);
		this.addFunctionFrame.setAlwaysOnTop(true);
		this.addFunctionFrame.setVisible(false);
        initComponents();
		this.jList1.setModel(list);
		this.jList1.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		this.selectionListener = new SelectionListener();
		this.jList1.addListSelectionListener(this.selectionListener);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        bindingGroup = new org.jdesktop.beansbinding.BindingGroup();

        jScrollPane1 = new javax.swing.JScrollPane();
        jList1 = new javax.swing.JList();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JSeparator();

        setMaximumSize(new java.awt.Dimension(200, 32767));

        jList1.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        jList1.setMaximumSize(new java.awt.Dimension(42, 65));
        jList1.setMinimumSize(new java.awt.Dimension(42, 65));

        org.jdesktop.beansbinding.Binding binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, jList1, org.jdesktop.beansbinding.ObjectProperty.create(), jList1, org.jdesktop.beansbinding.BeanProperty.create("elements"));
        bindingGroup.addBinding(binding);

        jScrollPane1.setViewportView(jList1);

        jButton1.setText("add");
        jButton1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                jButton1MouseReleased(evt);
            }
        });

        jButton2.setText("remove");
        jButton2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                jButton2MouseReleased(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 145, Short.MAX_VALUE)
            .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, 145, Short.MAX_VALUE)
            .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, 145, Short.MAX_VALUE)
            .addComponent(jSeparator1, javax.swing.GroupLayout.DEFAULT_SIZE, 145, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 94, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        bindingGroup.bind();
    }// </editor-fold>//GEN-END:initComponents

	private void jButton1MouseReleased(java.awt.event.MouseEvent evt)//GEN-FIRST:event_jButton1MouseReleased
	{//GEN-HEADEREND:event_jButton1MouseReleased
		// add button
//		Function f = new Plot2D("", Plot2D.EXPLICIT);
//		f.setEnabled(false);
//		this.wd.getModel().addRenderable(f);
//		update();
		this.addFunctionFrame.setVisible(true);
	}//GEN-LAST:event_jButton1MouseReleased

	private void jButton2MouseReleased(java.awt.event.MouseEvent evt)//GEN-FIRST:event_jButton2MouseReleased
	{//GEN-HEADEREND:event_jButton2MouseReleased
		// remove button
		if(this.currentFunction != null){
			this.currentFunction.setEnabled(false);
			this.wd.getModel().removeRenderable(this.currentFunction);
			update();
		}
	}//GEN-LAST:event_jButton2MouseReleased


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JList jList1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private org.jdesktop.beansbinding.BindingGroup bindingGroup;
    // End of variables declaration//GEN-END:variables

	private void setList(String[] list){
		int selectionIndex = this.jList1.getSelectedIndex();
		this.list.strings.clear();
		this.list.strings.addAll(Arrays.asList(list));
		if(list.length <= selectionIndex){
			this.jList1.setListData(list);
			this.jList1.clearSelection();
		}
		else{
			this.jList1.removeListSelectionListener(this.selectionListener);
			this.jList1.setListData(list);
			this.jList1.setSelectedIndex(selectionIndex);
			this.jList1.addListSelectionListener(this.selectionListener);
		}
	}

	public void selectLast(){
		int selectionIndex = this.jList1.getModel().getSize()-1;
		this.jList1.getSelectionModel().setSelectionInterval(0, selectionIndex);
	}

//	private void add(String s){
//		this.list.strings.add(list.getSize()+1 + ". " + s);
//		this.jList1.setListData(this.list.strings.toArray());
//		this.jList1.setSelectedIndex(list.getSize() - 1);
//	}
//
//	private void remove(String s){
//		int selectionIndex = this.jList1.getSelectedIndex();
//		this.list.strings.remove(s);
//		this.jList1.setListData(this.list.strings.toArray());
//		this.jList1.setSelectedIndex(selectionIndex);
//	}

//	private ArrayList<String> getList(){
//		return this.list.strings;
//	}

	public ArrayList<Function> getFunctions(){

		ArrayList<Renderable> tmp = new ArrayList<Renderable>();
		tmp.addAll(SceneProvider.getInstance().model2D.getRenderables());
		tmp.addAll(SceneProvider.getInstance().model3D.getRenderables());
		
		ArrayList<Function> functions = new ArrayList<Function>();
		
		for(Renderable r : tmp){
			if(r instanceof Function){
				functions.add((Function)r);
			}
		}
		return functions;
	}

	public String getFunctionPrefix(int type){
		if(type == Function.TYPE_2D_EXPLICIT){
			return "f(x)=";
		}
		if(type == Function.TYPE_2D_IMPLICIT){
			return "f:";
		}

		if(type == Function.TYPE_3D_EXPLICIT){
			return "3D f:";
		}
		return "";
	}

	public void setCurrentFunction(Function f){
		if(f.getType() == Function.TYPE_3D_EXPLICIT){
			SceneProvider.getInstance().set3DScene();
		}
		else{
			SceneProvider.getInstance().set2DScene();
		}

		firePropertyChange("Explorer.selection", this.currentFunction, f);
		this.currentFunction = f;
	}

	public void update(){

		ArrayList<Renderable> tmp = new ArrayList<Renderable>();
		tmp.addAll(SceneProvider.getInstance().model2D.getRenderables());
		tmp.addAll(SceneProvider.getInstance().model3D.getRenderables());

		int counter = 0;
		for(Renderable r : tmp){
			if(r instanceof Function)
				counter += 1;
		}
		this.list.strings.clear();

		if(counter == 0){
			setList(new String[]{});
			return;
		}

		String[] list = new String[counter];

		counter = 0;
		for(int i=0; i<tmp.size(); i++){
			Renderable r = tmp.get(i);
			if(r instanceof Function){
				Function f = (Function)r;
				list[counter] = counter +". " + getFunctionPrefix(f.getType()) + f.getFunction();
				counter += 1;
			}
		}
		setList(list);
	}

	public void propertyChange(PropertyChangeEvent evt)
	{
		if(evt.getPropertyName().equals("Explorer.updateSelectionName")){
			update();
		}
	}

	private class EntryListModel extends AbstractListModel{
		private static final long serialVersionUID = 1L;

		public ArrayList<String> strings = new ArrayList<String>();
		public int getSize() { return strings.size(); }
		public Object getElementAt(int i) { return strings.get(i); }
		
	}

	private class SelectionListener implements ListSelectionListener{

		int currentIndex = -1;

		public void valueChanged(ListSelectionEvent e)
		{
			if(!e.getValueIsAdjusting()){
				if(e.getFirstIndex() == e.getLastIndex() && currentIndex != -1){
//				if(e.getFirstIndex() == e.getLastIndex() && e.getFirstIndex() == currentIndex){
					setCurrentFunction(null);
					currentIndex = -1;
				}
				else if(currentIndex != e.getFirstIndex())
				{
					setCurrentFunction(getFunctions().get(e.getFirstIndex()));
					currentIndex = e.getFirstIndex();
				}
				else if(currentIndex != e.getLastIndex()){
					setCurrentFunction(getFunctions().get(e.getLastIndex()));
					currentIndex = e.getLastIndex();
				}
			}
		}
	}

}
