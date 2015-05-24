/*
insert package here
*/

import java.io.PrintStream;

/**
 * @author Felix H. Patzschke
 * A Java class that pretty much sums up the mathematical Object known as "Vector".
 */
public class Vector {
    
    /**
     * Component
     */
    public double x,y,z;
    
    /**
     * Normalized Coordinate-Axis-Vector
     */
    public static final Vector i = new Vector(1,0,0), 
                               j = new Vector(0,1,0), 
                               k = new Vector(0,0,1);
    /**
     * Neutral element of Vector addition
     */
    public static final Vector o = new Vector(0,0,0);
    
    /**
     * Creates a Vector with the components x, y and z.
     * @param x x-Component
     * @param y y-Component
     * @param z z-Component 
     */
    public Vector(double x, double y, double z){
        this.x = x;
        this.y = y;
        this.z = z;
    }
    
    /**
     * Creates a copy of the Vector {@code v}.
     * @param v Vector to create a copy of
     */
    public Vector(Vector v){
        this.x = v.x;
        this.y = v.y;
        this.z = v.z;
    }
    
    // Getters and Setters
    public synchronized double getX(){
        return x;
    }

    public synchronized void setX(double x) {
        this.x = x;
    }

    public synchronized double getY() {
        return y;
    }

    public synchronized void setY(double y) {
        this.y = y;
    }

    public synchronized double getZ() {
        return z;
    }

    public synchronized void setZ(double z) {
        this.z = z;
    }

    // Random Vectors
    public static Vector randN() {
        return random(0d, 1d);
    }
    
    public static Vector random(Vector max){
        return new Vector(Math.random()*max.x, Math.random()*max.y, Math.random()*max.z);
    }
    
    public static Vector random(double minAbs, double maxAbs){
        Vector v = new Vector(1,0,0);
        v.rotate(Vector.j, Math.random()*Math.PI*2d);
        v.rotate(Vector.k, Math.random()*Math.PI*2d);
        v.sMultiply(minAbs + Math.random()*(maxAbs-minAbs));
        return v;
    }
    
    public static Vector randN2D(){
        return random2D(0d, 1d);
    }
    
    public static Vector random2D(Vector2D max){
        return new Vector(Math.random()*max.x, Math.random()*max.y, 0);
    }
    
    public static Vector random2D(double minAbs, double maxAbs){
        Vector v = new Vector(1,0,0);
        v.rotate(Vector.j, Math.random()*Math.PI*2d);
        v.rotate(Vector.k, Math.random()*Math.PI*2d);
        v.sMultiply(minAbs + Math.random()*(maxAbs-minAbs));
        return v;
    }
    
    // Operators which return the result to the first Vector3D operand
    /**
     * Scales the Vector to an absolute Value of 1
     */
    public void normalize(){
        this.sMultiply(1d/this.abs());
    }
    
    /**
     * The resulting Vector points in the opposite direction. Otherwise it stays 
     * the same. Essentially it's s-multiplication by -1. I honestly don't get, 
     * why you would need a method only for this. Cant you just use {@code v.sMultiply(-1)} 
     * or {@code v.rotate(Vector.i, 180)}? Are you so damn lazy that you can't 
     * type in 7 more characters?
     */
    public void invert(){
        this.sMultiply(-1);
    }
    
    /**
     * Adds the specified Vector {@code v}
     * @param v Vector to add
     */
    public void add(Vector v){
        x = x+v.x;
        y = y+v.y;
        z = z+v.z;
    }
    
    /**
     * Subtracts the specified Vector {@code v}
     * @param v Vector to subtract
     */
    public void subtract(Vector v){
        x = x-v.x;
        y = y-v.y;
        z = z-v.z;
    }
    
    /**
     * Conducts s-multiplication of the Vector with the given real Number {@code r}
     * @param r rational number to s-multiply with
     */
    public void sMultiply(double r){
        this.x = r*x;
        this.y = r*y;
        this.z = r*z;
    }
    
    /**
     * Multiplies the Vector with the given Matrix {@code m}, specified as the
     * array of rational numbers. (Transforms the Vector using the Matrix) Typically the matrix only needs to have as much
     * columns as the Vector has components, but as of now this method only works
     * with 3x3-Matrixes
     * @param m Matrix to multiply with ([x][y])
     * @throws UnsupportedOperationException in case the Matrix' dimensions are not 3x3
     */
    public void applyMatrix(double[][] m) throws UnsupportedOperationException{
        if(m.length==3 && m[0].length==3){
            double a,b,c;
            a = x*m[0][0] + y*m[0][1] + z*m[0][2];
            b = x*m[1][0] + y*m[1][1] + z*m[1][2];
            c = x*m[2][0] + y*m[2][1] + z*m[2][2];
            this.x = a;
            this.y = b;
            this.z = c;
        }else{
            throw new java.lang.UnsupportedOperationException("Dimension error");
        }
    }
    
