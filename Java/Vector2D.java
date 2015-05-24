/*
insert package here
*/

import java.io.PrintStream;

/**
 * @author Felix H. Patzschke
 */
public class Vector2D {
    
    public double x,y;
    // Static Vectors
    public static final Vector2D e_x = new Vector2D(1d, 0d), 
                                 e_y = new Vector2D(0d, 1d),
                                   o = new Vector2D(0d, 0d);
    
    
    public Vector2D(double x, double y){
        this.x = x;
        this.y = y;
    }
    
    public Vector2D(Vector2D v){
        this.x = v.x;
        this.y = v.y;
    }
    
    
    // Operators which return the result to the first Vector3D operand
    public void normalize(){
        this.sMultiply(1d/this.abs());
    }
    
    public void add(Vector2D v){
        x = x+v.x;
        y = y+v.y;
    }
    
    public void subtract(Vector2D v){
        x = x-v.x;
        y = y-v.y;
    }
    
    public void sMultiply(double r){
        this.x = r*x;
        this.y = r*y;
    }
    
    public void applyMatrix(double[][] m) throws UnsupportedOperationException{
        if(m.length==2 && m[0].length==2){
            double a,b,c;
            a = x*m[0][0] + y*m[0][1];
            b = x*m[1][0] + y*m[1][1];
            this.x = a;
            this.y = b;
        }else{
            throw new java.lang.UnsupportedOperationException("Dimension error");
        }
    }
    
    public void rotate(int degrees){
        this.rotate(((double)degrees)*Math.PI/((double)180.0));
    }
    
    public void rotate(double alpha){
        
        double cos = Math.cos(alpha);
        double sin = Math.sin(alpha);
        
        double[][] m = {
            {cos,  -sin},
            {sin,   cos}
        };
        this.applyMatrix(m);
        
    }
    
    // Operators with one arg which return the result via the method itself
    public Vector2D plus(Vector2D v){
        return new Vector2D(this.x+v.x, this.y+v.y);
    }
    
    public Vector2D minus(Vector2D v){
        return new Vector2D(this.x-v.x, this.y-v.y);
    }
    
    public Vector2D times(double r){
        return new Vector2D(r*x, r*y);
    }
    
    public double angle(Vector2D v){
        return Math.acos(this.point(v)/(this.abs()*v.abs()));
    }
    
    public double point(Vector2D v){
        return x*v.x + y*v.y;
    }
    
    public double cross(Vector2D b){
        return x*b.y - y*b.x;
    }
    
    public Vector2D matrixProduct(double[][] m){
        if(m.length==2 && m[0].length==2){
            double a,b,c;
            a = x*m[0][0] + y*m[0][1];
            b = x*m[1][0] + y*m[1][1];
            return new Vector2D(a, b);
        }else{
            throw new java.lang.UnsupportedOperationException("Dimension error");
        }
    }
    
    public Vector2D getRotation(Vector3D axis, double rad){
        
        double cos = Math.cos(rad);
        double sin = Math.sin(rad);
        
        double[][] m = {
            {cos,  -sin},
            {sin,   cos}
        };
        return this.matrixProduct(m);
        
    }
    
    // Static Operators with complete set of args
    public static Vector2D e(Vector2D v){
        return v.times(1d/v.abs());
    }
    
    public static Vector2D plus(Vector2D a, Vector2D b){
        return a.plus(b);
    }
    
    public static Vector2D minus(Vector2D a, Vector2D b){
        return a.minus(b);
    }
    
    public static Vector2D times(double r, Vector2D v){
        return v.times(r);
    }
    
    public static double angle(Vector2D a, Vector2D b){
        return a.angle(b);
    }
    
    public static double point(Vector2D a, Vector2D b){
        return a.point(b);
    }
    
    public static double cross(Vector2D a, Vector2D b){
        return a.cross(b);
    }
    
    public static Vector2D getRotation(Vector2D v, double rad){
        
        double cos = Math.cos(rad);
        double sin = Math.sin(rad);
        
        double[][] m = {
            {cos,  -sin},
            {sin,   cos}
        };
        return v.matrixProduct(m);
        
    }
    
    // Other mathematical operators
    public double abs(){
        return Math.sqrt(x*x+y*y);
    }
    
    // Non-mathematical operators
    public void print(PrintStream stream){
        stream.println(  "/ " + x);
        stream.println( "\\ " + y);
    }
    
    public Vector parseVector(){
        return new Vector(x,y,0);
    }
    
    public static Vector parseVector(Vector2D v2d){
        return new Vector(v2d.x, v2d.y, 0);
    }
    
}