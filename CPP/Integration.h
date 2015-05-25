/**
 *@author Felix
 */

namespace phs
{
	
	/**
	 * vector integration methods
	 */
	Vector integrate_eulerForward(Vector(*integrand)(Vector), Vector a, Vector b, unsigned int precision)
	{
		Vector dx = b-a*(1.0/precision);
		dx *= (1.0/precision);
		Vector integral = Vector(0,0,0);
		Vector arg = a;
		integral += integrand(arg);
		for(unsigned int c=1; c<precision; c++)
		{
			arg += dx;
			integral += integrand(arg);
		}
		return (integral*(dx.abs()));
	}

	Vector integrate_eulerBackward(Vector(*integrand)(Vector), Vector a, Vector b, unsigned int precision)
	{
		Vector dx = b-a*(1.0/precision);
		dx *= (1.0/precision);
		Vector integral = Vector(0,0,0);
		Vector arg = a;
		for(unsigned int c=0; c<precision; c++)
		{
			arg += dx;
			integral += integrand(arg);
		}
		return (integral*(dx.abs()));
	}

	Vector integrate_eulerTriangles(Vector(*integrand)(Vector), Vector a, Vector b, unsigned int precision)
	{
		Vector dx = (b-a)*(1.0/precision);
		Vector integral = Vector(0,0,0);
		Vector arg = a;
		
		integral += (integrand(arg)+integrand(arg+dx));
		for(unsigned int c=1; c<precision; c++)
		{
			arg += dx;
			integral += (integrand(arg)+integrand(arg+dx));
		}
		return (integral*0.5*dx.abs());
	}

	/**
	 * double integration methods
	 */
	double integrate_eulerForward(double(*integrand)(double), double a, double b, unsigned int precision)
	{
		double dx = dx = (b-a)/precision;
		double integral = 0;
		for( double x=a; a<=x&&x<b-(dx/2.0) || a>=x&&x>b+(dx/2.0); x+=dx )
		{
			integral += integrand(x);
		}
		return (integral*dx);
	}

	double integrate_eulerBackward(double(*integrand)(double), double a, double b, unsigned int precision)
	{
		double dx = (b-a)/precision;

		double integral = 0;
		for( double x=a+dx; a+dx<=x&&x<b+(dx/2.0) || a+dx>=x&&x>b-(dx/2.0); x+=dx )
		{
			integral += integrand(x);
		}
		return (integral*dx);
	}

	double integrate_eulerTriangles(double(*integrand)(double), double a, double b, unsigned int precision)
	{
		double dx = (b-a)/precision;
		double integral = 0;
		double x = a;

		integral += (integrand(x)+integrand(x+dx));
		for(unsigned int c=1; c<precision; c++)
		{
			x += dx;
			integral += (integrand(x)+integrand(x+dx));
		}
		return (0.5*integral*dx);
	}

}