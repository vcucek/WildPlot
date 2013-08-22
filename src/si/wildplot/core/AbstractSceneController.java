package si.wildplot.core;

import si.wildplot.core.view.View;
import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import si.wildplot.core.render.Renderable;
import javax.media.opengl.GLContext;
import si.wildplot.common.util.GLTaskService;
import si.wildplot.common.util.Logging;
import si.wildplot.core.jocl.BasicCLContext;
import si.wildplot.core.layer.Layer;
import si.wildplot.core.render.Model;

/**
 *
 * @author vito
 */
public abstract class AbstractSceneController extends WildPlotObjectImpl{

	private DrawContext dc = null;
	private View view = null;
	private Model model = null;
	private BasicCLContext clContext = null;

	private GLTaskService taskService = GLTaskService.getInstance();

	public AbstractSceneController(){
		dc = new DrawContextImpl();
	}

	protected void initializeDrawContext(DrawContext dc){

		if (GLContext.getCurrent() == null)
        {
            String msg = Logging.getMessage("GLContext is not current on this thread");
            Logging.logger().severe(msg);
            throw new IllegalArgumentException(msg);
        }

		dc.initialize(GLContext.getCurrent());
		dc.setView(this.view);
		dc.setModel(this.model);
		dc.setCLContext(clContext);
	}

	public int repaint(){
		initializeDrawContext(this.dc);
		taskService.flush(dc);
		doRepaint(dc);
		DrawContextImpl.redrawRequest -= 1;
		return DrawContextImpl.redrawRequest;
	}

	public View getView(){
		return this.view;
	}

	public void setView(View view){
		if (this.view != null)
            this.view.removePropertyChangeListener(this);
        if (view != null)
            view.addPropertyChangeListener(this);

        View oldView = this.view;
        this.view = view;

        this.firePropertyChange(PCKey.VIEW, oldView, view);
	}

	public Model getModel()
	{
		return this.model;
	}

	public void setModel(Model model)
	{
		if (this.model != null)
            this.model.removePropertyChangeListener(this);
        if (model != null)
            model.addPropertyChangeListener(this);

        Model oldModel = this.model;
        this.model = model;
        this.firePropertyChange(PCKey.MODEL, oldModel, model);
	}

	public void setCLContext(BasicCLContext clContext){
		this.clContext = clContext;
	}

	public void addRenderable(Renderable r){
		if(this.dc != null){
			this.dc.addRenderable(r);
		}
	}

	public void removeRenderable(Renderable r){
		if(this.dc != null){
			this.dc.removeRenderable(r);
		}
	}

	protected void initializeFrame(DrawContext dc){
		GL2 gl = dc.getGL().getGL2();
		gl.glPushAttrib(GL2.GL_VIEWPORT_BIT | GL2.GL_ENABLE_BIT | GL2.GL_TRANSFORM_BIT);

        gl.glMatrixMode(GL2.GL_MODELVIEW);
        gl.glPushMatrix();
        gl.glLoadIdentity();

        gl.glMatrixMode(GL2.GL_PROJECTION);
        gl.glPushMatrix();
        gl.glLoadIdentity();

        gl.glEnable(GL.GL_DEPTH_TEST);
	}

	protected void finalizeFrame(DrawContext dc){
		GL2 gl = dc.getGL().getGL2();

        gl.glMatrixMode(GL2.GL_MODELVIEW);
        gl.glPopMatrix();

        gl.glMatrixMode(GL2.GL_PROJECTION);
        gl.glPopMatrix();

        gl.glPopAttrib();
	}

	protected void clear(DrawContext dc){
		dc.getGL().glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        dc.getGL().glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
	}

	protected void applyView(DrawContext dc){
		this.dc.getView().apply(dc);
	}

	protected void preRender(DrawContext dc){
		for(Layer layer : this.model.getLayers()){
			layer.preRender(dc);
		}
	}

	protected void pick(DrawContext dc){
		if(!dc.isPickingEnabled())
			return;

		for(Layer layer : this.model.getLayers()){
			if(layer.isPickEnabled()){
				layer.pick(dc);
			}
		}

	}

	protected void draw(DrawContext dc){
		for(Layer layer : this.model.getLayers()){
			layer.render(dc);
		}
	}

	protected abstract void doRepaint(DrawContext dc);

}
