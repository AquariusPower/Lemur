package com.github.devconslejme;

import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.simsilica.lemur.GuiGlobals;
import com.simsilica.lemur.Panel;
import com.simsilica.lemur.component.BorderLayout;
import com.simsilica.lemur.component.QuadBackgroundComponent;
import com.simsilica.lemur.core.GuiComponent;
import com.simsilica.lemur.core.GuiControl;
import com.simsilica.lemur.dnd.DragAndDropControl;
import com.simsilica.lemur.dnd.DragAndDropListener;
import com.simsilica.lemur.dnd.DragEvent;
import com.simsilica.lemur.dnd.DragStatus;
import com.simsilica.lemur.dnd.Draggable;
import com.simsilica.lemur.style.Attributes;
import com.simsilica.lemur.style.ElementId;
import com.simsilica.lemur.style.StyleAttribute;
import com.simsilica.lemur.style.StyleDefaults;
import com.simsilica.lemur.style.Styles;

public class ResizablePanel extends Panel implements Draggable {
	private BorderLayout layout;
	private Panel contents;
	private int iBorderSize = 10; //TODO 3
	private QuadBackgroundComponent	qbcBorder = new QuadBackgroundComponent();
//	private ResizerCursorListener	clResizer = new ResizerCursorListener();
//	class ResizerCursorListener implements CursorListener{}
	
	private ResizerDragAndDropListener	dndlCursorListener = new ResizerDragAndDropListener();
	public Vector3f	v3fDragFrom;
	class ResizerDragAndDropListener implements DragAndDropListener{
		@Override
		public Draggable onDragDetected(DragEvent event) {
			if(event.getTarget() instanceof Draggable){
				return (Draggable)event.getTarget();
			}
			return null;
		}

		@Override
		public void onDragEnter(DragEvent event) {
			if(event.getTarget()==ResizablePanel.this){
				v3fDragFrom = event.getCollision().getContactPoint();
			}
		}

		@Override
		public void onDragExit(DragEvent event) {
			if(event.getTarget()==ResizablePanel.this){
				v3fDragFrom=null;
			}
		}

		@Override
		public void onDragOver(DragEvent event) {
			if(event.getTarget()==ResizablePanel.this){
				Vector3f v3fOldSize = new Vector3f(ResizablePanel.this.getPreferredSize());
				Vector3f v3fPanelCenter=ResizablePanel.this.getLocalTranslation().add(v3fOldSize.divide(2f));
				Vector3f v3fNewSize = v3fOldSize.clone();
				float fDeltaX = v3fDragFrom.x - event.getX();
				float fDeltaY = v3fDragFrom.y - event.getY();
				float fEdgeMultX=1.0f;if(event.getX()>v3fPanelCenter.x)fEdgeMultX=-1.0f;
				float fEdgeMultY=1.0f;if(event.getY()<v3fPanelCenter.y)fEdgeMultX=-1.0f;
				v3fNewSize.x-=fEdgeMultX*fDeltaX;
				v3fNewSize.y+=fEdgeMultY*fDeltaY;
				v3fDragFrom.x-=fDeltaX;
				v3fDragFrom.y-=fDeltaY;
				if(v3fNewSize.x<20)v3fNewSize.x=20;
				if(v3fNewSize.y<20)v3fNewSize.y=20;
				ResizablePanel.this.setPreferredSize(v3fNewSize);
			}
		}

		@Override
		public void onDrop(DragEvent event) {
			if(event.getTarget()==ResizablePanel.this){
			}
		}

		@Override
		public void onDragDone(DragEvent event) {
			if(event.getTarget()==ResizablePanel.this){
			}
		}
	}
	
	public static final String LAYER_RESIZABLE_BORDERS = "resizableBorders";
	
  public ResizablePanel( float width, float height, String style ) {
    super(false, new ElementId("resizablePanel"), style);
    
    this.layout = new BorderLayout();
    getControl(GuiControl.class).setLayout(layout);
    
    getControl(GuiControl.class).setPreferredSize(new Vector3f(width, height, 0));
    
    // Set our layers
    getControl(GuiControl.class).setLayerOrder(LAYER_INSETS, 
                                               LAYER_BORDER, 
                                               LAYER_BACKGROUND,
                                               LAYER_RESIZABLE_BORDERS);
    
    setBorder(qbcBorder);
    setBorderSize(iBorderSize);
    
    Styles styles = GuiGlobals.getInstance().getStyles();
    styles.applyStyles(this, getElementId().getId(), style);
    
    addControl(new DragAndDropControl(dndlCursorListener));
//    CursorEventControl.addListenersToSpatial(this, dndlCursorListener);
//    MouseEventControl.addListenersToSpatial(this, dndlCursorListener);
  }
	
  @StyleDefaults("resizablePanel")
  public static void initializeDefaultStyles( Attributes attrs ) {
      attrs.set( "resizableBorders", new QuadBackgroundComponent(ColorRGBA.Gray), false );
  }
  
	@StyleAttribute(value="resizableBorders", lookupDefault=false)
	public void setResizableBorders( GuiComponent bg ) {        
	    getControl(GuiControl.class).setComponent(LAYER_RESIZABLE_BORDERS, bg);   
	}
	
  public GuiComponent getResizableBorders() {
    return getControl(GuiControl.class).getComponent(LAYER_RESIZABLE_BORDERS);
  }
  
	/**
	 *  Resets the child contents that will be expanded/collapsed
	 *  with the rollup.
	 */
	public void setContents( Panel p ) {
	    if( this.contents == p ) {
	        return;
	    }
	    
	    if( this.contents != null) {
	        layout.removeChild(contents);
	    }
	    
	    this.contents = p;
	    if( this.contents != null ) {
	      if( contents.getParent() == null ) {
	        layout.addChild(contents,  BorderLayout.Position.Center);
//	        contents.setInsets(new Insets3f(iBorderSize, iBorderSize, iBorderSize, iBorderSize));
	      }
	    }
	}
	
	public void setBorderSize(int i){
		this.iBorderSize=i;
    qbcBorder.setMargin(iBorderSize, iBorderSize);
	}

	@Override
	public void setLocation(float x, float y) {
	}

	@Override
	public Vector2f getLocation() {
		return null;
	}

	@Override
	public void updateDragStatus(DragStatus status) {
	}

	@Override
	public void release() {
	}
	
}
