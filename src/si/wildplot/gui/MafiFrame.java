package si.wildplot.gui;

import com.jogamp.opengl.util.FPSAnimator;
import java.awt.EventQueue;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPopupMenu;
import javax.swing.UIManager;
import javax.swing.WindowConstants;
import si.wildplot.core.view.BasicView;
import si.wildplot.core.view.View;
import si.wildplot.core.Window;
import si.wildplot.core.WindowGLCanvas;
import si.wildplot.core.SceneProvider;
import si.wildplot.core.input.ViewInputHandler;
import si.wildplot.core.input.ViewInputHandler2D;
import si.wildplot.core.render.BasicModel;
import si.wildplot.core.render.CoordinateSystem2D;
import si.wildplot.core.render.CoordinateSystem3D;
import si.wildplot.core.render.Model;
import si.wildplot.core.render.Plot3D;
import si.wildplot.core.render.SoftBody;
import si.wildplot.core.render.TestSample;
import si.wildplot.core.view.View2D;
import si.wildplot.core.view.ViewCombined3D2D;

/**
 * @author Vito Cucek
 */
public class MafiFrame extends JFrame{


    static {
        // When using a GLCanvas, we have to set the Popup-Menues to be HeavyWeight,
        // so they display properly on top of the GLCanvas
        JPopupMenu.setDefaultLightWeightPopupEnabled(false);
    }

	private static final int FPS = 60;
	
	private static final long serialVersionUID = 1L;
    private WindowGLCanvas glCanvas;
	private Explorer explorer;
	private Operations operations;
    private final FPSAnimator animator;

    public MafiFrame() {
        initComponents();
        setTitle("Mafi");

        animator = new FPSAnimator(glCanvas, FPS, true);

        this.addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent e) {
                // Run this on another thread than the AWT event queue to
                // make sure the call to Animator.stop() completes before
                // exiting
                new Thread(new Runnable() {

                    public void run() {
						
						if(animator.isStarted()){
							animator.stop();
						}
						
						if(glCanvas != null){
							((Window)glCanvas).shutdown();
						}

                        System.exit(0);
                    }
                }).start();
            }
        });
    }

    @Override
    public void setVisible(boolean show){
        if(!show)
            animator.stop();
        super.setVisible(show);
        if(!show)
            animator.start();
    }

    private void initComponents() {
        JLabel label = new JLabel();
        this.glCanvas = SceneProvider.getInstance().window;

		Model model2D = SceneProvider.getInstance().model2D;
		model2D.addLayer("functions", 10);
		model2D.addLayer("coordsys", 20);

		Model model3D = SceneProvider.getInstance().model3D;
		model3D.addLayer("functions", 10);
		model3D.addLayer("coordsys", 20);

		View view2D = SceneProvider.getInstance().view2D;
		View view3D = SceneProvider.getInstance().view3D;

		boolean is3D = false; 

		if(is3D){
			glCanvas.setModel(model3D);
			glCanvas.setView(view3D);

			model3D.setCurrentLayer("coordsys");
			model3D.addRenderable(new CoordinateSystem3D());
			model3D.setCurrentLayer("functions");

			model3D.addRenderable(new Plot3D("5.0*(sin(x)+cos(y))"));
		}
		else{
			glCanvas.setModel(model2D);
			glCanvas.setView(view2D);

			model3D.setCurrentLayer("coordsys");
			model3D.addRenderable(new CoordinateSystem3D());
			model3D.setCurrentLayer("functions");

			model2D.setCurrentLayer("coordsys");
			model2D.addRenderable(new CoordinateSystem2D());
			model2D.setCurrentLayer("functions");
		}
		
//		glCanvas.setInputHandler(inputHandler2D);

//		glCanvas.getModel().addRenderable(new TestSample());
//		glCanvas.getModel().addRenderable(new Plot2D());

		//////////////test//////////////////
//		glCanvas.getModel().addRenderable(new Plot3D("100.0*(sin(1.0/x)+cos(1.0/y))"));
//		glCanvas.getModel().addRenderable(new Plot3D("(pow(2.5,x)+cos(y))"));
//		model3D.addRenderable(new Plot3D("x*y"));
//		model3D.addRenderable(new Plot3D("sin(x*y)/x"));
//		model3D.addRenderable(new Plot3D("(y+log(x))"));
		//glCanvas.getModel().addRenderable(new Plot3D("cos(x)+0.1*cos(10.0*x)+0.01*cos(100.0*x)+0.1*sin(10.0*y)+0.01*sin(100.0*y)+sin(y)"));
//		glCanvas.getModel().addRenderable(new Plot3D("step(0.0,x) * step(0.0,y)"));
		////////////////////////////////////

		/////Soft body animation test//////////
		
		//SoftBody sb = new SoftBody();
		//glCanvas.getInputHandler().addMouseMotionListener(sb);
		//model2D.addRenderable(sb);

		///////////////////////////////////////

        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);

        label.setText("Below you see a GLCanvas");

		explorer = new Explorer(this.glCanvas);
		operations = new Operations(((Window)this.glCanvas).getCLContext());
		InputField inputField = new InputField(this.glCanvas);
		inputField.addPropertyChangeListener(this.operations);

		//connect inputfiled and explorer
		inputField.addPropertyChangeListener(explorer);
		explorer.addPropertyChangeListener(inputField);

        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);

		/////////////////Horizontal group/////////////////////
		GroupLayout.ParallelGroup horizontalG = layout.createParallelGroup(Alignment.LEADING);
		GroupLayout.SequentialGroup expAndCanvasH = layout.createSequentialGroup();
