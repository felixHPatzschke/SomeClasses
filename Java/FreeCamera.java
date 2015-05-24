/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package subido.glUtil;

import subido.math.Vector;

/**
 *
 * @author Felix Patzschke
 */
public class FreeCamera extends Camera{

    private final boolean overrideRotation, overrideTranslation;
    
    public FreeCamera(double ex, double ey, double ez, double cx, double cy, double cz, double ux, double uy, double uz, boolean overrideRotation, boolean overrideTranslation) {
        super(ex, ey, ez, cx, cy, cz, ux, uy, uz);
        this.overrideRotation = overrideRotation;
        this.overrideTranslation = overrideTranslation;
        if(this.overrideRotation){
            up = up.getPerpendicularComponent(eye);
            up.normalize();
        }
    }
    
    
    @Override
    public void rotate(int x, int y){
        if(this.overrideRotation){
            this.eye.rotate(up.getNormalized(), -x);
            
            Vector axis = eye.cross(up);
            axis.normalize();
            this.eye.rotate(axis, y);
            this.up.rotate(axis, y);
        }else{
            super.rotate(x, y);
        }
    }
    
    
    @Override
    public void translate(double x, double y){
        if(this.overrideTranslation){
            this.center.add(up.getPerpendicularComponent(eye).getNormalized().times(y));
            this.center.add(up.cross(eye).getNormalized().times(x).getInversion());
        }else{
            super.translate(x, y);
        }
    }
    
    
}