    /**
     * Rotates the Vector around the given axis, specified by the Vector 
     * {@code axis} at the given angle {@code degrees} anti-clockwise. The 
     * Vector needs to be normalized. The angle can only be a whole number. Uses 
     * the {@link applyMatrix}-Method.
     * @param axis Vector that specifies the axis to rotate around
     * @param degrees angle in degrees
     */
    public void rotate(Vector axis, int degrees){
        this.rotate(axis, ((double)degrees)*Math.PI/((double)180.0));
    }
    
    /**
     * Rotates the Vector. The given Vector specifies the axis of rotation
     * with its direction, and the value of the rotation by its length.
     * @param angVel angular Velocity (sort of)
     */
    public void rotate(Vector angVel){
        this.rotate(angVel.getNormalized(), angVel.abs());
    }
    
    /**
     * Rotates the Vector around the given axis at the given angle.
     * Axis is specified by the Vector {@code a}. Angle is specified by {@code rad}.
     * The Axis-Vector needs to be normalized. Rotation is anti-clockwise.
     * Uses the {@link applyMatrix}-Method.
     * @param a Vector that specifies the axis to rotate around
     * @param rad angle in radians
     */
    public void rotate(Vector a, double rad){
        
        /*
         *  nx�(1-cos) + cos        nxny(1-cos) - nz sin    nxnz(1-cos) + ny sin
         *  nxny(1-cos) + nz sin    ny�(1-cos) + cos        nynz(1-cos) - nx sin
         *  nxnz(1-cos) - ny sin    nzny(1-cos) + nx sin    nz�(1-cos) + cos
         */
        
        double cos = Math.cos(rad);
        double sin = Math.sin(rad);
        
        double[][] m = {
            {a.x*a.x*(1.0-cos) + cos,           a.y*a.x*(1.0-cos) - a.z*sin,    a.z*a.x*(1.0-cos) + a.y*sin},
            {a.x*a.y*(1.0-cos) + a.z*sin,       a.y*a.y*(1.0-cos) + cos,        a.z*a.y*(1.0-cos) - a.x*sin},
            {a.x*a.z*(1.0-cos) - a.y*sin,       a.y*a.z*(1.0-cos) + a.x*sin,    a.z*a.z*(1.0-cos) + cos}
        };
        this.applyMatrix(m);
        
    }
    
    // Operators with one arg which return the result via the method itself
    /**
     * @return the normalized Vector
     */
    public Vector getNormalized(){
        return Vector.e(this);
    }
    
    /**
     * The resulting Vector points in the opposite direction. Otherwise it stays 
     * the same. Essentially it's s-multiplication by -1. I honestly don't get, 
     * why you would need a method only for this. Cant you just use {@code v.times(-1)} 
     * or {@code v.getRotation(Vector.i, 180)}? Are you so damn lazy?
     * @return the goddamn inverted Vector
     */
    public Vector getInversion(){
        return this.times(-1);
    }
    
    /**
     * Adds the Vector to the given Vector {@code v}.
     * @param v the Vector to add
     * @return the vector sum
     */
    public Vector plus(Vector v){
        return new Vector(this.x+v.x, this.y+v.y, this.z+v.z);
    }
    
    /**
     * Subtracts the given Vector {@code v}.
     * @param v the Vector to subtract
     * @return the difference vector
     */
    public Vector minus(Vector v){
        return new Vector(this.x-v.x, this.y-v.y, this.z-v.z);
    }
    
    /**
     * S-Multiplies (scales) the given real number {@code r} with the Vector. 
     * @param r the rational Number to multiply with
     * @return the rescaled Vector
     */
    public Vector times(double r){
        return new Vector(r*x, r*y, r*z);
    }
    
    /**
     * Angle between Vectors. Result is always non-negative and smaller than 180° or PI.
     * @param v the Vector to get the respective angle to
     * @return the angle between the Vector and another Vector {@code v}
     */
    public double angle(Vector v){
        return Math.acos(this.point(v)/(this.abs()*v.abs()));
    }
    
    /**
     * Scalar- or Point-Product
     * @param v the Vector to multiply with
     * @return scalar product of the Vectors
     */
    public double point(Vector v){
        return x*v.x + y*v.y + z*v.z;
    }
    
    /**
     * Vector- or Cross-Product. Resulting Vector is always orthogonal to both
     * operands. Also |a x b| = |a|*|b|*sine of the angle between a and b.
     * @param b the Vector to multiply with.
     * @return vector product of the Vectors
     */
    public Vector cross(Vector b){
        return new Vector(
                y*b.z - z*b.y,
                z*b.x - x*b.z,
                x*b.y - y*b.x
        );
    }
    