//		GroupLayout.SequentialGroup sgH = layout.createSequentialGroup();

		horizontalG.addGroup(layout.createSequentialGroup()
                    .addComponent(inputField));

		expAndCanvasH.addGroup(layout.createSequentialGroup()
					.addGroup(layout.createParallelGroup()
                    	.addComponent(explorer)
                    	.addComponent(operations)));

		expAndCanvasH.addGroup(layout.createSequentialGroup()
                    .addComponent(glCanvas, GroupLayout.DEFAULT_SIZE, 380, Short.MAX_VALUE));

		horizontalG.addGroup(expAndCanvasH);
		layout.setHorizontalGroup(horizontalG);

		/////////////////Vertical group/////////////////////
		GroupLayout.SequentialGroup verticalG = layout.createSequentialGroup();
		GroupLayout.ParallelGroup expAndCanvasV = layout.createParallelGroup(Alignment.TRAILING);

		verticalG.addGroup(layout.createSequentialGroup()
                    .addComponent(inputField));

		expAndCanvasV.addGroup(layout.createParallelGroup(Alignment.LEADING)
					.addGroup(layout.createSequentialGroup()
                    	.addComponent(explorer)
                    	.addComponent(operations)));

//		expAndCanvasV.addGroup(layout.createSequentialGroup()

		expAndCanvasV.addGroup(layout.createSequentialGroup()
//                .addPreferredGap(ComponentPlacement.RELATED)
                .addComponent(glCanvas, GroupLayout.DEFAULT_SIZE, 255, Short.MAX_VALUE));

		verticalG.addGroup(expAndCanvasV);
		layout.setVerticalGroup(verticalG);

        pack();
    }

    public static void main(String args[]) {
        // Run this in the AWT event thread to prevent deadlocks and race conditions
        EventQueue.invokeLater(new Runnable() {
            public void run() {

                // switch to system l&f for native font rendering etc.
                try{
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                }catch(Exception ex) {
                    Logger.getLogger(getClass().getName()).log(Level.INFO, "can not enable system look and feel", ex);
                }
				System.setProperty("sun.awt.noerasebackground", "true");

                MafiFrame frame = new MafiFrame();
                frame.setVisible(true);
            }
        });
    }
}
