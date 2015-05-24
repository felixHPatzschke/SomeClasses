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
		integral += integrand(arg)*(dx.abs());
		for(unsigned int c=1; c<precision; c++)
		{
			arg += dx;
			integral += integrand(arg)*(dx.abs());
		}
		return integral;
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
			integral += integrand(arg)*(dx.abs());
		}
		return integral;
	}

	Vector integrate_eulerTriangles(Vector(*integrand)(Vector), Vector a, Vector b, unsigned int precision)
	{
		Vector dx = b-a*(1.0/precision);
		Vector integral = Vector(0,0,0);
		Vector arg = a;
		integral += (integrand(arg)+(integrand(arg+dx)-integrand(arg))*0.5)*(dx.abs());
		for(unsigned int c=1; c<precision; c++)
		{
			arg += dx;
			integral += (integrand(arg)+(integrand(arg+dx)-integrand(arg))*0.5)*(dx.abs());
		}
		return integral;
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
			integral += dx*integrand(x);
		}
		return integral;
	}

	double integrate_eulerBackward(double(*integrand)(double), double a, double b, unsigned int precision)
	{
		double dx = (b-a)/precision;

		double integral = 0;
		for( double x=a+dx; a+dx<=x&&x<b+(dx/2.0) || a+dx>=x&&x>b-(dx/2.0); x+=dx )
		{
			integral += dx*integrand(x);
		}
		return integral;
	}

	double integrate_eulerTriangles(double(*integrand)(double), double a, double b, unsigned int precision)
	{
		double dx = (b-a)/precision;
		double integral = 0;
		double x = a;

		integral += dx*(integrand(x)+((integrand(x+dx)-integrand(x))/2.0));
		for(unsigned int c=1; c<precision; c++)
		{
			x += dx;
			integral += dx*(integrand(x)+((integrand(x+dx)-integrand(x))/2.0));
		}
		/*
		if(dx>0.0)
		{
			for( double x=a; x<b-(dx/2.0); x+=dx )
			{
				
			}
		}else if(dx<0.0)
		{
			for( double x=a; x>b+(dx/2.0); x+=dx )
			{
				integral += dx*(integrand(x)-((integrand(x+dx)-integrand(x))/2.0));
			}
		}else
		{
			//dx should really not be 0
		}
		*/
		return integral;
	}

}