    /**
     * Multiplies the Vector with the given Matrix {@code m}, 
     * specified as the array of rational numbers. (Transforms the Vector using
     * the Matrix) Typically the matrix only needs to have as much
     * columns as the Vector has components, but as of now this method only works
     * with 3x3-Matrixes.
     * @param m the Matrix to multiply with
     * @return transformed Vector
     */
    public Vector matrixProduct(double[][] m){
        if(m.length==3 && m[0].length==3){
            double a,b,c;
            a = x*m[0][0] + y*m[0][1] + z*m[0][2];
            b = x*m[1][0] + y*m[1][1] + z*m[1][2];
            c = x*m[2][0] + y*m[2][1] + z*m[2][2];
            return new Vector(a, b, c);
        }else{
            throw new java.lang.UnsupportedOperationException("Dimension error");
        }
    }
    
    /**
     * Rotates the Vector around the given axis, specified by the Vector 
     * {@code axis} at the given angle {@code degrees} anti-clockwise. The 
     * Vector needs to be normalized. Uses the {@link applyMatrix}-Method.
     * @param axis Vector that specifies the axis to rotate around
     * @param rad angle in radians
     * @return the rotated Vector
     */
    public Vector getRotation(Vector axis, double rad){
        
        /*
         *  nx�(1-cos) + cos        nxny(1-cos) - nz sin    nxnz(1-cos) + ny sin
         *  nxny(1-cos) + nz sin    ny�(1-cos) + cos        nynz(1-cos) - nx sin
         *  nxnz(1-cos) - ny sin    nzny(1-cos) + nx sin    nz�(1-cos) + cos
         */
        
        double cos = Math.cos(rad);
        double sin = Math.sin(rad);
        
        double[][] m = {
            {axis.x*axis.x*(1.0-cos) + cos,           axis.y*axis.x*(1.0-cos) - axis.z*sin,    axis.z*axis.x*(1.0-cos) + axis.y*sin},
            {axis.x*axis.y*(1.0-cos) + axis.z*sin,    axis.y*axis.y*(1.0-cos) + cos,           axis.z*axis.y*(1.0-cos) - axis.x*sin},
            {axis.x*axis.z*(1.0-cos) - axis.y*sin,    axis.y*axis.z*(1.0-cos) + axis.x*sin,    axis.z*axis.z*(1.0-cos) + cos}
        };
        return this.matrixProduct(m);
        
    }
    
    /**
     * Projects the Vector onto the given Vector v.
     * @param v the Vector to project onto
     * @return the parallel Component to v
     */
    public Vector getParallelComponent(Vector v){
        return e(v).times(this.point(v)/v.abs());
    }
    
    /**
     * Projects the Vector onto the given Vector v. Result can be negative
     * @param v the Vector to project onto
     * @return the oriented value of the parallel Component to v
     */
    public double getParallelComponentValue(Vector v){
        return (this.point(v)/v.abs());
    }
    
    /**
     * Projects the Vector onto a plane, to which v is normal.
     * Can also be seen as the smallest Vector b, for which {@code this.cross(b)}
     * would give the same result as {@code this.cross(v)}.
     * @param v normal Vector to the plane to project onto
     * @return orthogonal Component to v
     */
    public Vector getPerpendicularComponent(Vector v){
        return this.minus(this.getParallelComponent(v));
    }
    
    // Static Operators with complete set of args
    /**
     * Normalizes the Argument. Result has an absolute value of 1.
     * @param v Vector to normalize
     * @return the normalized Vector
     */
    public static Vector e(Vector v){
        return v.times(1d/v.abs());
    }
    
    /**
     * The resulting Vector points in the opposite direction. Otherwise it stays 
     * the same. Essentially it's s-multiplication by -1. I honestly don't get, 
     * why you would need a method only for this. Cant you just use {@code v.times(-1)} 
     * or {@code v.getRotation(Vector.i, 180)}? Are you so damn lazy?
     * @param v the Vector you want to multiply by frickin' -1
     * @return the goddamn inverted Vector
     */
    public static Vector anti(Vector v){
        return v.times(-1);
    }
    
    /**
     * Adds the given Vectors.
     * @param a 
     * @param b
     * @return the vector sum
     */
    public static Vector plus(Vector a, Vector b){
        return a.plus(b);
    }
    
    /**
     * Subtracts Vector {@code b} from {@code a}.
     * @param a 
     * @param b 
     * @return the vector difference
     */
    public static Vector minus(Vector a, Vector b){
        return a.minus(b);
    }
    
    /**
     * S-Multiplies the Vector {@code v} with te real number {@code r}.
     * @param r
     * @param v
     * @return the rescaled Vector
     */
    public static Vector times(double r, Vector v){
        return v.times(r);
    }
    
