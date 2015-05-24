/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package subido.glUtil;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import subido.math.*;
import subido.Keys;

/**
 *
 * @author Felix Patzschke
 */
public class Camera {
    
    /**
     * Vector that specifies WHERE the camera is in relation to its viewpoint. 
     * This one meant to be relative to the CENTER-Vector.
     */
    protected Vector eye;
    /** 
     * Vector that specifies TOWARDS what that camera is facing.
     * Non-relative
     */
    protected Vector center;
    /** 
     * Vector that specifies the camera's rotation, where "UP" is.
     * Surpisingly doesn't need to be orthogonal to the CENTER-EYE-line
     */
    protected Vector up;
    /**
     * Zooming speed factor
     * is defined as 1 in constructor, but can be changed.
     */
    protected float zF;
    /**
     * Field of view (Angle in Degrees)
     */
    protected double fov;
    
    /**
     * Constructor
     * 
     * @param ex the EYE-Vector's x-Component
     * @param ey the EYE-Vector's y-Component
     * @param ez the EYE-Vector's z-Component
     * @param cx the CENTER-Vector's x-Component
     * @param cy the CENTER-Vector's y-Component
     * @param cz the CENTER-Vector's z-Component
     * @param ux the UP-Vector's x-Component
     * @param uy the UP-Vector's y-Component
     * @param uz the UP-Vector's z-Component
     */
    public Camera(double ex, double ey, double ez, double cx, double cy, double cz, double ux, double uy, double uz) {
        this.eye = new Vector(ex, ey, ez);
        this.center = new Vector(cx, cy, cz);
        this.up = new Vector(ux, uy, uz);
        this.zF = 1f;
        this.fov = 45d;
    }
    
    /**
     * @return the relative EYE position
     */
    public Vector getEye(){
        return this.eye.plus(this.center);
    }
    
    /**
     * @return the CENTER-Vector
     */
    public Vector getCenter(){
        return this.center;
    }
    
    /**
     * @return the UP-Vector
     */
    public Vector getUp(){
        return this.up;
    }
    
    /**
     * @return relative zooming speed
     */
    public float getZoomFactor(){
        return this.zF;
    }
    
    /**
     * @return the Field of View (Angle in degrees)
     */
    public double getFOV(){
        return this.fov;
    }
    
    /**
     * Chages zoomin' speed.
     * 
     * @param zoomFactor new value for zoom speed
     */
    public void setZoomFactor(float zoomFactor){
        this.zF = zoomFactor;
    }
    
    /**
     * Rotates the camera. Plug the x- and y-Movement of your Mouse into this method
     * for rotating the camera as in a typical 3D-Viewing program.
     * TODO: add a condition, so that it doesn't wobble around for EYE || (0,0,1)
     * 
     * @param x the Mouse' x-motion
     * @param y the Mouse' y-motion
     */
    public void rotate(int x, int y){
        this.eye.rotate(Vector.k, -1*x);
        //TODO add condition, so that it doesn't wobble for eye || k 
        this.eye.rotate(Vector.e(eye.cross(Vector.k)), 1*y);
    }
    
    /**
     * Rotates the camera. Plug the x- and y-Movement of your Mouse into this method
     * for rotating the camera as in a typical 3D-Viewing program. Plug in a z-Value
     * in order to rotate the up-Vector.
     * 
     * @param x
     * @param y
     * @param z 
     */
    public void rotate(int x, int y, int z){
        this.rotate(x, y);
        this.up.rotate(eye, z);
    }
    
    /**
     * Rotates the relative EYE-Vector around the z-Axis.
     * Uses the given number of degrees.
     * 
     * @param zDegrees the angle to rotate for
     */
    public void rotate(int zDegrees){
        this.eye.rotate(Vector.k, -1*zDegrees);
    }
    
    /**
     * Translates the Camera
     * along the projection of the EYE-Vector for pressing up/down, 
     * Orthogonal to it for pressing left/right, 
     * 
     * @param evt KeyEvent to plug into this
     */
    public void translate(KeyEvent evt){
        if(evt.getExtendedKeyCode() == Keys.KEY_CODE_UP){
            this.center.add(new Vector(-eye.x, -eye.y, 0));
        }else if(evt.getExtendedKeyCode() == Keys.KEY_CODE_DOWN){
            this.center.add(new Vector(eye.x, eye.y, 0));
        }else if(evt.getExtendedKeyCode() == Keys.KEY_CODE_LEFT){
            this.center.add(new Vector(-eye.y, eye.x, 0));
        }else if(evt.getExtendedKeyCode() == Keys.KEY_CODE_RIGHT){
            this.center.add(new Vector(eye.y, -eye.x, 0));
        }
    }
    
    /**
     * Translates the camera. Plug the x- and y-Movement of your Mouse into this method
     * for rotating the camera as in a typical 3D-Viewing program.
     * TODO: add a condition, so that it doesn't wobble around for EYE || (0,0,1)
     * 
     * @param x the Mouse' x-motion
     * @param y the Mouse' y-motion
     */
    public void translate(double x, double y){
        this.center.add(new Vector(eye.y, -eye.x, 0).times(-x/eye.abs()));
        this.center.add(Vector.k.times(-y));
    }
    
    /**
     * Zooms the Camera
     * 
     * @param evt MouseWheelEvent to plug into this
     */
    public void zoom(MouseWheelEvent evt){
        this.eye.sMultiply(1 + (evt.getPreciseWheelRotation()*0.05*((double)zF)));
    }
    
    /**
     * Zooms the Camera
     * 
     * @param rot typically MouseWheelEvent.getPreciseWheelRotation()
     */
    public void zoom(double rot){
        this.eye.sMultiply(1 + (rot*0.05*((double)zF)));
    }
    
    private void cleanFOV(){
        fov = fov%360;
        fov = Math.abs(fov);
    }
    
    /**
     * Changes the Field of View.
     * @param rot typically MouseWheelEvent.getPreciseWheelRotation()
     */
    public void changeFOV(double rot){
        fov += rot;
        this.cleanFOV();
    }
    
    /**
     * Sets the FoV to a specific Value
     * @param newFOV new FoV (Angle in Degrees)
     */
    public void setFOV(double newFOV){
        this.fov = newFOV;
        this.cleanFOV();
    }
    
}
