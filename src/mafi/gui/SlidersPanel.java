package mafi.gui;

import javax.swing.GroupLayout.ParallelGroup;
import javax.swing.GroupLayout.SequentialGroup;
import mafi.common.util.SpecialParameter;
import mafi.core.Window;

/**
 *
 * @author vito
 */
public class SlidersPanel extends javax.swing.JPanel{
	private static final long serialVersionUID = 1L;

	private boolean enabled = false;;

	public SlidersPanel()
	{
		clear();
	}

	public void setSliders(Window wd, SpecialParameter[] spParams){
		Slider[] sliders = new Slider[spParams.length];
		for(int i=0; i<spParams.length; i++){
			sliders[i] = new Slider(wd, spParams[i]);
		}

		this.setSliders(sliders);
	}

	public final void setSliders(Slider[] sliders){

		if(enabled){
			this.removeAll();
		}

		javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);

		ParallelGroup horizontal = layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING);

		for(Slider s : sliders){
			horizontal.addComponent(s);
			this.enabled = true;
		}

		SequentialGroup vertical = layout.createSequentialGroup();

		for(Slider s : sliders){
			vertical.addComponent(s);
			this.enabled = true;
		}

		layout.setHorizontalGroup(horizontal);
		layout.setVerticalGroup(vertical);

		this.validate();
	}

	public final void clear(){
		this.removeAll();
		javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);

		ParallelGroup horizontal = layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING);

		horizontal.addGap(0);

		SequentialGroup vertical = layout.createSequentialGroup();

		vertical.addGap(0);

		layout.setHorizontalGroup(horizontal);
		layout.setVerticalGroup(vertical);

		this.validate();
		this.enabled = false;
	}
}