    /**
     * Calculates the angle between the given Vectors {@code a} and {@code b}. 
     * Result is always a non-negative real number smaller than 180° or PI.
     * @param a
     * @param b
     * @return the angle between the Vectors
     */
    public static double angle(Vector a, Vector b){
        return a.angle(b);
    }
    
    /**
     * Calculates the scalar- or point-product of the given Vectors {@code a} and {@code b}.
     * @param a
     * @param b
     * @return the scalar- or point-product
     */
    public static double point(Vector a, Vector b){
        return a.point(b);
    }
    
    /**
     * Calculates the vector- or cross-product of the given Vectors {@code a} and {@code b}.
     * @param a
     * @param b
     * @return vector- or cross-product
     */
    public static Vector cross(Vector a, Vector b){
        return a.cross(b);
    }
    
    /**
     * Rotates the Vector {@code v} around the axis, specified by the Vector {@code axis},
     * at the angle {@code rad}. The Axis-specifying Vector needs to be normalized.
     * Rotation is anti-clockwise.
     * @param v    
     * @param axis the Axis-Vector (Needs to be normalized)
     * @param rad  the angle in radians
     * @return rotated Vector
     */
    public static Vector getRotation(Vector v, Vector axis, double rad){
        
        /*
         *  nx�(1-cos) + cos        nxny(1-cos) - nz sin    nxnz(1-cos) + ny sin
         *  nxny(1-cos) + nz sin    ny�(1-cos) + cos        nynz(1-cos) - nx sin
         *  nxnz(1-cos) - ny sin    nzny(1-cos) + nx sin    nz�(1-cos) + cos
         */
        
        double cos = Math.cos(rad);
        double sin = Math.sin(rad);
        
        double[][] m = {
            {axis.x*axis.x*(1.0-cos) + cos,           axis.y*axis.x*(1.0-cos) - axis.z*sin,    axis.z*axis.x*(1.0-cos) + axis.y*sin},
            {axis.x*axis.y*(1.0-cos) + axis.z*sin,    axis.y*axis.y*(1.0-cos) + cos,           axis.z*axis.y*(1.0-cos) - axis.x*sin},
            {axis.x*axis.z*(1.0-cos) - axis.y*sin,    axis.y*axis.z*(1.0-cos) + axis.x*sin,    axis.z*axis.z*(1.0-cos) + cos}
        };
        return v.matrixProduct(m);
        
    }
    
    // Other mathematical operators
    /**
     * @return the absolute value of the Vector
     */
    public double abs(){
        return Math.sqrt(x*x+y*y+z*z);
    }
    
    /**
     * The length of the Vector squared (Comes in handy for g- or e-field-vectors)
     * @return the Vector's absolute value squared
     */
    public double absSqr(){
        return x*x+y*y+z*z;
    }
    
    @Override
    public boolean equals(Object o){
        if(o.getClass() == Vector.class){
            return this.equals((Vector)o);
        }else{
            return super.equals(o);
        }
    }
    
    @Override
    public String toString(){
        return "( " + String.format("%.4g%n", x) + " | " + String.format("%.4g%n", y) + " | " + String.format("%.4g%n", z) + " )";
    }
    
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + (int) (Double.doubleToLongBits(this.x) ^ (Double.doubleToLongBits(this.x) >>> 32));
        hash = 97 * hash + (int) (Double.doubleToLongBits(this.y) ^ (Double.doubleToLongBits(this.y) >>> 32));
        hash = 97 * hash + (int) (Double.doubleToLongBits(this.z) ^ (Double.doubleToLongBits(this.z) >>> 32));
        return hash;
    }
    
    /**
     * Determines whether two Vectors are identical
     * @param v Vector to be checked against
     * @return true if the Vectors are equal, flase if not
     */
    public boolean equals(Vector v){
        if(x==v.x){
            if(y==v.y){
                if(z==v.z){
                    return true;
                }
            }
        }
        return false;
    }
    
    /**
     * @param v the Vector to get the absolute Value of
     * @return the absolute value of the given Vector
     */
    public static double abs(Vector v){
        return Math.sqrt(v.x*v.x+v.y*v.y+v.z*v.z);
    }
    
    /**
     * The length of the Vector squared (Comes in handy for g- or e-field-vectors)
     * @param v the Vector to get the abs squared of
     * @return the Vector's absolute value squared
     */
    public static double absSqr(Vector v){
        return v.absSqr();
    }
    
    // Non-mathematical operators
    /**
     * Prints the Vector's components.
     * @param stream typically System.out
     */
    public void print(PrintStream stream){
        stream.println(  "/ " + x + " \\");
        stream.println(  "| " + y + " |");
        stream.println( "\\ " + z + " /");
    }
    
}
